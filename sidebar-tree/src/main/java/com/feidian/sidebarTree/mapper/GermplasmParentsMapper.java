package com.feidian.sidebarTree.mapper;


import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GermplasmParentsMapper {
    GermplasmParents getGermplasmParentsById(Long id);

    List<GermplasmParents> getAllGermplasmParents();

    //不带创建更新信息
    List<GermplasmParents> getAllGermplasmParentsInfo();

    void createGermplasmParents(GermplasmParents germplasmParents);

    void updateGermplasmParents(GermplasmParents germplasmParents);

    int deleteGermplasmParents(Long id);

    List<GermplasmParents> findGermplasmParents(GermplasmParents germplasmParents);

    GermplasmParents selectGermplasmParents(String material_id);
}