package com.hs.gms.srv.api.portal.menu;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hs.gms.srv.api.auth.menu.AuthService;
import com.hs.gms.srv.api.auth.menu.vo.AuthorizedMenuDataVO;
import com.hs.gms.std.common.controller.GMSCommonController;
import com.hs.gms.std.common.error.GMSException;
import com.hs.gms.std.common.response.ResponseVO;

/**
 * MenuController
 * 
 * @author BH Jun
 */
@RestController
public class MenuController extends GMSCommonController {

    @Autowired
    protected AuthService authService;

    @RequestMapping(value = "/portal/menus/lnb", method = RequestMethod.GET, produces = "application/json")
    public ResponseVO<AuthorizedMenuDataVO> getLNBMenuList(HttpServletRequest request) throws Exception {
        List<AuthorizedMenuDataVO> lnbMenuList = null;
        try {
            lnbMenuList = authService.getLNBMenuDataList();
        } catch(Exception e) {
            throw new GMSException(e);
        }
        return super.makeResponseData(HttpStatus.OK, lnbMenuList);
    }

    @RequestMapping(value = "/portal/menus/gnb", method = RequestMethod.GET, produces = "application/json")
    public ResponseVO<AuthorizedMenuDataVO> getGNBMenuList(HttpServletRequest request) throws Exception {
        List<AuthorizedMenuDataVO> gnbMenuList = null;

        try {
            gnbMenuList = authService.getGNBMenuDataList();
        } catch(Exception e) {
            throw new GMSException(e);
        }
        return super.makeResponseData(HttpStatus.OK, gnbMenuList);
    }
}
