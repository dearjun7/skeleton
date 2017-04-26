package com.hs.gms.srv.api.auth.menu.vo;

import java.io.Serializable;
import java.util.List;

/**
 * AuthorizedMenuDataVO
 * 
 * @author BH Jun
 */
public class AuthorizedMenuDataVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2716801858555196749L;

    private String displayType;
    private String allowAuthIds;
    private boolean hasChildNode;
    private boolean systemTenantOnly;
    private boolean openByNewWindow;
    private boolean hasDesc;

    private String menuId;
    private String menuName;
    private int seqNum;
    private String accessLink;
    private List<AuthorizedMenuDataVO> childMenu;

    private String descMessageId;
    private String descMessage;
    private String descImgLink;

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public String getAllowAuthIds() {
        return this.allowAuthIds;
    }

    public void setAllowAuthIds(String allowAuthIds) {
        this.allowAuthIds = allowAuthIds;
    }

    public boolean getHasChildNode() {
        return hasChildNode;
    }

    public void setHasChildNode(boolean hasChildNode) {
        this.hasChildNode = hasChildNode;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(int seqNum) {
        this.seqNum = seqNum;
    }

    public String getAccessLink() {
        return accessLink;
    }

    public void setAccessLink(String accessLink) {
        this.accessLink = accessLink;
    }

    public List<AuthorizedMenuDataVO> getChildMenu() {
        return childMenu;
    }

    public void setChildMenu(List<AuthorizedMenuDataVO> childMenu) {
        this.childMenu = childMenu;
    }

    public boolean getSystemTenantOnly() {
        return systemTenantOnly;
    }

    public void setSystemTenantOnly(boolean systemTenantOnly) {
        this.systemTenantOnly = systemTenantOnly;
    }

    public boolean getOpenByNewWindow() {
        return openByNewWindow;
    }

    public void setOpenByNewWindow(boolean openByNewWindow) {
        this.openByNewWindow = openByNewWindow;
    }

    public boolean getHasDesc() {
        return hasDesc;
    }

    public void setHasDesc(boolean hasDesc) {
        this.hasDesc = hasDesc;
    }

    public String getDescMessageId() {
        return descMessageId;
    }

    public void setDescMessageId(String descMessageId) {
        this.descMessageId = descMessageId;
    }

    public String getDescMessage() {
        return descMessage;
    }

    public void setDescMessage(String descMessage) {
        this.descMessage = descMessage;
    }

    public String getDescImgLink() {
        return descImgLink;
    }

    public void setDescImgLink(String descImgLink) {
        this.descImgLink = descImgLink;
    }
}
