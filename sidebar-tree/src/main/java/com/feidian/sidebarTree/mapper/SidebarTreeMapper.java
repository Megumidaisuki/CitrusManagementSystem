package com.feidian.sidebarTree.mapper;

import com.feidian.sidebarTree.domain.SidebarTree;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 侧边栏树的Mapper接口
 *
 * @author feidian
 * @date 2022-07-01
 */
@Mapper
public interface SidebarTreeMapper
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
     * 删除侧边栏树的
     *
     * @param treeId 侧边栏树的主键
     * @return 结果
     */
    public int deleteSidebarTreeByTreeId(Long treeId);

    /**
     * 批量删除侧边栏树的
     *
     * @param treeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSidebarTreeByTreeIds(Long[] treeIds);


    /**
     * 查询parentId为此的子节点
     * @param parentId 参数
     * @return 返回对应的子节点
     */
    public List<SidebarTree> selectTreeNodeByParentId(@Param("parentId") Long parentId,@Param("treeType") int treeType);

    public List<Long> selectAllParentTreeId();

    public List<SidebarTree> selectAllParentTreeByParentId(@Param("parentId") Long parentId);
}
