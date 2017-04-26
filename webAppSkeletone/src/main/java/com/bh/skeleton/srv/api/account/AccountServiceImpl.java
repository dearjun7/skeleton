package com.hs.gms.srv.api.account;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import com.hs.gms.srv.api.account.error.AccountErrorCode;
import com.hs.gms.srv.api.account.vo.LoginRequestVO;
import com.hs.gms.srv.api.account.vo.LoginResponseVO;
import com.hs.gms.srv.api.account.vo.LoginUserVO;
import com.hs.gms.srv.api.account.vo.UserDataVO;
import com.hs.gms.srv.api.dirweb.DirectoryParameterMaker;
import com.hs.gms.std.common.error.GMSException;
import com.hs.gms.std.common.service.cache.LoginFailAccessor;
import com.hs.gms.std.common.service.cache.UserSessionAccessor;
import com.hs.gms.std.common.service.cache.type.DeviceType;
import com.hs.gms.std.common.service.http.HttpConnector;
import com.hs.gms.std.common.service.reverseproxy.RequestProxySender;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Account 처리 Service Implement class
 * 
 * @author Ma JoonChae
 * @author BH Jun 2016.3.4 refactoring
 */
@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Value("#{config['gms.common.charset']}")
    private String charset;
    @Value("#{config['gms.directory.openapi.url']}")
    private String dirOpenApiURL;
    @Value("#{config['gms.common.login.fail_lock_count']}")
    private String loginFailLockCnt;
    @Value("#{config['gms.directory.pic.file_path']}")
    private String picFilePath;
    @Value("#{config['gms.directory.proxy.url']}")
    private String directiryWebURL;
    @Value("#{config['gms.directory.proxy.context']}")
    private String directoryURI;
    @Value("#{config['gms.common.api.domain']}")
    private String apiURL;
    @Value("#{config['gms.userpic.default.path']}")
    private String defaultUserPicPath;

    @Autowired
    private UserSessionAccessor userSessionDataAccess;
    @Autowired
    private LoginFailAccessor loginFailAccessor;
    @Autowired
    private AccountDAO accountDAO;
    @Autowired
    private HttpConnector httpConnect;
    @Autowired
    private RequestProxySender requestProxySender;

    private Map<String, String> headers = new ConcurrentHashMap<String, String>();

    @Override
    public UserDataVO login(LoginRequestVO loginRequestVO, String ipAddr, String language) throws Exception {
        JSONObject result = null;
        UserDataVO userData = null;
        DirectoryParameterMaker dirParamMaker = new DirectoryParameterMaker();
        String userEmail = loginRequestVO.getEmail();
        String loginPw = loginRequestVO.getAccountPw();
        String callResult = "";
        int userLoginFailCount = 0;

        // x-forwarded-for 설정 : 이중 로그인 처리 시 IP 체크를 위해 설정
        headers.put("X-Forwarded-For", ipAddr);
        headers.put("Content-Type", "application/x-www-form-urlencoded");

        try {
            userLoginFailCount = loginFailAccessor.getLoginFailCount(userEmail);

            if(userLoginFailCount >= Integer.parseInt(loginFailLockCnt)) {
                loginFailAccessor.removeLoginFailCount(userEmail);
                accountDAO.modifyUserAccountLock(userEmail);
            }

            //로그인 처리 시작
            callResult = httpConnect.sendPost(dirOpenApiURL, headers, dirParamMaker.getMakeLoginParams(userEmail, loginPw, language),
                    charset);
            result = JSONObject.fromObject(callResult);

            this.checkDirServiceLinkageError(result, userEmail);

            userData = this.convertJSONObjectToUserData(result, loginRequestVO.getDeviceType());
            userData.setUserAuth(this.getUserAuth(result.getString("id"), result.getString("key"), headers, dirParamMaker));
            userData.setClientIP(ipAddr);

            LOGGER.debug("login result : " + result);

            loginFailAccessor.removeLoginFailCount(userEmail);
        } catch(GMSException e) {
            throw new GMSException(e);
        } catch(Exception ex) {
            throw new GMSException(ex, AccountErrorCode.DIRECTORY_SYSTEM_ERROR);
        }

        return userData;
    }

    @Override
    public void logout(JSONObject claim) throws Exception {
        JSONObject result = null;
        DirectoryParameterMaker dirParamMaker = new DirectoryParameterMaker();
        UserDataVO userData = UserDataContextHolder.getUserData();
        Map<String, String> params = dirParamMaker.getMakeLogOutParams(userData.getUserKey());

        try {
            String responseStr = httpConnect.sendGet(dirOpenApiURL, null, params, charset);
            result = JSONObject.fromObject(responseStr);

            this.checkDirServiceLinkageError(result, null);

            userSessionDataAccess.removeUserData(claim);
            LOGGER.debug("logout result : " + result);
        } catch(Exception ex) {
            throw new GMSException(ex, AccountErrorCode.DIRECTORY_SYSTEM_ERROR);
        }
    }

    @Override
    public void signoff(JSONObject claim) throws Exception {
        String tenantId = claim.getString("tenantId");
        String userId = claim.getString("userId");

        for(DeviceType deviceType : DeviceType.values()) {
            String bucketId = userSessionDataAccess.getAccountBucketId(tenantId, deviceType.toString(), userId);

            userSessionDataAccess.removeUserData(bucketId);
        }
    }

    @Override
    public LoginResponseVO getUserInfo(HttpServletRequest request, UserDataVO userData, boolean isMobileServ, boolean includePicPath)
            throws Exception {
        LoginResponseVO result = null;
        Locale locale = (Locale) request.getAttribute(CookieLocaleResolver.class.getName() + ".LOCALE");
        String picURL = null;

        userData.setLanguage(locale.getLanguage());

        result = accountDAO.getLogInUserInfo(userData);

        if(result != null) {
            result.setUserAuth(accountDAO.getLoginedUserAuth(userData));
        } else {
            result = new LoginResponseVO();
        }

        picURL = apiURL + "/account/" + userData.getUserId() + "/picture";

        if(isMobileServ) {
            result.setUserId(userData.getUserId());
            result.setEmpCode(userData.getEmpcode());
            result.setDeptId(userData.getDeptId());
        }

        result.setPicURL(picURL);
        result.setTenantName(userData.getTenantName());

        return result;
    }

    @Override
    public void sendUserPicture(HttpServletRequest request, HttpServletResponse response, String userId) throws Exception {
        UserDataVO userData = new UserDataVO();
        OutputStream outputStream = null;
        InputStream inputStream = null;
        byte[] buffer = new byte[1024];
        int bytesRead;
        StringBuffer dirReqURL = new StringBuffer();
        CloseableHttpResponse proxyResponse = null;
        String userPicPath = null;

        try {
            userData.setTenantId(UserDataContextHolder.getUserData().getTenantId());
            userData.setUserId(userId);

            userPicPath = this.getUserInfo(request, userData, false, true).getPicPath();
            userPicPath = userPicPath == null || "".equals(userPicPath) ? defaultUserPicPath : userPicPath;

            dirReqURL.append(directiryWebURL);
            dirReqURL.append(directoryURI);
            dirReqURL.append(picFilePath);
            dirReqURL.append(userPicPath);

            proxyResponse = requestProxySender.executeToGetRes(request, HttpMethod.GET, dirReqURL.toString());

            Header[] proxyHeaders = proxyResponse.getAllHeaders();
            int statusCode = proxyResponse.getStatusLine().getStatusCode();

            for(Header tmpHeader : proxyHeaders) {
                response.setHeader(tmpHeader.getName(), tmpHeader.getValue());
            }

            response.setStatus(statusCode);

            if(statusCode == 200) {
                inputStream = new BufferedInputStream(proxyResponse.getEntity().getContent());
                outputStream = new BufferedOutputStream(response.getOutputStream());

                while((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        } catch(Exception e) {
            throw new GMSException(e);
        } finally {
            if(outputStream != null) {
                outputStream.close();
            }
            if(inputStream != null) {
                inputStream.close();
            }
            if(proxyResponse != null) {
                proxyResponse.close();
            }
        }
    }

    // 로그인된 사용자 권한 가져오기
    @SuppressWarnings("unchecked")
    private String[] getUserAuth(String userId, String userKey, Map<String, String> headers, DirectoryParameterMaker dirParamMaker)
            throws Exception {
        String authId = null;
        JSONObject authResultJson = null;
        String authResult = "";

        authResult = httpConnect.sendGet(dirOpenApiURL, headers, dirParamMaker.getMakeUserAuthParams("role", userId, userKey), charset);
        authResultJson = JSONObject.fromObject(authResult);

        this.checkDirServiceLinkageError(authResultJson, null);

        int resultCount = authResultJson.getInt("@totalcount");

        if(resultCount > 1) {
            JSONArray authIdArr = (JSONArray) authResultJson.get("role");
            List<JSONObject> authIdList = (List<JSONObject>) JSONArray.toCollection(authIdArr, JSONObject.class);

            for(JSONObject tmp : authIdList) {
                authId = authId == null ? tmp.getString("code") : (authId + ",") + tmp.getString("code");
            }
        } else if(resultCount == 1) {
            authId = ((JSONObject) authResultJson.get("role")).getString("code");
        }

        return authId == null ? null : authId.split(",");
    }

    // response data to UserDataVO Object
    private UserDataVO convertJSONObjectToUserData(JSONObject jsonObj, DeviceType deviceType) throws Exception {
        UserDataVO userData = new UserDataVO();

        userData.setUserData((LoginUserVO) JSONObject.toBean(jsonObj, LoginUserVO.class));
        userData.setDeviceType(deviceType.toString());

        return userData;
    }

    // directory API 호출결과가 Error 일 때, 처리 로직 
    private void checkDirServiceLinkageError(JSONObject jsonObj, String userEmail) throws GMSException {
        if(jsonObj.containsKey("status")) {
            if("999".equals(jsonObj.getString("status"))) {
                throw new GMSException(AccountErrorCode.DIRECTORY_SYSTEM_ERROR);
            }
        } else if(jsonObj.containsKey("code")) {
            String errorCode = jsonObj.getString("code");

            // Brute Force 공격 방지 대응으로 ID 및 패스워드 불일치에 대한 각각의 directory error code를 하나의 에러코드 통일  
            if("101".equals(errorCode) || "102".equals(errorCode)) {
                loginFailAccessor.setLoginFailCount(userEmail);

                throw new GMSException(AccountErrorCode.DIRECTORY_LOGIN_ERROR);

            } else if("105".equals(errorCode)) {
                throw new GMSException(AccountErrorCode.LOGIN_FAIL_LOCK, String.valueOf(loginFailLockCnt));

            } else {
                throw new GMSException(jsonObj.getString("message"), AccountErrorCode.DIRECTORY_LINKAGE_ERROR);
            }
        }
    }
}
