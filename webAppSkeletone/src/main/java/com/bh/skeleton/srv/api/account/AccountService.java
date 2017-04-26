package com.hs.gms.srv.api.account;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hs.gms.srv.api.account.vo.LoginRequestVO;
import com.hs.gms.srv.api.account.vo.LoginResponseVO;
import com.hs.gms.srv.api.account.vo.UserDataVO;

import net.sf.json.JSONObject;

/**
 * Account 처리 Service Interface
 * 
 * @author MaJoonChae
 * @author BH Jun 2016.3.4 JavaDoc 작성
 */
public interface AccountService {

    /**
     * User Login 처리
     * 
     * @param loginRequestVO
     *            LoginRequestVO - 사용자 Id와 Password 데이터가 존재하는 VO
     * @param ipAddr
     *            String - 요청된 사용자의 X-forwarded-for ip 주소
     * @param language
     *            String - 브라우저에 셋팅 된 기본 언어 코드
     * @return UserDataVO
     *         로그인 결과로 반환된 사용자 정보
     * @throws Exception
     */
    public UserDataVO login(LoginRequestVO loginRequestVO, String ipAddr, String language) throws Exception;

    /**
     * User Logout 처리
     * 
     * @param claim
     *            JSONObject - User Access Token에서의 사용자 정보를 담고 있는 body Data
     * @throws Exception
     */
    public void logout(JSONObject claim) throws Exception;

    /**
     * 사용자 탈퇴 시 사용자 Access token 삭제 처리
     * 
     * @param claim
     *            JSONObject - User Access Token에서의 사용자 정보를 담고 있는 body Data
     * @throws Exception
     */
    public void signoff(JSONObject claim) throws Exception;

    /**
     * @param request
     * @param userData
     * @param isMobileServ
     * @return LoginResponseVO
     * @throws Exception
     */
    public LoginResponseVO getUserInfo(HttpServletRequest request, UserDataVO userData, boolean isMobileSer, boolean includePicPath)
            throws Exception;

    public void sendUserPicture(HttpServletRequest request, HttpServletResponse response, String userId) throws Exception;
}