package com.itheima.mapper.mysql;

import com.itheima.domain.User;
import com.itheima.domain.UserLog;

import java.util.List;

public interface UserMapper {

	 User findByName(String name);
	
	 User findById(Integer id);

	 void addLog(UserLog userLog);

	 List<User> queryUserInfo();
}
