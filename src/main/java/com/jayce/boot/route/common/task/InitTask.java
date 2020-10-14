package com.jayce.boot.route.common.task;

import com.alibaba.fastjson.JSONObject;
import com.jayce.boot.route.entity.LibraryBook;
import com.jayce.boot.route.mapper.LibraryBookMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
@Slf4j
@Component
public class InitTask {
    @Autowired
    private LibraryBookMapper libraryBookMapper;

    @PostConstruct
    public void init() {
        List<LibraryBook> libraryBooks = libraryBookMapper.selectAll();
        log.info("初始化任务->"+JSONObject.toJSON(libraryBooks));
    }
}
