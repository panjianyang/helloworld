package com.itheima.service.impl;/*
 * @description: $param$
 * @date: $date$ $time$
 * @auther: pjy
 */

import com.itheima.service.AopLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@SuppressWarnings("all")
@Transactional(value = "mysqlTransactionManager", rollbackFor = { Exception.class })
public class AopLogServiceImpl implements AopLogService {
    @Override
    public void insertAopLog() {

    }
}
