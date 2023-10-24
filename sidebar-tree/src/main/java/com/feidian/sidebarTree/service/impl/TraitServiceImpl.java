package com.feidian.sidebarTree.service.impl;

import java.util.*;

import com.feidian.common.utils.DateUtils;
import com.feidian.common.utils.SecurityUtils;
import com.feidian.common.utils.StringUtils;
import com.feidian.sidebarTree.domain.*;
import com.feidian.sidebarTree.domain.vo.DataAnalysisVO;
import com.feidian.sidebarTree.domain.vo.TraitTypeVO;
import com.feidian.sidebarTree.domain.vo.TraitVO;
import com.feidian.sidebarTree.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.feidian.sidebarTree.service.ITraitService;
import com.feidian.sidebarTree.utils.InfoUtil;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author feidian
 * @date 2023-07-03
 */
@Service
public class TraitServiceImpl implements ITraitService
{

    @Autowired
    private ExcuteMapper excuteMapper;

    @Autowired
    private TraitMapper traitMapper;

    @Autowired
    private TraitTypeMapper traitTypeMapper;

    @Autowired
    private AsTraitTypeMapper asTraitTypeMapper;

    @Autowired
    private PhenotypeFileMapper phenotypeFileMapper;

    @Autowired
    private InfoUtil infoUtil;

    /**
     * 查询【请填写功能名称】
     *
     * @param traitId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public Trait selectTraitByTraitId(Long traitId)
    {
        return traitMapper.selectTraitByTraitId(traitId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param trait 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<Trait> selectTraitList(Trait trait)
    {
        return traitMapper.selectTraitList(trait);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param trait 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertTrait(Trait trait)
    {
        trait.setCreateTime(DateUtils.getNowDate());
        trait.setCreateBy(SecurityUtils.getUserId().toString());
        trait.setUpdateTime(DateUtils.getNowDate());
        trait.setUpdateBy(SecurityUtils.getUserId().toString());
        return traitMapper.insertTrait(trait);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param trait 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateTrait(Trait trait)
    {
        trait.setUpdateTime(DateUtils.getNowDate());
        trait.setUpdateBy(SecurityUtils.getUserId().toString());
        return traitMapper.updateTrait(trait);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param traitIds 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteTraitByTraitIds(Long[] traitIds)
    {
        return traitMapper.deleteTraitByTraitIds(traitIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param traitId 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteTraitByTraitId(Long traitId)
    {
        return traitMapper.deleteTraitByTraitId(traitId);
    }

    @Override
    public  List<TraitVO> listAllByType() {
        List<TraitVO> traitVOS =new ArrayList<>();
        HashMap<String,List<Trait>> result =new HashMap<>();
        List<TraitType> traitTypes = traitTypeMapper.selectTraitTypeList(new TraitType());
        for (TraitType type : traitTypes){
            TraitVO traitVO =new TraitVO();
            traitVO.setName(type.getTraitTypeName());
            traitVO.setId(type.getTraitTypeId());


            result.put(type.getTraitTypeName(),null);
            List<TraitVO> traits =new ArrayList<>();
            Long traitTypeId = type.getTraitTypeId();
            AsTraitType query =new AsTraitType();
            query.setTraitTypeId(traitTypeId);
            List<AsTraitType> asTraitTypes = asTraitTypeMapper.selectAsTraitTypeList(query);
            for (AsTraitType asTraitType: asTraitTypes){
                String traitId = asTraitType.getTraitId();
                Trait trait = traitMapper.selectTraitByTraitId(Long.valueOf(traitId));
                if(trait!=null){
                    TraitVO tvo =new TraitVO();
                    tvo.setId(trait.getTraitId());
                    tvo.setName(trait.getTraitName());
                    tvo.setChidren(null);
                    traits.add(tvo);
                }
            }
            traitVO.setChidren(traits);
            traitVOS.add(traitVO);
        }
        return traitVOS;
    }

    /*@Override
    @Cacheable(value = "listLocationByTraitId",key = "#id")
    public Set<String> listLocationByTraitId(Long id) {
        List<PhenotypeFile> phenotypeFiles = phenotypeFileMapper.selectAll();
        Set<String> locationres =new HashSet<>();
        for(PhenotypeFile file: phenotypeFiles){
            String tableName = file.getTableName();
            if(StringUtils.isNotNull(tableName) && StringUtils.isNotEmpty(tableName)){
                List<Map<String, Object>> maps = phenotypeFileMapper.selectAllColumns(tableName);
                for(Map<String, Object> map : maps){
                    for (String key : map.keySet()) {
                        if (key.contains("trait")) {
                            if (key.contains("id")) {
                                Object o = map.get(key);
                                if(o!=null){
                                    if(StringUtils.equals(o.toString(),id.toString())){
                                        locationres.add(file.getLocation());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return locationres;
    }*/


