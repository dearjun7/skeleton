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
package com.hs.gms.srv.api.notification.event;

import java.util.List;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.hs.gms.srv.api.account.vo.UserDataVO;
import com.hs.gms.srv.api.notification.event.vo.EventMessageVO;
import com.hs.gms.srv.api.notification.exchange.NotificationExchangeType;

import net.sf.json.JSONObject;

/**
 * EventGenerator.java
 * 
 * @author BH Jun
 */
public interface EventGenerator {

    public void eventTargetMethodPointCut();

    public void generateEventAfterExecuteMethod(JoinPoint joinPoint);

    public List<UserDataVO> getSendTargetUser(Map<String, Object> keyMap);

    public default void sendEvent(RabbitTemplate rabbitTemplate, NotificationExchangeType exchageType, EventMessageVO eventMessageVO) {
        JSONObject message = JSONObject.fromObject(eventMessageVO);

        rabbitTemplate.convertAndSend(exchageType.getNotificationExchageName(), message.toString());
    }
}
