package com.jayce.boot.route.common.base;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public abstract class BaseService<M extends Mapper<T>, T> {
    @Autowired
    protected M mapper;

    public BaseService() {
    }

    public void setMapper(M mapper) {
        this.mapper = mapper;
    }

    public T selectOne(T entity) {
        return this.mapper.selectOne(entity);
    }

    public T selectById(Object id) {
        return this.mapper.selectByPrimaryKey(id);
    }

    public List<T> selectList(T entity) {
        return this.mapper.select(entity);
    }

    public List<T> selectListAll() {
        return this.mapper.selectAll();
    }

    public Long selectCount(T entity) {
        return new Long((long) this.mapper.selectCount(entity));
    }

    public void insert(T entity) {
        this.mapper.insert(entity);
    }

    public void insertSelective(T entity) {
        this.mapper.insertSelective(entity);
    }

    public void delete(T entity) {
        this.mapper.delete(entity);
    }

    public void deleteById(Object id) {
        this.mapper.deleteByPrimaryKey(id);
    }

    public int deleteByExample(Object example) {
        return this.mapper.deleteByExample(example);
    }

    public void updateById(T entity) {
        this.mapper.updateByPrimaryKey(entity);
    }

    public void updateSelectiveById(T entity) {
        this.mapper.updateByPrimaryKeySelective(entity);
    }

    public List<T> selectByExample(Object example) {
        return this.mapper.selectByExample(example);
    }

    public int selectCountByExample(Object example) {
        return this.mapper.selectCountByExample(example);
    }

}
