package com.tenchael.toauth.web;

import static com.tenchael.toauth.commons.Settings.LOGINED_USER;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tenchael.toauth.domain.User;
import com.tenchael.toauth.service.UserService;

@Controller
public class UserController {

	private static final Logger logger = Logger.getLogger(HomeController.class);

	@Autowired
	private UserService userService;

	@RequestMapping(value = { "login" })
	public String login(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		if (null != session.getAttribute(LOGINED_USER)) {
			session.removeAttribute(LOGINED_USER);
		}
		User user = new User();
		model.addAttribute("bean", user);
		return "login";
	}

	@RequestMapping(value = { "logout" })
	public String logout(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		if (null != session.getAttribute(LOGINED_USER)) {
			logger.info("user logou, userId="
					+ session.getAttribute(LOGINED_USER));
			session.removeAttribute(LOGINED_USER);
		}
		return "index";
	}

	@RequestMapping(value = { "register" })
	public String register(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		if (null != session.getAttribute(LOGINED_USER)) {
			session.removeAttribute(LOGINED_USER);
		}
		User user = new User();
		model.addAttribute("bean", user);
		return "register";
	}

	@RequestMapping(value = { "save" }, method = RequestMethod.POST)
	public String save(User user, HttpServletRequest request, Model model) {
		User savedUser = userService.save(user);
		if (savedUser == null) {
			return "redirect:register";
		}
		HttpSession session = request.getSession();
		if (null == session.getAttribute(LOGINED_USER)) {
			session.setAttribute(LOGINED_USER, savedUser.getId());
		}
		return "redirect:loginUrl";
	}

	@RequestMapping(value = { "checkUser" }, method = RequestMethod.POST)
	public String checkUser(User user, HttpServletRequest request, Model model) {
		User queryUser = userService.checkUser(user);
		if (queryUser == null) {
			return "redirect:login";
		}
		HttpSession session = request.getSession();
		if (null == session.getAttribute(LOGINED_USER)) {
			session.setAttribute(LOGINED_USER, queryUser.getId());
		}
		return "redirect:loginUrl";
	}

}
