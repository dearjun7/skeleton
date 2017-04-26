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
 * BH Jun 2016. 8. 23. First Draft
 */
package com.hs.gms.srv.api.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hs.gms.srv.api.account.UserDataContextHolder;
import com.hs.gms.std.common.dao.GMSCommonDAO;

/**
 * CommonDataUtilDAOImpl.java
 * 
 * @author BH Jun
 */
@Repository
public class CommonDataUtilDAOImpl extends GMSCommonDAO implements CommonDataUtilDAO {

    @Override
    public String selectProcAuthName(String procId, String auth) {
        Map<String, String> params = new HashMap<String, String>();
        String tenantId = UserDataContextHolder.getUserData().getTenantId();
        List<String> procAuthNameList = null;
        StringBuffer result = new StringBuffer();
        int index = 0;

        params.put("tenantId", tenantId);
        params.put("procId", procId);
        params.put("auth", auth);

        procAuthNameList = super.getSqlSession().selectList("common.sql.selectProcAuthName", params);

        for(String procAuthName : procAuthNameList) {
            if(procAuthName == null) {
                continue;
            }

            if(index != 0) {
                result.append(", ");
            }
            result.append(procAuthName);
            index++;
        }

        return result.toString();
    }

    @Override
    public String selectProcAuthUserId(String procId, String auth) {
        Map<String, String> params = new HashMap<String, String>();
        String tenantId = UserDataContextHolder.getUserData().getTenantId();
        List<String> procAuthUserIdList = null;
        StringBuffer result = new StringBuffer();
        int index = 0;

        params.put("tenantId", tenantId);
        params.put("procId", procId);
        params.put("auth", auth);

        procAuthUserIdList = super.getSqlSession().selectList("common.sql.selectProcAuthUserId", params);

        for(String procAuthUserId : procAuthUserIdList) {
            if(procAuthUserId == null) {
                continue;
            }

            if(index != 0) {
                result.append(", ");
            }
            result.append(procAuthUserId);
            index++;
        }

        return result.toString();
    }

    @Override
    public String selectProcAuthDeptName(String procId, String auth) {
        Map<String, String> params = new HashMap<String, String>();
        String tenantId = UserDataContextHolder.getUserData().getTenantId();
        List<String> procAuthDeptNameList = null;
        StringBuffer result = new StringBuffer();
        int index = 0;

        params.put("tenantId", tenantId);
        params.put("procId", procId);
        params.put("auth", auth);

        procAuthDeptNameList = super.getSqlSession().selectList("common.sql.selectProcAuthDeptName", params);

        for(String procAuthDeptName : procAuthDeptNameList) {
            if(procAuthDeptName == null) {
                continue;
            }

            if(index != 0) {
                result.append(", ");
            }
            result.append(procAuthDeptName);
            index++;
        }

        return result.toString();
    }

    @Override
    public String selectProcAuthDeptId(String procId, String auth) {
        Map<String, String> params = new HashMap<String, String>();
        String tenantId = UserDataContextHolder.getUserData().getTenantId();
        List<String> procAuthDeptIdList = null;
        StringBuffer result = new StringBuffer();
        int index = 0;

        params.put("tenantId", tenantId);
        params.put("procId", procId);
        params.put("auth", auth);

        procAuthDeptIdList = super.getSqlSession().selectList("common.sql.selectProcAuthDeptId", params);

        for(String procAuthDeptId : procAuthDeptIdList) {
            if(procAuthDeptId == null) {
                continue;
            }

            if(index != 0) {
                result.append(", ");
            }
            result.append(procAuthDeptId);
            index++;
        }

        return result.toString();
    }

    @Override
    public String selectProcCustAttrUserAsscName(String procId, String userAsscType) {
        Map<String, String> params = new HashMap<String, String>();
        String tenantId = UserDataContextHolder.getUserData().getTenantId();
        List<String> procCustAttrUserAsscNameList = null;
        StringBuffer result = new StringBuffer();
        int index = 0;

        params.put("tenantId", tenantId);
        params.put("procId", procId);
        params.put("userAsscType", userAsscType);

        procCustAttrUserAsscNameList = super.getSqlSession().selectList("common.sql.selectProcCustAttrUserAsscName", params);

        for(String procCustAttrUserAsscName : procCustAttrUserAsscNameList) {
            if(procCustAttrUserAsscName == null) {
                continue;
            }

            if(index != 0) {
                result.append(", ");
            }

            result.append(procCustAttrUserAsscName);
            index++;
        }

        return result.toString();
    }

    @Override
    public String selectProcIdPath(String procId) {
        Map<String, String> params = new HashMap<String, String>();
        String tenantId = UserDataContextHolder.getUserData().getTenantId();
        List<String> procIdPathList = null;
        StringBuffer result = new StringBuffer();
        int index = 0;

        params.put("tenantId", tenantId);
        params.put("procId", procId);

        procIdPathList = super.getSqlSession().selectList("common.sql.selectProcIdPath", params);

        for(String procIdPath : procIdPathList) {
            if(procIdPath == null) {
                continue;
            }

            if(index != 0) {
                result.append(">");
            }
            result.append(procIdPath);
            index++;
        }

        return result.toString();
    }

    @Override
    public String selectProcPath(String procId) {
        Map<String, String> params = new HashMap<String, String>();
        String tenantId = UserDataContextHolder.getUserData().getTenantId();
        List<String> procPathList = null;
        StringBuffer result = new StringBuffer();
        int index = 0;

        params.put("tenantId", tenantId);
        params.put("procId", procId);

        procPathList = super.getSqlSession().selectList("common.sql.selectpProcPath", params);

        for(String procPath : procPathList) {
            if(procPath == null) {
                continue;
            }

            if(index != 0) {
                result.append(">");
            }
            result.append(procPath);
            index++;
        }

        return result.toString();
    }

    @Override
    public String selectWorkItemPrtcpName(String workRequestId, String workState) {
        Map<String, String> params = new HashMap<String, String>();
        String tenantId = UserDataContextHolder.getUserData().getTenantId();
        List<String> workItemPrtcpNameList = null;
        StringBuffer result = new StringBuffer();
        int index = 0;

        params.put("tenantId", tenantId);
        params.put("workRequestId", workRequestId);
        params.put("workState", workState);

        workItemPrtcpNameList = super.getSqlSession().selectList("common.sql.selectWorkItemPrtcpName", params);

        for(String workItemPrtcpName : workItemPrtcpNameList) {
            if(workItemPrtcpName == null) {
                continue;
            }

            if(index != 0) {
                result.append(",");
            }
            result.append(workItemPrtcpName);
            index++;
        }

        return result.toString();
    }
}
