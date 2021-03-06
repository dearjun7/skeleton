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
 * BH Jun 2016. 12. 22. First Draft
 */
package com.hs.gms.srv.api.notification.event.issue;

import java.util.List;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.hs.gms.srv.api.account.vo.UserDataVO;
import com.hs.gms.srv.api.changemng.changerequest.issue.vo.IssueModifyParamVO;
import com.hs.gms.srv.api.notification.event.EventGenerator;

/**
 * IssueModifyEventGenerator.java
 * 
 * @author BH Jun
 */
@Aspect
public class IssueModifyEventGenerator implements EventGenerator {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Pointcut("execution(* com.hs.gms.srv.api.changemng.changerequest.issue.IssueController.modifyIssueTypeChangeRequest(..))")
    public void eventTargetMethodPointCut() {
    }

    @After("eventTargetMethodPointCut()")
    public void generateEventAfterExecuteMethod(JoinPoint joinPoint) {
        Class<?> targetClass = joinPoint.getTarget().getClass();
        Signature signature = joinPoint.getSignature();
        String opName = signature.getName();
        IssueModifyParamVO param = null;
        String workRequestId = null;

        for(Object tmpObj : joinPoint.getArgs()) {
            if(tmpObj instanceof IssueModifyParamVO) {
                param = (IssueModifyParamVO) tmpObj;
            }

            if(tmpObj instanceof String) {
                workRequestId = (String) tmpObj;
            }
        }

        rabbitTemplate.convertAndSend("issueReqEvent", "{\"test\": \"test입니다.\"}");

        System.out.println(
                "================== " + workRequestId + "param : " + param.getProcId() + " : After Advice of PrintStringUsingAnnotation");
        System.out.println("***" + targetClass + "." + opName + "()" + "***");
    }

    @Override
    public List<UserDataVO> getSendTargetUser(Map<String, Object> keyMap) {
        // TODO Auto-generated method stub
        return null;
    }
}
