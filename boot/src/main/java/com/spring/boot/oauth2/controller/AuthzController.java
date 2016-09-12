package com.spring.boot.oauth2.controller;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.boot.oauth2.service.OAuthService;
import com.spring.boot.oauth2.service.Oauth2ClientService;

//授权控制器
@Controller
public class AuthzController {
	@Autowired
	OAuthService oAuthService;
	@Autowired
	Oauth2ClientService clientService;

	//http://localhost:8580/boot/authorize?client_id=ci1&response_type=code&redirect_uri=http://localhost:8580/boot/oauth2/client/login
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/authorize")
	public Object authorize(Model model, HttpServletRequest request) throws URISyntaxException, OAuthSystemException {
		try {
			// 构建OAuth 授权请求
			OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
			// 检查传入的客户端id是否正确
			if (!oAuthService.checkClientId(oauthRequest.getClientId())) {
				OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
						.setError(OAuthError.TokenResponse.INVALID_CLIENT)
						.setErrorDescription("INVALID_CLIENT_DESCRIPTION").buildJSONMessage();
				return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
			}

			Subject subject = SecurityUtils.getSubject();
			// 如果用户没有登录，跳转到登陆页面
			if (!subject.isAuthenticated()) {
					model.addAttribute("client", clientService.findByClientId(oauthRequest.getClientId()));
					String loginUrl = "oauth/login?client_id="+oauthRequest.getClientId()+"&response_type="+oauthRequest.getResponseType()+"&redirect_uri="+oauthRequest.getRedirectURI();
					model.addAttribute("loginUrl", loginUrl);
					return "oauthlogin";
			}

			String username = (String) subject.getPrincipal();
			// 生成授权码
			String authorizationCode = null;
			// responseType目前仅支持CODE，另外还有TOKEN
			String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
			if (responseType.equals(ResponseType.CODE.toString())) {
				OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
				authorizationCode = oauthIssuerImpl.authorizationCode();
				oAuthService.addAuthCode(authorizationCode, username);
			}
			// 进行OAuth响应构建
			OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(request,
					HttpServletResponse.SC_FOUND);
			// 设置授权码
			builder.setCode(authorizationCode);
			// 得到到客户端重定向地址
			String redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);

			// 构建响应
			final OAuthResponse response = builder.location(redirectURI).buildQueryMessage();
			// 根据OAuthResponse返回ResponseEntity响应
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(new URI(response.getLocationUri()));
			return new ResponseEntity(headers, HttpStatus.valueOf(response.getResponseStatus()));
		} catch (OAuthProblemException e) {
			// 出错处理
			String redirectUri = e.getRedirectUri();
			if (OAuthUtils.isEmpty(redirectUri)) {
				// 告诉客户端没有传入redirectUri直接报错
				return new ResponseEntity("OAuth callback url needs to be provided by client!!!", HttpStatus.NOT_FOUND);
			}
			// 返回错误消息（如?error=）
			final OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND).error(e)
					.location(redirectUri).buildQueryMessage();
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(new URI(response.getLocationUri()));
			return new ResponseEntity(headers, HttpStatus.valueOf(response.getResponseStatus()));
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/oauth/login",method=RequestMethod.POST)
	public Object login(Model model,HttpServletRequest request) {
		OAuthAuthzRequest oauthRequest = null;
		
		String msg = "";  
		String username = request.getParameter("username");
		String password = request.getParameter("password");
	
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		Subject subject = SecurityUtils.getSubject();  
		try {  
			oauthRequest = new OAuthAuthzRequest(request);
	        subject.login(token);  
	        if (subject.isAuthenticated()) {  
	        	 model.addAttribute("message", "success"); 
	        	String path = request.getContextPath();
	        	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path+"/";
	        	String redirect_uri =basePath+"authorize?client_id="+oauthRequest.getClientId()+"&response_type="+oauthRequest.getResponseType()+"&redirect_uri="+oauthRequest.getRedirectURI();
	        	// 进行OAuth响应构建
				OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(request,
						HttpServletResponse.SC_FOUND);
	        	// 构建响应
				final OAuthResponse response = builder.location(redirect_uri).buildQueryMessage();
				// 根据OAuthResponse返回ResponseEntity响应
				HttpHeaders headers = new HttpHeaders();
				headers.setLocation(new URI(response.getLocationUri()));
				return new ResponseEntity(headers, HttpStatus.valueOf(response.getResponseStatus())); 
	        }
	    } catch (IncorrectCredentialsException e) {  
	        msg = "登录密码错误. Password for account " + token.getPrincipal() + " was incorrect.";  
	        model.addAttribute("message", msg);  
	        System.out.println(msg);  
	    } catch (ExcessiveAttemptsException e) {  
	        msg = "登录失败次数过多";  
	        model.addAttribute("message", msg);  
	        System.out.println(msg);  
	    } catch (LockedAccountException e) {  
	        msg = "帐号已被锁定. The account for username " + token.getPrincipal() + " was locked.";  
	        model.addAttribute("message", msg);  
	        System.out.println(msg);  
	    } catch (DisabledAccountException e) {  
	        msg = "帐号已被禁用. The account for username " + token.getPrincipal() + " was disabled.";  
	        model.addAttribute("message", msg);  
	        System.out.println(msg);  
	    } catch (ExpiredCredentialsException e) {  
	        msg = "帐号已过期. the account for username " + token.getPrincipal() + "  was expired.";  
	        model.addAttribute("message", msg);  
	        System.out.println(msg);  
	    } catch (UnknownAccountException e) {  
	        msg = "帐号不存在. There is no user with username of " + token.getPrincipal();  
	        model.addAttribute("message", msg);  
	        System.out.println(msg);  
	    } catch (UnauthorizedException e) {  
	        msg = "您没有得到相应的授权！" + e.getMessage();  
	        model.addAttribute("message", msg);  
	        System.out.println(msg);  
	    } catch (OAuthSystemException e) {
	    	model.addAttribute("message", e.getMessage());  
			e.printStackTrace();
		} catch (URISyntaxException e) {
			model.addAttribute("message", e.getMessage());  
			e.printStackTrace();
		} catch (OAuthProblemException e) {
			model.addAttribute("message", e.getMessage());  
			e.printStackTrace();
		}  
		String loginUrl = "oauth/login?client_id="+oauthRequest.getClientId()+"&response_type="+oauthRequest.getResponseType()+"&redirect_uri="+oauthRequest.getRedirectURI();
		model.addAttribute("loginUrl", loginUrl);
	    return "oauthlogin";
	}
}
