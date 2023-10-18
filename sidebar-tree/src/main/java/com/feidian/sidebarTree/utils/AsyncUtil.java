package com.feidian.sidebarTree.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class AsyncUtil {

    /*@Autowired
    private GermplasmParentsMapper germplasmParentsMapper;*/

    /*@Async
    public void createGermplasmParents(HashSet<GermplasmParents> germplasmParentsHashSet){
        for (GermplasmParents germplasmParents : germplasmParentsHashSet) {
            germplasmParentsMapper.createGermplasmParents(germplasmParents);
        }
    }*/

    //todo 基因型上传/合并后异步生成图片
    @Async
    public void generateImg(Long fileId){
        System.out.println("开始生成图片");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("生成图片完成");
    }
}
