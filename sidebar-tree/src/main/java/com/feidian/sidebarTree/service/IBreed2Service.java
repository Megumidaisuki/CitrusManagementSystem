package com.feidian.sidebarTree.service;

import java.util.List;
import com.feidian.sidebarTree.domain.Breed2;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author feidian
 * @date 2023-09-10
 */
public interface IBreed2Service 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public Breed2 selectBreed2ById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param breed2 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<Breed2> selectBreed2List(Breed2 breed2);

    /**
     * 新增【请填写功能名称】
     * 
     * @param breed2 【请填写功能名称】
     * @return 结果
     */
    public int insertBreed2(Breed2 breed2);

    /**
     * 修改【请填写功能名称】
     * 
     * @param breed2 【请填写功能名称】
     * @return 结果
     */
    public int updateBreed2(Breed2 breed2);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteBreed2ByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteBreed2ById(Long id);
}
