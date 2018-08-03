package com.viettel.api.web.rest;

import java.util.ResourceBundle;

import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class BaseController {
	public static HttpSession getHttpSession() {
	    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	    ResourceBundle resource = ResourceBundle.getBundle("config/globalConfig");
	    String timeout = resource.getString("SESSION_TIME_OUT");
	    attr.getRequest().getSession().setMaxInactiveInterval(Integer.valueOf(timeout));
	    return attr.getRequest().getSession(true);
	}
}
