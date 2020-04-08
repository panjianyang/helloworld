package com.itheima.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.domain.UserLog;
import com.itheima.mapper.sqlserver.SqlUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itheima.domain.User;
import com.itheima.mapper.mysql.UserMapper;
import com.itheima.service.UserService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(value = "mysqlTransactionManager", rollbackFor = { Exception.class })
@SuppressWarnings("all")
public class UserServiceImpl implements UserService{

	//注入Mapper接口
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private SqlUserMapper sqlUserMapper;

	@Override
	public User findByName(String name) {
		userMapper.findByName(name);
		UserLog userLog = new UserLog();
		userLog.setName(name);
		userMapper.addLog(userLog);
		int a= 1/0;
		sqlUserMapper.findByName(name);
		return userMapper.findByName(name);
	}

	@Override
	public PageInfo<User> queryPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<User> lists = userMapper.queryUserInfo();
		PageInfo<User> pageInfo = new PageInfo<User>(lists);
		PageInfo <User>pageInfo1=new PageInfo(userMapper.queryUserInfo());

		return pageInfo;
	}

	@Override
	public User findById(Integer id) {
		return userMapper.findById(id);
	}

}
