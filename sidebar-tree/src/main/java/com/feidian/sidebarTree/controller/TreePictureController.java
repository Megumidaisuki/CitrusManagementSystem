package com.feidian.sidebarTree.controller;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.feidian.common.config.ZhongMiaoConfig;
import com.feidian.common.utils.StringUtils;
import com.feidian.common.utils.file.FileUtils;
import com.feidian.sidebarTree.domain.SidebarTree;
import com.feidian.sidebarTree.domain.TreeFile;
import com.feidian.sidebarTree.domain.TreePicture;
import com.feidian.sidebarTree.service.FillService;
import com.feidian.sidebarTree.service.ISidebarTreeService;
import com.feidian.sidebarTree.service.ITreePictureService;
import jnr.ffi.annotations.In;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.feidian.common.annotation.Log;
import com.feidian.common.core.controller.BaseController;
import com.feidian.common.core.domain.AjaxResult;
import com.feidian.common.enums.BusinessType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 【请填写功能名称】Controller
 *
 * @author feidian
 * @date 2022-07-03
 */
@RestController
@RequestMapping("/system/picture")
public class TreePictureController extends BaseController
{
    @Autowired
    private ITreePictureService treePictureService;
    @Autowired
    private FillService fillService;

    @Autowired
    private ISidebarTreeService sidebarTreeService;
    /**
     * 根据树的节点返回对应的图片url
     */

    @GetMapping("/list")
    public List<TreePicture> list(int treeId)
    {
        try {
            Long userId = getUserId();
        }catch (Exception e){
        }
        return treePictureService.getTreeByTreeId(treeId);

    }

    @GetMapping("/count")
    public AjaxResult nodePicCount(Long treeId){
        return AjaxResult.success(treePictureService.getNodePicCount(treeId));
    }

    /**
     * 上传图片
     */
    @PostMapping("/upload")
    public AjaxResult upload(int treeId,@RequestParam("file") MultipartFile file,int isShow){
        int res= fillService.upload(treeId, file, isShow);

        if(res==1){
            return AjaxResult.success("图片上传成功");
        }
        else if(res==0){
            return AjaxResult.error("图片上传失败");
        }
        else if(res==2){
            AjaxResult result =new AjaxResult(501,"部分图片上传成功，但存在不兼容文件，请检查文件类型与格式");
            return result;
        }
        return AjaxResult.error();
    }

    @PostMapping("/uploadChunk")
    public AjaxResult uploadChunk(@RequestParam("file") MultipartFile chunk,
                                  int totalChunks,
                                  int currentChunk){

        return fillService.uploadChunk(chunk,totalChunks,currentChunk);

    }



    @PostMapping("/mergeChunks")
    public AjaxResult mergeChunks(@RequestParam("fileName") String fileName,@RequestParam("treeId") int treeId,@RequestParam("isShow") int isShow){
        return fillService.mergeChunks(fileName,treeId,isShow);
    }




    @PostMapping("/uploadFromIp")
    public AjaxResult uploadFromIp(int treeId,@RequestParam("file") MultipartFile file){
        int isShow=1;
        int res= fillService.upload(treeId, file, isShow);

        if(res==1){
            return AjaxResult.success("图片上传成功");
        }
        else if(res==0){
            return AjaxResult.error("图片上传失败");
        }
        else if(res==2){
            AjaxResult result =new AjaxResult(501,"部分图片上传成功，但存在不兼容文件，请检查文件类型与格式");
            return result;
        }
        return AjaxResult.error();
    }


    @PostMapping("/uploadByIp")
    public AjaxResult uploadByIp(String ip,String parentFile,int treeId){
        try {
            // 定义要发送的字符
            AjaxResult result=new AjaxResult();
            String text = parentFile + "&&&" + treeId;

            // 创建HttpHeaders对象，设置Content-Type为text/plain
            HttpHeaders headers = new HttpHeaders();
            // headers.setContentType(MediaType.TEXT_PLAIN);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
            // 创建HttpEntity对象，将字符设置为请求体，并将HttpHeaders对象传递给构造函数
            HttpEntity<String> request = new HttpEntity<>(text, headers);

            // 使用RestTemplate发送HTTP POST请求
            RestTemplate restTemplate = new RestTemplate();
            //flask所在
            String url = "http://" + ip + ":9999/text";
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            //五秒无法连接就报超时
            factory.setConnectTimeout(3000);
            factory.setReadTimeout(2000);
            restTemplate.setRequestFactory(factory);

//            restTemplate.postForEntity(url, request, String.class);

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            // 获取Flask返回的响应
            String flaskResponse = response.getBody();
            System.out.println(flaskResponse);
            if (flaskResponse.equals("empty"))
                return new AjaxResult(550,"文件夹不存在");
            if(flaskResponse.equals("null"))
                return new AjaxResult(551,"文件夹为空");

        }
        catch (Exception e){
            e.printStackTrace();
            return error("连接失败，ip地址错误");
        }
        sidebarTreeService.updateIpAndPath(treeId,ip,parentFile);
        return success("上传成功");
    }


    /**
     * 获取【请填写功能名称】详细信息
     */
    //@PreAuthorize("@ss.hasPermi('system:picture:query')")
    @GetMapping(value = "/{pictureId}")
    public AjaxResult getInfo(@PathVariable("pictureId") Long pictureId)
    {
        return AjaxResult.success(treePictureService.selectTreePictureByPictureId(pictureId));
    }

    /**
     * 下载文件
     * @param pictureId
     * @param response
     * @param request
     */
    @GetMapping("/download")
    public void fileDownload(Integer pictureId, HttpServletResponse response, HttpServletRequest request) throws Exception {
        try{


            String filePath = treePictureService.selectPictureUrlById(pictureId);
            String realFileName = filePath.substring(filePath.lastIndexOf("\\")+1);
            filePath = filePath.replace("\\", "\\\\");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
        }
        catch (Exception e)
        {
            throw new Exception("文件下载失败");
        }
    }

    @GetMapping("/chunkDownload")
    


    /**
     * 新增【请填写功能名称】
     */
    //@PreAuthorize("@ss.hasPermi('system:picture:add')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TreePicture treePicture)
    {
        return toAjax(treePictureService.insertTreePicture(treePicture));
    }

    /**
     * 修改【请填写功能名称】
     */
    //@PreAuthorize("@ss.hasPermi('system:picture:edit')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TreePicture treePicture)
    {
        return toAjax(treePictureService.updateTreePicture(treePicture));
    }

    /**
     * 删除【请填写功能名称】
     */
    @GetMapping("/remove")
    public AjaxResult remove(int pictureId)
    {
        return toAjax(treePictureService.deleteTreePictureByPictureId((long)pictureId));
    }
}
