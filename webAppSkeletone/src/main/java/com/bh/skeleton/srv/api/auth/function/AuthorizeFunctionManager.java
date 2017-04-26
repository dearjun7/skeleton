package com.hs.gms.srv.api.auth.function;

import org.springframework.stereotype.Component;

import com.hs.gms.srv.api.account.UserDataContextHolder;
import com.hs.gms.srv.api.account.error.AccountErrorCode;
import com.hs.gms.srv.api.account.vo.UserDataVO;
import com.hs.gms.srv.api.auth.function.type.AuthorizeType;
import com.hs.gms.std.common.error.GMSException;

/**
 * AuthorizeFunctionManager
 * 
 * @author BH Jun
 */
@Component("AuthorizeFunctionManager")
public class AuthorizeFunctionManager {

    public void checkAuthValid(AuthorizeType[] hasAuths) throws Exception {
        if(!this.checkUserAuth(hasAuths)) {
            throw new GMSException(AccountErrorCode.ACCESS_DENIED);
        }
    }

    private boolean checkUserAuth(AuthorizeType[] hasAuths) throws Exception {
        boolean result = false;
        UserDataVO userData = UserDataContextHolder.getUserData();
        String[] userAuthArr = userData.getUserAuth();

        for(String userAuthId : userAuthArr) {
            for(AuthorizeType tmpAuthType : hasAuths) {
                if(userAuthId.equals(tmpAuthType.toString())) {
                    result = true;

                    break;
                }
            }
        }

        return result;
    }
}