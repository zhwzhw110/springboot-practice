package com.souche.db.aop;


import com.souche.db.annotation.PassportAccountIdHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wb-wsp312690
 * @Date 2017-9-15 17:28:57
 */
public class PassportAccountIdHandlerAspect {

    private static final Logger logger = LoggerFactory.getLogger(Logger.class);

    //private PersonRemoteService personRemoteService;

    //public void setPersonRemoteService(PersonRemoteService personRemoteService) {
    //this.personRemoteService = personRemoteService;
    //}
    @Pointcut("execution(public * com.souche.db.service.UserService.*(..))")
    public void aspect(){
    }
    /**
     * 如何切入
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("aspect()")
    public Object passportAccountIdHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法信息
        System.out.println("long");

        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();

        // 获取请求参数列表
        Object[] args = joinPoint.getArgs();
        if (args == null && args.length < 1) {
            return args;
        }

        // 获取方法参数注解信息
        Annotation[][] annotationss = method.getParameterAnnotations();
        for (int i = 0; i < annotationss.length; i++) {
            for (int j = 0; j < annotationss[i].length; j++) {
                // 判断注解类型是否为指定的注解
                if (annotationss[i][j].annotationType().equals(PassportAccountIdHandler.class)) {
                    // 获取方法参数类型
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    Class<?> clzz = parameterTypes[i];
                    // 判断是参数类型是否为基本类型或者是基本类型对应的包装类型，进行不同的处理。
                    if (clzz.isPrimitive() || this.isWrapClass(clzz) || clzz.getSimpleName().equals("String")) {
                        args = this.getSimpleTypeParams(((PassportAccountIdHandler) annotationss[i][j]), args, i);
                    } else {
                        // 当参数类型为复杂类型时，必须指定需要绑定参数的名称，并且注解值不能为空
                        if (!((PassportAccountIdHandler) annotationss[i][j]).value().isEmpty()) {
                            args = this.getComplexTypeParam(clzz, args, ((PassportAccountIdHandler) annotationss[i][j]), i);
                        }
                    }
                }
            }
        }
        return joinPoint.proceed(args);
    }

    /**
     * 获取参数类型为简单类型的数据
     *
     * @param annotation
     * @param args
     * @return
     */
    private Object[] getSimpleTypeParams(PassportAccountIdHandler annotation, Object[] args, int i) {
        // 判断该注解是否为我们需要处理的注解
        if (PassportAccountIdHandler.class.isInstance(annotation)) {
            // 获取方法形参的具体参数
            Object param = args[i];
            try {
               /* PojoResult<Long> accountId = personRemoteService.getAccountIdByPassportAccountId(Long.valueOf(param.toString()));
                if (accountId != null && accountId.getContent() != null) {
                    if (args[i].getClass().getSimpleName().equals("String")) {
                        args[i] = accountId.getContent().toString();
                    } else if (args[i].getClass().getSimpleName().equals("Long")) {
                        args[i] = accountId.getContent();
                    }
                }*/


                args[i] = 111L;


            } catch (RuntimeException e) {
                logger.error("passportAccountId convert to accountId error", e);
            }
        }
        return args;
    }

    /**
     * 复杂类型的处理
     *
     * @param clzz
     * @param args
     * @param annotation
     * @param i
     * @return
     * @throws IllegalAccessException
     */
    private Object[] getComplexTypeParam(Class<?> clzz, Object[] args, PassportAccountIdHandler annotation, int i) throws Exception {
        // 获取形参类型中所有属性及所有父类属性。
        Field[] declaredFields = getAllFields(clzz);
        for (Field field : declaredFields) {
            if (StringUtils.equals(field.getName(), annotation.value())) {
                field.setAccessible(true);
                Long account = Long.parseLong(field.get(args[i]).toString());
                try {
                    //PojoResult<Long> accountId = personRemoteService.getAccountIdByPassportAccountId(account);

                    Object accountId = 222L;

                    if (accountId != null) {
                        // 获取所有方法包括父类方法
                        Method[] allMethods = getAllMethods(clzz);
                        // 拼接set方法名称
                        String setMethod = "set" + annotation.value().substring(0, 1).toUpperCase() + annotation.value().substring(1);
                        for (Method method : allMethods) {
                            // 如果所有方法数组中有该方法则调用方法设置值。
                            if (method.getName().equals(setMethod)) {
                                if (field.getType().getSimpleName().equals("String")) {
                                    method.invoke(args[i], String.valueOf(333L));
                                } else if (field.getType().getSimpleName().equals("Long")) {
                                    method.invoke(args[i], 444L);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("passportAccountId convert to accountId error", e);
                }
            }
        }
        return args;
    }

    /**
     * 判断一个类型是否为包装类型
     *
     * @param clz
     * @return
     */

    private static boolean isWrapClass(Class clz) {
        try {
            return ((Class) clz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取类的所有属性，包括其所有父类的属性。
     *
     * @param clzz
     * @return
     */
    private static Field[] getAllFields(Class<?> clzz) {
        List<Field> fieldList = new ArrayList<>();
        while (clzz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clzz.getDeclaredFields())));
            clzz = clzz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }

    /**
     * 获取所有方法，包括父类方法。
     *
     * @param clzz
     * @return
     */
    private static Method[] getAllMethods(Class<?> clzz) {
        List<Method> methods = new ArrayList<>();
        while (clzz != null) {
            methods.addAll(new ArrayList<>(Arrays.asList(clzz.getDeclaredMethods())));
            clzz = clzz.getSuperclass();
        }
        Method[] methodArr = new Method[methods.size()];
        methods.toArray(methodArr);
        return methodArr;
    }
}