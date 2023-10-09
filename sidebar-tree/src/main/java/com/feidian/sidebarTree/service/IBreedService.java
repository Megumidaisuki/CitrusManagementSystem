package com.feidian.sidebarTree.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.feidian.sidebarTree.domain.Breed;
import org.springframework.web.multipart.MultipartFile;

/**
 * breedService接口
 * 
 * @author feidian
 * @date 2023-07-03
 */
public interface IBreedService 
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
     * 批量删除breed
     * 
     * @param breedIds 需要删除的breed主键集合
     * @return 结果
     */
    public int deleteBreedByBreedIds(Long[] breedIds);

    /**
     * 删除breed信息
     * 
     * @param breedId breed主键
     * @return 结果
     */
    public int deleteBreedByBreedId(Long breedId);

    Breed taskManager(Long genofileId, Long phenofileId,Integer scene,Integer genofileFlag,Integer phenofileFlag);


    Long uploadFile(MultipartFile file, Integer type);

    String resourceDownload(Long fileId, Integer type,Integer flag) throws Exception;

    int getCount();

    String downloadPdf(Long breedId);

    ArrayList<String> getResultData(Long id);
}
