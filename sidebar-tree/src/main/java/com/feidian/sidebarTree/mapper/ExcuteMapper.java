package com.feidian.sidebarTree.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ExcuteMapper {
    void excute(@Param("sql") String sql);
}
