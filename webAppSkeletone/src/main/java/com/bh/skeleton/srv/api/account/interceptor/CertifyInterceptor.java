package com.hs.gms.srv.api.account.interceptor;

import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.InvalidSignatureException;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import com.hs.gms.srv.api.account.AccessTokenGenerator;
import com.hs.gms.srv.api.account.CookieOperations;
import com.hs.gms.srv.api.account.UserDataContextHolder;
import com.hs.gms.srv.api.account.error.AccountErrorCode;
import com.hs.gms.srv.api.account.vo.AccessToken;
import com.hs.gms.srv.api.account.vo.UserDataVO;
import com.hs.gms.std.common.datasource.DataSourceContextHolder;
import com.hs.gms.std.common.datasource.type.DataSourceType;
import com.hs.gms.std.common.error.CommonErrorCode;
import com.hs.gms.std.common.error.GMSException;
import com.hs.gms.std.common.service.cache.SystemDataAccessor;
import com.hs.gms.std.common.service.cache.TenantCacheAccessor;
import com.hs.gms.std.common.service.cache.UserSessionAccessor;
import com.hs.gms.std.common.service.cache.type.key.UserSessionKeyType;

import net.sf.json.JSONObject;

/**
 * User access Token의 인증 및 검증 처리 Interceptor.
 * 
 * @author BH Jun
 */
