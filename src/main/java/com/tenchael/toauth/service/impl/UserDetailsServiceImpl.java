package com.tenchael.toauth.service.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenchael.toauth.domain.User;
import com.tenchael.toauth.domain.UserDetails;
import com.tenchael.toauth.repository.UserDetailsDao;
import com.tenchael.toauth.service.UserDetailsService;

@Service
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

	private static final Logger logger = Logger
			.getLogger(UserDetailsServiceImpl.class);
	@Autowired
	private UserDetailsDao userDetailsDao;

	@Override
	public UserDetails get(Integer id) {
		return userDetailsDao.findOne(id);
	}

	@Override
	@Transactional(readOnly = false)
	public UserDetails save(UserDetails entity) {
		return userDetailsDao.save(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public UserDetails update(UserDetails entity) {
		return userDetailsDao.save(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public UserDetails delete(Integer id) {
		logger.info("delete UserDetails id=" + id);
		UserDetails userDetails = get(id);
		userDetailsDao.delete(id);
		return userDetails;
	}

	@Override
	public List<UserDetails> findAll() {
		return userDetailsDao.findAll();
	}

	@Override
	public Page<UserDetails> findAll(Pageable pageable) {
		return userDetailsDao.findAll(pageable);
	}

	@Override
	public UserDetails getByUserId(final Integer userId) {
		Specification<UserDetails> spec = new Specification<UserDetails>() {

			@Override
			public Predicate toPredicate(Root<UserDetails> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.<User> get("user").<Integer> get("id"),
						userId);
			}

		};
		List<UserDetails> list = userDetailsDao.findAll(spec);
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

}
