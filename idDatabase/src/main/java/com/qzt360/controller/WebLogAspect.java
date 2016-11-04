package com.qzt360.controller;

import com.qzt360.pub.PubPara;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Order(5)
@Component
public class WebLogAspect {
	ThreadLocal<Long> startTime = new ThreadLocal<>();

	@Pointcut("execution(public * com.qzt360.controller.IDController.*(..))")
	public void webLog() {
	}

	@Before("webLog()")
	public void doBefore(JoinPoint joinPoint) throws Throwable {
		startTime.set(System.currentTimeMillis());

		// 接收到请求，记录请求内容
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();

		// 记录下请求内容
		log.info("RequestURL:{}, Method:{}, RemoteAddr:{}, ClassMethod:{}, args:{}", request.getRequestURL().toString(),
				request.getMethod(), request.getRemoteAddr(),
				joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName(),
				Arrays.toString(joinPoint.getArgs()));
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("strUrl", request.getRequestURL().toString());
		json.put("strHttpMethod", request.getMethod());
		json.put("strRemoteAddr", request.getRemoteAddr());
		json.put("strClassMethod",
				joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
		json.put("strArgs", Arrays.toString(joinPoint.getArgs()));
		json.put("dtTime", new Date(System.currentTimeMillis()));
		PubPara.listSystemLog.add(json);
	}

	@AfterReturning(returning = "ret", pointcut = "webLog()")
	public void doAfterReturning(Object ret) throws Throwable {
		// 处理完请求，返回内容
		log.info("Spend Time : {}", (System.currentTimeMillis() - startTime.get()));
	}
}
