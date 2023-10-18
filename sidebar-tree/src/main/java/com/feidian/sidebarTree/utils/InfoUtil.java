package com.feidian.sidebarTree.utils;

import com.feidian.sidebarTree.domain.Trait;
import com.feidian.sidebarTree.mapper.TraitMapper;
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
}