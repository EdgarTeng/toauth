package com.tenchael.toauth.web;

import static com.tenchael.toauth.commons.Settings.LOGINED_USER;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tenchael.toauth.service.AuthService;
import com.tenchael.toauth.service.UserDetailsService;

@Controller
public class AuthController {

	private static final Logger logger = Logger.getLogger(HomeController.class);

	@Autowired
	private AuthService authService;

	@Autowired
	private UserDetailsService userDetailsService;

	@RequestMapping(value = { "/loginUrl" })
	public String getLoginUrl(HttpServletRequest request, Model model) {
		Integer uid = null;
		HttpSession session = request.getSession();
		if (null != session.getAttribute(LOGINED_USER)) {
			uid = (Integer) session.getAttribute(LOGINED_USER);
		}
		String loginUrl = authService.getLoginUrl(uid);
		logger.info(loginUrl);
		model.addAttribute("loginUrl", loginUrl);
		return "redirect:" + loginUrl;
	}

	@RequestMapping("/getCode/{uid}")
	public String getCode(@PathVariable(value = "uid") Integer uid,
			String code, String openid, HttpServletRequest request, Model model) {
		authService.obtainBasicAccountInfo(code, openid, uid);
		return "do";
	}

	@RequestMapping(value = { "/getUserInfo" })
	public @ResponseBody Map<String, String> getUserInfo(
			HttpServletRequest request, Model model) {
		Integer uid = null;
		HttpSession session = request.getSession();
		Map<String, String> ret = new HashMap<String, String>();
		if (null != session.getAttribute(LOGINED_USER)) {
			uid = (Integer) session.getAttribute(LOGINED_USER);
			String userInfo = authService.getUserInfo(uid);
			logger.info(userInfo);			
			ret.put("userInfo", userInfo);
		}		
		return ret;
	}

	@RequestMapping(value = { "/forwardMsg" })
	public @ResponseBody Map<String, String> forwardMsg(
			HttpServletRequest request, String msg, Model model) {
		Integer uid = null;
		HttpSession session = request.getSession();
		Map<String, String> ret = new HashMap<String, String>();
		if (null != session.getAttribute(LOGINED_USER)) {
			uid = (Integer) session.getAttribute(LOGINED_USER);
			String userInfo = authService.forwardMsg(uid, msg);
			logger.info(userInfo);			
			ret.put("userInfo", userInfo);
		}		
		return ret;
	}

}
