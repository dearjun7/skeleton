package com.hs.gms.srv.api.auth.menu.vo;

import java.io.Serializable;
import java.util.List;

/**
 * AuthorizedMenuDataListVO
 * 
 * @author BH Jun
 */
public class AuthorizedMenuDataListVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6742268528638263714L;

    private List<AuthorizedMenuDataVO> menuList;

    public List<AuthorizedMenuDataVO> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<AuthorizedMenuDataVO> menuList) {
        this.menuList = menuList;
    }
}
