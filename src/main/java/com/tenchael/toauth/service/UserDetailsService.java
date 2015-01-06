package com.tenchael.toauth.service;

import com.tenchael.toauth.domain.UserDetails;

public interface UserDetailsService extends BaseService<UserDetails, Integer> {

	UserDetails getByUserId(Integer userId);

}
