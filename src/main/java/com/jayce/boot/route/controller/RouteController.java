package com.jayce.boot.route.controller;

import com.jayce.boot.route.common.enums.BusinessCodeEnum;
import com.jayce.boot.route.common.exception.BusinessException;
import com.jayce.boot.route.entity.LibraryBook;
import com.jayce.boot.route.service.LibraryBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/route")
public class RouteController {
    @Autowired
    private LibraryBookService libraryBookService;

    @RequestMapping(value = "/book", method = RequestMethod.GET)
    public List<LibraryBook> main() {
        //libraryBookService=SpringUtil.getBean("libraryBookService");
        return libraryBookService.selectListAll();
    }

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String bookList(@RequestParam String string) {
        return string;
    }

    @RequestMapping(value = "/exception", method = RequestMethod.GET)
    public String exception() {
        throw new BusinessException(BusinessCodeEnum.BUSINESS);
    }

    @RequestMapping(value = "/book", method = RequestMethod.POST)
    public void bookInsert(@RequestBody @Validated(value = LibraryBook.Add.class) LibraryBook book) {
        libraryBookService.insertSelective(book);
    }

    @RequestMapping(value = "/book", method = RequestMethod.PUT)
    public void bookUpdate(@RequestBody @Validated(value = LibraryBook.Update.class) LibraryBook book) {
        libraryBookService.updateSelectiveById(book);
    }
}
