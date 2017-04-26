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
 * BH Jun 2017. 2. 9. First Draft
 */
package com.hs.gms.srv.api.common.workflow.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * WorkRequestVO.java
 * 
 * @author BH Jun
 */
public class WorkRequestVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1832890779660508264L;

    private String tenantId;
    private String workRequestId;

    private String requestTitle;
    private String requestDesc;
    private Date requestDate;

    private String requestor;
    private String requestorName;
    private String requestDeptId;
    private String requestDeptName;

    private String workState;
    private String workProcessType;

    private String workAction;
    private String workDesc;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getWorkRequestId() {
        return workRequestId;
    }

    public void setWorkRequestId(String workRequestId) {
        this.workRequestId = workRequestId;
    }

    public String getRequestTitle() {
        return requestTitle;
    }

    public void setRequestTitle(String requestTitle) {
        this.requestTitle = requestTitle;
    }

    public String getRequestDesc() {
        return requestDesc;
    }

    public void setRequestDesc(String requestDesc) {
        this.requestDesc = requestDesc;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getRequestor() {
        return requestor;
    }

    public void setRequestor(String requestor) {
        this.requestor = requestor;
    }

    public String getRequestorName() {
        return requestorName;
    }

    public void setRequestorName(String requestorName) {
        this.requestorName = requestorName;
    }

    public String getRequestDeptId() {
        return requestDeptId;
    }

    public void setRequestDeptId(String requestDeptId) {
        this.requestDeptId = requestDeptId;
    }

    public String getRequestDeptName() {
        return requestDeptName;
    }

    public void setRequestDeptName(String requestDeptName) {
        this.requestDeptName = requestDeptName;
    }

    public String getWorkState() {
        return workState;
    }

    public void setWorkState(String workState) {
        this.workState = workState;
    }

    public String getWorkProcessType() {
        return workProcessType;
    }

    public void setWorkProcessType(String workProcessType) {
        this.workProcessType = workProcessType;
    }

    public String getWorkAction() {
        return workAction;
    }

    public void setWorkAction(String workAction) {
        this.workAction = workAction;
    }

    public String getWorkDesc() {
        return workDesc;
    }

    public void setWorkDesc(String workDesc) {
        this.workDesc = workDesc;
    }
}
