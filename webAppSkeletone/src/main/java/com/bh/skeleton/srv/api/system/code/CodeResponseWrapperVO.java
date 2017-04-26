package com.hs.gms.srv.api.system.code;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.hs.gms.std.common.response.ResponseVO;

/**
 * CodeResponseWrapperVO
 * 
 * @author BH Jun
 */
@XmlRootElement(name = "response")
@XmlSeeAlso({CodeVO.class})
public class CodeResponseWrapperVO extends ResponseVO<CodeVO> {

    /**
     * 
     */
    private static final long serialVersionUID = -5507923825693793330L;

}
