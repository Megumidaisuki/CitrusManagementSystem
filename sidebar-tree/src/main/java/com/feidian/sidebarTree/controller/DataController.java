package com.feidian.sidebarTree.controller;
import com.feidian.common.core.controller.BaseController;
import com.feidian.common.core.domain.AjaxResult;
import com.feidian.sidebarTree.domain.Data;
import com.feidian.sidebarTree.domain.Time;
import com.feidian.sidebarTree.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 传感器Controller
 *
 * @author feidian
 * @date 2023-10-11
 */
@RestController
@RequestMapping("/citrus/data")
public class DataController extends BaseController{
    @Autowired
    private SensorService sensorService;

    // 处理POST请求，用于接收传感器数据并存储到数据库
    @PostMapping("/getdata")
    public String getData(@RequestBody Data data) {
        // 打印接收到的数据，用于验证
        System.out.println(data.getAmbientHumidity());

        // 存储数据到数据库
        int s = sensorService.saveData(data);
        if (s == 1) {
            return "success";
        }
        return "false";
    }

    // 处理GET请求，用于获取所有传感器数据
    @GetMapping("/all")
    public AjaxResult getAll() throws ParseException {
        // 删除数据
        String code = sensorService.deleteData();
        System.out.println(code);

        // 返回所有传感器数据
        return AjaxResult.success(sensorService.selectAll());
    }

    // 处理POST请求，获取指定时间范围内的环境温度数据
    @PostMapping("/ambientTemperature")
    @ResponseBody
    public AjaxResult getAmbientTemperature(@RequestBody Time time) {
        String starttime = time.getStarttime();
        String endtime = time.getEndtime();
        return AjaxResult.success(sensorService.selectAmbientTemperature(starttime, endtime));
    }

    // 处理POST请求，获取指定时间范围内的环境湿度数据
    @PostMapping("/ambientHumidity")
    @ResponseBody
    public AjaxResult getAmbientHumidity(@RequestBody Time time) {
        String starttime = time.getStarttime();
        String endtime = time.getEndtime();
        return AjaxResult.success(sensorService.selectAmbientHumidity(starttime, endtime));
    }

    // 处理POST请求，获取指定时间范围内的CO2浓度数据
    @PostMapping("/CO2")
    @ResponseBody
    public AjaxResult getCO2(@RequestBody Time time) {
        String starttime = time.getStarttime();
        String endtime = time.getEndtime();
        return AjaxResult.success(sensorService.selectCO2(starttime, endtime));
    }

    // 处理POST请求，获取指定时间范围内的光照强度数据
    @PostMapping("/lightIntensity")
    @ResponseBody
    public AjaxResult getlightIntensity(@RequestBody Time time) {
        String starttime = time.getStarttime();
        String endtime = time.getEndtime();
        return AjaxResult.success(sensorService.selectLightIntensity(starttime, endtime));
    }

    // 处理POST请求，获取指定时间范围内的露点温度数据
    @PostMapping("/dewTemp")
    @ResponseBody
    public AjaxResult getDewTemp(@RequestBody Time time) {
        String starttime = time.getStarttime();
        String endtime = time.getEndtime();
        return AjaxResult.success(sensorService.selectDewTemp(starttime, endtime));
    }

    // 处理GET请求，获取最近一段时间内的环境温度数据
    @GetMapping("/ambientTemperatureTime")
    public AjaxResult getambientTemperatureByTime(int i) {
        // 获取当前时间
        Calendar calendar = Calendar.getInstance();
        Date date1 = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowtime = format.format(date1);

        // 获取过去时间
        String pasttime = sensorService.pastTime(i);

        // 查询数据并返回结果
        List<Float> temp = sensorService.selectAmbientTemperature(pasttime, nowtime);
        if (temp.isEmpty()) {
            return AjaxResult.success("暂无数据");
        }
        return AjaxResult.success(sensorService.selectAmbientTemperatureByTime(temp, i));
    }

    // 处理GET请求，获取最近一段时间内的环境湿度数据
    @GetMapping("/ambientHumidityTime")
    public AjaxResult getAmbientAmbientHumidityByTime(int i) {
        // 获取当前时间
        Calendar calendar = Calendar.getInstance();
        Date date1 = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowtime = format.format(date1);

        // 获取过去时间
        String pasttime = sensorService.pastTime(i);

        // 查询数据并返回结果
        List<Float> temp = sensorService.selectAmbientHumidity(pasttime, nowtime);
        if (temp.isEmpty()) {
            return AjaxResult.success("暂无数据");
        }
        return AjaxResult.success(sensorService.selectAmbientHumidityByTime(temp, i));
    }

    // 处理GET请求，获取最近一段时间内的CO2浓度数据
    @GetMapping("CO2Time")
    public AjaxResult getCO2ByTime(int i) {
        // 获取当前时间
        Calendar calendar = Calendar.getInstance();
        Date date1 = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowtime = format.format(date1);


        // 获取过去一段时间的起始时间
        String pasttime = sensorService.pastTime(i);

        // 查询 CO2 数据
        List<Integer> temp = sensorService.selectCO2(pasttime, nowtime);

        // 如果查询结果为空，返回暂无数据
        if (temp.isEmpty()) {
            return AjaxResult.success("暂无数据");
        }

        // 返回查询结果
        return AjaxResult.success(sensorService.selectCO2ByTime(temp, i));
    }

    @GetMapping("lightIntensityTime")
    public AjaxResult getlightIntensityByTime(int i) {
        // 获取当前日期和时间
        Calendar calendar = Calendar.getInstance();
        Date date1 = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowtime = format.format(date1);

        // 获取过去一段时间的起始时间
        String pasttime = sensorService.pastTime(i);

        // 查询光照强度数据
        List<Integer> temp = sensorService.selectLightIntensity(pasttime, nowtime);

        // 如果查询结果为空，返回暂无数据
        if (temp.isEmpty()) {
            return AjaxResult.success("暂无数据");
        }

        // 返回查询结果
        return AjaxResult.success(sensorService.selectLightIntensityByTime(temp, i));
    }

    @GetMapping("/dewTempTime")
    public AjaxResult getdewTempByTime(int i) {
        // 获取当前日期和时间
        Calendar calendar = Calendar.getInstance();
        Date date1 = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowtime = format.format(date1);

        // 获取过去一段时间的起始时间
        String pasttime = sensorService.pastTime(i);

        // 查询露点温度数据
        List<String> temp = sensorService.selectDewTemp(pasttime, nowtime);

        // 如果查询结果为空，返回暂无数据
        if (temp.isEmpty()) {
            return AjaxResult.success("暂无数据");
        }

        // 返回查询结果
        return AjaxResult.success(sensorService.selectDewTempByTime(temp, i));
    }





}