    public boolean traitIsExist(List<DataAnalysisVO> list,String traitName){
        for (int i = list.size() - 1; i >= 0; i--) {
            if(StringUtils.equals(list.get(i).getTraitName(),traitName)){
                return true;
            }
        }
        return false;
    }

    public static double calculateMax(List<Double> values) {
        if (values.isEmpty()) {
            return (double) 0;
        }

        double max = values.get(0);
        for (double value : values) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public static double calculateMin(List<Double> values) {
        if (values.isEmpty()) {
            return (double) 0;
        }

        double min = values.get(0);
        for (double value : values) {
            if (value < min) {
                min = value;
            }
        }
        return min;
    }

    public static double calculateAverage(List<Double> values) {
        if (values.isEmpty()) {
            return (double) 0;
        }

        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.size();
    }



    @Override
    public List<DataAnalysisVO> dataAnalysis(Long id) {
        List<String> result = new ArrayList<>();
        PhenotypeFile file = phenotypeFileMapper.selectPhenotypeFileByFileId(id);
        System.out.println(file);
        String tableName = file.getTableName();

        //结果总览
        List<String> allColumns = phenotypeFileMapper.getAllColumns(tableName);
        System.out.println(allColumns);

        //初始化列表
        for (int i = allColumns.size() - 1; i >= 0; i--) {
            String col = allColumns.get(i);
            if(col.contains("trait_id")){
                DataAnalysisVO data =new DataAnalysisVO();
                result.add(col);
            }
        }
        //性状名:数据
        HashMap<String,List<Double>> datanumber =new HashMap<>();

        //获取所有信息
        List<Map<String, Object>> maps = phenotypeFileMapper.selectAllColumns(tableName);
        //遍历每一行
        for (int i = 0; i < maps.size(); i++) {
            //从maps中以此获取想要的数据
            Map<String, Object> map = maps.get(i);
            for (String dataAnalysisVO : result){
                String traitref = dataAnalysisVO;
                Long traitId = Long.valueOf((map.get(traitref)).toString());
                Trait trait = traitMapper.selectTraitByTraitId(traitId);
                String traitValue = traitref.replace("id", "value");
                System.out.println(traitValue);
                Object o = map.get(traitValue);
                if(o==null) continue;
                Double value = Double.valueOf(o.toString());
                if(value!=0||value!=null){
                    List<Double> doubles;
                    String traitName = trait.getTraitName();
                    if(datanumber.containsKey(traitName)){
                        doubles = datanumber.get(traitName);
                        doubles.add(value);
                    }
                    else{
                        doubles =new ArrayList<>();
                        doubles.add(value);
                    }
                    datanumber.put(traitName,doubles);
                }

            }
        }

        List<DataAnalysisVO> res =new ArrayList<>();
        for (Map.Entry<String, List<Double>> entry : datanumber.entrySet()) {
            String name = entry.getKey();
            List<Double> values = entry.getValue();
            Trait query =new Trait();
            query.setTraitName(name);
            Trait trait = traitMapper.selectTraitList(query).get(0);
            if(values.size()==0) {
                DataAnalysisVO dataAnalysisVO = new DataAnalysisVO(
                        trait.getTraitId(),
                        trait.getTraitName(),
                        Double.valueOf(0),
                        Double.valueOf(0),
                        Double.valueOf(0));
            }
            else{
                double max = calculateMax(values);
                double min = calculateMin(values);
                double average = calculateAverage(values);
                DataAnalysisVO dataAnalysisVO =new DataAnalysisVO(
                        trait.getTraitId(),
                        trait.getTraitName(),
                        max,
                        min,
                        average);
                res.add(dataAnalysisVO);
            }
        }

        return res;
    }

    @Override
    public List<Trait> getTraitByType(Long typeId) {
        List<Trait> result =new ArrayList<>();
        AsTraitType asTraitType =new AsTraitType();
        asTraitType.setTraitTypeId(typeId);
        List<AsTraitType> asTraitTypes = asTraitTypeMapper.selectAsTraitTypeList(asTraitType);
        for(AsTraitType as:asTraitTypes){
            String traitId = as.getTraitId();
            Trait trait = traitMapper.selectTraitByTraitId(Long.valueOf(traitId));
            result.add(trait);
        }
        return result;
    }

    @Override
    public DataAnalysisVO dataAnalysisByName(Long id, String traitName) {
        Trait tn =new Trait();
        tn.setTraitName(traitName);
        Trait list = traitMapper.selectTraitList(tn).get(0);
        Long mainTraitId = list.getTraitId();

        PhenotypeFile file = phenotypeFileMapper.selectPhenotypeFileByFileId(id);
        String tableName = file.getTableName();

        //获取所有信息
        List<Map<String, Object>> maps = phenotypeFileMapper.selectAllColumns(tableName);
        List<Double> data = new ArrayList<>();
        String mainkey =null;
        System.out.println("mainTraitId"+mainTraitId);
        for(Map<String, Object> map: maps){
            System.out.println(map);
            boolean flag =false;
            for (Map.Entry<String, Object> entry : map.entrySet() ){
                String key = entry.getKey();
                String value =  (entry.getValue().toString());
                System.out.println(value);
                if(StringUtils.equals(value,mainTraitId.toString())){
                    mainkey = key;
                    flag=true;
                    break;
                }
            }
            if(flag==false)
                throw new NullPointerException();
        }
        String valuekey = mainkey.replace("id", "value");
        for(Map<String, Object> map: maps) {
            Object o = map.get(valuekey);
            if(o!=null) {
                Double value = Double.valueOf(o.toString());
                data.add(value);
            }
        }

        double max = calculateMax(data);
        double min = calculateMin(data);
        double average = calculateAverage(data);
        DataAnalysisVO dataAnalysisVO =new DataAnalysisVO(
                list.getTraitId(),
                list.getTraitName(),
                max,
                min,
                average);
        return dataAnalysisVO;
    }

    @Override
    public List<DataAnalysisVO> dataAnalysisBymaterialId(Long id, String materialId) {


        List<String> result = new ArrayList<>();
        PhenotypeFile file = phenotypeFileMapper.selectPhenotypeFileByFileId(id);
        System.out.println(file);
        String tableName = file.getTableName();

        //结果总览
        List<String> allColumns = phenotypeFileMapper.getAllColumns(tableName);
        System.out.println(allColumns);

        //初始化列表
        for (int i = allColumns.size() - 1; i >= 0; i--) {
            String col = allColumns.get(i);
            if(col.contains("trait_id")){
                DataAnalysisVO data =new DataAnalysisVO();
                result.add(col);
            }
        }
        //性状名:数据
        HashMap<String,List<Double>> datanumber =new HashMap<>();

        //获取所有信息
        List<Map<String, Object>> maps = phenotypeFileMapper.selectAllColumns(tableName);
        //获取我们想要查询的单个的材料的数据
        HashMap<Double,String> valueAndCreateTimeMap = new HashMap<>();
        Map<String, AbstractMap.SimpleEntry<Double,String>> wannaMap = new HashMap<>();

        //获取性状
        HashMap<Long, String> traitMap = infoUtil.getTraitsMapReverse();
        HashMap<Long, TraitType> traitTypeMap = infoUtil.getTraitTypeMap();

        //遍历每一行
        for (int i = 0; i < maps.size(); i++) {
            //从maps中以此获取想要的数据
            Map<String, Object> map = maps.get(i);

            for (String dataAnalysisVO : result){
                String traitref = dataAnalysisVO;
                Long traitId = Long.valueOf((map.get(traitref)).toString());
                String traitName = traitMap.get(traitId);
                String traitValue = traitref.replace("id", "value");
                System.out.println(traitValue);
                Object o = map.get(traitValue);
                if(o==null||StringUtils.equals(o.toString(),"NA")) {
                    continue;
                }
                Double value = Double.valueOf(o.toString());
                if(value!=0||value!=null){
                    List<Double> doubles;
                    if(datanumber.containsKey(traitName)){
                        doubles = datanumber.get(traitName);
                        doubles.add(value);
                    }
                    else{
                        doubles =new ArrayList<>();
                        doubles.add(value);
                    }
                    datanumber.put(traitName,doubles);
                }
            }
        }

        //遍历每一行
        for (int i = 0; i < maps.size(); i++) {
            //从maps中以此获取想要的数据
            Map<String, Object> map = maps.get(i);
            String material_id = map.get("material_id").toString();
            String create_time = map.get("create_time").toString();
            System.out.println(material_id);
            System.out.println(materialId);
            if(!StringUtils.equals(material_id,materialId)) continue;

            for (String dataAnalysisVO : result){
                String traitref = dataAnalysisVO;
                Long traitId = Long.valueOf((map.get(traitref)).toString());
                String traitName = traitMap.get(traitId);
                String traitValue = traitref.replace("id", "value");
                System.out.println(traitValue);
                Object o = map.get(traitValue);
                if(o==null||StringUtils.equals(o.toString(),"NA")) {
                    continue;
                }
                Double value = Double.valueOf(o.toString());
                if(value!=0||value!=null){
                    wannaMap.put(traitName, new AbstractMap.SimpleEntry<>(value,create_time));
                }
            }
        }

        List<DataAnalysisVO> res =new ArrayList<>();
        for (Map.Entry<String, List<Double>> entry : datanumber.entrySet()) {
            String name = entry.getKey();
            List<Double> values = entry.getValue();
            Trait query =new Trait();
            query.setTraitName(name);
            Trait trait = traitMapper.selectTraitListWithoutDeleted(query).get(0);
            if(values.size()==0) {
                DataAnalysisVO dataAnalysisVO = new DataAnalysisVO(
                        trait.getTraitId(),
                        trait.getTraitName(),
                        Double.valueOf(0),
                        Double.valueOf(0),
                        Double.valueOf(0));
            }
            else{
                double max = calculateMax(values);
                double min = calculateMin(values);
                double average = calculateAverage(values);
                DataAnalysisVO dataAnalysisVO =new DataAnalysisVO(
                        trait.getTraitId(),
                        trait.getTraitName(),
                        traitTypeMap.get(trait.getTraitId()).getTraitTypeId(),
                        traitTypeMap.get(trait.getTraitId()).getTraitTypeName(),
                        wannaMap.get(name).getKey(),
                        max,
                        min,
                        average,
                        wannaMap.get(name).getValue());
                res.add(dataAnalysisVO);
            }
        }
        return res;
    }

    //1.6 大查询 模糊匹配 根据性状名称模糊匹配
    @Override
    public List<Trait> selectTrait(String trait_name) {
        return traitMapper.selectTrait(trait_name);
    }

    //1.6
    public Integer checkTraitName(String trait_name){
        return traitMapper.checkTraitName(trait_name);
    }
}
