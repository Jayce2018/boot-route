package com.jayce.boot.route.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jayce.boot.route.common.base.BaseService;
import com.jayce.boot.route.entity.LibraryBook;
import com.jayce.boot.route.mapper.LibraryBookMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@Service
public class LibraryBookService extends BaseService<LibraryBookMapper, LibraryBook> {
    @Autowired
    private LibraryBookMapper libraryBookMapper;

    @ApiOperation(value = "分页测试")
    public PageInfo<LibraryBook> testPage(Integer pageNum, Integer pageSize) {
        if (null != pageNum && null != pageSize) {
            PageHelper.startPage(pageNum, pageSize);
        }
        List<LibraryBook> libraryBooks = libraryBookMapper.selectAll();
        PageInfo<LibraryBook> pageInfo=new PageInfo(libraryBooks);
        return pageInfo;
    }

    @ApiOperation(value = "插入数据")
    @Transactional(rollbackFor = Exception.class)
    public void insertEntity(LibraryBook libraryBook) {
        libraryBookMapper.insertSelective(libraryBook);
        //throw new RejectedExecutionException("拒绝");
    }

    @ApiOperation(value = "包装回滚")
//    @Transactional(rollbackFor = RejectedExecutionException.class)
    public void insertEntityPack(LibraryBook libraryBook) {
        insertEntity(libraryBook);
        throw new RejectedExecutionException("1");
    }
}
