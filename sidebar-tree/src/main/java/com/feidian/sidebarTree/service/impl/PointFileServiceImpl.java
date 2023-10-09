package com.feidian.sidebarTree.service.impl;

import java.util.List;
import com.feidian.common.utils.DateUtils;
import com.feidian.sidebarTree.domain.PointFile;
import com.feidian.sidebarTree.mapper.PointFileMapper;
import com.feidian.sidebarTree.service.IPointFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author feidian
 * @date 2022-11-07
 */
//废弃
@Service
public class PointFileServiceImpl implements IPointFileService
{
    @Autowired(required = false)
    private PointFileMapper pointFileMapper;

    @Override
    public String selectByArr(PointFile pointFile) {
        String s = null;
        s=pointFileMapper.selectByArr(pointFile);
        return s;
    }

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public PointFile selectPointFileById(Long id)
    {
        return pointFileMapper.selectPointFileById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param pointFile 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<PointFile> selectPointFileList(PointFile pointFile)
    {
        return pointFileMapper.selectPointFileList(pointFile);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param pointFile 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertPointFile(PointFile pointFile)
    {
        List<PointFile> pointFiles = pointFileMapper.selectPointFileList(pointFile);
        if(pointFiles.size()==0)//为0
        {
            pointFile.setCreateTime(DateUtils.getNowDate());
            return pointFileMapper.insertPointFile(pointFile);
        }
        else{
            pointFile.setUpdateTime(DateUtils.getNowDate());
            return pointFileMapper.updatePointFile(pointFile);
        }

    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param pointFile 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updatePointFile(PointFile pointFile)
    {
        pointFile.setUpdateTime(DateUtils.getNowDate());
        return pointFileMapper.updatePointFile(pointFile);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deletePointFileByIds(Long[] ids)
    {
        return pointFileMapper.deletePointFileByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deletePointFileById(Long id)
    {
        return pointFileMapper.deletePointFileById(id);
    }
}
