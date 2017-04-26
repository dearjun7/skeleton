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
package com.hs.gms.srv.api.common.workflow.state;

import java.util.List;

import com.hs.gms.srv.api.common.workflow.WorkAuth;
import com.hs.gms.srv.api.common.workflow.error.WorkRequestErrorCode;
import com.hs.gms.srv.api.common.workflow.state.action.WorkAction;
import com.hs.gms.srv.api.common.workflow.state.action.item.WorkItem;
import com.hs.gms.srv.api.common.workflow.type.WorkProcessType;
import com.hs.gms.std.common.error.GMSException;

/**
 * RequestState.java
 * 
 * @author BH Jun
 */
public interface WorkRequestState<T> {

    /**
     * RequestState Code를 반환한다.
     * 
     * @return String requestStateCode
     */
    public String getWorkRequestStateCode();

    /**
     * 현재 RequestState에서 사용되는 초기화 된 WorkAction 객체를 반환한다.
     * 
     * @return WorkAction
     */
    public WorkAction<T> getWorkAction();

    /**
     * 요청된 requestStateCode를 기반으로 현재 사용할 RequestState 객체를 반환한다.
     * 
     * @param requestStateCode
     * @return RequestState
     */
    public WorkRequestState<T> getCurrentWorkState(String requestStateCode);

    /**
     * 현재 요청에 대한 RequestProcessType을 반환한다.<br>
     * RequestProcessType 상세 <br>
     * CHANGE("C")<br>
     * ISSUE("I")<br>
     * ISOINTRO("S")<br>
     * 
     * @return RequestProcessType
     */
    public WorkProcessType getWorkProcessType();

    /**
     * 현재 진행 중인 RequestState를 처리하는 방법이 경합인지 여부를 반환한다.<br>
     * 
     * @return boolean
     *         true - 경합(참여자 중 한사람만 승인해도 다음 RequestState로 진행)
     *         false - 합의(참여자 전체가 승인해야 다음 RequestState로 진행)
     */
    public boolean isRaceConditionState();

    public boolean hasCancelAction();

    /**
     * 현재 RequestState가 모든 사용자에게 요청을 허용한 지를 체크한다.
     * 
     * @return boolean
     *         true - 모든 사용자 상태 변경 허용
     *         false - 참여자로 등록된 사용자에게만 상태 변경 허용
     */
    public boolean isAllowToRequestAllUser();

    /**
     * 현재 요청된 RequestState가 처리 된 후 처리할 다음 RequestState 상태를 TargetVO에 Setting한다.
     * 
     * @param currentWorkActionStr
     * @param arg
     * @param valueSettingTargetVO
     */
    public default String getNextRequestStateCode(String currentWorkActionCode, String arg) {
        WorkAction<T> currentWorkAction = this.getCurrentWorkAction(currentWorkActionCode);
        WorkRequestState<T> nextRequestState = currentWorkAction.getNextWorkRequestState(arg);
        //        String workActionCode = null;

        if(nextRequestState == null) {
            throw new GMSException(WorkRequestErrorCode.HAS_NO_PROCESSING_STATE);
        }

        //        workActionCode = currentWorkAction.getWorkActionCode();

        //        valueSettingTargetVO.setNextRequestState(nextRequestState.getRequestStateCode());
        //        valueSettingTargetVO.setWorkAction(workActionCode);

        return nextRequestState.getWorkRequestStateCode();
    }

    /**
     * 요청된 workActionCode에 대치되는 Type 값을 지닌 WorkAction 객체를 반환한다.
     * 
     * @param reqWorkActionCode
     *            String 요청된 WorkActionCode
     * @return WorkAction - Type 값을 가진 WorkAction
     */
    public default WorkAction<T> getCurrentWorkAction(String reqWorkActionCode) {
        WorkAction<T> result = null;

        if((WorkProcessType.ISSUE.getValue()).equals(this.getWorkProcessType().getValue()) && reqWorkActionCode == null) {
            reqWorkActionCode = "Register";
        }

        try {
            WorkAction<T> WorkAction = this.getWorkAction();

            if(WorkAction == null) {
                throw new GMSException(WorkRequestErrorCode.HAS_NO_PROCESSING_STATE);
            }

            result = WorkAction.getWorkAction(reqWorkActionCode);
        } catch(IllegalArgumentException ie) {
            throw new GMSException(WorkRequestErrorCode.INCORRECT_WORK_ACTOIN_TYPE);
        }

        return result;
    }

    /**
     * 현재 처리 중인 RequestState에서 처리할 PreWorker의 List를 반환한다.
     * 
     * @param requestParam
     *            T
     * @return List - RequestStateWorker List
     */
    public default List<WorkItem<T>> getPreWorkItemList(String reqWorkActionCode) {
        WorkAction<T> currentWorkAction = this.getCurrentWorkAction(reqWorkActionCode);

        return currentWorkAction.getPreWorkItemList();
    }

    /**
     * 현재 처리 중인 RequestState에서 처리할 PostWorker의 List를 반환한다.
     * 
     * @param reqWorkActionCode
     *            String
     * @return List - RequestStateWorker List
     */
    public default List<WorkItem<T>> getPostWorkItemList(String reqWorkActionCode) {
        WorkAction<T> currentWorkAction = this.getCurrentWorkAction(reqWorkActionCode);

        return currentWorkAction.getPostWorkItemList();
    }

    /**
     * @param requestParam
     * @return
     */
    public boolean isFinishRequestState();

    /**
     * 다음에 처리할 RequestState의 승인/검토 자에 대한 정보를 반환한다.
     * 
     * @param reqWorkActionCode
     *            String
     * @return ProcAuth 승인 /검토 자 정보
     */
    public default WorkAuth getNextAuthCode(String reqWorkActionCode) {
        WorkAction<T> currentWorkAction = this.getCurrentWorkAction(reqWorkActionCode);

        return currentWorkAction.getNextAuthCode();
    }
}
