package com.feidian.sidebarTree.service.impl;

import com.feidian.sidebarTree.domain.Data;
import com.feidian.sidebarTree.mapper.SensorMapper;
import com.feidian.sidebarTree.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class SensorServiceImpl implements SensorService {
    @Autowired
    private SensorMapper sensorMapper;

    @Override
    public int saveData(Data data) {
        sensorMapper.saveData(data);
        return 1;
    }

    @Override
    public List<Data> selectAll() {
        return sensorMapper.selectAll();
    }

    @Override
    public List<Float> selectAmbientTemperature(String starttime,String endtime) {
        return sensorMapper.selectAmbientTemperature(starttime,endtime);
    }

    @Override
    public List<Float> selectAmbientHumidity(String starttime,String endtime) {
        return sensorMapper.selectAmbientHumidity(starttime,endtime);
    }

    @Override
    public List<Integer> selectCO2(String starttime,String endtime) {
        return sensorMapper.selectCO2(starttime,endtime);
    }

    @Override
    public List<String> selectPow() {
        return sensorMapper.selectPow();
    }

    @Override
    public List<String> selectRSSI() {
        return sensorMapper.selectRSSI();
    }

    @Override
    public List<Integer> selectLightIntensity(String starttime,String endtime) {
        return sensorMapper.selectLightIntensity(starttime,endtime);
    }

    @Override
    public List<String> selectDewTemp(String starttime, String endtime) {
        return sensorMapper.selectDewTemp(starttime,endtime);
    }

    @Override
    public String deleteData() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-3);
        Date date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowtime = formatter.format(date);
        int i=sensorMapper.deleteData(nowtime);
        if(i==1){
            return "success";
        }
        return "false";
    }

    @Override
    public String pastTime(int i) {
        if(i==1){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR,-12);
            Date date = calendar.getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowtime = formatter.format(date);
            return nowtime;
        }else if(i==2){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE,-1);
            Date date = calendar.getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowtime = formatter.format(date);
            return nowtime;
        }else if (i==3){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE,-7);
            Date date = calendar.getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowtime = formatter.format(date);
            return nowtime;
        }else if(i==4){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH,-1);
            Date date = calendar.getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowtime = formatter.format(date);
            return nowtime;
        }
        return "请给出正确时间";
    }

    @Override
    public List<Float> selectAmbientTemperatureByTime(List<Float> temp, int i) {
        Float[] array = new Float[temp.size()];
        temp.toArray(array);
        List<Float> target = new ArrayList<>();
        target.add(array[0]);
        if(i==1){   //12h 刻度5min 每隔5个存一条
            for(int x=1;x<array.length;x++){
                if(x%5==0){
                    target.add(array[x]);
                }
            }
        }else if(i==2){//1天 刻度10min 每隔10个存一个
            for(int x=1;x<array.length;x++){
                if(x%10==0){
                    target.add(array[x]);
                }
            }
        }else if(i==3){
            for(int x=1;x<array.length;x++){
                if(x%60==0){
                    target.add(array[x]);
                }
            }
        }else if(i==4){
            for(int x=1;x<array.length;x++){
                if(x%240==0){
                    target.add(array[x]);
                }
            }
        }
        return target;
    }

    @Override
    public List<Float> selectAmbientHumidityByTime(List<Float> temp, int i) {
        Float[] array = new Float[temp.size()];
        temp.toArray(array);
        List<Float> target = new ArrayList<>();
        target.add(array[0]);
        if(i==1){   //12h 刻度5min 每隔5个存一条
            for(int x=1;x<array.length;x++){
                if(x%5==0){
                    target.add(array[x]);
                }
            }
        }else if(i==2){//1天 刻度10min 每隔10个存一个
            for(int x=1;x<array.length;x++){
                if(x%10==0){
                    target.add(array[x]);
                }
            }
        }else if(i==3){
            for(int x=1;x<array.length;x++){
                if(x%60==0){
                    target.add(array[x]);
                }
            }
        }else if(i==4){
            for(int x=1;x<array.length;x++){
                if(x%240==0){
                    target.add(array[x]);
                }
            }
        }
        return target;
    }

    @Override
    public List<Integer> selectCO2ByTime(List<Integer> temp, int i) {
        Integer[] array = new Integer[temp.size()];
        temp.toArray(array);
        List<Integer> target = new ArrayList<>();
        target.add(array[0]);
        if(i==1){   //12h 刻度5min 每隔5个存一条
            for(int x=1;x<array.length;x++){
                if(x%5==0){
                    target.add(array[x]);
                }
            }
        }else if(i==2){//1天 刻度10min 每隔10个存一个
            for(int x=1;x<array.length;x++){
                if(x%10==0){
                    target.add(array[x]);
                }
            }
        }else if(i==3){
            for(int x=1;x<array.length;x++){
                if(x%60==0){
                    target.add(array[x]);
                }
            }
        }else if(i==4){
            for(int x=1;x<array.length;x++){
                if(x%240==0){
                    target.add(array[x]);
                }
            }
        }
        return target;
    }

    @Override
    public List<Integer> selectLightIntensityByTime(List<Integer> temp, int i) {
        Integer[] array = new Integer[temp.size()];
        temp.toArray(array);
        List<Integer> target = new ArrayList<>();
        target.add(array[0]);
        if(i==1){   //12h 刻度5min 每隔5个存一条
            for(int x=1;x<array.length;x++){
                if(x%5==0){
                    target.add(array[x]);
                }
            }
        }else if(i==2){//1天 刻度10min 每隔10个存一个
            for(int x=1;x<array.length;x++){
                if(x%10==0){
                    target.add(array[x]);
                }
            }
        }else if(i==3){
            for(int x=1;x<array.length;x++){
                if(x%60==0){
                    target.add(array[x]);
                }
            }
        }else if(i==4){
            for(int x=1;x<array.length;x++){
                if(x%240==0){
                    target.add(array[x]);
                }
            }
        }
        return target;
    }

    @Override
    public List<String> selectDewTempByTime(List<String> temp, int i) {
        String[] array = new String[temp.size()];
        temp.toArray(array);
        List<String> target = new ArrayList<>();
        target.add(array[0]);
        if(i==1){   //12h 刻度5min 每隔5个存一条
            for(int x=1;x<array.length;x++){
                if(x%5==0){
                    target.add(array[x]);
                }
            }
        }else if(i==2){//1天 刻度10min 每隔10个存一个
            for(int x=1;x<array.length;x++){
                if(x%10==0){
                    target.add(array[x]);
                }
            }
        }else if(i==3){
            for(int x=1;x<array.length;x++){
                if(x%60==0){
                    target.add(array[x]);
                }
            }
        }else if(i==4){
            for(int x=1;x<array.length;x++){
                if(x%240==0){
                    target.add(array[x]);
                }
            }
        }
        return target;
    }


}

