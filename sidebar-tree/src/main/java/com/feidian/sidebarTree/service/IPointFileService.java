package com.feidian.sidebarTree.service;

import com.feidian.sidebarTree.domain.PointFile;

import java.util.List;
/**
 * 【请填写功能名称】Service接口
 * 
 * @author feidian
 * @date 2022-11-07
 */
public interface IPointFileService 
{

    String selectByArr(PointFile pointFile);

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public PointFile selectPointFileById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param pointFile 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<PointFile> selectPointFileList(PointFile pointFile);

    /**
     * 新增【请填写功能名称】
     * 
     * @param pointFile 【请填写功能名称】
     * @return 结果
     */
    public int insertPointFile(PointFile pointFile);

    /**
     * 修改【请填写功能名称】
     * 
     * @param pointFile 【请填写功能名称】
     * @return 结果
     */
    public int updatePointFile(PointFile pointFile);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deletePointFileByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deletePointFileById(Long id);
}
