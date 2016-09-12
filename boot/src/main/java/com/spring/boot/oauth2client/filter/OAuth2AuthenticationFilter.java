package com.spring.boot.oauth2client.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;

import com.spring.boot.oauth2client.token.OAuth2Token;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class OAuth2AuthenticationFilter extends AuthenticatingFilter {
	// oauth2 authc code参数名
	private String authcCodeParam = "code";
	// 客户端id
	private String clientId;
	// 服务器端登录成功/失败后重定向到的客户端地址
	private String redirectUrl;
	// oauth2服务器响应类型
	private String responseType = "code";
	private String failureUrl;

	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String code = httpRequest.getParameter(authcCodeParam);
		return new OAuth2Token(code,code);
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		String error = request.getParameter("error");
		String errorDescription = request.getParameter("error_description");
		if (error != null && !error.equals("")) {// 如果服务端返回了错误
			WebUtils.issueRedirect(request, response,
					failureUrl + "?error=" + error + "error_description=" + errorDescription);
			return false;
		}
		Subject subject = getSubject(request, response);
		if (!subject.isAuthenticated()) {
			String authcCodeParamO = request.getParameter(authcCodeParam);
			if (authcCodeParamO == null || authcCodeParamO.equals("")) {
				// 如果用户没有身份验证，且没有auth code，则重定向到服务端授权
				saveRequestAndRedirectToLogin(request, response);
				return false;
			}
		}
		// 执行父类里的登录逻辑，调用Subject.login登录
		return executeLogin(request, response);
	}

	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		return false;
	}

	// 登录成功后的回调方法 重定向到成功页面
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		//issueSuccessRedirect(request, response);
		//return false;
		return true;
	}

	// 登录失败后的回调
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException ae, ServletRequest request,
			ServletResponse response) {
		ae.printStackTrace();
		Subject subject = getSubject(request, response);
		if (subject.isAuthenticated() || subject.isRemembered()) {
			try { // 如果身份验证成功了 则也重定向到成功页面
				issueSuccessRedirect(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try { // 登录失败时重定向到失败页面
				WebUtils.issueRedirect(request, response, failureUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
