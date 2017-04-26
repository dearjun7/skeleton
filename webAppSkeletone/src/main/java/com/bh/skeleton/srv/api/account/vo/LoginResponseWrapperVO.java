package com.hs.gms.srv.api.account.vo;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.hs.gms.std.common.response.ResponseVO;

/**
 * LoginResponsWrapperVO
 * 
 * @author BH Jun
 */
@XmlRootElement(name = "response")
@XmlSeeAlso({LoginResponseVO.class})
public class LoginResponseWrapperVO extends ResponseVO<LoginResponseVO> {

    /**
     * 
     */
    private static final long serialVersionUID = 7587473778424837966L;

}
