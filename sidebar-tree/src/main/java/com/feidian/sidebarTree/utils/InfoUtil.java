package com.feidian.sidebarTree.utils;

import com.feidian.sidebarTree.domain.AsTraitType;
import com.feidian.sidebarTree.domain.Trait;
import com.feidian.sidebarTree.domain.TraitType;
import com.feidian.sidebarTree.domain.vo.DataAnalysisVO;
import com.feidian.sidebarTree.domain.vo.TraitTypeVO;
import com.feidian.sidebarTree.mapper.AsTraitTypeMapper;
import com.feidian.sidebarTree.mapper.TraitMapper;
import com.feidian.sidebarTree.mapper.TraitTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



import java.util.HashMap;
import java.util.List;


@Component
public class InfoUtil{

    private HashMap<String,Long> traitsMap = null;

    private HashMap<Long,String> traitsMapReverse = null;

    private HashMap<Long,Trait> traitsMapObjectReverse = null;


    @Autowired
    private TraitMapper traitMapper;

    @Autowired
    private TraitTypeMapper traitTypeMapper;

    @Autowired
    private AsTraitTypeMapper asTraitTypeMapper;


    public HashMap<String,Long> getTraitsMap(){
//        if(traitsMap == null){
            List<Trait> traitList = traitMapper.selectTraitListWithoutDeleted(new Trait());
            HashMap<String, Long> traitsMap = new HashMap<>();
            for (Trait trait : traitList) {
                traitsMap.put(trait.getTraitName(), trait.getTraitId());
            }
            this.traitsMap = traitsMap;
//        }
        return traitsMap;
    }


    public HashMap<Long,String> getTraitsMapReverse(){
        List<Trait> traitList = traitMapper.selectTraitListWithoutDeleted(new Trait());
        HashMap<Long, String> traitsMap = new HashMap<>();
        for (Trait trait : traitList) {
            traitsMap.put(trait.getTraitId(), trait.getTraitName());
        }
        this.traitsMapReverse = traitsMap;
        return traitsMapReverse;
    }

    public HashMap<Long,Trait> getTraitsObjectMapReverse(){
        List<Trait> traitList = traitMapper.selectTraitListWithoutDeleted(new Trait());
        HashMap<Long, Trait> traitsMap = new HashMap<>();
        for (Trait trait : traitList) {
            traitsMap.put(trait.getTraitId(), trait);
        }
        this.traitsMapObjectReverse = traitsMap;
        return traitsMapObjectReverse;
    }

    //获取性状ID与性状类型的对应关系表
    public HashMap<Long,TraitType> getTraitTypeMap(){
        //查询性状Id、性状名称、性状类型id、性状类型名称
        List<TraitTypeVO> traitTypeList = traitMapper.selectTraitAndTraitTypeList();
        HashMap<Long, TraitType> traitsMap = new HashMap<>();
        for (TraitTypeVO trait : traitTypeList) {
            traitsMap.put(trait.getTraitId(),new TraitType(trait.getTraitTypeId(),trait.getTraitTypeName()));
        }
        return traitsMap;
    }
}