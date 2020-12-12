package com.jayce.boot.route.mapper;

import com.jayce.boot.route.entity.LibraryBook;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
public interface LibraryBookMapper extends Mapper<LibraryBook> {
    String testSql(@Param("bookId") Long pram1, @Param("bookName") String param2);
}