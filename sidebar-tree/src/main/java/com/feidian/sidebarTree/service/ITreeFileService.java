package com.feidian.sidebarTree.service;

import com.feidian.sidebarTree.domain.TreeFile;
import com.feidian.sidebarTree.domain.vo.TreeFileVo;

import java.util.List;

/**
 * 树节点上的文件Service接口
 *
 * @author feidian
 * @date 2022-07-04
 */
public interface ITreeFileService
{
    /**
     * 查询树节点上的文件
     *
     * @param fileId 树节点上的文件主键
     * @return 树节点上的文件
     */
    public TreeFile selectTreeFileByFileId(Long fileId);

    /**
     * 查询树节点上的文件列表
     *
     * @param treeFile 树节点上的文件
     * @return 树节点上的文件集合
     */
    public List<TreeFile> selectTreeFileList(TreeFileVo treeFile);

    /**
     * 新增树节点上的文件
     *
     * @param treeFile 树节点上的文件
     * @return 结果
     */
    public int insertTreeFile(TreeFile treeFile);

    /**
     * 修改树节点上的文件
     *
     * @param treeFile 树节点上的文件
     * @return 结果
     */
    public int updateTreeFile(TreeFile treeFile);

    /**
     * 批量删除树节点上的文件
     *
     * @param fileIds 需要删除的树节点上的文件主键集合
     * @return 结果
     */
    public int deleteTreeFileByFileIds(Long[] fileIds);

    /**
     * 删除树节点上的文件信息
     *
     * @param fileId 树节点上的文件主键
     * @return 结果
     */
    public int deleteTreeFileByFileId(Long fileId);
}
