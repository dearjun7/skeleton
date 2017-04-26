
package com.hs.gms.srv.api.account;

import java.util.concurrent.TimeUnit;

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
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;
import org.springframework.web.util.CookieGenerator;

import com.hs.gms.srv.api.account.vo.UserClaim;
import com.hs.gms.srv.api.account.vo.UserDataVO;
import com.hs.gms.srv.api.personal.language.PersonalLanguageService;
import com.hs.gms.std.common.service.cache.SystemDataAccessor;
import com.hs.gms.std.common.service.cache.UserSessionAccessor;
import com.hs.gms.std.common.service.cache.type.key.SystemDataKeyType;

import net.sf.json.JSONObject;

/**
 * Open API 접근 용 Access Token 생성기.
 * 
 * @author BH Jun
 */

@Component
public class AccessTokenGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessTokenGenerator.class);

    @Value("#{config['gms.cookie.token.name']}")
    private String defaultAccessTokenName;
    @Value("#{config['gms.cookie.token.allow_domain']}")
    private String defaultAllowDomain;
    @Value("#{config['gms.cookie.locale.name']}")
    private String defaultLocaleName;
    @Value("#{config['gms.cookie.token.expire_minutes']}")
    private int defaultTokenExpireMinuteTime;

    private String accessTokenName = null;
    private String allowDomain = null;
    private String localeName = null;
    private int tokenExpireMinuteTime = 0;

    private static final String DEFAULT_LANGUAGE = "ko";

    @Autowired
    private UserSessionAccessor userSessionDataAccess;
    @Autowired
    private SystemDataAccessor systemDataAccess;
    @Autowired
    protected PersonalLanguageService personalLanguageService;

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
        this.accessTokenName = this.defaultAccessTokenName;
        this.allowDomain = this.defaultAllowDomain;
        this.localeName = this.defaultLocaleName;
        this.tokenExpireMinuteTime = this.defaultTokenExpireMinuteTime;
    }

    /**
     * AccessToken을 생성하여, Response Cookie에 setCookie 명령을 Client에 전달.
     * 
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @param userData
     *            UserDataVO
     * @throws Exception
     */
    public void setCookieAccessToken(HttpServletRequest request, HttpServletResponse response, UserDataVO userData) throws Exception {
        this.setCookieAccessToken(request, response, userData, true);
    }

    /**
     * AccessToken을 생성하여, Response Cookie에 setCookie 명령을 Client에 전달.
     * 
     * @param request
     * @param response
     * @param userData
     * @param isSecure
     * @throws Exception
     */
    public void setCookieAccessToken(HttpServletRequest request, HttpServletResponse response, UserDataVO userData, boolean isSecure)
            throws Exception {
        CookieGenerator tokenCookieGenerator = new CookieGenerator();
        CookieGenerator langCookieGenerator = new CookieGenerator();

        String accessToken = this.createAccessToken(userData, request.getServerName());
        String language = personalLanguageService.getUserLanguage(userData);

        tokenCookieGenerator.setCookieHttpOnly(true);
        tokenCookieGenerator.setCookieSecure(isSecure);
        tokenCookieGenerator.setCookieDomain(allowDomain);
        tokenCookieGenerator.setCookieName(accessTokenName);
        tokenCookieGenerator.setCookiePath("/");
        tokenCookieGenerator.addCookie(response, accessToken);

        langCookieGenerator.setCookieHttpOnly(false);
        langCookieGenerator.setCookieDomain(allowDomain);
        langCookieGenerator.setCookieName(localeName);
        langCookieGenerator.setCookiePath("/");
        langCookieGenerator.addCookie(response, language);

        this.setUserDataToCache(userData);
    }

    public void setUserDataToCache(UserDataVO userData) throws Exception {
        String bucketId = userSessionDataAccess.getAccountBucketId(userData.getTenantId(), userData.getDeviceType().toString(),
                userData.getUserId());
        int cacheExpireTime = NumberUtils.convertNumberToTargetClass(TimeUnit.MINUTES.toSeconds(tokenExpireMinuteTime), Integer.class);
        String userDataJson = null;

        userDataJson = JSONObject.fromObject(userData).toString();

        LOGGER.debug("cacheExpireTime :  after " + cacheExpireTime + " secs");

        LOGGER.debug("bucket ID : " + bucketId);
        LOGGER.debug("user Data Json : " + userDataJson);

        userSessionDataAccess.setUserData(bucketId, userDataJson);
    }

    //    /**
    //     * 하나의 클라이언트에 또 다시 이미 로그인이 되어있는지를 체크하는 로직.
    //     * 
    //     * @param request
    //     *            HttpServletRequest
    //     * @return boolean true : 이미 로그인 되어 있음, false : 로그인 정보 없음.
    //     */
    //    public boolean hasAlreadyToken(HttpServletRequest request) {
    //        boolean result = false;
    //
    //        Cookie accessTokenCookie = WebUtils.getCookie(request, accessTokenName);
    //
    //        LOGGER.debug("request accessTokenCookie : " + accessTokenCookie);
    //
    //        if(accessTokenCookie != null) {
    //            result = accessTokenCookie.getValue().length() > 0 ? true : false;
    //        }
    //        LOGGER.debug("duplication token name : " + result);
    //
    //        return result;
    //    }

    public String getBrowserLanguage(HttpServletRequest request) {
        String requestLang = request.getHeader("Accept-Language");
        String result = null;

        if(requestLang != null && !"".equals(requestLang)) {
            result = requestLang.split(",")[0].split("-")[0];
        } else {
            result = DEFAULT_LANGUAGE;
        }

        return result;
    }

    private String createAccessToken(UserDataVO userData, String tokenIssuer) throws Exception {
        String version = systemDataAccess.getTokenVersion();
        String secretKey = systemDataAccess.getSecretKey();
        long epochTime = System.currentTimeMillis() / 1000;
        UserClaim userClaim = this.getUserClaim(userData, tokenIssuer, epochTime, version);

        JSONObject claim = JSONObject.fromObject(userClaim);
        MacSigner signer = new MacSigner(secretKey);
        Jwt jwt = JwtHelper.encode(claim.toString(), signer);
        byte[] byteAccessToken = jwt.bytes();

        LOGGER.debug("encryption secret key : " + secretKey);
        LOGGER.debug("token cookie version(" + SystemDataKeyType.TOKEN_VERSION.toString() + ") : " + version);
        LOGGER.debug(new String(byteAccessToken, 0, byteAccessToken.length));
        LOGGER.debug(jwt.toString());

        return new String(byteAccessToken, 0, byteAccessToken.length);
    }

    private UserClaim getUserClaim(UserDataVO userData, String tokenIssuer, long issueAt, String tokenVersion) {
        UserClaim userClaim = new UserClaim();
        long expireTime = new DateTime(issueAt * 1000).plusMinutes(tokenExpireMinuteTime).getMillis() / 1000;

        LOGGER.debug("issueAt = " + new DateTime(issueAt * 1000) + "(" + issueAt + "), expireTime = " + new DateTime(expireTime * 1000)
                + "(" + expireTime + ")");

        userData.setIssueAt(String.valueOf(issueAt));
        userData.setExpireTime(String.valueOf(expireTime));

        userClaim.setTenantId(userData.getTenantId());
        userClaim.setUserId(userData.getUserId());
        userClaim.setDeviceType(userData.getDeviceType().toString());
        userClaim.setAudience(userData.getClientIP());

        userClaim.setIssuer(tokenIssuer);
        userClaim.setIssueAt(issueAt);
        userClaim.setVersion(tokenVersion);

        return userClaim;
    }
}
