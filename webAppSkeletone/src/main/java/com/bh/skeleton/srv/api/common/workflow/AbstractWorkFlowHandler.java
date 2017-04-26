/**
 * Any software product designated as "MyWorks Proprietary Software,"
 * including computer software and may include associated media, printed
 * materials, and
 * "online" or electronic documentation ("SOFTWARE PRODUCT") is a copyrighted
 * and
 * proprietary property of MyWorks CO., LTD (“MyWorks”).
 ** The SOFTWARE PRODUCT must
 * (i) be used for MyWorks’s approved business purposes only,
 * (ii) not be contaminated by open source codes,
 * (iii) must not be used in any ways that will require it to be disclosed or
 * licensed freely to third parties or public,
 * (vi) must not be subject to reverse engineering, decompling or diassembling.
 ** MyWorks does not grant the recipient any intellectual property rights,
 * indemnities or warranties and
 * takes on no obligations regarding the SOFTWARE PRODUCT
 * except as otherwise agreed to under a separate written agreement with the
 * recipient,
 ** Revision History
 * Author Date Description
 * ------------------- ---------------- --------------------------
 * BH Jun 2016. 5. 20. First Draft
 */
package com.hs.gms.srv.api.common.workflow;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.hs.gms.srv.api.account.UserDataContextHolder;
import com.hs.gms.srv.api.account.vo.UserDataVO;
import com.hs.gms.srv.api.common.CommonMessageResourceHandler;
import com.hs.gms.srv.api.common.workflow.error.WorkRequestErrorCode;
import com.hs.gms.srv.api.common.workflow.state.WorkRequestState;
import com.hs.gms.srv.api.common.workflow.state.action.item.WorkItem;
import com.hs.gms.srv.api.common.workflow.type.FinishWorkActionCode;
import com.hs.gms.srv.api.common.workflow.type.WorkItemStateType;
import com.hs.gms.srv.api.common.workflow.vo.PrtcpVO;
import com.hs.gms.srv.api.common.workflow.vo.WorkItemVO;
import com.hs.gms.srv.api.common.workflow.vo.WorkRequestVO;
import com.hs.gms.std.common.error.GMSException;

/**
 * AbstractWorkFlowHandler.java
 * 
 * @author BH Jun
 */
public abstract class AbstractWorkFlowHandler<T extends WorkRequestVO> {

    private static final String WORKACTION_INIT_VAL = "Progress";
    private static final String CANCEL_WORKACTION_CODE = "CANCEL";
    public static final String SYSTEM_USER = "999999999";
    public static final String SYSTEM_USER_NAME_PROP_ID = "gms.common.system.user.name";

    private WorkFlowDAO<T> workFlowDAO;

    protected final void setWorkFlowDAO(WorkFlowDAO<T> workFlowDAO) {
        this.workFlowDAO = workFlowDAO;
    }

