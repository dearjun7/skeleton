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
import com.hs.gms.srv.api.changemng.changerequest.issue.vo.IssueCreateParamVO;
import com.hs.gms.srv.api.notification.event.EventGenerator;
import com.hs.gms.srv.api.notification.event.vo.EventMessageVO;
import com.hs.gms.srv.api.notification.exchange.NotificationExchangeType;

/**
 * IssueCreateEventGenerator.java
 * 
 * @author BH Jun
 */
@Aspect
public class IssueCreateEventGenerator implements EventGenerator {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Pointcut("execution(* com.hs.gms.srv.api.changemng.changerequest.issue.IssueController.createIssueTypeChangeRequest(..))")
    public void eventTargetMethodPointCut() {
    }

    @After("eventTargetMethodPointCut()")
    public void generateEventAfterExecuteMethod(JoinPoint joinPoint) {
        Class<?> targetClass = joinPoint.getTarget().getClass();
        Signature signature = joinPoint.getSignature();
        String opName = signature.getName();
        IssueCreateParamVO param = null;

        for(Object tmpObj : joinPoint.getArgs()) {
            if(tmpObj instanceof IssueCreateParamVO) {
                param = (IssueCreateParamVO) tmpObj;
            }
        }

        EventMessageVO eventMessageVO = new EventMessageVO();

        this.sendEvent(rabbitTemplate, NotificationExchangeType.ISSUE_REQ_EVENT, eventMessageVO);
    }

    @Override
    public List<UserDataVO> getSendTargetUser(Map<String, Object> keyMap) {
        // TODO Auto-generated method stub
        return null;
    }
}
