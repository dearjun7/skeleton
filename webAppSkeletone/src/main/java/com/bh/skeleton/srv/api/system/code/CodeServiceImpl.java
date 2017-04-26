package com.hs.gms.srv.api.system.code;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hs.gms.srv.api.account.UserDataContextHolder;
import com.hs.gms.srv.api.account.vo.UserDataVO;
import com.hs.gms.std.common.pagination.PaginationQueryParamVO;

/**
 * CodeServiceImpl
 * 
 * @author Bh Jun
 */
@Service
public class CodeServiceImpl implements CodeService {

    @Autowired
    private CodeDAO codeDAO;

    @Override
    public List<CodeVO> getCodeList(int pageViewSize, int currentPageNum) throws Exception {
        CodeVO codeVo = new CodeVO();
        PaginationQueryParamVO<CodeVO> paramVO = new PaginationQueryParamVO<CodeVO>();
        UserDataVO userData = UserDataContextHolder.getUserData();

        codeVo.setTenantId(userData.getTenantId());
        codeVo.setpCode(null);

        paramVO.setQueryParam(codeVo);
        paramVO.setLanguage(userData.getLanguage());

        return codeDAO.selectCodeListByParentCode(paramVO, pageViewSize, currentPageNum);
    }

    @Override
    public int getCodeTotalCount() throws Exception {
        CodeVO codeVo = new CodeVO();
        PaginationQueryParamVO<CodeVO> paramVO = new PaginationQueryParamVO<CodeVO>();
        UserDataVO userData = UserDataContextHolder.getUserData();

        codeVo.setTenantId(userData.getTenantId());
        codeVo.setpCode(null);

        paramVO.setQueryParam(codeVo);
        paramVO.setLanguage(userData.getLanguage());

        return codeDAO.selectCodeTotalCountByParentCode(paramVO);
    }

}
