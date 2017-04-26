package com.hs.gms.srv.api.account;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hs.gms.srv.api.account.vo.LoginResponseVO;
import com.hs.gms.srv.api.account.vo.UserDataVO;
import com.hs.gms.std.common.dao.GMSCommonDAO;

/**
 * AccountDAOImpl
 * 
 * @author BH Jun
 */
@Repository
public class AccountDAOImpl extends GMSCommonDAO implements AccountDAO {

    @Override
    public LoginResponseVO getLogInUserInfo(UserDataVO userData) {
        return super.getSqlSession().selectOne("account.selectUserInfo", userData);
    }

    @Override
    public List<String> getLoginedUserAuth(UserDataVO userData) {
        return super.getSqlSession().selectList("account.selectUserAuth", userData);
    }

    @Override
    public void modifyUserAccountLock(String userEmail) {
        super.getSqlSession().update("account.updateUserAccountLock", userEmail);
    }
}
