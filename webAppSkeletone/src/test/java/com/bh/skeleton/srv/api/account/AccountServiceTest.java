package com.hs.gms.srv.api.account;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import com.hs.gms.srv.api.account.vo.LoginRequestVO;
import com.hs.gms.srv.api.account.vo.UserDataVO;
import com.hs.gms.std.common.error.GMSException;
import com.hs.gms.std.common.service.cache.UserSessionAccessor;
import com.hs.gms.std.common.service.http.HttpConnector;
import com.hs.gms.std.common.test.AbstractApplicationContext;

import net.sf.json.JSONObject;

public class AccountServiceTest extends AbstractApplicationContext {

    @Value("#{config['gms.common.charset']}")
    private String charset;
    @Value("#{config['gms.directory.openapi.url']}")
    private String dirOpenApiURL;

    private JSONObject claim = new JSONObject();

    @Mock
    private UserSessionAccessor userSessionDataAccess;
    @InjectMocks
    private AccountService accountService = new AccountServiceImpl();

    @Before
    public void setUp() throws Exception {
        ReflectionTestUtils.setField(accountService, "charset", charset);
        ReflectionTestUtils.setField(accountService, "dirOpenApiURL", dirOpenApiURL);
        ReflectionTestUtils.setField(accountService, "httpConnect", new HttpConnector());

        claim.put("audience", super.testClientIP);
        claim.put("deviceType", super.testDeviceType);
        claim.put("userKey", "cm3H8P");
        claim.put("tenantId", "001000000");
        claim.put("issueAt", 1455528290);
        claim.put("userId", "000000008");
        claim.put("version", "v1");
        claim.put("issuer", "gms.MyWorks.co.kr");
        claim.put("expireTime", 1455614690);

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLogin() throws Exception {
        UserDataVO userDataVO = this.login(super.testEmail, super.testPw, super.testDeviceType);

        Assert.assertNotNull(userDataVO);
        Assert.assertNotNull(userDataVO.getTenantId());
        Assert.assertNotNull(userDataVO.getUserId());
        Assert.assertNotNull(userDataVO.getUserKey());
        Assert.assertNotNull(userDataVO.getUserName());
        Assert.assertNotNull(userDataVO.getDeviceType());
        Assert.assertNotNull(userDataVO.getUserAuth());
        Assert.assertNotNull(userDataVO.getClientIP());
    }

    @Test
    public void testWrongPasswordLogin() throws Exception {
        try {
            this.login(super.testEmail, "123", super.testDeviceType);

            Assert.fail();
        } catch(GMSException e) {
            Assert.assertSame(GMSException.class, e.getClass());
        }
    }

    @Test
    public void testEmptyIDPWLogin() throws Exception {
        try {
            this.login("", "", super.testDeviceType);

            Assert.fail();
        } catch(GMSException e) {
            Assert.assertSame(GMSException.class, e.getClass());
        }
    }

    @Test
    public void testLogout() throws Exception {
        this.login(super.testEmail, super.testPw, super.testDeviceType);

        accountService.logout(claim);
    }

    @Test
    public void testSignoff() throws Exception {
        accountService.signoff(claim);
    }

    private UserDataVO login(String testEmail, String testPw, String testDeviceType) throws Exception {
        LoginRequestVO loginRequestVO = new LoginRequestVO();

        loginRequestVO.setEmail(testEmail);
        loginRequestVO.setAccountPw(testPw);
        loginRequestVO.setDeviceType(testDeviceType);

        UserDataVO userDataVO = accountService.login(loginRequestVO, testClientIP, "ko");

        UserDataContextHolder.setUserData(userDataVO);

        return userDataVO;
    }
}
