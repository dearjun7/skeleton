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
package com.hs.gms.srv.api.common.workflow.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * WorkItemProgressDetailVO.java
 * 
 * @author BH Jun
 */
public class WorkItemProgressDetailVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2557624989713377137L;

    private Date cmpltDate;
    private String workState;
    private String workStateName;
    private String workActionName;
    private String workerId;
    private String workerName;
    private String workDeptId;
    private String workDeptName;
    private String workDesc;

    public Date getCmpltDate() {
        return cmpltDate;
    }

    public void setCmpltDate(Date cmpltDate) {
        this.cmpltDate = cmpltDate;
    }

    public String getWorkState() {
        return workState;
    }

    public void setWorkState(String workState) {
        this.workState = workState;
    }

    public String getWorkStateName() {
        return workStateName;
    }

    public void setWorkStateName(String workStateName) {
        this.workStateName = workStateName;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
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

    public String getWorkActionName() {
        return workActionName;
    }

    public void setWorkActionName(String workActionName) {
        this.workActionName = workActionName;
    }

    public String getWorkDesc() {
        return workDesc;
    }

    public void setWorkDesc(String workDesc) {
        this.workDesc = workDesc;
    }
}
