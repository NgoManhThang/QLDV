package com.viettel.api.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.viettel.api.dto.boc.BocUserDto;

@Component("roleChecker")
public class RoleChecker {
	
	public static Logger LOGGER = LoggerFactory.getLogger(RoleChecker.class);
	
	public static boolean hasValidRole(String roleUser) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		BocUserDto bocUserDto = (BocUserDto) auth.getDetails();
		if(bocUserDto.getListRole().contains(roleUser)) {
			return true;
		} else {
			return false;
		}
	}
}
