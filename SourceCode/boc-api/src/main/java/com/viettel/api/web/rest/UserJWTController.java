package com.viettel.api.web.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.viettel.api.dto.qldv.EmployeeDto;
import com.viettel.api.service.boc.CommonService;
import com.viettel.api.service.qldv.CommonQldvService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.viettel.api.config.Constants;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.boc.BocRoleDto;
import com.viettel.api.dto.boc.BocRoleTargetDto;
import com.viettel.api.dto.boc.BocUnitDto;
import com.viettel.api.dto.boc.BocUserDto;
import com.viettel.api.security.jwt.JWTConfigurer;
import com.viettel.api.security.jwt.TokenProvider;
import com.viettel.api.service.boc.BocUserService;
import com.viettel.api.utils.StringUtils;
import com.viettel.api.vm.LoginVM;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping(Constants.API_PATH_PREFIX + "access")
public class UserJWTController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(UserJWTController.class);

    private  TokenProvider tokenProvider;
    
    @Autowired
    private HttpSession httpSession;
    
    @Autowired
    BocUserService bocUserService;

    @Autowired
	CommonQldvService commonService;

    public UserJWTController(BocUserService bocUserService, TokenProvider tokenProvider) {
        this.bocUserService = bocUserService;
        this.tokenProvider = tokenProvider;
    }
    
    @GetMapping("/checkSession")
    @Timed
    public ResponseEntity<?> checkSession(HttpServletRequest request) {
    	EmployeeDto employeeDto = new EmployeeDto();
    	try {
			employeeDto = (EmployeeDto) httpSession.getAttribute("userToken");
		} catch (IllegalStateException e) {
			log.error(e.getMessage(), e);
			employeeDto = null;
		}
		return ResponseEntity.ok(employeeDto);
    }
    
    @GetMapping("/getCsrfToken")
    @Timed
    public ResponseEntity<?> getCsrfToken(HttpServletRequest request) {
    	CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
		return ResponseEntity.ok(csrf);
    }
    
    @GetMapping("/logout")
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IllegalStateException {
    	try {
    		httpSession.setAttribute("userToken", null);
        	httpSession.invalidate();
		} catch (IllegalStateException e) {
			log.error(e.getMessage(), e);
		}
    }

    @PostMapping("/login")
    @Timed
    public ResponseEntity<?> authorize(@Valid @RequestBody LoginVM loginVM, HttpServletResponse response
    		, HttpServletRequest request) {
    	try {
    		ResultDto resultDto = new ResultDto();
//    		BocUserDto bocUserDto = bocUserService.getUserByUserName(loginVM.getUsername());
			EmployeeDto employeeDto = commonService.getEmployeeByUserName(loginVM.getUsername());
			if(employeeDto.getUserId() != null &&  "1".equals(employeeDto.getStatus())) {
				if(StringUtils.passwordEncoder().matches(loginVM.getPassword(), employeeDto.getPassword())) {
//				if("123456".equals(employeeDto.getPassword())) {
//					List<BocRoleDto> listBocRoleDto = bocUserService.getListRoleByUserName(loginVM.getUsername());
//					List<String> listRole = new ArrayList<>();
//					List<BocUnitDto> listBocUnitDto = bocUserService.getListUnitByUserName(loginVM.getUsername());
//					List<String> listUnit = new ArrayList<>();
//					List<BocRoleTargetDto> listBocRoleTargetDto = bocUserService.getListRoleTargetByUserName(loginVM.getUsername());
//					List<String> listRoleTarget = new ArrayList<>();
//					if(!listBocUnitDto.isEmpty()) {
//						bocUserDto.setRegionLevel(listBocUnitDto.get(0).getRegionLevel());
//					}
//					listRole.add("DASHBOARD");

					List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
					GrantedAuthority grantedAutority = new SimpleGrantedAuthority("DASHBOARD");
					authorities.add(grantedAutority);
//
//					// Set role for jwt user
//					for (int i = 0; i < listBocRoleDto.size(); i++) {
//						listRole.add(listBocRoleDto.get(i).getRoleCode());
//						grantedAutority = new SimpleGrantedAuthority(listBocRoleDto.get(i).getRoleCode());
//						authorities.add(grantedAutority);
//					}
//					for (int j = 0; j < listBocUnitDto.size(); j++) {
//						listUnit.add(listBocUnitDto.get(j).getUnitCode());
//					}
//					for (int j = 0; j < listBocRoleTargetDto.size(); j++) {
//						listRoleTarget.add(listBocRoleTargetDto.get(j).getRoleTargetCode());
//					}
//					bocUserDto.setPassword(null);
//					bocUserDto.setListRole(listRole);
//					bocUserDto.setListUnit(listUnit);
//					bocUserDto.setListRoleTarget(listRoleTarget);
					
					UsernamePasswordAuthenticationToken authenticationToken =
				            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword(), authorities);
					boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
					String jwt = tokenProvider.createToken(authenticationToken, rememberMe, employeeDto);
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					response.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
					resultDto.setAuthToken("Bearer " + jwt);
					resultDto.setKey("200");
					resultDto.setObject(employeeDto);
					httpSession = getHttpSession();
					httpSession.setAttribute("userToken", employeeDto);
	    			return ResponseEntity.ok(resultDto);
				} else {
					resultDto.setKey("400");
					return ResponseEntity.ok(resultDto);
				}
			} else {
				resultDto.setKey("403");
				return ResponseEntity.ok(resultDto);
			}
    	} catch(Exception ex) {
    		log.trace("Authentication exception trace: {}", ex);
    		return new ResponseEntity<>(Collections.singletonMap("AuthenticationException",
                    ex.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
