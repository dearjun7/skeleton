package com.hs.gms.srv.api.account;

import java.util.List;

import com.hs.gms.srv.api.account.vo.LoginResponseVO;
import com.hs.gms.srv.api.account.vo.UserDataVO;

/**
 * AccountDAO
 * 
 * @author BH Jun
 */
public interface AccountDAO {

    public LoginResponseVO getLogInUserInfo(UserDataVO userData);

    public List<String> getLoginedUserAuth(UserDataVO userData);

    public void modifyUserAccountLock(String userEmail);
}
