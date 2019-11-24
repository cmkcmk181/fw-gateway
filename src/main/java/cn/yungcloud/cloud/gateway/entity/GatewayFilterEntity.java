package cn.yungcloud.cloud.gateway.entity;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by caimingkai on 2019/2/18.
 */
@Data
public class GatewayFilterEntity {
    //Filter Name
    private String name;
    //对应的路由规则
    private Map<String, String> args = new LinkedHashMap<>();
}
