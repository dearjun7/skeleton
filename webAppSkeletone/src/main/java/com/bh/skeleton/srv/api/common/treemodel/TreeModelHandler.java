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
 * BH Jun 2016. 7. 12. First Draft
 */
package com.hs.gms.srv.api.common.treemodel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hs.gms.srv.api.proc.master.ProcMasterDAO;
import com.hs.gms.srv.api.proc.master.type.ViewAuth;
import com.hs.gms.srv.api.proc.master.vo.ProcMasterVO;
import com.hs.gms.std.common.error.CommonErrorCode;
import com.hs.gms.std.common.error.GMSException;
import com.hs.gms.std.common.service.cache.UserCacheAccessor;

import net.sf.json.JSONObject;

/**
 * TreeModelHandler.java
 * 
 * @author BH Jun
 */
public abstract class TreeModelHandler<T> {

    @Autowired
    protected UserCacheAccessor userCacheAccessor;

    protected abstract T getObjectDataForNode(String param, String nodeId, boolean isOnlyOwnProc, boolean isRootNode, boolean checkAuth,
            boolean isSystemTenantData, String editState) throws Exception;

    protected abstract List<T> getObjectDataListForNode(TreeNode targetTreeNode, boolean isOnlyOwnProc, boolean checkAuth,
            boolean isSystemTenantData, String editState, String param) throws Exception;

    protected abstract List<T> getSearchedObjectDataListForNode(String param, String nodeName, boolean isOnlyOwnProc, boolean checkAuth,
            boolean isSystemTenantData) throws Exception;

    protected abstract JSONObject getTreeNodeFromCache(String cacheKey, boolean isOnlyOwnProc) throws Exception;

    protected abstract void setTreeNodeToCache(TreeNode treeNode, String cacheKey, boolean isOnlyOwnProc) throws Exception;

    protected abstract void removeTreeNodeInCache(String cacheKey, boolean isOnlyOwnProc) throws Exception;

    protected abstract TreeNode initNode(T sourceObj) throws Exception;

    public final void expandNode(TreeModelVO treeModelVO, String nodeId, String editState, boolean isOnlyOwnProc, String param)
            throws Exception {
        this.expandNode(treeModelVO, nodeId, isOnlyOwnProc, false, false, editState, param);
    }

    public final void expandNode(TreeModelVO treeModelVO, String nodeId, boolean isOnlyOwnProc, boolean checkAuth,
            boolean isSystemTenantData, String editState, String param) throws Exception {
        TreeNode treeNode = treeModelVO.getTreeNodeSearch(nodeId);

        if(treeNode != null) {
            TreeModelVO tempModel = new TreeModelVO();

            if(treeNode.getParentId().equals(ProcMasterDAO.MASTER_ROOT_PARENTID)) {
                this.setNode(nodeId, tempModel, isOnlyOwnProc, checkAuth, isSystemTenantData, editState, param);
            } else {
                tempModel.setRootNode(this.initTreeNode(treeNode));
            }

            this.setChildrenNode(tempModel.getRootNode(), isOnlyOwnProc, checkAuth, isSystemTenantData, editState, param);

            treeNode.setTreeNode(tempModel.getRootNode());
        }
    }

    public final void setSearchNodes(TreeNode targetTreeNode, String param, String nodeName, boolean isOnlyOwnProc, boolean checkAuth,
            boolean isSystemTenantData) throws Exception {
        if(targetTreeNode == null) {
            return;
        }

        List<TreeNode> treeNodes = new ArrayList<TreeNode>();
        List<T> sourceObjectList = this.getSearchedObjectDataListForNode(param, nodeName, isOnlyOwnProc, checkAuth, isSystemTenantData);

        for(T sourceObj : sourceObjectList) {
            TreeNode treeNode = this.initNode(sourceObj);
            treeNodes.add(treeNode);
        }

        targetTreeNode.setExpanded(true);
        targetTreeNode.setChildrenNode(treeNodes);
    }

    public final void setNode(String nodeId, TreeModelVO targetTreeModel, boolean isOnlyOwnProc, boolean checkAuth,
            boolean isSystemTenantData, String editState, String queryParam) throws Exception {
        T sourceObj = this.getObjectDataForNode(queryParam, nodeId, isOnlyOwnProc, false, checkAuth, isSystemTenantData, editState);
        String auth = null;

        if(sourceObj == null) {
            return;
        }

        if(sourceObj instanceof ProcMasterVO) {
            auth = ((ProcMasterVO) sourceObj).getViewAuth();

            if(auth.equals(ViewAuth.NONE.getValue())) {
                return;
            }
        }

        targetTreeModel.setRootNode(this.initNode(sourceObj));
    }

    public final void setRootNode(TreeModelVO targetTreeModel, boolean isOnlyOwnProc, boolean isSystemTenantData, String editState,
            String queryParam) throws Exception {
        TreeNode treeNode = null;
        boolean checkAuth = true;
        T sourceObj = this.getObjectDataForNode(queryParam, null, isOnlyOwnProc, true, checkAuth, isSystemTenantData, editState);

        if(sourceObj != null) {
            treeNode = this.initNode(sourceObj);
            targetTreeModel.setRootNode(treeNode);
        }
    }

