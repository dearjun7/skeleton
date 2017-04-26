package com.hs.gms.srv.api.account;

import com.hs.gms.srv.api.account.vo.UserDataVO;

/**
 * User의 요청 thread 별로 User Data를 저장, 선택하는 class.
 * 
 * @author BH Jun
 */
public class UserDataContextHolder {

    private static final ThreadLocal<UserDataVO> contextHolder = new ThreadLocal<UserDataVO>();

    public static void setUserData(UserDataVO userDataVO) {

        contextHolder.set(userDataVO);
    }

    public static UserDataVO getUserData() {
        return contextHolder.get();
    }

    public static void clearUserData() {
        contextHolder.remove();
    }
}
