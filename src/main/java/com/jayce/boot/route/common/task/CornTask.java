package com.jayce.boot.route.common.task;

import com.alibaba.fastjson.JSONObject;
import com.jayce.boot.route.entity.LibraryBook;
import com.jayce.boot.route.mapper.LibraryBookMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CornTask {
    @Autowired
    private LibraryBookMapper libraryBookMapper;

    @Scheduled(cron = "*/30 * * * * ?")
    public void scheduled() {
        List<LibraryBook> libraryBooks = libraryBookMapper.selectAll();
        log.info("定时任务->"+JSONObject.toJSON(libraryBooks));
    }
}
