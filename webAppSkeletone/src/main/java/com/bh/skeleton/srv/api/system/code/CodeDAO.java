package com.hs.gms.srv.api.system.code;

import java.util.List;

import com.hs.gms.std.common.pagination.PaginationQueryParamVO;

/**
 * CodeDAO
 * 
 * @author BH Jun
 */
public interface CodeDAO {

    public List<CodeVO> selectCodeListForCaching(String tenantId, String pcode, String language);

    public List<CodeVO> selectCodeListByParentCode(PaginationQueryParamVO<CodeVO> paramVO);

    public int selectCodeTotalCountByParentCode(PaginationQueryParamVO<CodeVO> queryParamVO);

    public List<CodeVO> selectCodeListByParentCode(PaginationQueryParamVO<CodeVO> queryParamVO, int pageViewSize, int currentPageNum);

    public List<String> selectSomeColumnListInCodeData(GatheringEnableColumnType columnType);

    public CodeVO selectCode(String tenantId, String pcode, String code, String language);

    public List<String> selectAllPcodeListByTenantId(String tenantId);
}
