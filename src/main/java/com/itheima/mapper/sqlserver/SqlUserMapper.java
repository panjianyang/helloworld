package com.itheima.mapper.sqlserver;

import com.itheima.domain.User;

public interface SqlUserMapper {

	 User findByName(String name);
	
	 User findById(Integer id);
}
