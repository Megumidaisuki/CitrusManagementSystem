package com.feidian.sidebarTree.exception;

/**
 * @Description: 自定义的校园二手交易平台业务处理过程发生异常
 * @Author: Mark
 * @CreateDate: 2018/10/9 18:06
 * @Version: 2.0
 * @Copyright : 豆浆油条个人非正式工作室
 */
public class Campuso2oException extends RuntimeException{
    public Campuso2oException(String origin){
        super("{"+origin+"}发生了异常：");
    }
    public Campuso2oException(String origin,Exception ex){
        super("{"+origin+"}发生了异常："+parseErrMsg(ex));

    }

    private static String parseErrMsg(Exception ex) {
        if(ex == null){
            return "异常堆栈获为空";
        }
        String errMsg = "";
        StackTraceElement[] stackTrace = ex.getStackTrace();
        for (StackTraceElement s : stackTrace) {
            errMsg+="\tat " + s + "\r\n";
        }
        return errMsg;
    }
}


