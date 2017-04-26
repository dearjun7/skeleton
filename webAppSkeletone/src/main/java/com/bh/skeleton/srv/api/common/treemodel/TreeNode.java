package com.hs.gms.srv.api.common.treemodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

/**
 * 트리 모델을 구성하는 Tree Node를 정의하는 class
 * 
 * @author Ma, JoonChae
 */
public class TreeNode implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TreeNode.class);

    /**
     * 
     */
    private static final long serialVersionUID = -7943403988344730333L;

    protected int dispOrder;
    protected int depth;

    protected boolean expanded;
    protected boolean hasChildren;
    protected boolean editAuth;

    protected String nodeId;
    protected String nodeName;
    protected String parentId;
    protected String nodeType;
    protected String nodeVersion;
    protected String exceptFlag;
    protected String useFlag;
    protected String newFlag;

    protected List<TreeNode> childrenNode;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getExceptFlag() {
        return exceptFlag;
    }

    public void setExceptFlag(String exceptFlag) {
        this.exceptFlag = exceptFlag;
    }

    public String getUseFlag() {
        return useFlag;
    }

    public void setUseFlag(String useFlag) {
        this.useFlag = useFlag;
    }

    public String getNewFlag() {
        return newFlag;
    }

    public void setNewFlag(String newFlag) {
        this.newFlag = newFlag;
    }

    public boolean isEditAuth() {
        return editAuth;
    }

    public void setEditAuth(boolean editAuth) {
        this.editAuth = editAuth;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeVersion() {
        return nodeVersion;
    }

    public void setNodeVersion(String nodeVersion) {
        this.nodeVersion = nodeVersion;
    }

    public int getDispOrder() {
        return dispOrder;
    }

    public void setDispOrder(int dispOrder) {
        this.dispOrder = dispOrder;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public List<TreeNode> getChildrenNode() {
        return childrenNode;
    }

    public void setChildrenNode(List<TreeNode> childrenNode) {
        if(childrenNode != null && childrenNode.size() > 0) {
            this.hasChildren = true;
        }

        this.childrenNode = childrenNode;
    }

    public void addChildNode(TreeNode childNode) {
        if(this.childrenNode == null) {
            this.childrenNode = new ArrayList<TreeNode>();
        }

        String childNodeId = childNode.getNodeId();
        for(int i = 0; i < this.childrenNode.size(); i++) {
            TreeNode tmpNode = this.childrenNode.get(i);
            if(childNodeId.equals(tmpNode.getNodeId())) {
                this.childrenNode.remove(i);
                break;
            }
        }

        if(childNode.getDispOrder() < 1) {
            childNode.setDispOrder(this.childrenNode.size());
        }

        if(childNode.getDepth() <= this.depth) {
            childNode.setDepth(this.depth + 1);
        }

        this.childrenNode.add(childNode);
        this.hasChildren = true;
    }

    public void setJSONObjectToTreeNode() {
        setJSONObjectToTreeNode(this);
    }

    private void setJSONObjectToTreeNode(TreeNode treeNode) {
        if(treeNode.isHasChildren()) {
            List<TreeNode> tempList = new ArrayList<TreeNode>();
            for(Object childNode : treeNode.getChildrenNode()) {
                JSONObject json = JSONObject.fromObject(childNode);
                TreeNode child = (TreeNode) JSONObject.toBean(json, TreeNode.class);

                tempList.add(child);
                setJSONObjectToTreeNode(child);
            }

            treeNode.setChildrenNode(tempList);
        }
    }

    public void expandTreeNode(TreeNode expandNode) {
        expandTreeNode(this, expandNode);
    }

    private void expandTreeNode(TreeNode treeNode, TreeNode expandNode) {
        if(treeNode != null) {
            if(treeNode.getNodeId().equals(expandNode.getNodeId())) {
                treeNode.setExpanded(true);

                treeNode.setTreeNode(expandNode);
                return;
            }

            List<TreeNode> childrenNode = treeNode.getChildrenNode();
            if(childrenNode == null || treeNode.childrenNode.size() == 0) {
                treeNode.setExpanded(false);
                return;
            }

            for(TreeNode childNode : childrenNode) {
                expandTreeNode(childNode, expandNode);
            }
        }
    }

    public void setTreeNode(TreeNode newTreeNode) {
        if(newTreeNode == null) {
            return;
        }

        this.setNodeName(newTreeNode.getNodeName());
        this.setDispOrder(newTreeNode.getDispOrder());
        this.setNodeType(newTreeNode.getNodeType());
        this.setEditAuth(newTreeNode.isEditAuth());
        this.setExceptFlag(newTreeNode.getExceptFlag());
        this.setUseFlag(newTreeNode.getUseFlag());
        this.setNewFlag(newTreeNode.getNewFlag());

        List<TreeNode> childrenNodes = this.getChildrenNode();
        List<TreeNode> newChildrenNodes = newTreeNode.getChildrenNode();

        if(newChildrenNodes == null || newChildrenNodes.size() == 0) {
            this.setExpanded(false);
            this.setChildrenNode(null);
        } else {
            if(childrenNodes != null && childrenNodes.size() > 0) {
                for(int i = 0; i < newChildrenNodes.size(); i++) {
                    TreeNode newChild = newChildrenNodes.get(i);
                    for(int ii = 0; ii < childrenNodes.size(); ii++) {
                        TreeNode child = childrenNodes.get(ii);
                        if(newChild.getNodeId().equals(child.getNodeId())) {
                            newChild.setExpanded(child.isExpanded());
                            if(newChild.isHasChildren()) {
                                newChild.setChildrenNode(child.getChildrenNode());
                            }
                            break;
                        }
                    }
                }
            }

            this.setChildrenNode(newChildrenNodes);
        }

        if(this.getChildrenNode() != null && this.getChildrenNode().size() > 0) {
            this.sortChildren(this.getNodeId());
        }
    }

    public void collapseTreeNode(String nodeId) {
        collapseTreeNode(this, nodeId);
    }

    private void collapseTreeNode(TreeNode treeNode, String nodeId) {
        if(treeNode != null) {
            if(treeNode.getNodeId().equals(nodeId)) {
                treeNode.setExpanded(false);
                treeNode.setHasChildren(true);
                // treeNode.setChildrenNode(null);
                return;
            }

            List<TreeNode> childrenNode = treeNode.getChildrenNode();
            if(childrenNode == null) {
                return;
            }

            for(TreeNode childNode : childrenNode) {
                collapseTreeNode(childNode, nodeId);
            }
        }
    }

    public void removeTreeNode(String nodeId) {
        removeTreeNode(this, nodeId);
    }

    private void removeTreeNode(TreeNode treeNode, String nodeId) {
        if(treeNode != null) {
            List<TreeNode> childrenNode = treeNode.getChildrenNode();
            if(childrenNode == null) {
                return;
            }

            for(TreeNode childNode : childrenNode) {
                if(nodeId.equals(childNode.getNodeId())) {
                    childrenNode.remove(childNode);
                    if(childrenNode == null || childrenNode.size() == 0) {
                        treeNode.setExpanded(false);
                    }
                    return;
                } else {
                    removeTreeNode(childNode, nodeId);
                }
            }
        }
    }

    private int getmaxDispOrder(List<TreeNode> treeNodes) {
        int maxDispOrder = 0;

        if(treeNodes == null) {
            maxDispOrder = 0;
        } else {
            for(TreeNode treeNode : treeNodes) {
                if(maxDispOrder < treeNode.getDispOrder()) {
                    maxDispOrder = treeNode.getDispOrder();
                }
            }
        }

        return maxDispOrder;
    }

    public void moveTreeNode(String fromNodeId, String toNodeId) {
        TreeNode fromNode = getTreeNode(this, fromNodeId, null);
        if(fromNode != null) {
            TreeNode toNode = getTreeNode(this, toNodeId, null);
            if(toNode != null && toNode.getChildrenNode() != null && toNode.getChildrenNode().size() > 0) {
                int maxDispOrder = getmaxDispOrder(toNode.getChildrenNode());

                LOGGER.debug("fromNode : " + fromNode);
                fromNode.setParentId(toNode.getNodeId());
                fromNode.setDepth(toNode.getDepth() + 1);
                fromNode.setDispOrder(maxDispOrder + 1);

                addTreeNode(this, toNode.getNodeId(), fromNode);
            }

            removeTreeNode(fromNodeId);
        }
    }

    public void addTreeNode(String parentId, TreeNode addNode) {
        addTreeNode(this, parentId, addNode);
    }

    private void addTreeNode(TreeNode treeNode, String parentId, TreeNode addNode) {
        if(treeNode != null) {
            List<TreeNode> childrenNodes = treeNode.getChildrenNode();
            if(childrenNodes == null) {
                return;
            }

            for(TreeNode childNode : childrenNodes) {
                if(parentId.equals(childNode.getNodeId())) {
                    childNode.setExpanded(true);
                    childNode.addChildNode(addNode);
                    return;
                } else {
                    addTreeNode(childNode, parentId, addNode);
                }
            }
        }
    }

    private TreeNode getTreeNode(TreeNode treeNode, String nodeId, TreeNode result) {
        if(treeNode != null) {
            if(treeNode.getNodeId().equals(nodeId)) {
                return treeNode;
            }

            List<TreeNode> childrenNodes = treeNode.getChildrenNode();
            if(childrenNodes != null && childrenNodes.size() > 0) {
                for(TreeNode childNode : childrenNodes) {
                    result = getTreeNode(childNode, nodeId, result);
                }
            }
        }

        return result;
    }

    public void sortChildren(String parentId) {
        TreeNode treeNode = getTreeNode(this, parentId, null);
        if(treeNode != null) {
            sortChildren(treeNode);
        }
    }

    public void sortChildren(String parentId, List<TreeNode> sortNodes) {
        TreeNode treeNode = getTreeNode(this, parentId, null);

        if(treeNode != null) {
            List<TreeNode> childrenNodes = treeNode.getChildrenNode();
            if(childrenNodes != null && childrenNodes.size() > 0) {
                for(TreeNode childNode : childrenNodes) {
                    for(TreeNode sortNode : sortNodes) {
                        if(childNode.getNodeId().equals(sortNode.getNodeId())) {
                            childNode.setDispOrder(sortNode.getDispOrder());
                            break;
                        }
                    }
                }
            }

            sortChildren(treeNode);
        }
    }

    private void sortChildren(TreeNode treeNode) {
        if(treeNode == null) {
            return;
        }

        List<TreeNode> childrenNodes = treeNode.getChildrenNode();
        if(childrenNodes != null && childrenNodes.size() > 0) {
            for(int i = 0; i < childrenNodes.size() - 1; i++) {
                for(int ii = i + 1; ii < childrenNodes.size(); ii++) {
                    if(childrenNodes.get(i).getDispOrder() > childrenNodes.get(ii).getDispOrder()) {
                        TreeNode temp = childrenNodes.get(i);

                        childrenNodes.set(i, childrenNodes.get(ii));
                        childrenNodes.set(ii, temp);
                    }
                }
            }
        }
    }

    public TreeNode getTreeNodeSearch(String nodeId) {
        TreeNode treeNode = getTreeNode(this, nodeId, null);
        return treeNode;
    }
}
