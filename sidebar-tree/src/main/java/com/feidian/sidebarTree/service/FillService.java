package com.feidian.sidebarTree.service;

import com.feidian.common.core.domain.AjaxResult;
import com.feidian.sidebarTree.domain.TreeFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

public interface FillService {

    public int upload(int treeId, MultipartFile file, int isShow);

    public boolean uploadFile(int treeId,MultipartFile file,int isShow,String description,String fileName,String dateTime);

    public String getFilePath(String fileName);

    public String getFilename(int treeId);

    public String getFileUrl(String fileName,int treeId);

    public void deleteImage(String url);

    public void downloadFile(HttpServletRequest request, HttpServletResponse response);

    public String dealCsv(int treeId) throws IOException, ParseException;

    public String dealCsvByDates(String[] dateArr, int treeId) throws IOException, ParseException;

    TreeFile getFile(Integer id);

    AjaxResult uploadChunk(MultipartFile chunk, int totalChunks, int currentChunk);

    AjaxResult mergeChunks(String fileName,int treeId,int isShow);
}
