package com.hs.gms.srv.api.common.workflow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.hs.gms.srv.api.account.UserDataContextHolder;
import com.hs.gms.srv.api.account.vo.UserDataVO;
import com.hs.gms.srv.api.common.workflow.vo.WorkItemProgressDetailVO;
import com.hs.gms.srv.api.common.workflow.vo.WorkItemVO;
import com.hs.gms.srv.api.common.workflow.vo.WorkRequestVO;
import com.hs.gms.std.common.dao.GMSCommonDAO;
import com.hs.gms.std.common.dao.SequenceTable;

public abstract class AbstractWorkFlowDAOImpl<T extends WorkRequestVO>extends GMSCommonDAO implements WorkFlowDAO<T> {

    @Override
    public T selectWorkRequest(String workRequestId) {
        Map<String, Object> params = new HashMap<String, Object>();
        String tenantId = UserDataContextHolder.getUserData().getTenantId();

        params.put("tenantId", tenantId);
        params.put("workRequestId", workRequestId);

        return super.getSqlSession().selectOne("common.workflow.workFlow.selectWorkRequest", params);
    }

    @SuppressWarnings("unchecked")
    @Override
    public String insertWorkRequest(T workRequestVO) throws Exception {
        String workRequestId = super.selectSequence(SequenceTable.WORKREQUEST);
        Class<?> clazz = Class.forName(workRequestVO.getClass().getName());
        T param = (T) clazz.newInstance();

        BeanUtils.copyProperties(workRequestVO, param);

        param.setWorkRequestId(workRequestId);
        param.setTenantId(UserDataContextHolder.getUserData().getTenantId());
        param.setRequestor(UserDataContextHolder.getUserData().getUserId());
        param.setRequestorName(UserDataContextHolder.getUserData().getUserName());
        param.setRequestDeptId(UserDataContextHolder.getUserData().getDeptId());
        param.setRequestDeptName(UserDataContextHolder.getUserData().getDeptName());
        param.setRequestDate(super.getNowDate());

        super.getSqlSession().insert("common.workflow.workFlow.insertWorkRequest", param);

        return workRequestId;
    }

    @Override
    public int updateWorkRequest(T workRequestVO) {
        String tenantId = UserDataContextHolder.getUserData().getTenantId();
        workRequestVO.setTenantId(tenantId);

        return super.getSqlSession().update("common.workflow.workFlow.updateWorkRequest", workRequestVO);
    }

    @Override
    public int deleteWorkRequest(String workRequestId) {
        Map<String, Object> params = new HashMap<String, Object>();
        String tenantId = UserDataContextHolder.getUserData().getTenantId();

        params.put("workRequestId", workRequestId);
        params.put("tenantId", tenantId);

        return super.getSqlSession().delete("common.workflow.workFlow.deleteWorkRequest", params);
    }

    @Override
    public List<WorkItemVO> selectCurrentWorkItemList(String searchWorkItemState, String workRequestId, String currentWorkState) {
        Map<String, Object> params = new HashMap<String, Object>();
        String tenantId = UserDataContextHolder.getUserData().getTenantId();

        params.put("workItemState", searchWorkItemState);
        params.put("tenantId", tenantId);
        params.put("workRequestId", workRequestId);
        params.put("workState", currentWorkState);

        return super.getSqlSession().selectList("common.workflow.workFlow.selectWorkItem", params);
    }

    @Override
    public List<WorkItemVO> selectWorkHistory(String workRequestId) {
        Map<String, Object> params = new HashMap<String, Object>();
        UserDataVO userData = UserDataContextHolder.getUserData();

        params.put("workRequestId", workRequestId);
        params.put("tenantId", userData.getTenantId());
        params.put("language", userData.getLanguage());
        params.put("systemTenantId", super.systemTenantId);

        return super.getSqlSession().selectList("common.workflow.workFlow.selectWorkHistory", params);
    }

    @Override
    public boolean selectWorkItemParticipantUserYN(String searchWorkItemState, String workRequestId, String workStateCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        UserDataVO userData = UserDataContextHolder.getUserData();

        params.put("workItemState", searchWorkItemState);
        params.put("tenantId", userData.getTenantId());
        params.put("workRequestId", workRequestId);
        params.put("userId", userData.getUserId());
        params.put("deptId", userData.getDeptId());
        params.put("workStateCode", workStateCode);

        return (int) super.getSqlSession().selectOne("common.workflow.workFlow.selectWorkItemParticipantUserCount", params) > 0 ? true
                : false;
    }

    @Override
    public List<WorkItemProgressDetailVO> selectWorkItemProgressDetailList(String workRequestId) {
        Map<String, String> queryParam = new HashMap<String, String>();
        UserDataVO userData = UserDataContextHolder.getUserData();

        queryParam.put("systemTenantId", super.systemTenantId);
        queryParam.put("tenantId", userData.getTenantId());
        queryParam.put("language", userData.getLanguage());
        queryParam.put("workRequestId", workRequestId);

        return super.getSqlSession().selectList("common.workflow.workFlow.selectWorkItemProgressDetailList", queryParam);
    }

    @Override
    public int insertWorkItem(WorkItemVO workItemVO) {
        workItemVO.setCreateDate(super.getNowDate());
        workItemVO.setTenantId(UserDataContextHolder.getUserData().getTenantId());

        return super.getSqlSession().insert("common.workflow.workFlow.insertWorkItem", workItemVO);
    }

    @Override
    public int updateWorkItem(WorkItemVO workItemVO) {
        workItemVO.setTenantId(UserDataContextHolder.getUserData().getTenantId());

        return super.getSqlSession().update("common.workflow.workFlow.updateWorkItem", workItemVO);
    }

    @Override
    public int deleteWorkItem(String workRequestId) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("tenantId", UserDataContextHolder.getUserData().getTenantId());
        params.put("workRequestId", workRequestId);

        return super.getSqlSession().delete("common.workflow.workFlow.deleteWorkItem", params);
    }

    @Override
    public boolean selectWorkRequestorYN(String workRequestId) {
        Map<String, String> queryParam = new HashMap<String, String>();
        UserDataVO userData = UserDataContextHolder.getUserData();

        queryParam.put("tenantId", userData.getTenantId());
        queryParam.put("userId", userData.getUserId());
        queryParam.put("workRequestId", workRequestId);

        return (int) super.getSqlSession().selectOne("common.workflow.workFlow.selectWorkRequestorYN", queryParam) > 0 ? true : false;
    }

    @Override
    public boolean selectIsEnablePublish(String workRequestId) {
        Map<String, String> params = new HashMap<String, String>();

        params.put("workRequestId", workRequestId);
        params.put("tenantId", UserDataContextHolder.getUserData().getTenantId());

        return (int) super.getSqlSession().selectOne("common.workflow.workFlow.selectEnablePublishCount", params) == 0 ? true : false;
    }
}
