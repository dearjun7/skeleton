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
package com.hs.gms.srv.api.auth.menu;

import static com.hs.gms.srv.api.auth.menu.AuthorizedMenuSource.ADM_LNB_AUTH_MENU_LIST_NAME;
import static com.hs.gms.srv.api.auth.menu.AuthorizedMenuSource.GNB_AUTH_MENU_LIST_NAME;
import static com.hs.gms.srv.api.auth.menu.AuthorizedMenuSource.LNB_AUTH_MENU_LIST_NAME;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.hs.gms.srv.api.account.UserDataContextHolder;
import com.hs.gms.srv.api.account.vo.UserDataVO;
import com.hs.gms.srv.api.auth.menu.vo.AuthorizedMenuDataVO;
import com.hs.gms.std.common.service.cache.SystemDataAccessor;
import com.hs.gms.std.common.util.JSONConverter;

import net.sf.json.JSONObject;;

/**
 * AuthServiceImpl.java
 * 
 * @author BH Jun
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Value("#{config['gms.common.system.tenantid']}")
    private String systemTenantId;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;
    @Autowired
    private SystemDataAccessor systemDataAccessor;

    @Override
    public List<AuthorizedMenuDataVO> getLNBMenuDataList(boolean isAdminService) throws Exception {
        UserDataVO userData = UserDataContextHolder.getUserData();
        Locale locale = new Locale(userData.getLanguage());
        String[] userAuthIds = userData.getUserAuth();
        boolean isSystemTenant = systemTenantId.equals(userData.getTenantId()) ? true : false;
        String menuListName = null;

        if(isAdminService) {
            menuListName = ADM_LNB_AUTH_MENU_LIST_NAME;
        } else {
            menuListName = LNB_AUTH_MENU_LIST_NAME;
        }

        return this.getMenuDataList(menuListName, userAuthIds, isSystemTenant, locale);
    }

    @Override
    public List<AuthorizedMenuDataVO> getLNBMenuDataList() throws Exception {
        return this.getLNBMenuDataList(false);
    }

    @Override
    public List<AuthorizedMenuDataVO> getGNBMenuDataList() throws Exception {
        UserDataVO userData = UserDataContextHolder.getUserData();
        Locale locale = new Locale(userData.getLanguage());
        String[] userAuthIds = userData.getUserAuth();
        boolean isSystemTenant = systemTenantId.equals(userData.getTenantId()) ? true : false;

        return this.getMenuDataList(GNB_AUTH_MENU_LIST_NAME, userAuthIds, isSystemTenant, locale);
    }

    private List<AuthorizedMenuDataVO> getMenuDataList(String menuListName, String[] authIds, boolean isSystemTenant, Locale locale)
            throws Exception {
        JSONObject menuListJson = systemDataAccessor.getAuthMenuList(authIds, menuListName, isSystemTenant);

        if(menuListName.equals(ADM_LNB_AUTH_MENU_LIST_NAME)) {
            menuListName = LNB_AUTH_MENU_LIST_NAME;
        }

        List<AuthorizedMenuDataVO> menuList = JSONConverter.jsonObjectToList(menuListJson, menuListName, AuthorizedMenuDataVO.class);

        if(locale != null) {
            //메뉴 명 i18n 맵핑
            for(AuthorizedMenuDataVO menu : menuList) {
                this.setMenuName(menu, locale);
            }
        }

        return menuList;
    }

    private void setMenuName(AuthorizedMenuDataVO menu, Locale locale) {
        menu.setMenuName(messageSource.getMessage(menu.getMenuId(), null, locale));

        if(menu.getHasDesc()) {
            menu.setDescMessage(messageSource.getMessage(menu.getDescMessageId(), null, locale));
        }

        List<AuthorizedMenuDataVO> childMenuList = null;

        if(menu.getHasChildNode()) {
            childMenuList = menu.getChildMenu();
            List<AuthorizedMenuDataVO> newChildMenuList = new ArrayList<AuthorizedMenuDataVO>();

            for(Object objChildMenu : childMenuList) {
                JSONObject jsonChildMenu = JSONObject.fromObject(objChildMenu);
                AuthorizedMenuDataVO childMenu = (AuthorizedMenuDataVO) JSONObject.toBean(jsonChildMenu, AuthorizedMenuDataVO.class);

                newChildMenuList.add(childMenu);

                this.setMenuName(childMenu, locale);
            }

            menu.setChildMenu(newChildMenuList);
        }
    }
}
