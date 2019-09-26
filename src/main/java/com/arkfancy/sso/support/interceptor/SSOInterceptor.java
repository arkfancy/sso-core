package com.arkfancy.sso.support.interceptor;

import java.lang.invoke.MethodHandles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.baomidou.kisso.SSOHelper;
import com.baomidou.kisso.annotation.Action;
import com.baomidou.kisso.annotation.Login;
import com.baomidou.kisso.common.SSOConstants;
import com.baomidou.kisso.common.util.HttpUtil;
import com.baomidou.kisso.security.token.SSOToken;
import com.baomidou.kisso.web.handler.KissoDefaultHandler;
import com.baomidou.kisso.web.handler.SSOHandlerInterceptor;

public class SSOInterceptor extends HandlerInterceptorAdapter {

	private static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private SSOHandlerInterceptor handlerInterceptor;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// 处理 Controller 方法 判断 handler 是否为 HandlerMethod 实例
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		// 优先获取方法上的注解 没有则获取类上的注解
		Login login = getLoginAnnotation((HandlerMethod) handler);

		// 没注解或注解等于忽略时 忽略
		if (login == null || login.action() == Action.Skip) {
			return true;
		}

		// 正常执行拦截怕判断
		SSOToken ssoToken = SSOHelper.getSSOToken(request);
		if (ssoToken == null) {
			if (HttpUtil.isAjax(request)) {
				// Handler 处理 AJAX 请求
				getHandlerInterceptor().preTokenIsNullAjax(request, response);
			} else {
				// token 为空，调用 Handler 处理 返回 true 继续执行，清理登录状态并重定向至登录界面
				if (getHandlerInterceptor().preTokenIsNull(request, response)) {
					logger.warn("logout. request url:{}", request.getRequestURL());
					// 返回跳转路径
					SSOHelper.clear401Login(request, response);
				}
			}
			return false;
		}

		// 验证通过，request 设置 token 减少二次解密
		request.setAttribute(SSOConstants.SSO_TOKEN_ATTR, ssoToken);
		return true;

	}

	private Login getLoginAnnotation(HandlerMethod handlerMethod) {
		// 优先获取方法上的注解 没有则获取类上的注解
		Login login = AnnotationUtils.getAnnotation(handlerMethod.getMethod(), Login.class);
		if (login == null) {
			login = AnnotationUtils.getAnnotation(handlerMethod.getBeanType(), Login.class);
		}
		return login;
	}

	public SSOHandlerInterceptor getHandlerInterceptor() {
		if (handlerInterceptor == null) {
			return KissoDefaultHandler.getInstance();
		}
		return handlerInterceptor;
	}

	public void setHandlerInterceptor(SSOHandlerInterceptor handlerInterceptor) {
		this.handlerInterceptor = handlerInterceptor;
	}

}
