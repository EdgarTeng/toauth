package com.tenchael.toauth.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenchael.toauth.commons.DEncryptionUtils;
import com.tenchael.toauth.domain.User;
import com.tenchael.toauth.domain.UserDetails;
import com.tenchael.toauth.repository.UserDao;
import com.tenchael.toauth.service.UserDetailsService;
import com.tenchael.toauth.service.UserService;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

	private static final Logger logger = Logger
			.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public User get(Integer id) {
		return userDao.findOne(id);
	}

	@Override
	@Transactional(readOnly = false)
	public User save(User entity) {
		if (findByUsername(entity.getUsername()) == null) {
			// 说明用户尚未注册
			String cryptedPwd = DEncryptionUtils.sha256Hex(entity.getPassword()
					+ entity.getUsername().trim());
			User user = new User(entity.getUsername(), cryptedPwd, new Date());
			user = userDao.save(user);
			UserDetails userDetails = new UserDetails(user);
			userDetailsService.save(userDetails);
			logger.info("a new user register, username=" + user.getUsername());
			return user;
		}
		return null;
	}

	@Override
	@Transactional(readOnly = false)
	public User update(User entity) {
		String cryptedPwd = DEncryptionUtils.sha256Hex(entity.getPassword()
				+ entity.getUsername().trim());
		entity.setPassword(cryptedPwd);
		return userDao.save(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public User delete(Integer id) {
		logger.info("delete User id=" + id);
		User user = get(id);
		userDao.delete(id);
		return user;
	}

	@Override
	public List<User> findAll() {
		return userDao.findAll();
	}

	@Override
	public Page<User> findAll(Pageable pageable) {
		return userDao.findAll(pageable);
	}

	@Override
	public User findByUsername(String username) {
		return userDao.findByUsername(username);
	}

	@Override
	public User checkUser(User user) {
		User queryUser = findByUsername(user.getUsername());
		if (queryUser == null) {
			logger.info("user is not exit, username=" + user.getUsername());
			return queryUser;
		}
		if (!queryUser.getPassword().equals(
				DEncryptionUtils.sha256Hex(user.getPassword()
						+ user.getUsername().trim()))) {
			logger.info("wrong password, username=" + user.getUsername());
			return null;
		}
		return queryUser;
	}

}