    public final void setChildrenNode(TreeNode targetTreeNode, boolean isOnlyOwnProc, boolean checkAuth, boolean isSystemTenantData,
            String editState, String param) throws Exception {
        List<T> treeDataList = null;
        List<TreeNode> treeNodeList = new ArrayList<TreeNode>();

        if(targetTreeNode == null) {
            return;
        }

        treeDataList = this.getObjectDataListForNode(targetTreeNode, isOnlyOwnProc, checkAuth, isSystemTenantData, editState, param);

        for(T treeDataVO : treeDataList) {
            if(treeDataVO instanceof ProcMasterVO) {
                if(((ProcMasterVO) treeDataVO).getViewAuth().equals(ViewAuth.NONE.getValue())) {
                    continue;
                }
            }

            treeNodeList.add(this.initNode(treeDataVO));
        }

        targetTreeNode.setChildrenNode(treeNodeList);
    }

    public final TreeModelVO getTreeNodeFromCache(String keyHeader, String keyBody, boolean isOnlyOwnProc) throws Exception {
        JSONObject treeStatus = null;
        TreeNode treeNode = null;
        TreeModelVO treeModel = null;

        try {
            treeStatus = this.getTreeNodeFromCache(this.makeTreeStatusCacheKey(keyHeader, keyBody), isOnlyOwnProc);
            treeNode = (TreeNode) JSONObject.toBean(treeStatus, TreeNode.class);
            if(treeNode != null) {
                treeModel = new TreeModelVO();

                treeNode.setJSONObjectToTreeNode();
                treeNode.sortChildren(treeNode.getNodeId());
                treeNode.setExpanded(true);
                treeModel.setRootNode(treeNode);
            }
        } catch(GMSException e) {
            if(e.getErrorCode().equals(CommonErrorCode.NO_DATA_FOUND)) {
                treeModel = null;
            }
        }

        return treeModel;
    }

    public final void setTreeNodeToCache(TreeNode treeNode, String keyHeader, String keyBody, boolean isOnlyOwnProc) throws Exception {
        this.setTreeNodeToCache(treeNode, this.makeTreeStatusCacheKey(keyHeader, keyBody), isOnlyOwnProc);
    }

    public final void removeTreeNodeInCache(String keyHeader, String keyBody, boolean isOnlyOwnProc) throws Exception {
        this.removeTreeNodeInCache(this.makeTreeStatusCacheKey(keyHeader, keyBody), isOnlyOwnProc);
    }

    public final TreeNode initTreeNode(TreeNode sourceObj) throws Exception {
        TreeNode treeNode = new TreeNode();

        treeNode.setNodeId(sourceObj.getNodeId());
        treeNode.setNodeName(sourceObj.getNodeName());
        treeNode.setDepth(sourceObj.getDepth());
        treeNode.setDispOrder(sourceObj.getDispOrder());
        treeNode.setParentId(sourceObj.getParentId());
        treeNode.setNodeType(sourceObj.getNodeType());
        treeNode.setNodeVersion(sourceObj.getNodeVersion());
        treeNode.setEditAuth(sourceObj.isEditAuth());
        treeNode.setExpanded(sourceObj.isExpanded());
        treeNode.setHasChildren(sourceObj.isHasChildren());
        treeNode.setChildrenNode(null);

        if(sourceObj.getExceptFlag() == null) {
            treeNode.setExceptFlag("N");
        } else {
            treeNode.setExceptFlag(sourceObj.getExceptFlag());
        }

        treeNode.setUseFlag(sourceObj.getUseFlag());
        treeNode.setNewFlag(sourceObj.getNewFlag());

        return treeNode;
    }

    public final TreeModelVO load(String queryParam, boolean isOnlyOwnProc, boolean checkAuth, boolean isSystemTenantData, String editState)
            throws Exception {
        T sourceObj = this.getObjectDataForNode(queryParam, null, isOnlyOwnProc, true, checkAuth, isSystemTenantData, editState);

        return this.load(sourceObj, isOnlyOwnProc, checkAuth, isSystemTenantData, editState);
    }

    public TreeModelVO load(T sourceObj, boolean isOnlyOwnProc, boolean checkAuth, boolean isSystemTenantData, String editState)
            throws Exception {
        TreeModelVO resultTreeModel = new TreeModelVO();
        TreeNode treeNode = null;

        if(sourceObj == null) {
            return null;
        }

        treeNode = this.initNode(sourceObj);

        this.setChildrenNode(treeNode, isOnlyOwnProc, checkAuth, isSystemTenantData, editState, null);
        resultTreeModel.setRootNode(treeNode);

        return resultTreeModel;
    }

    private String makeTreeStatusCacheKey(String keyHeader, String keyBody) {
        return keyHeader + ":" + keyBody;
    }
}
