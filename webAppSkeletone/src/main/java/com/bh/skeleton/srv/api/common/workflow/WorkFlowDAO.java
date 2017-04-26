package com.hs.gms.srv.api.common.workflow;

import java.util.List;

import com.hs.gms.srv.api.common.workflow.vo.WorkItemProgressDetailVO;
import com.hs.gms.srv.api.common.workflow.vo.WorkItemVO;
import com.hs.gms.srv.api.common.workflow.vo.WorkRequestVO;

public interface WorkFlowDAO<T extends WorkRequestVO> {

    public T selectWorkRequest(String workRequestId);

    public String insertWorkRequest(T workRequestVO) throws Exception;

    public int updateWorkRequest(T workRequestVO);

    public int deleteWorkRequest(String workRequestId);

    public List<WorkItemVO> selectCurrentWorkItemList(String searchWorkItemState, String workRequestId, String currentWorkState);

    public List<WorkItemVO> selectWorkHistory(String workRequestId);

    public boolean selectWorkItemParticipantUserYN(String searchWorkItemState, String workRequestId, String workStateCode);

    public List<WorkItemProgressDetailVO> selectWorkItemProgressDetailList(String workRequestId);

    public int insertWorkItem(WorkItemVO workItemVO);

    public int updateWorkItem(WorkItemVO workItemVO);

    public int deleteWorkItem(String workRequestId);

    public boolean selectWorkRequestorYN(String workRequestId);

    public boolean selectIsEnablePublish(String workRequestId);
}
