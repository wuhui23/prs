package com.wymessi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wymessi.exception.CustomException;
import com.wymessi.po.SysUser;
import com.wymessi.service.UserService;
import com.wymessi.utils.Md5Utils;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * 跳转到注册页面
	 * 
	 * @return
	 */
	@RequestMapping("/registerPage")
	public String registerPage() {
		return "applicant/register";
	}

	/**
	 * 项目申请者注册
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/register")
	public String register(HttpServletRequest request, Model model, SysUser user) throws Exception {
		String md5Password = Md5Utils.md5(user.getPassword()); // 密码采用MD5加密
		user.setPassword(md5Password);
		userService.registerApplicant(user);
		model.addAttribute("message", "注册成功");
		model.addAttribute("href", "/prs/");
		return "applicant/register";
	}

	/**
	 * 用户登录
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/login")
	public String login(SysUser sysUser, HttpSession session, Model model) throws Exception {
		sysUser.setPassword(Md5Utils.md5(sysUser.getPassword()));
		SysUser user = userService.login(sysUser);
		if (user == null) {
			model.addAttribute("message", "请正确填写角色、用户名与密码");
			return "login";
		}
		session.setAttribute("user", user);
		return "forward:/user/" + user.getRoleId() + "/afterLoginPage";

	}

	/**
	 * 用户退出登录
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/logout")
	public String logout(HttpSession session) throws Exception {
		session.invalidate();;
		return "login";

	}
	
	/**
	 * 登陆后跳转的页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/{role}/afterLoginPage")
	public String afterLoginPage(HttpServletRequest request, HttpSession session, @PathVariable String role) throws Exception {
		if (session.getAttribute("user") == null) {
			throw new CustomException("未登录，请先登录", "/prs/");
		}
		String path = null;
		switch (role) {
		case "1":
			path = "applicant/upload";
			break;
		case "2":
			path = "expert/baseinfo";
			break;
		case "3":
			path = "system/baseinfo";
			break;
		default:
			break;
		}

		return path;
	}

	/**
	 * 基本信息页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/baseinfoPage")
	public String baseInfoPage(HttpServletRequest request, HttpSession session, String role) throws Exception {
		if (session.getAttribute("user") == null) {
			throw new CustomException("未登录，请先登录", "/prs/");
		}
		String path = null;
		switch (role) {
		case "1":
			path = "applicant/baseinfo";
			break;
		case "2":
			path = "expert/baseinfo";
			break;
		default:
			break;
		}
		return path;
	}
	
	/**
	 * 将用户基本信息以json的形式返回
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/userBaseInfo.json")
	public Map<String,Object> baseinfoPage(Model model, HttpSession session, HttpServletRequest request) throws Exception {
		if (session.getAttribute("user") == null) {
			throw new CustomException("未登录，请先登录", "/prs/");
		}
		Map<String,Object> map = new HashMap<String, Object>();
		List<SysUser> users = new ArrayList<SysUser>();
		users.add((SysUser)session.getAttribute("user"));
		map.put("code", "0");
		map.put("count", "1000");
		map.put("msg", "");
		map.put("data", users);
		
		return map;
	}
}