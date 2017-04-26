package com.hs.gms.srv.api.system.code;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hs.gms.std.common.dao.GMSCommonDAO;
import com.hs.gms.std.common.pagination.PaginationQueryParamVO;

/**
 * CodeDAOImpl
 * 
 * @author BH Jun
 */
@Repository
public class CodeDAOImpl extends GMSCommonDAO implements CodeDAO {

    @Override
    public List<CodeVO> selectCodeListForCaching(String tenantId, String pcode, String language) {
        Map<String, String> paramMap = new HashMap<String, String>();

        paramMap.put("pCode", pcode);
        paramMap.put("tenantId", tenantId);
        paramMap.put("language", language);

        return super.getSqlSession().selectList("system.code.selectCodeListForCaching", paramMap);
    }

    @Override
    public List<CodeVO> selectCodeListByParentCode(PaginationQueryParamVO<CodeVO> paramVO, int pageViewSize, int currentPageNum) {
        if(pageViewSize != 0 || currentPageNum != 0) {
            super.setPagination(paramVO, pageViewSize, currentPageNum);
        }

        return super.getSqlSession().selectList("system.code.selectCodeListByParentCode", paramVO);
    }

    @Override
    public List<CodeVO> selectCodeListByParentCode(PaginationQueryParamVO<CodeVO> paramVO) {
        return this.selectCodeListByParentCode(paramVO, 0, 0);
    }

    @Override
    public int selectCodeTotalCountByParentCode(PaginationQueryParamVO<CodeVO> paramVO) {

        return super.getSqlSession().selectOne("selectCodeTotalCountByParentCode", paramVO);
    }

    @Override
    public List<String> selectSomeColumnListInCodeData(GatheringEnableColumnType columnType) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("columnName", columnType.getColumnName());

        return super.getSqlSession().selectList("system.code.selectSomeColumnListInCodeData", param);
    }

    @Override
    public CodeVO selectCode(String tenantId, String pcode, String code, String language) {
        Map<String, String> paramMap = new HashMap<String, String>();

        paramMap.put("pCode", pcode);
        paramMap.put("code", code);
        paramMap.put("tenantId", tenantId);
        paramMap.put("language", language);

        return super.getSqlSession().selectOne("system.code.selectCode", paramMap);
    }

    @Override
    public List<String> selectAllPcodeListByTenantId(String tenantId) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("columnName", GatheringEnableColumnType.P_CODE.getColumnName());
        param.put("tenantId", tenantId);

        return super.getSqlSession().selectList("system.code.selectSomeColumnListInCodeData", param);
    }
}
