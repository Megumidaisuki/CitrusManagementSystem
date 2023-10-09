package com.feidian.sidebarTree.service.impl;

import java.util.List;
import java.util.Objects;

import com.feidian.sidebarTree.domain.TreeFile;
import com.feidian.sidebarTree.domain.vo.TreeFileVo;
import com.feidian.sidebarTree.mapper.TreeFileMapper;
import com.feidian.sidebarTree.service.ITreeFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.feidian.common.utils.SecurityUtils.getUserId;


/**
 * 树节点上的文件Service业务层处理
 *
 * @author feidian
 * @date 2022-07-04
 */
//已弃用
@Service
public class TreeFileServiceImpl implements ITreeFileService
{
    @Autowired
    private TreeFileMapper treeFileMapper;

    /**
     * 查询树节点上的文件
     *
     * @param fileId 树节点上的文件主键
     * @return 树节点上的文件
     */
    @Override
    public TreeFile selectTreeFileByFileId(Long fileId)
    {
        return treeFileMapper.selectTreeFileByFileId(fileId);
    }

    /**
     * 查询树节点上的文件列表
     *
     * @param treeFile 树节点上的文件
     * @return 树节点上的文件
     */
    @Override
    public List<TreeFile> selectTreeFileList(TreeFileVo treeFile)
    {
        List<TreeFile> treeFiles = treeFileMapper.selectTreeFileList(treeFile);
        Long userId = null;
        //未登录时不显示未公开的文件
        try{
            userId = getUserId();
        }catch (Exception ignored){
        }
        if(Objects.isNull(userId)){
            treeFiles.removeIf(treeFile1 -> treeFile1.getFileStatus() == 0);
        }
        return treeFiles;
    }

    /**
     * 新增树节点上的文件
     *
     * @param treeFile 树节点上的文件
     * @return 结果
     */
    @Override
    public int insertTreeFile(TreeFile treeFile)
    {
        treeFile.setCreateBy(getUserId().toString());
        return treeFileMapper.insertTreeFile(treeFile);
    }

    /**
     * 修改树节点上的文件
     *
     * @param treeFile 树节点上的文件
     * @return 结果
     */
    @Override
    public int updateTreeFile(TreeFile treeFile)
    {
        treeFile.setUpdateBy(getUserId().toString());
        return treeFileMapper.updateTreeFile(treeFile);
    }

    /**
     * 批量删除树节点上的文件
     *
     * @param fileIds 需要删除的树节点上的文件主键
     * @return 结果
     */
    @Override
    public int deleteTreeFileByFileIds(Long[] fileIds)
    {
        return treeFileMapper.deleteTreeFileByFileIds(fileIds);
    }

    /**
     * 删除树节点上的文件信息
     *
     * @param fileId 树节点上的文件主键
     * @return 结果
     */
    @Override
    public int deleteTreeFileByFileId(Long fileId)
    {
        return treeFileMapper.deleteTreeFileByFileId(fileId);
    }
}
