package com.spring.boot.transaction.util;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;  
@Aspect  
@Component  
public class DataSourceAspect {
//	@Pointcut("execution(* com.kingdee.mapper.*.*(..))")    
//    public void pointCut(){};  
    
// @Before(value = "pointCut()")  
	 public void before(JoinPoint point)
	    {
	        Object target = point.getTarget();
	        String method = point.getSignature().getName();

	        //Class<?>[] classz = target.getClass().getInterfaces();
	        Class<?> classz =  target.getClass();

	        Class<?>[] parameterTypes = ((MethodSignature) point.getSignature())
	                .getMethod().getParameterTypes();
	        try {
	            Method m = classz.getMethod(method, parameterTypes);
	            if (m != null && m.isAnnotationPresent(DataSource.class)) {
	                DataSource data = m
	                        .getAnnotation(DataSource.class);
	                DynamicDataSourceHolder.putDataSource(data.value());
	                System.out.println(data.value());
	            }
	            
	        } catch (Exception e) {
	            // TODO: handle exception
	        }
	    }
}
