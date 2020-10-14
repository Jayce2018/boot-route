package com.jayce.boot.route.controller;

import com.github.pagehelper.PageInfo;
import com.jayce.boot.route.common.Thread.BookListThread;
import com.jayce.boot.route.common.enums.BusinessCodeEnum;
import com.jayce.boot.route.common.exception.BusinessException;
import com.jayce.boot.route.common.util.DesignThreadPool;
import com.jayce.boot.route.common.util.template.ExcelOutputWithTemplateUtils;
import com.jayce.boot.route.entity.LibraryBook;
import com.jayce.boot.route.function.apiversion.ApiVersion;
import com.jayce.boot.route.service.LibraryBookService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

@RestController
@RequestMapping("/route")
public class RouteController {
    @Autowired
    private LibraryBookService libraryBookService;

    private Integer lopNum = 200;

    @RequestMapping(value = "/test", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    public String bookInsert() throws Exception {

        /*LibraryBook bookAdd = new LibraryBook();
        bookAdd.setType(1);
        bookAdd.setBookName("哈赛");
        libraryBookService.insertEntityPack(bookAdd);*/
        CountDownLatch countDownLatch = new CountDownLatch(1);

        try {
            DesignThreadPool pool = DesignThreadPool.getInstance();
            pool.execute(() -> {
                LibraryBook book = new LibraryBook();
                book.setType(1);
                book.setBookName("哈赛");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                libraryBookService.insertEntityPack(book);
                countDownLatch.countDown();
            });
            while (countDownLatch.getCount()!=0){
                if(pool.getExceptionList().contains("1")){
                    throw new BusinessException("当前任务异常终止");
                }
            }
            countDownLatch.await();
        } catch (Exception e) {
            throw new BusinessException("back-2");
        }
        return "主线程返回";
    }

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

    @RequestMapping(value = "/outputExcel", method = RequestMethod.POST)
    @ApiOperation(value = "outputExcel", notes = "导出Excel", produces = "application/json")
    public void outputExcel( HttpServletResponse response) throws Exception {
        //参数
        Integer type = 2;
        Integer fileType = 1;
        String fileTempUrl="";
        String fileLocalSaveUrl="d:/excel";
        //数据源
        HashMap<Integer, String[][]> dateMap = new HashMap<>();
        String[][] stringDate = new String[4][13];
        stringDate[0][0] = "书籍导出标题";
        stringDate[3][0] = "0";
        stringDate[3][1] = "哈姆雷特";
        stringDate[3][2] = "jayce";
        stringDate[3][3] = "2018-9";
        stringDate[3][4] = "sydfdasd-sadsb";
        stringDate[3][5] = "3014";

        dateMap.put(0, stringDate);
        //类型判断
        switch (fileType){
            //xlsx
            case 1:{
                fileTempUrl="\\excel\\temp.xlsx";
                break;
            }
            //xls
            case 2:{
                fileTempUrl="\\excel\\temp.xlsx";
                break;
            }
        }
        //解析模板
        ExcelOutputWithTemplateUtils.parseExcelResource(fileTempUrl);
        switch (type){
            //本地
            case 1:{
                ExcelOutputWithTemplateUtils.exportLocal("模板结果测试", fileTempUrl, dateMap, fileLocalSaveUrl);
                break;
            }
            //web
            case 2:{
                ExcelOutputWithTemplateUtils.exportResponse("模板结果测试", fileTempUrl, dateMap, response);
                break;
            }
            default:{
                break;
            }
        }
    }
}
