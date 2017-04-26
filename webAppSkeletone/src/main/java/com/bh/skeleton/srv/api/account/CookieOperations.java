package com.hs.gms.srv.api.account;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.util.CookieGenerator;
import org.springframework.web.util.WebUtils;

import com.hs.gms.srv.api.account.error.AccountErrorCode;
import com.hs.gms.srv.api.account.vo.AccessToken;
import com.hs.gms.std.common.error.GMSException;

/**
 * CookieOperations
 * 
 * @author BH Jun
 */
@Component
public class CookieOperations {

    private static final Logger LOGGER = LoggerFactory.getLogger(CookieOperations.class);

    @Value("#{config['gms.cookie.token.name']}")
    private String defatultAccessTokenName;
    @Value("#{config['gms.cookie.token.allow_domain']}")
    private String defaultAllowDomain;

    private String accessTokenName = null;
    private String allowDomain = null;

    public void setAccessTokenName(String accessTokenName) {
        this.accessTokenName = accessTokenName;
    }

    public void setAllowDomain(String allowDomain) {
        this.allowDomain = allowDomain;
    }

    @PostConstruct
    public void init() {
        this.accessTokenName = this.defatultAccessTokenName;
        this.allowDomain = this.defaultAllowDomain;
    }

    public AccessToken convertCookieToAccessToken(HttpServletRequest request) throws GMSException {
        AccessToken accessToken = new AccessToken();

        Cookie accessTokenCookie = WebUtils.getCookie(request, accessTokenName);

        LOGGER.debug("request accessTokenCookie : " + accessTokenCookie);

        if(accessTokenCookie != null) {
            String tmpTokenString = accessTokenCookie.getValue();

            LOGGER.debug("tmpTokenString : " + tmpTokenString);

            accessToken.setToken(tmpTokenString);

            try {
                accessToken.setClaim(JwtHelper.decode(tmpTokenString).getClaims());
            } catch(IllegalArgumentException e) {
                throw new GMSException(AccountErrorCode.INVALID_TOKEN);
            }
        } else {
            throw new GMSException(AccountErrorCode.ACCESS_TOKEN_IS_NULL);
        }

        return accessToken;
    }

    public void removeCookieAccessToken(HttpServletResponse response) throws Exception {
        CookieGenerator cookieGenerator = new CookieGenerator();

        cookieGenerator.setCookieHttpOnly(true);
        cookieGenerator.setCookieSecure(true);

        LOGGER.debug("remove Cookie Name = " + accessTokenName);
        cookieGenerator.setCookieDomain(allowDomain);
        cookieGenerator.setCookieName(accessTokenName);
        cookieGenerator.setCookiePath("/");
        cookieGenerator.removeCookie(response);
    }
}
