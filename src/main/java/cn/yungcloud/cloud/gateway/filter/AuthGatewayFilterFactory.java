package cn.yungcloud.cloud.gateway.filter;

import cn.yungcloud.cloud.core.common.entity.ResponseEntity;
import cn.yungcloud.cloud.gateway.client.OAuthResourceClient;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by caimingkai on 2019/3/4.
 */
@Component
@RefreshScope
public class AuthGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthGatewayFilterFactory.Config> {
    /*@Value("${ignore-urls}")
    private String IgnoreUrls;*/
    @Autowired
    private OAuthResourceClient oAuthResourceClient;

    public AuthGatewayFilterFactory() {
        super(Config.class);
    }
    public static class Config {
        //Put the configuration properties for your filter here
    }
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String token = exchange.getRequest().getHeaders().getFirst("Authorization");
            //websocket 将token放到url变量中
            if(StringUtils.isEmpty(token)) {
                token = exchange.getRequest().getQueryParams().getFirst("token");
                //将token添加到请求头中
                if(token!=null)
                    exchange.getRequest().mutate().header("Authorization", token).build();
            }
            String uri=exchange.getRequest().getURI().getPath();
            String method= exchange.getRequest().getMethod().name();
            Map<String,Object> params=new HashMap<>();
            params.put("uri",uri);
            params.put("method",method);
            params.put("token",token);

            /*//判断是否允许匿名访问
            // URLs匹配
            AntPathMatcher antPathMatcher = new AntPathMatcher();
            String[] anonUrls=IgnoreUrls.split(",");
            boolean ignoreAccess=false;
            for (String url:anonUrls) {
                if (antPathMatcher.match(url,uri)) {
                    ignoreAccess = true;
                    break;
                }
            }
            //校验jwtToken的合法性
            if (ignoreAccess||token != null) {
                // 合法
                // 将用户id作为参数传递下去
                return chain.filter(exchange);
            }*/

            ResponseEntity responseEntity=oAuthResourceClient.checkPermissions(params);
            if(responseEntity.getStatus()>-1){
                return chain.filter(exchange);
            }

            //不合法(响应未登录的异常)
            ServerHttpResponse response = exchange.getResponse();
            //设置headers
            HttpHeaders httpHeaders = response.getHeaders();
            httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
            httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
            //设置body
            String warningStr = JSON.toJSONString(responseEntity);
            DataBuffer bodyDataBuffer = response.bufferFactory().wrap(warningStr.getBytes());

            return response.writeWith(Mono.just(bodyDataBuffer));
        };
    }
}
