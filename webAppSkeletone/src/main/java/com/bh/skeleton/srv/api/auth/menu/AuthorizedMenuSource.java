package com.hs.gms.srv.api.auth.menu;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import com.hs.gms.srv.api.auth.menu.vo.AuthorizedMenuDataListVO;
import com.hs.gms.srv.api.auth.menu.vo.AuthorizedMenuDataVO;
import com.hs.gms.std.common.error.GMSException;
import com.hs.gms.std.common.service.cache.SystemDataAccessor;
import com.hs.gms.std.common.service.cache.valuekey.SystemDataValueKey;
import com.hs.gms.std.common.util.JSONConverter;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.BooleanConverter;
import com.thoughtworks.xstream.converters.basic.StringConverter;

import net.sf.json.JSONObject;

/**
 * AuthorizedMenuSource
 * 
 * @author BH Jun
 */
public class AuthorizedMenuSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizedMenuSource.class);

    @Value("classpath:/config/menu/display/lnb-menu.xml")
    private Resource LNBMenuResource;
    @Value("classpath:/config/menu/display/gnb-menu.xml")
    private Resource GNBMenuResource;

    @Value("#{config['gms.common.system.tenantid']}")
    protected String systemTenantId;

    private boolean isAdminService = false;
    private boolean reload = false;

    public static final String ADM_LNB_AUTH_MENU_LIST_NAME = SystemDataValueKey.ADM_LNB_AUTH_MENU_LIST_NAME.getValueKey();
    public static final String LNB_AUTH_MENU_LIST_NAME = SystemDataValueKey.LNB_AUTH_MENU_LIST_NAME.getValueKey();
    public static final String GNB_AUTH_MENU_LIST_NAME = SystemDataValueKey.GNB_AUTH_MENU_LIST_NAME.getValueKey();

    @Autowired
    private SystemDataAccessor systemDataAccessor;

    public void setIsAdminService(boolean isAdminService) {
        this.isAdminService = isAdminService;
    }

    public void setReload(boolean reload) {
        this.reload = reload;
    }

    public void init() throws Exception {

        if(reload) {
            LOGGER.info("Init Loading Authorized Menus");

            if(isAdminService) {
                LOGGER.info("Loading and Set ADMIN-LNB Menu Resource to Redis from file [" + LNBMenuResource.getFilename() + "]");
                this.setAuthMenuToRedis(LNBMenuResource, ADM_LNB_AUTH_MENU_LIST_NAME);
            } else {
                LOGGER.info("Loading and Set LNB Menu Resource to Redis from file [" + LNBMenuResource.getFilename() + "]");
                this.setAuthMenuToRedis(LNBMenuResource, LNB_AUTH_MENU_LIST_NAME);

                LOGGER.info("Loading and Set GNB Menu Resource to Redis from file [" + GNBMenuResource.getFilename() + "]");
                this.setAuthMenuToRedis(GNBMenuResource, GNB_AUTH_MENU_LIST_NAME);
            }
        }
    }

    private void setAuthMenuToRedis(Resource menuResource, String menuListName) throws IOException {
        BufferedInputStream fileReader = null;
        AuthorizedMenuDataListVO menuDataList = null;

        try {
            fileReader = new BufferedInputStream(new FileInputStream(menuResource.getFile()));
            menuDataList = this.convertXmlToMenuDataListVO(fileReader);

            String menuType = menuListName;

            if(menuListName.equals(ADM_LNB_AUTH_MENU_LIST_NAME)) {
                menuListName = LNB_AUTH_MENU_LIST_NAME;
            }

            JSONObject menuList = JSONConverter.listToJsonObject(menuDataList.getMenuList(), menuListName);

            systemDataAccessor.setAuthMenuList(menuList, menuType);
        } catch(Exception e) {
            throw new GMSException(e);
        } finally {
            if(fileReader != null) {
                fileReader.close();
            }
        }
    }

    private AuthorizedMenuDataListVO convertXmlToMenuDataListVO(InputStream fileReader) {
        XStream xstream = new XStream();

        xstream.alias("menu", AuthorizedMenuDataVO.class);
        xstream.alias("childMenu", AuthorizedMenuDataVO.class);
        xstream.alias("menuList", AuthorizedMenuDataListVO.class);

        xstream.addImplicitCollection(AuthorizedMenuDataVO.class, "childMenu");
        xstream.addImplicitCollection(AuthorizedMenuDataListVO.class, "menuList");

        xstream.useAttributeFor(AuthorizedMenuDataVO.class, "displayType");
        xstream.useAttributeFor(AuthorizedMenuDataVO.class, "allowAuthIds");
        xstream.registerConverter(new StringConverter());

        xstream.useAttributeFor(AuthorizedMenuDataVO.class, "hasChildNode");
        xstream.useAttributeFor(AuthorizedMenuDataVO.class, "systemTenantOnly");
        xstream.useAttributeFor(AuthorizedMenuDataVO.class, "openByNewWindow");
        xstream.useAttributeFor(AuthorizedMenuDataVO.class, "hasDesc");
        xstream.registerConverter(new BooleanConverter());

        AuthorizedMenuDataListVO menuDataList = (AuthorizedMenuDataListVO) xstream.fromXML(fileReader);

        return menuDataList;
    }
}