public class CertifyInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CertifyInterceptor.class);

    @Value("#{config['gms.directory.proxy.context']}")
    private String dirURI;
    @Value("#{config['gms.filestorage.proxy.context']}")
    private String fstURI;
    @Value("#{config['gms.square.proxy.context']}")
    private String sqrURI;
    @Value("#{config['gms.common.web.domain']}")
    private String indexURL;

    @Autowired
    private SystemDataAccessor systemDataAccess;
    @Autowired
    private UserSessionAccessor userSessionDataAccess;
    @Autowired
    private TenantCacheAccessor tenantCacheDataAccess;
    @Autowired
    private CookieOperations cookieOperations;
    @Autowired
    private AccessTokenGenerator accessTokenGenerator;

    private String accessTokenName = null;
    private String allowDomain = null;
    private String localeName = null;
    private int tokenExpireMinuteTime = 0;

    public void setAccessTokenName(String accessTokenName) {
        this.accessTokenName = accessTokenName;
    }

    public void setAllowDomain(String allowDomain) {
        this.allowDomain = allowDomain;
    }

    public void setLocaleName(String localeName) {
        this.localeName = localeName;
    }

    public void setTokenExpireMinuteTime(int tokenExpireMinuteTime) {
        this.tokenExpireMinuteTime = tokenExpireMinuteTime;
    }

    @PostConstruct
    public void init() {
        if(this.accessTokenName != null) {
            this.accessTokenGenerator.setAccessTokenName(this.accessTokenName);
            this.cookieOperations.setAccessTokenName(this.accessTokenName);
        }

        if(this.allowDomain != null) {
            this.accessTokenGenerator.setAllowDomain(this.allowDomain);
            this.cookieOperations.setAllowDomain(this.allowDomain);
        }

        if(this.localeName != null) {
            this.accessTokenGenerator.setLocaleName(this.localeName);
        }

        if(this.tokenExpireMinuteTime != 0) {
            this.accessTokenGenerator.setTokenExpireMinuteTime(this.tokenExpireMinuteTime);
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            AccessToken accessToken = cookieOperations.convertCookieToAccessToken(request);

            this.verifyUserAccessToken(response, accessToken);
            this.certifyUserAccessToken(request, response, accessToken);

            UserDataVO userData = UserDataContextHolder.getUserData();

            accessTokenGenerator.setUserDataToCache(userData);
        } catch(Exception ex) {
            LOGGER.debug("Error URI : " + request.getRequestURI());

            if((request.getRequestURI()).startsWith(dirURI) || (request.getRequestURI()).startsWith(fstURI)
                    || (request.getRequestURI()).startsWith(sqrURI)) {
                if(RequestMethod.GET.toString().equals(request.getMethod())) {
                    response.sendRedirect(indexURL);
                }
            }

            throw new GMSException(ex);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        DataSourceContextHolder.clearDataSourceType();
        UserDataContextHolder.clearUserData();
    }

    private void verifyUserAccessToken(HttpServletResponse response, AccessToken accessToken) throws Exception {
        LOGGER.debug("verifyUserAccessToken" + accessToken);

        try {
            String secretKey = systemDataAccess.getSecretKey();
            String requestedToken = accessToken.getToken();

            LOGGER.debug("requested token : " + requestedToken);

            MacSigner signer = new MacSigner(secretKey);
            Jwt verifiedJwt = JwtHelper.decodeAndVerify(requestedToken, signer);

            LOGGER.debug("verifiedJwt : " + verifiedJwt);
        } catch(GMSException ge) {
            if(ge.getErrorCode().equals(CommonErrorCode.NO_DATA_FOUND)) {
                throw new GMSException(AccountErrorCode.SECRET_KEY_IS_NULL);
            } else {
                throw new GMSException(ge);
            }
        } catch(InvalidSignatureException ise) {
            cookieOperations.removeCookieAccessToken(response);
            userSessionDataAccess.removeUserData(userSessionDataAccess.getAccountBucketId(JSONObject.fromObject(accessToken.getClaim())));

            throw new GMSException(ise, AccountErrorCode.INVALID_TOKEN);
        } catch(Exception e) {
            throw new Exception(e);
        }
    }

    private void certifyUserAccessToken(HttpServletRequest request, HttpServletResponse response, AccessToken accessToken)
            throws Exception {
        LOGGER.debug("certifyUserAccessToken" + accessToken.toString());

        JSONObject claim = JSONObject.fromObject(accessToken.getClaim());
        Locale locale = (Locale) request.getAttribute(CookieLocaleResolver.class.getName() + ".LOCALE");

        String reqVersion = (String) claim.get("version");
        String reqIssueAt = String.valueOf(claim.get("issueAt"));
        String reqAudience = (String) claim.get("audience");
        String reqTenantId = (String) claim.get("tenantId");
        String storedVersion = null;

        try {
            storedVersion = systemDataAccess.getTokenVersion();

        } catch(GMSException ge) {
            if(ge.getErrorCode().equals(CommonErrorCode.NO_DATA_FOUND)) {
                throw new GMSException(AccountErrorCode.TOKEN_VERSION_IS_NULL);
            } else {
                throw new GMSException(ge);
            }
        }

        String bucketId = userSessionDataAccess.getAccountBucketId(claim);

        LOGGER.debug("reqVersion : " + reqVersion);
        LOGGER.debug("storedVersion : " + storedVersion);
        LOGGER.debug("reqIssueAt : " + reqIssueAt);
        LOGGER.debug("reqAudience : " + reqAudience);
        LOGGER.debug("reqTenantId : " + reqTenantId);

        try {
            this.checkTokenVersion(storedVersion, reqVersion);

            UserDataContextHolder.setUserData(this.checkAndGetTokenUser(bucketId, reqIssueAt, locale));
            DataSourceContextHolder.setDatasourceType(DataSourceType.valueOf(tenantCacheDataAccess.getTenantDatasource(reqTenantId)));

        } catch(GMSException e1) {
            cookieOperations.removeCookieAccessToken(response);

            if(!(e1.getErrorCode().equals(AccountErrorCode.LOGEDIN_ANOTHER_CLIENT))) {
                userSessionDataAccess.removeUserData(bucketId);
            }

            throw new GMSException(e1);
        } catch(Exception e2) {
            throw new Exception(e2);
        }
    }

    private void checkTokenVersion(String storedVersion, String reqVersion) throws GMSException {
        if(!reqVersion.equals(storedVersion)) {
            throw new GMSException(AccountErrorCode.DOES_NOT_MATCH_TOKEN_VERSION);
        }
    }

    private UserDataVO checkAndGetTokenUser(String bucketId, String reqIssueAt, Locale locale) throws Exception {
        UserDataVO result = null;
        String userSessionKeyType = UserSessionKeyType.USER_DATA.toString();

        LOGGER.debug("bucketId : " + bucketId);
        LOGGER.debug("userSessionKeyType : " + userSessionKeyType);

        if(userSessionDataAccess.hasUserData(bucketId)) {
            JSONObject storedUserData = JSONObject.fromObject(userSessionDataAccess.getUserData(bucketId));
            String storedIssueAt = storedUserData.getString("issueAt");
            long expireTime = Long.parseLong(storedUserData.getString("expireTime"));

            LOGGER.debug("redisUserData : " + storedUserData);
            LOGGER.debug("redisIssueAt : " + storedIssueAt);
            LOGGER.debug("reqIssueAt : " + reqIssueAt);

            if(!storedIssueAt.equals(reqIssueAt)) {
                throw new GMSException(AccountErrorCode.LOGEDIN_ANOTHER_CLIENT);
            }

            result = (UserDataVO) JSONObject.toBean(storedUserData, UserDataVO.class);
            result.setLanguage(locale.getLanguage());
            result.setExpireTime(this.checkTokenExpireTime(expireTime));
        } else {
            throw new GMSException(AccountErrorCode.INVALID_TOKEN);
        }

        return result;
    }

    private String checkTokenExpireTime(long expireTime) throws GMSException {
        long epochTime = System.currentTimeMillis() / 1000;
        long newExpireTime = 0;

        if(epochTime > expireTime) {
            throw new GMSException(AccountErrorCode.TOKEN_IS_EXPIRED);
        }

        newExpireTime = new DateTime(epochTime * 1000).plusMinutes(tokenExpireMinuteTime).getMillis() / 1000;

        return String.valueOf(newExpireTime);
    }
}
