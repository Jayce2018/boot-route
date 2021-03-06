package com.jayce.boot.route.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jayce.boot.route.common.thread.BusinessThreadPool;
import com.jayce.boot.route.common.util.MybatisSqlUtil;
import com.jayce.boot.route.entity.LibraryBook;
import com.jayce.boot.route.mapper.LibraryBookMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="/aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
public class AaController {
    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    /**
     * saas服务的上下文路径
     */
    @Value("${server.servlet.context-path:${server.context-path:}}")
    private String contextPath;

    @Autowired
    private LibraryBookMapper libraryBookMapper;

    @RequestMapping(value = "v1/getAllUrl", method ={RequestMethod.POST, RequestMethod.GET})
    public Object getAllUrl() {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

//		List<String> urlList = new ArrayList<>();
//		for (RequestMappingInfo info : map.keySet()) {
//			// 获取url的Set集合，一个方法可能对应多个url
//			Set<String> patterns = info.getPatternsCondition().getPatterns();
//
//			for (String url : patterns) {
//				urlList.add(url);
//			}
//		}

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            Map<String, String> map1 = new HashMap<String, String>();
            RequestMappingInfo info = m.getKey();
            HandlerMethod method = m.getValue();
            //RequestMappingInfo condition
            map1.put("RequestMappingInfo", JSONObject.toJSONString(info));
            PatternsRequestCondition p = info.getPatternsCondition();
            for (String url : p.getPatterns()) {
                map1.put("url", url);
            }
            // 类名
            map1.put("className", method.getMethod().getDeclaringClass().getName());
            // 方法名
            map1.put("method", method.getMethod().getName());
            RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
            for (RequestMethod requestMethod : methodsCondition.getMethods()) {
                map1.put("type", requestMethod.toString());
            }

            list.add(map1);
        }

        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSON(list).toString());

        return jsonArray;
    }


    @RequestMapping(value = "v1/design", method = RequestMethod.POST)
    public Object design() {
        return contextPath;
    }

    @RequestMapping(value = "getSql", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String getSql() {
        //libraryBookMapper.insertSelective();
        LibraryBook book=new LibraryBook();
        book.setBookName("sql语句获取");
        book.setStatus(1);
        String finalSql = MybatisSqlUtil.getExeSql(sqlSessionFactory, book, "com.jayce.boot.route.mapper.LibraryBookMapper.testSql");
        System.out.println("finalSql->"+finalSql);
        return finalSql;
    }

    @RequestMapping(value = "test", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public void test() throws InterruptedException {
        BusinessThreadPool pool = BusinessThreadPool.getInstance(BusinessThreadPool.PoolNameEnum.POOL_TEST.getValue());
        pool.execute(() -> {
            System.out.println("test");
        });
        pool.shutdown();
        while (true) {
            System.out.println("对象为空：" + (null == BusinessThreadPool.map.get(BusinessThreadPool.PoolNameEnum.POOL_TEST.getValue())));
            System.out.println("池关闭：" + pool.isShutdown());
            Thread.sleep(1000);
        }
    }

}
