package cn.yungcloud.cloud.gateway.entity;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by caimingkai on 2019/2/18.
 */
@Data
public class GatewayPredicateEntity {
    //断言对应的Name
    private String name;
    //配置的断言规则
    private Map<String, String> args = new LinkedHashMap<>();
}
