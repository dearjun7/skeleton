package com.hs.gms.srv.api.common.treemodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 트리 모델
 * 
 * @author Ma, JoonChae
 */
public class TreeModelVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5940367237819594752L;

    private static final Logger LOGGER = LoggerFactory.getLogger(TreeModelVO.class);
    /**
     * 
     */

    private String rootId;
    private TreeNode rootNode;
    private List<String> expandedNodeIds;

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public TreeNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(TreeNode rootNode) {
        this.rootNode = rootNode;
        this.rootId = rootNode.getNodeId();
    }

    public TreeNode getTreeNodeSearch(String nodeId) {
        return rootNode.getTreeNodeSearch(nodeId);
    }

    public List<String> getExpandedNodeIds() {
        expandedNodeIds = new ArrayList<String>();
        this.setExpandedNodeId(expandedNodeIds, rootNode);

        return expandedNodeIds;
    }

    private void setExpandedNodeId(List<String> expandedNodeIds, TreeNode treeNode) {
        if(treeNode != null) {
            String nodeID = treeNode.getNodeId();
            if(!treeNode.isExpanded()) {
                return;
            }

            LOGGER.debug(nodeID + ", " + treeNode.isExpanded());
            expandedNodeIds.add(nodeID);
            if(!treeNode.isHasChildren()) {
                return;
            }

            List<TreeNode> childrenNode = treeNode.getChildrenNode();
            if(childrenNode == null) {
                treeNode.setHasChildren(false);
                return;
            }

            for(TreeNode childNode : childrenNode) {
                if(childNode.isExpanded()) {
                    this.setExpandedNodeId(expandedNodeIds, childNode);
                }
            }
        }
    }
}
