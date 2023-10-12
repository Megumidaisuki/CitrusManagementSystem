package com.feidian.sidebarTree.service;

import com.feidian.sidebarTree.domain.Data;
import jnr.ffi.annotations.In;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;


public interface SensorService {
    public int saveData(Data data);

    public List<Data> selectAll();

    public List<Float> selectAmbientTemperature(String starttime,String endtime);

    public List<Float> selectAmbientHumidity(String starttime,String endtime);

    public List<Integer> selectCO2(String starttime,String endtime);

    public List<String> selectPow();

    public List<String> selectRSSI();

    public List<Integer> selectLightIntensity(String starttime,String endtime);

    public List<String> selectDewTemp(String starttime,String endtime);

    public String deleteData() throws ParseException;

    public String pastTime(int i);

    public List<Float> selectAmbientTemperatureByTime(List<Float> temp,int i);

    public List<Float> selectAmbientHumidityByTime(List<Float> temp,int i);

    public List<Integer> selectCO2ByTime(List<Integer> temp,int i);

    public List<Integer> selectLightIntensityByTime(List<Integer> temp,int i);

    public List<String> selectDewTempByTime(List<String> temp,int i);


}
