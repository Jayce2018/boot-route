package com.jayce.boot.route.common.thread;

import com.jayce.boot.route.common.util.SpringUtil;
import com.jayce.boot.route.entity.LibraryBook;
import com.jayce.boot.route.service.LibraryBookService;

import java.util.List;

public class BookListThread implements Runnable {

    private LibraryBookService libraryBookService = SpringUtil.getBean("libraryBookService");

    List<LibraryBook> list;

    public BookListThread(List<LibraryBook> list) {
        this.list = list;
    }

    @Override
    public void run() {
        list.addAll(libraryBookService.selectListAll());
    }
}
