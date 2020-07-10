package com.jayce.boot.route.function.apiversion;

import com.jayce.boot.route.common.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

@Slf4j
public class CustomRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    @Override
    protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
        // 扫描类上的 @ApiVersion
        ApiVersion apiVersion = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
        RequestCondition<ApiVersionCondition> requestCondition = createRequestCondition(apiVersion);
        return null!=requestCondition?requestCondition:super.getCustomTypeCondition(handlerType);
    }

    @Override
    protected RequestCondition<?> getCustomMethodCondition(Method method) {
        // 扫描方法上的 @ApiVersion
        ApiVersion apiVersion = AnnotationUtils.findAnnotation(method, ApiVersion.class);
        RequestCondition<ApiVersionCondition> requestCondition = createRequestCondition(apiVersion);
        return null!=requestCondition?requestCondition:super.getCustomMethodCondition(method);
    }

    private RequestCondition<ApiVersionCondition> createRequestCondition(ApiVersion apiVersion) {
        if (Objects.isNull(apiVersion)) {
            return null;
        }
        String value = apiVersion.value();
        return new ApiVersionCondition(value);
    }

    @Override
    protected RequestMappingInfo getMatchingMapping(RequestMappingInfo info, HttpServletRequest request) {
        RequestMethodsRequestCondition methods = info.getMethodsCondition().getMatchingCondition(request);
        if (methods == null) {
            return null;
        }
        ParamsRequestCondition params = info.getParamsCondition().getMatchingCondition(request);
        if (params == null) {
            return null;
        }
        HeadersRequestCondition headers = info.getHeadersCondition().getMatchingCondition(request);
        if (headers == null) {
            return null;
        }
        ConsumesRequestCondition consumes = info.getConsumesCondition().getMatchingCondition(request);
        if (consumes == null) {
            return null;
        }
        ProducesRequestCondition produces = info.getProducesCondition().getMatchingCondition(request);
        if (produces == null) {
            return null;
        }

        PatternsRequestCondition patterns = info.getPatternsCondition().getMatchingCondition(request);
        if (patterns == null) {
            return null;
        }

        //带注解不匹配，强制拦截
        ApiVersionCondition custom=null;
        if(null!=request.getHeader(CommonConstant.API_VERSION_HEADER)) {
            RequestCondition<?> customCondition = info.getCustomCondition();
            if(customCondition instanceof ApiVersionCondition) {
                ApiVersionCondition apiVersionCondition =(ApiVersionCondition) info.getCustomCondition();
                custom = Objects.requireNonNull(apiVersionCondition).getMatchingCondition(request);
                //有版本请求头，带版本信息的匹配没有对应项，拦截
                if (custom == null) {
                    return null;
                }
            }else{
                //有版本请求头，没带版本的匹配项，直接拦截
                return null;
            }
        }else{
            //无信息头，patterns匹配不识别附加信息，配到带版本头给拦截掉
            RequestCondition<?> customCondition = info.getCustomCondition();
            if(customCondition instanceof ApiVersionCondition) {
                ApiVersionCondition apiVersionCondition =(ApiVersionCondition) info.getCustomCondition();
                if (null != apiVersionCondition.getApiVersion()) {
                    return null;
                }
            }
        }


        return new RequestMappingInfo(info.getName(), patterns, methods, params, headers, consumes, produces, custom);
    }
}