    /**
     * 요청된 RequestState에 따른 workflow를 실행/처리 한다.
     * 
     * @param param
     *            - 요청된 타입의 파라메터 정보
     * @param initRequestState
     *            - workflow 처리에서 사용될 초기화된 RequestState 객체
     * @param requestStateCode
     *            - param에 requestStateCode 정보가 없을 경우 강제로 RequestState에서
     *            처리할 상태값 셋팅하기 위한 requestStateCode
     * @param doPreExecution
     *            - 실행 전 전처리기 수행 여부(최초 상태 처리 시 반드시 수행 필요) true / false
     * @return newWorkRequestId
     *         - 새로 생성된 워크플로우 요청 private Key
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public final String execute(T param, WorkRequestState<T> initRequestState, String requestStateCode, boolean doPreExecution)
            throws Exception {
        WorkItemVO currentWorkItemVO = null;
        WorkRequestState<T> executionState = null;
        WorkRequestState<T> nextExecuteState = null;
        String reqProcTypeCode = initRequestState.getWorkProcessType().getValue();
        String newWorkRequestId = null;
        T currentRequestStateData = null;
        Class<?> clazz = Class.forName(param.getClass().getName());
        T executeRequestStateData = (T) clazz.newInstance();

        BeanUtils.copyProperties(param, executeRequestStateData);

        if(doPreExecution) {
            String workRequestId = this.preExecute(executeRequestStateData, initRequestState, requestStateCode, reqProcTypeCode);
            currentRequestStateData = this.getCurrentWorkRequestData(workRequestId);
            newWorkRequestId = workRequestId;

            executeRequestStateData.setWorkRequestId(newWorkRequestId);
        } else {
            currentRequestStateData = this.getCurrentWorkRequestData(executeRequestStateData.getWorkRequestId());

            this.setAdditionalParamForExecute(executeRequestStateData, currentRequestStateData);
        }

        executionState = initRequestState.getCurrentWorkState(currentRequestStateData.getWorkState());
        currentWorkItemVO = this.getCurrentWorkItem(doPreExecution, executeRequestStateData.getWorkRequestId(), executionState);

        this.mainExecute(executeRequestStateData, currentRequestStateData, currentWorkItemVO, executionState, doPreExecution);

        nextExecuteState = executionState.getCurrentWorkState(currentWorkItemVO.getNextRequestState());

        if(nextExecuteState != null) {
            if(nextExecuteState.isFinishRequestState() && workFlowDAO.selectIsEnablePublish(executeRequestStateData.getWorkRequestId())) {
                this.afterCompleteExecute(executeRequestStateData, nextExecuteState);
            }
        }

        return newWorkRequestId;
    }

    /**
     * 요청된 RequestState에 따른 workflow를 실행/처리 한다.
     * 
     * @param param
     *            - 요청된 타입의 파라메터 정보
     * @param requestStateCode
     *            - param에 requestStateCode 정보가 없을 경우 강제로 RequestState에서
     *            처리할 상태값 셋팅하기 위한 requestStateCode
     * @throws Exception
     */
    public final void execute(T param, WorkRequestState<T> workState) throws Exception {
        this.execute(param, workState, null, false);
    }

    protected String preExecute(T param, WorkRequestState<T> initRequestState, String workState, String workProcessType) throws Exception {
        String workRequestId = null;
        T initParam = this.getInitializedPreExecParam(param, initRequestState, workState, workProcessType);

        workRequestId = workFlowDAO.insertWorkRequest(initParam);

        return workRequestId;
    }

    private void mainExecute(T param, T currentRequestStateData, WorkItemVO currentWorkItemVO, WorkRequestState<T> executionState,
            boolean doPreExecution) throws Exception {
        String reqWorkActionCode = param.getWorkAction();
        List<WorkItem<T>> preWorkers = executionState.getPreWorkItemList(reqWorkActionCode);
        List<WorkItem<T>> postWorkers = executionState.getPostWorkItemList(reqWorkActionCode);
        List<PrtcpVO> prtcpList = null;
        String nextRequestStateArg = this.getNextRequestStateArg(param, currentRequestStateData, executionState);

        currentRequestStateData.setWorkAction(param.getWorkAction());
        currentWorkItemVO
                .setNextRequestState(executionState.getNextRequestStateCode(currentRequestStateData.getWorkAction(), nextRequestStateArg));

        prtcpList = this.getNextParticipantList(currentRequestStateData, executionState);

        boolean isOnlyAllowActionBySystem = executionState.getCurrentWorkAction(reqWorkActionCode).isOnlyAllowActionBySystem();

        if(!isOnlyAllowActionBySystem) {
            this.checkReqUserParticipant(doPreExecution, currentRequestStateData, executionState);
        }

        this.doWork(preWorkers, param);

        if(!doPreExecution) {
            if(this.isEnableToGoNextState(param.getWorkRequestId(), executionState)) {
                this.modifyWorkItemState(param, executionState, isOnlyAllowActionBySystem);

                if(this.isAllowToGoToNextState(param.getWorkRequestId(), executionState.getWorkRequestStateCode())) {
                    this.modifyToNextWorkRequestState(param, currentWorkItemVO.getNextRequestState(),
                            executionState.isFinishRequestState());
                    this.createNextWorkItem(param.getWorkRequestId(), prtcpList, currentWorkItemVO);
                }
            }
        } else {
            this.modifyToNextWorkRequestState(param, currentWorkItemVO.getNextRequestState(), executionState.isFinishRequestState());
            this.createNextWorkItem(currentRequestStateData.getWorkRequestId(), prtcpList, currentWorkItemVO);
        }

        this.doWork(postWorkers, param);
    }

