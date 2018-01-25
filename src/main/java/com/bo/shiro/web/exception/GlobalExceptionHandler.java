package com.bo.shiro.web.exception;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description @ControllerAdvice 注解定义全局异常处理类：捕获异常统一处理
 * @author 王博
 * @version 2018年1月22日　下午11:03:58
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	
	private final static Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	private final static String EXCEPTION_MSG_KEY = "expMsg";
	
    /**
     * @Description 身份认证失败异常
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler({UnauthenticatedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Object processUnauthenticatedException(NativeWebRequest request, UnauthenticatedException e) {
        LOG.info("Unauthenticated Exception " + e.getMessage());
		// ModelAndView mv = new ModelAndView();
		// mv.addObject(EXCEPTION_MSG_KEY, e.getMessage());
		// mv.setViewName("unauthenticated");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(EXCEPTION_MSG_KEY, e.getMessage());
        return jsonObject;
    }
	
    /**
     * @Description 无权限异常
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler({UnauthorizedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ModelAndView processUnauthorizedException(NativeWebRequest request, UnauthorizedException e) {
        LOG.info("Unauthorized Exception " + e.getMessage());
    	ModelAndView mv = new ModelAndView();
        mv.addObject(EXCEPTION_MSG_KEY, e.getMessage());
        mv.setViewName("unauthorized");
        return mv;
    }

	@ExceptionHandler(BusinessException.class)
	@ResponseBody
	public void handleBizExp(HttpServletRequest request, Exception e) {
		LOG.info("Business exception " + e.getMessage());
		request.getSession(true).setAttribute(EXCEPTION_MSG_KEY, e.getMessage());
	}
    
	@ExceptionHandler(SQLException.class)
	@ResponseBody
	public Object processSqlException(SQLException e) {
		LOG.info("SQL Exception " + e.getMessage());
		// ModelAndView mv = new ModelAndView();
		// mv.addObject(EXCEPTION_MSG_KEY, e.getMessage());
		// mv.setViewName("sql_error");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(EXCEPTION_MSG_KEY, e.getMessage());
		return jsonObject;
	}
}