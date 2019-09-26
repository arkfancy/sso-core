package com.arkfancy.sso.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.kisso.common.util.HttpUtil;
import com.baomidou.kisso.service.ConfigurableAbstractKissoService;

public class CustomKissoService extends ConfigurableAbstractKissoService {

	public void clear401Login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		/* 清理当前登录状态 */
		clearLogin(request, response);

		// 修改返回401代码和目标路径
		response.setStatus(401);
		String returnUrl = request.getParameter(config.getParamReturl());
		response.getWriter().write(HttpUtil.encodeRetURL(config.getLoginUrl(), config.getParamReturl(), returnUrl));
	}
}
