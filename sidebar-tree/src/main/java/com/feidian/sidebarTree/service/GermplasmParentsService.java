package com.feidian.sidebarTree.service;

import com.feidian.sidebarTree.domain.GermplasmParents;

import java.util.List;

public interface GermplasmParentsService {
    GermplasmParents getGermplasmParentsById(Long id);

    List<GermplasmParents> getAllGermplasmParents();

    GermplasmParents createGermplasmParents(GermplasmParents germplasmParents);

    GermplasmParents updateGermplasmParents(GermplasmParents germplasmParents);

    int deleteGermplasmParents(Long id);

    List<GermplasmParents> findGermplasmParents(GermplasmParents germplasmParents);
}
