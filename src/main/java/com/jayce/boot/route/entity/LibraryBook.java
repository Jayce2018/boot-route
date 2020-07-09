package com.jayce.boot.route.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "library_book")
public class LibraryBook {
    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    @NotNull(groups = {Update.class},message = "书籍主键不能为空")
    private Long bookId;

    @Column(name = "book_name")
    @NotBlank(groups = {Add.class},message = "书名不能为空")
    private String bookName;

    private Integer type;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "create_time")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "update_time")
    private Date updateTime;

    @Transient
    private List<Long> idList;

    /**
     * 标记接口
     */
    public interface Update {
    }

    public interface Add {
    }
}