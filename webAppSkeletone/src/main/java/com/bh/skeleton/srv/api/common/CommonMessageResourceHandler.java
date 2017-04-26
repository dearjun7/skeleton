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
 * BH Jun 2016. 10. 5. First Draft
 */
package com.hs.gms.srv.api.common;

import java.util.Locale;

import org.springframework.context.MessageSource;

import com.hs.gms.srv.api.account.UserDataContextHolder;
import com.hs.gms.std.common.service.bean.SpringApplicationContext;

/**
 * CommonMessageResourceHandler.java
 * 
 * @author BH Jun
 */
public class CommonMessageResourceHandler {

    /**
     * @param msgCode
     * @param args
     * @return
     */
    public static String getMessage(String msgCode, String... args) throws Exception {
        MessageSource messageSource = (MessageSource) SpringApplicationContext.getBean("messageSource");
        String language = UserDataContextHolder.getUserData().getLanguage();

        return messageSource.getMessage(msgCode, args, new Locale(language));
    }

    /**
     * @param msgCode
     * @return
     */
    public static String getMessage(String msgCode) throws Exception {
        String[] args = null;

        return getMessage(msgCode, args);
    }
}
