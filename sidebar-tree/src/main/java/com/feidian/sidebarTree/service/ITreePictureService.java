package com.feidian.sidebarTree.service;

import com.feidian.sidebarTree.domain.TreePicture;

import java.util.List;
import java.util.Map;

/**
 * 【请填写功能名称】Service接口
 *
 * @author feidian
 * @date 2022-07-03
 */
public interface ITreePictureService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param pictureId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public TreePicture selectTreePictureByPictureId(Long pictureId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param treePicture 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<TreePicture> selectTreePictureList(TreePicture treePicture);

    /**
     * 新增【请填写功能名称】
     *
     * @param treePicture 【请填写功能名称】
     * @return 结果
     */
    public int insertTreePicture(TreePicture treePicture);

    /**
     * 修改【请填写功能名称】
     *
     * @param treePicture 【请填写功能名称】
     * @return 结果
     */
    public int updateTreePicture(TreePicture treePicture);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param pictureIds 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteTreePictureByPictureIds(Long[] pictureIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param pictureId 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteTreePictureByPictureId(Long pictureId);

    public List<TreePicture> getTreeByTreeId(int treeId);

    public List<Object> getNodePicCount(Long treeId);

}
