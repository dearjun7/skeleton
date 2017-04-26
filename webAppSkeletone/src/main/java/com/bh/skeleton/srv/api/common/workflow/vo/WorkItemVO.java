package com.hs.gms.srv.api.common.workflow.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.hs.gms.srv.api.common.workflow.state.action.item.WorkItem;

/**
 * WorkItem
 * 
 * @author Ma, JoonChae
 */
public class WorkItemVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4979242966236761044L;

    private int workSeq;
    private String workRequestId;
    private String prtcp;
    private String prtcpAuth;
    private String prtcpType;
    private String prtcpName;
    private String workState;
    private String workStateName;
    private String nextRequestState;
    private String workAction;
    private String workActionName;
    private String checkoutUserId;
    private String workItemState;
    private String worker;
    private String workerName;
    private String workDeptId;
    private String workDeptName;
    private String workDesc;
    private Date createDate;
    private Date cmpltDate;

    private String tenantId;

    private List<WorkItem<?>> requestStateWorkerList;

    private List<PrtcpVO> newPrtcpVO;

    public String getWorkRequestId() {
        return workRequestId;
    }

    public void setWorkRequestId(String workRequestId) {
        this.workRequestId = workRequestId;
    }

    public int getWorkSeq() {
        return workSeq;
    }

    public void setWorkSeq(int workSeq) {
        this.workSeq = workSeq;
    }

    public String getPrtcp() {
        return prtcp;
    }

    public void setPrtcp(String prtcp) {
        this.prtcp = prtcp;
    }

    public String getPrtcpAuth() {
        return prtcpAuth;
    }

    public void setPrtcpAuth(String prtcpAuth) {
        this.prtcpAuth = prtcpAuth;
    }

    public String getPrtcpType() {
        return prtcpType;
    }

    public void setPrtcpType(String prtcpType) {
        this.prtcpType = prtcpType;
    }

    public String getPrtcpName() {
        return prtcpName;
    }

    public void setPrtcpName(String prtcpName) {
        this.prtcpName = prtcpName;
    }

    public String getWorkState() {
        return workState;
    }

    public void setWorkState(String workState) {
        this.workState = workState;
    }

    public String getNextRequestState() {
        return nextRequestState;
    }

    public void setNextRequestState(String nextRequestState) {
        this.nextRequestState = nextRequestState;
    }

    public String getWorkAction() {
        return workAction;
    }

    public void setWorkAction(String workAction) {
        this.workAction = workAction;
    }

    public String getCheckoutUserId() {
        return checkoutUserId;
    }

    public void setCheckoutUserId(String checkoutUserId) {
        this.checkoutUserId = checkoutUserId;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getWorkDeptId() {
        return workDeptId;
    }

    public void setWorkDeptId(String workDeptId) {
        this.workDeptId = workDeptId;
    }

    public String getWorkDeptName() {
        return workDeptName;
    }

    public void setWorkDeptName(String workDeptName) {
        this.workDeptName = workDeptName;
    }

    public String getWorkDesc() {
        return workDesc;
    }

    public void setWorkDesc(String workDesc) {
        this.workDesc = workDesc;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getCmpltDate() {
        return cmpltDate;
    }

    public void setCmpltDate(Date cmpltDate) {
        this.cmpltDate = cmpltDate;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public List<WorkItem<?>> getPostWorkItemList() {
        return requestStateWorkerList;
    }

    public void setRequestStateWorkerList(List<WorkItem<?>> requestStateWorkerList) {
        this.requestStateWorkerList = requestStateWorkerList;
    }

    public List<PrtcpVO> getNewPrtcpVO() {
        return newPrtcpVO;
    }

    public void setNewPrtcpVO(List<PrtcpVO> newPrtcpVO) {
        this.newPrtcpVO = newPrtcpVO;
    }

    public String getWorkItemState() {
        return workItemState;
    }

    public void setWorkItemState(String workItemState) {
        this.workItemState = workItemState;
    }

    public String getWorkStateName() {
        return workStateName;
    }

    public void setWorkStateName(String workStateName) {
        this.workStateName = workStateName;
    }

    public String getWorkActionName() {
        return workActionName;
    }

    public void setWorkActionName(String workActionName) {
        this.workActionName = workActionName;
    }
}
