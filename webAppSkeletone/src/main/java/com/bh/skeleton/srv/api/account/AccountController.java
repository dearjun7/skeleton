package com.hs.gms.srv.api.account;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hs.gms.srv.api.account.vo.AccessToken;
import com.hs.gms.srv.api.account.vo.LoginRequestVO;
import com.hs.gms.srv.api.account.vo.LoginResponseVO;
import com.hs.gms.srv.api.account.vo.UserDataVO;
import com.hs.gms.std.common.controller.GMSCommonController;
import com.hs.gms.std.common.error.GMSException;
import com.hs.gms.std.common.response.CommonResponseWrapperVO;
import com.hs.gms.std.common.response.CommonResultDataVO;
import com.hs.gms.std.common.response.ResponseVO;
import com.hs.gms.std.common.service.cache.type.DeviceType;
import com.hs.gms.std.common.service.http.HttpUtil;

import net.sf.json.JSONObject;

/**
 * Account 처리 Controller
 * 
 * @author BH Jun
 */
@RestController
public class AccountController extends GMSCommonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    @Value("#{config['gms.cookie.token.name']}")
    protected String accessTokenName;

    @Autowired
    protected AccountService accountService;
    @Autowired
    protected AccessTokenGenerator accessTokenGenerator;
    @Autowired
    protected CookieOperations cookieOperations;

    @RequestMapping(value = "/account/login", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseVO<LoginResponseVO> doLogIn(@RequestBody @Valid LoginRequestVO loginRequestVO,
            @RequestParam(required = false, value = "isMobileServ") boolean isMobileServ, BindingResult bindingResult,
            HttpServletRequest request, HttpServletResponse response) {
        LoginResponseVO result = null;

        if(bindingResult != null && bindingResult.hasErrors()) {
            throw new GMSException(bindingResult.getFieldError());
        }

        try {
            String ipAddr = HttpUtil.getClientIp(request);
            String deviceType = this.getRequestDeviceType(request).toString();
            UserDataVO userData = null;

            loginRequestVO.setDeviceType(deviceType);

            LOGGER.debug("Login Start - deviceType:" + deviceType.toString());

            //            if(accessTokenGenerator.hasAlreadyToken(request)) {
            //                throw new GMSException(AccountErrorCode.ALREADY_LOGGED_IN);
            //            }

            String language = accessTokenGenerator.getBrowserLanguage(request);

            // directory Server에 로그인을 요청한다.
            userData = accountService.login(loginRequestVO, ipAddr, language);
            accessTokenGenerator.setCookieAccessToken(request, response, userData);
            result = accountService.getUserInfo(request, userData, isMobileServ, false);
        } catch(Exception e) {
            throw new GMSException(e);
        }

        return super.makeResponseData(HttpStatus.OK, result);
    }

    @RequestMapping(value = "/account/logout", method = RequestMethod.POST, produces = {"application/json", "application/xml"})
    public ResponseVO<CommonResultDataVO> doLogOut(HttpServletRequest request, HttpServletResponse response) {
        CommonResponseWrapperVO responseWrapper = new CommonResponseWrapperVO();

        try {
            LOGGER.debug("LogOut Start");

            AccessToken accessToken = cookieOperations.convertCookieToAccessToken(request);

            // directory Server에 로그아웃을 요청한다.
            accountService.logout(JSONObject.fromObject(accessToken.getClaim()));
            cookieOperations.removeCookieAccessToken(response);
        } catch(Exception e) {
            throw new GMSException(e);
        }

        return super.wrappingResponseDataForXML(super.makeResponseData(HttpStatus.OK, super.getProcessSuccessCode()), responseWrapper);
    }

    @RequestMapping(value = "/account/signoff", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseVO<CommonResultDataVO> doSignOff(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.debug("SignOff Start");

        try {
            AccessToken accessToken = cookieOperations.convertCookieToAccessToken(request);

            accountService.signoff(JSONObject.fromObject(accessToken.getClaim()));
            cookieOperations.removeCookieAccessToken(response);
        } catch(Exception e) {
            throw new GMSException(e);
        }

        return super.makeResponseData(HttpStatus.OK, super.getProcessSuccessCode());
    }

    @RequestMapping(value = "/account/checkaccesstoken", method = RequestMethod.GET, produces = "application/json")
    public ResponseVO<CommonResultDataVO> checkAccessToken() {
        return super.makeResponseData(HttpStatus.OK, super.getProcessSuccessCode());
    }

    @RequestMapping(value = "/account/{userId}/picture", method = RequestMethod.GET)
    public void sendUserPicture(HttpServletRequest request, HttpServletResponse response, @PathVariable String userId) throws GMSException {
        try {
            accountService.sendUserPicture(request, response, userId);
        } catch(Exception e) {
            throw new GMSException(e);
        }
    }

    protected DeviceType getRequestDeviceType(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        boolean mobile1 = false;
        boolean mobile2 = false;
        boolean modeler = false;

        if(userAgent != null && !"".equals(userAgent)) {
            mobile1 = userAgent.matches(
                    ".*(iPhone|iPod|Android|Windows CE|BlackBerry|Symbian|Windows Phone|webOS|Opera Mini|Opera Mobi|POLARIS|IEMobile|lgtelecom|nokia|SonyEricsson).*");
            mobile2 = userAgent.matches(".*(LG|SAMSUNG|Samsung).*");
            modeler = userAgent.matches(".*(compatible|Win32|WinHttp|WinRequest).*");
        }

        if(mobile1 || mobile2) {
            return DeviceType.MOBILE;
        }

        if(modeler) {
            return DeviceType.MODELER;
        }

        return DeviceType.WEB;
    }
}
