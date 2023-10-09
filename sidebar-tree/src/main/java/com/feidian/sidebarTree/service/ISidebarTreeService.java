package com.feidian.sidebarTree.service;

import com.feidian.sidebarTree.domain.SidebarTree;

import java.util.HashMap;
import java.util.List;


/**
 * 侧边栏树的Service接口
 *
 * @author feidian
 * @date 2022-07-01
 */
public interface ISidebarTreeService
{
    /**
     * 查询侧边栏树的
     *
     * @param treeId 侧边栏树的主键
     * @return 侧边栏树的
     */
    public SidebarTree selectSidebarTreeByTreeId(Long treeId);

    /**
     * 查询侧边栏树的列表
     *
     * @param sidebarTree 侧边栏树的
     * @return 侧边栏树的集合
     */
    public List<SidebarTree> selectSidebarTreeList(SidebarTree sidebarTree);

    /**
     * 新增侧边栏树的
     *
     * @param sidebarTree 侧边栏树的
     * @return 结果
     */
    public int insertSidebarTree(SidebarTree sidebarTree);

    /**
     * 修改侧边栏树的
     *
     * @param sidebarTree 侧边栏树的
     * @return 结果
     */
    public int updateSidebarTree(SidebarTree sidebarTree);

    /**
     * 批量删除侧边栏树的
     *
     * @param treeIds 需要删除的侧边栏树的主键集合
     * @return 结果
     */
    public int deleteSidebarTreeByTreeIds(Long[] treeIds);

    /**
     * 删除侧边栏树的信息
     *
     * @param treeId 侧边栏树的主键
     * @return 结果
     */
    public int deleteSidebarTreeByTreeId(Long treeId);

    /**
     * 获取子节点列表
     *
     * @param t 返回的总的SidebarTree对象，作为侧边栏树
     * @return 返回子节点列表
     */
    public List<SidebarTree> getChildrenList(SidebarTree t);

    /**
     * 查询树的第一层节点
     *
     * @param tree 传入的参数
     * @return 返回所有第一层节点
     */
    public List<SidebarTree> selectTreeList(SidebarTree tree);

    /**
     * 查询树的所有子节点
     * @return 返回的所有节点
     */
    public SidebarTree selectAllTreeNode(boolean isAdmin,SidebarTree tree);

    /**
     * 递归所有的树节点
     * @return
     */
    public SidebarTree recursionAllNodes(boolean isAdmin,SidebarTree tree,List<SidebarTree> nextNode,int treetype);

    HashMap<String, Integer> getLogNumbers(Long treeId);

    void updateIpAndPath(int treeId, String ip, String parentFile);
}
