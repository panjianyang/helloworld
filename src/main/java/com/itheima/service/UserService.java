package com.itheima.service;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.User;

public interface UserService {

	User findByName(String name);

	User findById(Integer id);

	PageInfo<User> queryPage(int pageNum, int pageSize);
}