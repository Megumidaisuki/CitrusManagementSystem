package com.feidian.sidebarTree.utils;

import com.feidian.sidebarTree.domain.Population;
import com.feidian.sidebarTree.mapper.PopulationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CacheUtil {

    @Autowired
    private PopulationMapper populationMapper;

    @Cacheable(value = "populations",key = "#root.targetClass + '.' + #root.methodName + #root.args")
    public List<Population> getPopulationList() {
        return populationMapper.selectPopulationList(new Population());
    }
}
