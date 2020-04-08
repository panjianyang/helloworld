package com.itheima.aop;

import com.itheima.service.AopLogService;
import com.itheima.util.IpUtils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Component
@Aspect
public class LogAop {
    @Autowired
    private AopLogService aopLogService;
    private final Logger logger = LoggerFactory.getLogger(LogAop.class);

    @Pointcut("execution(public * com.itheima.controller..*.*(..))")//切入点描述 这个是controller包的切入点
    public void controllerLog(){}//签名，可以理解成这个切入点的一个名称

    @Pointcut("execution(public * com.itheima.uicontroller..*.*(..))")//切入点描述，这个是uiController包的切入点
    public void uiControllerLog(){}

    @Around("controllerLog() || uiControllerLog()") //在切入点的方法run之前要干的
    public Object logBeforeController(ProceedingJoinPoint joinPoint) {


        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();//这个RequestContextHolder是Springmvc提供来获得请求的东西
        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();

        // 记录下请求内容
        logger.info("访问的URL : " + request.getRequestURL().toString());
        logger.info("访问的对应的方法 : " + request.getMethod());
        logger.info("################IP : " + IpUtils.getIpFromRequest(request));

        logger.info("################THE ARGS OF THE CONTROLLER : " + Arrays.toString(joinPoint.getArgs()));
        Object obj = null;
        try {
            obj= joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        //下面这个getSignature().getDeclaringTypeName()是获取包+类名的   然后后面的joinPoint.getSignature.getName()获取了方法名
        logger.info("################CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        //logger.info("################TARGET: " + joinPoint.getTarget());//返回的是需要加强的目标类的对象
        //logger.info("################THIS: " + joinPoint.getThis());//返回的是经过加强后的代理类的对象
        return obj;
    }


}
