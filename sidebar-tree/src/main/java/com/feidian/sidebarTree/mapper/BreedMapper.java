package com.feidian.sidebarTree.mapper;

import java.util.List;
import com.feidian.sidebarTree.domain.Breed;

/**
 * breedMapper接口
 * 
 * @author feidian
 * @date 2023-07-03
 */
public interface BreedMapper 
{
    /**
     * 查询breed
     * 
     * @param breedId breed主键
     * @return breed
     */
    public Breed selectBreedByBreedId(Long breedId);

    /**
     * 查询breed列表
     * 
     * @param breed breed
     * @return breed集合
     */
    public List<Breed> selectBreedList(Breed breed);

    /**
     * 新增breed
     * 
     * @param breed breed
     * @return 结果
     */
    public int insertBreed(Breed breed);

    /**
     * 修改breed
     * 
     * @param breed breed
     * @return 结果
     */
    public int updateBreed(Breed breed);

    /**
     * 删除breed
     * 
     * @param breedId breed主键
     * @return 结果
     */
    public int deleteBreedByBreedId(Long breedId);

    /**
     * 批量删除breed
     * 
     * @param breedIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBreedByBreedIds(Long[] breedIds);

    void inserPhenotypeFile(String uploadFilePath);

    void inserGenotypeFile(String uploadFilePath);

    Long getLastInsertedFileId();

    String selectBreedPhenotypeFile(Long fileId);

    String selectBreedGenotypeFile(Long fileId);

    String selectBreedPhenotypeNewFile(Long fileId);

    String selectBreedGenotypeNewFile(Long fileId);

    int getCount();

}
