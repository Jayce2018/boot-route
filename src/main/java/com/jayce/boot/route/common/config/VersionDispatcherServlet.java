package com.jayce.boot.route.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class VersionDispatcherServlet extends DispatcherServlet {
    private static final long serialVersionUID = 1L;

    @Autowired
    private WebApplicationContext applicationContext;

    @Override
    @Transactional(rollbackFor = Exception.class)
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        //拦截(指定版本号，但版本不匹配，走默认无版本方法)
        String url = request.getRequestURI();
        String requestHeader = request.getHeader("X-Api-Version");
        List<RequestMappingInfo> rightMappings = map.keySet().stream().filter(fl ->
                (fl.getPatternsCondition().getPatterns().stream().filter(fil -> fil.equals(url)).toArray().length == 1)
                        && (null != fl.getCustomCondition())
                        && (null != fl.getCustomCondition().getMatchingCondition(request))
        ).collect(Collectors.toList());
        if (null != requestHeader && rightMappings.size() == 0) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }
        super.doDispatch(request, response);
    }
}
