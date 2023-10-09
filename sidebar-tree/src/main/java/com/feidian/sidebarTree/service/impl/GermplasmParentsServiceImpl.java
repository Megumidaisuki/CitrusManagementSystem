package com.feidian.sidebarTree.service.impl;
import com.feidian.common.core.redis.RedisCache;
import com.feidian.common.utils.SecurityUtils;
import com.feidian.sidebarTree.domain.GermplasmParents;
import com.feidian.sidebarTree.mapper.GermplasmParentsMapper;
import com.feidian.sidebarTree.service.GermplasmParentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GermplasmParentsServiceImpl implements GermplasmParentsService {
    @Autowired
    private GermplasmParentsMapper germplasmParentsMapper;

    @Autowired
    private RedisCache redisCache;//redis缓存

    @Override
    public GermplasmParents getGermplasmParentsById(Long id) {
        String cacheKey = "germplasmParents:id:" + id;
        GermplasmParents germplasmParents = redisCache.getCacheObject(cacheKey);
        if (germplasmParents == null) {
            germplasmParents = germplasmParentsMapper.getGermplasmParentsById(id);
            System.out.println("没有缓存");
            if (germplasmParents != null) {
                redisCache.setCacheObject(cacheKey, germplasmParents);
            }
        }
        return germplasmParents;
    }

    @Override
    public List<GermplasmParents> getAllGermplasmParents() {
        List<GermplasmParents> germplasmParentsList = germplasmParentsMapper.getAllGermplasmParents();
        if (germplasmParentsList != null && !germplasmParentsList.isEmpty()) {
            for (GermplasmParents germplasmParents : germplasmParentsList) {
                String cacheKey = "germplasmParents:id:" + germplasmParents.getId();
                redisCache.setCacheObject(cacheKey, germplasmParents);
            }
        }
        return germplasmParentsList;
    }


    @Override
    public GermplasmParents createGermplasmParents(GermplasmParents germplasmParents) {
        //查询是否存在
        GermplasmParents query = new GermplasmParents();
        query.setFather(germplasmParents.getFather());
        query.setMaterialId(germplasmParents.getMaterialId());
        query.setMother(germplasmParents.getMother());
        List<GermplasmParents> list = germplasmParentsMapper.findGermplasmParents(query);
        if (list.size()!=0) return  list.get(0);
        String username = SecurityUtils.getUsername();
        germplasmParents.setCreateBy(username);

        germplasmParentsMapper.createGermplasmParents(germplasmParents);

        GermplasmParents result = germplasmParentsMapper.findGermplasmParents(germplasmParents).get(0);
        String cacheKey = "germplasmParents:id:" + result.getId();
        redisCache.setCacheObject(cacheKey, result);
        return result;
    }

    @Override
    public GermplasmParents updateGermplasmParents(GermplasmParents germplasmParents) {
        String username = SecurityUtils.getUsername();
        germplasmParents.setUpdateBy(username);
        germplasmParentsMapper.updateGermplasmParents(germplasmParents);
        GermplasmParents germplasmParentsById = germplasmParentsMapper.getGermplasmParentsById(germplasmParents.getId());
        String cacheKey = "germplasmParents:id:" + germplasmParentsById.getId();
        redisCache.setCacheObject(cacheKey, germplasmParentsById);
        return germplasmParents;
    }

    @Override
    public int deleteGermplasmParents(Long id) {
        int i = germplasmParentsMapper.deleteGermplasmParents(id);
        String cacheKey = "germplasmParents:id:" + id;
        redisCache.deleteObject(cacheKey);
        return i;
    }

    @Override
    public List<GermplasmParents> findGermplasmParents(GermplasmParents germplasmParents) {
        return germplasmParentsMapper.findGermplasmParents(germplasmParents);
    }
}
