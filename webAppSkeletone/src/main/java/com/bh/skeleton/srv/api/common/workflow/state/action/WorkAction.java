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
 * BH Jun 2016. 5. 18. First Draft
 */
package com.hs.gms.srv.api.common.workflow.state.action;

import java.util.List;

import com.hs.gms.srv.api.common.workflow.WorkAuth;
import com.hs.gms.srv.api.common.workflow.state.WorkRequestState;
import com.hs.gms.srv.api.common.workflow.state.action.item.WorkItem;

/**
 * WorkAction
 * 
 * @author BH Jun
 */
public interface WorkAction<T> {

    /**
     * RequestState에 대하여 승인/검토 자가 요청한 wokrAction Code를 반환한다.
     * 
     * @return String workActionCode
     */
    public String getWorkActionCode();

    /**
     * 승인/검토 자가 요청한 wokrActionCode에 따른 다음에 진행될 RequestState 값을 반환한다.
     * 
     * @param argument
     *            String aurgument - 다음 RequestState 값을 판단하기 위한 parameter(필요 시
     *            사용)
     * @return RequestState nextRequestState Object
     */
    public WorkRequestState<T> getNextWorkRequestState(String argument);

    /**
     * 승인/검토 자가 처리 중인 RequestState에서 workActionCode에 따라 실행될 WorkFlow에서 선행으로 실행 될
     * worker 목록을 List객체로 반환한다.<br>
     * 만약 처리 중인 RequestState에서 요청한 workAction에 대하여 실행할 worker가 존재하지 않는다면<br>
     * 값을 null로 return 한다.
     * 
     * @return List requestStateWorkerList
     */
    public List<WorkItem<T>> getPreWorkItemList();

    /**
     * 승인/검토 자가 처리 중인 RequestState에서 workActionCode에 따라 실행될 WorkFlow에서 후행으로 실행 될
     * worker 목록을 List객체로 반환한다.<br>
     * 만약 처리 중인 RequestState에서 요청한 workAction에 대하여 실행할 worker가 존재하지 않는다면<br>
     * 값을 null로 return 한다.
     * 
     * @return List requestStateWorkerList
     */
    public List<WorkItem<T>> getPostWorkItemList();

    /**
     * 현재 승인/검토 자가 요청한 wokrActionCode에 따른 다음에 진행될 WorkState의 권한 코드를 반환하다.
     * 
     * @return WorkAuth 다음 workState에 사용될 권한 코드
     */
    public WorkAuth getNextAuthCode();

    /**
     * 현재 workActionType이 요청을 등록한 사용자에 의해 실행될 수 있는지를 확인한다.
     * 
     * @return boolean
     *         true - 실행 가능
     *         false - 실행 불가능
     */
    public boolean isAllowWorkActionByRequestor();

    /**
     * 현재 처리 대상이되는 WorkActionType implements 객체를 반환한다.
     * 
     * @param workActionCode
     *            String
     * @return WorkActionType
     */
    public WorkAction<T> getWorkAction(String workActionCode);

    public boolean isOnlyAllowActionByRequestorAuth();

    public boolean isOnlyAllowActionBySystem();
}
