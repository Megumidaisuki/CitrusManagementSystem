package com.feidian.sidebarTree.mapper;

import com.feidian.sidebarTree.domain.Data;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * SensorMapper接口
 *
 * @author feidian
 * @date 2023-10-10
 */
@Mapper
public interface SensorMapper {
    public int saveData(Data data);

    public List<Data> selectAll();

    public List<Float> selectAmbientTemperature(@Param("starttime") String starttime, @Param("endtime") String endtime);

    public List<Float> selectAmbientHumidity(@Param("starttime") String starttime, @Param("endtime") String endtime);

    public List<Integer> selectCO2(@Param("starttime") String starttime, @Param("endtime") String endtime);

    @Select("SELECT CONVERT(pow, CHAR) FROM sensor")
    public List<String> selectPow();

    @Select("SELECT CONVERT(RSSI, CHAR) FROM sensor")
    public List<String> selectRSSI();

    public List<Integer> selectLightIntensity(@Param("starttime") String starttime, @Param("endtime") String endtime);

    public int deleteData(String nowtime);

    public List<String> selectDewTemp(@Param("starttime") String starttime, @Param("endtime") String endtime);
}
