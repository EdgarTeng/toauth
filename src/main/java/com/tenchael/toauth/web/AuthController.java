package com.tenchael.toauth.web;

import static com.tenchael.toauth.commons.Settings.LOGINED_USER;

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
			String loginUrl = authService.generateThirdPartLoginAddr(uid);
			logger.info(loginUrl);
			return "redirect:" + loginUrl;
		}
		return "redirect:login";
	}

	@RequestMapping("/getCode/{uid}")
	public String getCode(@PathVariable(value = "uid") Integer uid,
			String openkey, String code, String openid,
			HttpServletRequest request, Model model) {
		authService.storeBasicThirdPartInfo(code, openid, openkey, uid);
		model.addAttribute("uid", uid);
		return "do";
	}

	@RequestMapping(value = { "/getUserInfo" }, produces = "plain/text; charset=UTF-8")
	public @ResponseBody String getUserInfo(Integer uid,
			HttpServletRequest request, Model model) {
		String userInfo = authService.getUserInfo(uid);
		logger.info(userInfo);
		return userInfo;
	}

	@RequestMapping(value = { "/forwardMsg" }, produces = "plain/text; charset=UTF-8")
	public @ResponseBody String forwardMsg(Integer uid,
			HttpServletRequest request, String msg, Model model) {
		logger.info("forward message: " + msg);
		String responseText = authService.forwardMessage(uid, msg);
		logger.info(responseText);
		return responseText;
	}

}
