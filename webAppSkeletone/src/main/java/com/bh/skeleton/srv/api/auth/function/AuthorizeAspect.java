/**
 * Any software product designated as "MyWorks Proprietary Software,"
 * including computer software and may include associated media, printed
 * materials, and
 * "online" or electronic documentation ("SOFTWARE PRODUCT") is a copyrighted
 * and
 * proprietary property of MyWorks CO., LTD (“MyWorks”).
 ** The SOFTWARE PRODUCT must
 * (i) be used for MyWorks’s approved business purposes only,
 * (ii) not be contaminated by open source codes,
 * (iii) must not be used in any ways that will require it to be disclosed or
 * licensed freely to third parties or public,
 * (vi) must not be subject to reverse engineering, decompling or diassembling.
 ** MyWorks does not grant the recipient any intellectual property rights,
 * indemnities or warranties and
 * takes on no obligations regarding the SOFTWARE PRODUCT
 * except as otherwise agreed to under a separate written agreement with the
 * recipient,
 ** Revision History
 * Author Date Description
 * ------------------- ---------------- --------------------------
 * BH Jun 2016. 6. 22. First Draft
 */
package com.hs.gms.srv.api.auth.function;

import javax.annotation.Resource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.hs.gms.srv.api.auth.function.annotation.Authorized;
import com.hs.gms.srv.api.auth.function.type.AuthorizeType;

/**
 * AuthorizeAspect.java
 * 
 * @author BH Jun
 */
@Aspect
@Component
public class AuthorizeAspect {

    @Resource(name = "AuthorizeFunctionManager")
    AuthorizeFunctionManager authFuncManager;

    @Before("@annotation(com.hs.gms.srv.api.auth.function.annotation.Authorized)")
    private void checkAuthorization(JoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
        Authorized authAnnotation = methodSignature.getMethod().getAnnotation(Authorized.class);
        AuthorizeType[] hasAuths = authAnnotation.hasAuths();

        authFuncManager.checkAuthValid(hasAuths);
    }
}
