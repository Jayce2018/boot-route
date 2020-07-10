package com.jayce.boot.route.function.apiversion;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;

@Data
@Slf4j
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {

    private String apiVersion;

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    ApiVersionCondition(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    @Override
    public ApiVersionCondition combine(ApiVersionCondition other) {
        // 最近优先原则，方法定义的 @ApiVersion > 类定义的 @ApiVersion
        return new ApiVersionCondition(other.getApiVersion());
    }

    @Override
    public ApiVersionCondition getMatchingCondition(HttpServletRequest request) {
        String headVersion = request.getHeader("X-Api-Version");
        if (!("".equals(headVersion))) {
            if( null == headVersion){
                return null;
            }
            // 获得符合匹配条件的ApiVersionCondition
            if (headVersion.equals(getApiVersion())) {
                return this;
            }
        }
        return null;
    }

    @Override
    public int compareTo(ApiVersionCondition other, HttpServletRequest request) {
        // 当出现多个符合匹配条件的ApiVersionCondition
        throw new IllegalArgumentException("Condition匹配不唯一");
    }

}