    private T getCurrentWorkRequestData(String workRequestId) {
        T currentRequestStateData = workFlowDAO.selectWorkRequest(workRequestId);

        if(currentRequestStateData == null) {
            throw new GMSException(WorkRequestErrorCode.HAS_NO_CHANGE_REQUEST);
        }

        return currentRequestStateData;
    }

    private void modifyToNextWorkRequestState(T param, String nextRequestStateCode, boolean isFinishState) {

        if(!isFinishState) {
            param.setWorkState(nextRequestStateCode);
            workFlowDAO.updateWorkRequest(param);
        }
    }

    private void afterCompleteExecute(T param, WorkRequestState<T> executionState) throws Exception {
        T currentWorkRequestVO = this.getCurrentWorkRequestData(param.getWorkRequestId());
        WorkItemVO afterCompleteWorkItem = null;
        String finishWorkActionCode = FinishWorkActionCode.FINISH.getFinishWorkActionCode();
        String currentWorkRequestStateCode = executionState.getWorkRequestStateCode();
        List<WorkItem<T>> preWorkers = null;
        List<WorkItem<T>> postWorkers = null;

        currentWorkRequestVO.setWorkAction(finishWorkActionCode);
        currentWorkRequestVO.setWorkState(currentWorkRequestStateCode);
        param.setWorkAction(finishWorkActionCode);
        param.setWorkState(executionState.getWorkRequestStateCode());

        String reqWorkActionCode = currentWorkRequestVO.getWorkAction();

        preWorkers = executionState.getPreWorkItemList(reqWorkActionCode);
        postWorkers = executionState.getPostWorkItemList(reqWorkActionCode);
        afterCompleteWorkItem = this.getCurrentWorkItem(false, currentWorkRequestVO.getWorkRequestId(), executionState);
        afterCompleteWorkItem.setWorkAction(finishWorkActionCode);

        this.doWork(preWorkers, param);
        this.modifyWorkItemState(currentWorkRequestVO, executionState, false);
        this.modifyToNextWorkRequestState(param, afterCompleteWorkItem.getNextRequestState(), executionState.isFinishRequestState());
        this.doWork(postWorkers, param);
    }

    private void doWork(List<WorkItem<T>> workers, T param) throws Exception {
        if(workers != null && workers.size() > 0) {
            for(WorkItem<T> worker : workers) {
                worker.work(param);
            }
        }
    }

    private void createNextWorkItem(String workRequestId, List<PrtcpVO> prtcpList, WorkItemVO workItemVO) {
        WorkItemVO tmpWorkItemVO = new WorkItemVO();

        tmpWorkItemVO.setWorkRequestId(workRequestId);
        tmpWorkItemVO.setWorkItemState(WorkItemStateType.CREATED.getWorkItemStateCode());
        tmpWorkItemVO.setWorkState(workItemVO.getNextRequestState());
        tmpWorkItemVO.setWorkAction(WORKACTION_INIT_VAL);

        for(PrtcpVO prtcpVO : prtcpList) {
            tmpWorkItemVO.setPrtcp(prtcpVO.getPrtcp());
            tmpWorkItemVO.setPrtcpAuth(prtcpVO.getPrtcpAuth());
            tmpWorkItemVO.setPrtcpName(prtcpVO.getPrtcpName());
            tmpWorkItemVO.setPrtcpType(prtcpVO.getPrtcpType());

            workFlowDAO.insertWorkItem(tmpWorkItemVO);
        }
    }

