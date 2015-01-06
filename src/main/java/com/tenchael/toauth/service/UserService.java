package com.tenchael.toauth.service;

import com.tenchael.toauth.domain.User;

public interface UserService extends BaseService<User, Integer> {

	User findByUsername(String username);

	User checkUser(User user);

}
