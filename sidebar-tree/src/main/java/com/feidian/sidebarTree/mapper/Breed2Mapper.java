package com.feidian.sidebarTree.mapper;

import java.util.List;
import com.feidian.sidebarTree.domain.Breed2;
import org.apache.ibatis.annotations.Mapper;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ruoyi
 * @date 2023-09-10
 */
@Mapper
public interface Breed2Mapper
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
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteBreed2ById(Long id);


    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBreed2ByIds(Long[] ids);


    Integer selectMaxId();
}