    private boolean isAllowToGoToNextState(String workRequestId, String currentRequestStateCode) {
        boolean result = false;
        List<WorkItemVO> currentWorkItemList = workFlowDAO.selectCurrentWorkItemList(WorkItemStateType.CREATED.getWorkItemStateCode(),
                workRequestId, currentRequestStateCode);

        if(currentWorkItemList.size() == 0) {
            result = true;
        }

        return result;
    }

    private WorkItemVO getCurrentWorkItem(boolean doPreExecution, String workRequestId, WorkRequestState<T> executionState) {
        WorkItemVO result = null;
        List<WorkItemVO> checkWorkItemList = null;

        if(doPreExecution) {
            result = new WorkItemVO();
            result.setWorkRequestId(workRequestId);
            result.setWorkSeq(1);
            result.setWorkAction(WORKACTION_INIT_VAL);
            result.setWorkItemState(WorkItemStateType.CREATED.getWorkItemStateCode());
        } else {
            checkWorkItemList = workFlowDAO.selectCurrentWorkItemList(WorkItemStateType.CREATED.getWorkItemStateCode(), workRequestId,
                    executionState.getWorkRequestStateCode());

            if(checkWorkItemList.size() > 0) {
                result = checkWorkItemList.get(0);
            } else {
                if(executionState.isFinishRequestState()) {
                    throw new GMSException(WorkRequestErrorCode.ALREADY_FINISH_REQUEST);
                } else {
                    throw new GMSException(WorkRequestErrorCode.ALREADY_APPROVE_REQUEST);
                }
            }

            this.checkWorkItemFinishState(result.getWorkAction());
        }
        return result;
    }

    private void modifyWorkItemState(T param, WorkRequestState<T> executionState, boolean isOnlyAllowActionBySystem) throws Exception {
        List<WorkItemVO> currentWorkItemList = workFlowDAO.selectCurrentWorkItemList(WorkItemStateType.CREATED.getWorkItemStateCode(),
                param.getWorkRequestId(), executionState.getWorkRequestStateCode());
        UserDataVO userData = UserDataContextHolder.getUserData();
        String workItemStateCode = null;
        String reqWorkActionCode = executionState.getCurrentWorkAction(param.getWorkAction()).getWorkActionCode();

        if(isOnlyAllowActionBySystem) {
            UserDataVO copyUserData = new UserDataVO();
            BeanUtils.copyProperties(userData, copyUserData);

            copyUserData.setUserId(SYSTEM_USER);
            copyUserData.setDeptId(SYSTEM_USER);
            copyUserData.setUserName(CommonMessageResourceHandler.getMessage(SYSTEM_USER_NAME_PROP_ID));

            userData = copyUserData;
        }

        for(WorkItemVO tmpWorkItem : currentWorkItemList) {
            boolean isParticipant = false;

            if(executionState.isAllowToRequestAllUser()) {
                isParticipant = true;
            } else {
                isParticipant = this.isCurrentUserInWorkItemParticipant(userData.getUserId(), userData.getDeptId(), tmpWorkItem.getPrtcp(),
                        tmpWorkItem.getPrtcpType());
            }

            if(executionState.isRaceConditionState()) {
                if(reqWorkActionCode.toUpperCase().equals(CANCEL_WORKACTION_CODE)) {
                    workItemStateCode = WorkItemStateType.DEADWITEM.getWorkItemStateCode();
                } else {
                    workItemStateCode = this.getWorkItemStatByRequestStateOfRaceCondition(isParticipant);
                }

                this.setModifyWorkItemParams(tmpWorkItem, reqWorkActionCode, workItemStateCode, param.getWorkDesc(), userData);
                workFlowDAO.updateWorkItem(tmpWorkItem);
            } else if(executionState.hasCancelAction()
                    && executionState.getCurrentWorkAction(param.getWorkAction()).isOnlyAllowActionByRequestorAuth()) {
                workItemStateCode = WorkItemStateType.DEADWITEM.getWorkItemStateCode();
                this.setModifyWorkItemParams(tmpWorkItem, reqWorkActionCode, workItemStateCode, param.getWorkDesc(), userData);
                workFlowDAO.updateWorkItem(tmpWorkItem);
            } else {
                if(isParticipant) {
                    workItemStateCode = WorkItemStateType.COMPLETED.getWorkItemStateCode();

                    this.setModifyWorkItemParams(tmpWorkItem, reqWorkActionCode, workItemStateCode, param.getWorkDesc(), userData);
                    workFlowDAO.updateWorkItem(tmpWorkItem);

                    break;
                }
            }
        }
    }

