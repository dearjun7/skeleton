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
 * BH Jun 2017. 1. 13. First Draft
 */
package com.hs.gms.srv.api.personal.language;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.hs.gms.srv.api.account.UserDataContextHolder;
import com.hs.gms.srv.api.account.vo.UserDataVO;

/**
 * PersonalLanguageServiceImpl.java
 * 
 * @author BH Jun
 */
@Service
public class PersonalLanguageServiceImpl implements PersonalLanguageService {

    @Autowired
    private PersonalLanguageDAO personalLangDAO;

    @Override
    public String getUserLanguage() throws Exception {
        return personalLangDAO.selectUserLanguage(UserDataContextHolder.getUserData());
    }

    @Override
    public String getUserLanguage(UserDataVO userData) throws Exception {
        return personalLangDAO.selectUserLanguage(userData);
    }

    @Override
    public void modifyUserLanguage(String language) throws Exception {
        String localeStr = null;

        try {
            localeStr = LanguageToLocale.valueOf(language.toUpperCase()).getLocaleString();
        } catch(IllegalArgumentException ie) {
            localeStr = LanguageToLocale.KO.getLocaleString();
        }

        personalLangDAO.updateUserLanguage(UserDataContextHolder.getUserData(), localeStr);
        this.resolvingRequestLocale(language);
    }

    private void resolvingRequestLocale(String language) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);

        if(localeResolver == null) {
            throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
        }

        localeResolver.setLocale(request, response, new Locale(language));
    }

    private enum LanguageToLocale {
        KO("ko_KR"),
        EN("en_US"),
        JA("ja_JP"),
        ZH("zh_CN");

        private String localeString;

        LanguageToLocale(String localeString) {
            this.localeString = localeString;
        }

        public String getLocaleString() {
            return this.localeString;
        }
    }
}
