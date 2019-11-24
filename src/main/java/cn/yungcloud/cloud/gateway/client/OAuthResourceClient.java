package cn.yungcloud.cloud.gateway.client;

import cn.yungcloud.cloud.core.common.entity.ResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by caimingkai on 2019/4/5.
 */
@FeignClient(name="fw-oauth")
@RequestMapping(value = "/oauth")
public interface OAuthResourceClient {
    @RequestMapping(value ="/resource/permissions/checking", method = RequestMethod.GET, consumes = "application/json")
    public ResponseEntity checkPermissions(@RequestParam Map<String,Object> params);

}
