package com.jayce.boot.route.controller;

import com.github.pagehelper.PageInfo;
import com.jayce.boot.route.common.Thread.BookListThread;
import com.jayce.boot.route.common.enums.BusinessCodeEnum;
import com.jayce.boot.route.common.exception.BusinessException;
import com.jayce.boot.route.common.util.DesignThreadPool;
import com.jayce.boot.route.entity.LibraryBook;
import com.jayce.boot.route.function.apiversion.ApiVersion;
import com.jayce.boot.route.service.LibraryBookService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Vector;

@RestController
@RequestMapping("/route")
public class RouteController {
    @Autowired
    private LibraryBookService libraryBookService;

    private Integer lopNum = 200;

    @ApiOperation(value = "分页测试")
    @RequestMapping(value = "/book", method = RequestMethod.GET)
    public PageInfo<LibraryBook> main() {
        return libraryBookService.testPage(2,5);
    }

    @ApiOperation(value = "性能测试-循环")
    @RequestMapping(value = "/book/lop", method = RequestMethod.GET)
    public List<LibraryBook> bookLop() {
        long start = System.currentTimeMillis();
        List<LibraryBook> result = new Vector<>();
        for (int i = 0; i < lopNum; i++) {
            result.addAll(libraryBookService.selectListAll());
        }
        long end = System.currentTimeMillis();
        System.out.println("运行时间：" + (end - start));
        System.out.println("size：" + result.size());
        return result;
    }

    @ApiOperation(value = "性能测试-线程池")
    @RequestMapping(value = "/book/thread", method = RequestMethod.GET)
    public List<LibraryBook> bookThread() throws InterruptedException {
        long start = System.currentTimeMillis();
        List<LibraryBook> result = new Vector<>();
        DesignThreadPool pool = DesignThreadPool.getInstance();
        for (int i = 0; i < lopNum; i++) {
            pool.execute(new BookListThread(result));
        }
        do {
            Thread.sleep(10);
        } while (pool.isWork());
        long end = System.currentTimeMillis();
        System.out.println("运行时间：" + (end - start));
        System.out.println("size：" + result.size());
        return result;
    }

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String bookList(@RequestParam String string) {
        return string;
    }

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    @ApiVersion(value = "15")
    public String bookListV15(@RequestParam String string) {
        return "15->"+string;
    }

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    @ApiVersion(value = "10")
    public String bookListV10(@RequestParam String string) {
        return "10->"+string;
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

    @RequestMapping(value = "/prod", produces = {
            "application/JSON"
    })
    @ResponseBody
    String getProduces() {
        return "Produces attribute";
    }

    @RequestMapping(value = "/cons", consumes = {
            "application/JSON",
            "application/XML"
    })
    String getConsumes() {
        return "Consumes attribute";
    }
}
