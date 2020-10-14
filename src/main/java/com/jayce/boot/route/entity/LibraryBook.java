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
    @Transient
    private Boolean flag;

    public LibraryBook() {
    }

    public LibraryBook(String bookName, Integer type, Integer status) {
        this.bookName = bookName;
        this.type = type;
        this.status = status;
        this.createTime=new Date();
        this.updateTime=new Date();
    }

    public LibraryBook(String bookName, Integer status) {
        this.bookName = bookName;
        this.status = status;
        this.createTime=new Date();
        this.updateTime=new Date();
    }

    public enum FlagEnum {
        //
        FLAG_ENUM_NO(1, "1"),
        FLAG_ENUM_YES(2, "2"),
        ;

        private Integer code;
        private String value;

        FlagEnum(Integer code, String value) {
            this.code = code;
            this.value = value;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }



    /**
     * 标记接口
     */
    public interface Update {
    }

    public interface Add {
    }
}