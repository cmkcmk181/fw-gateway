package cn.yungcloud.cloud.gateway.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caimingkai on 2019/2/18.
 */
@Data
public class GatewayRouteEntity {
    //路由的Id
    private String id;
    //路由断言集合配置
    private List<GatewayPredicateEntity> predicates = new ArrayList<>();
    //路由过滤器集合配置
    private List<GatewayFilterEntity> filters = new ArrayList<>();
    //路由规则转发的目标uri
    private String uri;
    //路由执行的顺序
    private int order = 0;
}