    private void setModifyWorkItemParams(WorkItemVO setTargetVO, String reqWorkActionCode, String workItemStateCode, String workDesc,
            UserDataVO userData) {
        setTargetVO.setWorkItemState(workItemStateCode);
        setTargetVO.setWorker(userData.getUserId());
        setTargetVO.setWorkerName(userData.getUserName());
        setTargetVO.setWorkDeptId(userData.getDeptId());
        setTargetVO.setTenantId(userData.getTenantId());
        setTargetVO.setWorkDesc(workDesc);
        setTargetVO.setWorkAction(reqWorkActionCode);
        setTargetVO.setCmpltDate(this.getNowDate());
    }

    private String getWorkItemStatByRequestStateOfRaceCondition(boolean isParticipant) {
        String result = null;

        if(isParticipant) {
            result = WorkItemStateType.COMPLETED.getWorkItemStateCode();
        } else {
            result = WorkItemStateType.DEADWITEM.getWorkItemStateCode();
        }

        return result;
    }

    private boolean isCurrentUserInWorkItemParticipant(String currentUserId, String currentUserDeptId, String participantId,
            String participantType) {
        boolean result = false;

        if("U".equals(participantType)) {
            if(currentUserId.equals(participantId)) {
                result = true;
            }
        } else if("D".equals(participantType)) {
            if(currentUserDeptId.equals(participantId)) {
                result = true;
            }
        }

        return result;
    }

    private void checkWorkItemFinishState(String currentWorkActionCode) {
        String savedWorkAction = currentWorkActionCode == null ? "" : currentWorkActionCode;

        if((savedWorkAction).equals(FinishWorkActionCode.FINISH.getFinishWorkActionCode())) {
            throw new GMSException(WorkRequestErrorCode.ALREADY_FINISH_REQUEST);
        }
    }

    private Date getNowDate() {
        java.util.Date utilDate = new java.util.Date();

        return new Date(utilDate.getTime());
    }

    /**
     * workflowDAO를 set하기 위한 초기화 메소드
     */
    public abstract void init();

    protected abstract T getInitializedPreExecParam(T param, WorkRequestState<T> initRequestState, String workState,
            String workProcessType);

    protected abstract void setAdditionalParamForExecute(T param, T currentRequestStateData);

    protected abstract String getNextRequestStateArg(T param, T currentRequestStateData, WorkRequestState<T> executionState)
            throws Exception;

    protected abstract boolean isEnableToGoNextState(String workRequestId, WorkRequestState<T> executionState);

    protected abstract void checkReqUserParticipant(boolean doPreExecution, T param, WorkRequestState<T> executionState);

    protected abstract List<PrtcpVO> getNextParticipantList(T currentRequestStateData, WorkRequestState<T> executionState) throws Exception;
}
