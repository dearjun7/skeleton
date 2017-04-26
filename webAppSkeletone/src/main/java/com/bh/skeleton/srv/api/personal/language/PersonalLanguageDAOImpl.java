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

import org.apache.commons.lang.SerializationUtils;
import org.springframework.stereotype.Repository;

import com.hs.gms.srv.api.account.vo.UserDataVO;
import com.hs.gms.std.common.dao.GMSCommonDAO;

/**
 * PersonalLanguageDAOImpl.java
 * 
 * @author BH Jun
 */
@Repository
public class PersonalLanguageDAOImpl extends GMSCommonDAO implements PersonalLanguageDAO {

    @Override
    public String selectUserLanguage(UserDataVO userData) {
        return super.getSqlSession().selectOne("personal.language.selectUserLanguage", userData);
    }

    @Override
    public void updateUserLanguage(UserDataVO userData, String localeStr) {
        UserDataVO paramVO = (UserDataVO) SerializationUtils.clone(userData);

        paramVO.setLanguage(localeStr);

        super.getSqlSession().update("personal.language.updateUserLanguage", paramVO);
    }
}
