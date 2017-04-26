package com.hs.gms.srv.api.account;

import javax.servlet.http.Cookie;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.hs.gms.std.common.test.AbstractApplicationContext;

public class AccountControllerTest extends AbstractApplicationContext {

    @Value("#{config['gms.cookie.token.name']}")
    private String accessTokenName;

    private MockMvc mockMvc;
    private Cookie cookie;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.cookie = super.getAccessToken(mockMvc);
    }

    @Test
    public void testLoginIsOK() throws Exception {
        ResultActions resultActions = this.mockMvc
                .perform(MockMvcRequestBuilders.post("/account/login").contentType(MediaType.APPLICATION_JSON).content(loginParam));

        resultActions.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.cookie().exists(accessTokenName));
    }

    @Test
    public void testLoginWithNotNullClientIPIsOK() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/account/login")
                .contentType(MediaType.APPLICATION_JSON).content(loginParam).header("X-Forwarded-For", "127.0.0.1"));

        resultActions.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.cookie().exists(accessTokenName));
    }

    @Test
    public void testLoginIsAlready() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(
                MockMvcRequestBuilders.post("/account/login").contentType(MediaType.APPLICATION_JSON).content(loginParam).cookie(cookie));

        resultActions.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.cookie().doesNotExist(accessTokenName));
    }

    @Test
    public void testLoginIsWrongPassword() throws Exception {
        String loginParam = "{\"email\":\"" + testEmail + "\", \"accountPw\":\"\", \"deviceType\":\"" + testDeviceType + "\"}";

        ResultActions resultActions = this.mockMvc
                .perform(MockMvcRequestBuilders.post("/account/login").contentType(MediaType.APPLICATION_JSON).content(loginParam));

        resultActions.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.cookie().doesNotExist(accessTokenName));
    }

    @Test
    public void testLoginIsEmptyIDPW() throws Exception {
        String loginParam = "{\"accountId\":\"\", \"accountPw\":\"\", \"deviceType\":\"" + testDeviceType + "\"}";

        ResultActions resultActions = this.mockMvc
                .perform(MockMvcRequestBuilders.post("/account/login").contentType(MediaType.APPLICATION_JSON).content(loginParam));

        resultActions.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.cookie().doesNotExist(accessTokenName));
    }

    @Test
    public void testLogoutIsOK() throws Exception {
        ResultActions resultActions = this.mockMvc
                .perform(MockMvcRequestBuilders.post("/account/logout").contentType(MediaType.APPLICATION_JSON).cookie(cookie));

        resultActions.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testLogoutIsNullToken() throws Exception {
        ResultActions resultActions = this.mockMvc
                .perform(MockMvcRequestBuilders.post("/account/logout").contentType(MediaType.APPLICATION_JSON));
        resultActions.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void testSignOffIsOK() throws Exception {
        ResultActions resultActions = this.mockMvc
                .perform(MockMvcRequestBuilders.post("/account/signoff").contentType(MediaType.APPLICATION_JSON).cookie(cookie));

        resultActions.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    }
}
