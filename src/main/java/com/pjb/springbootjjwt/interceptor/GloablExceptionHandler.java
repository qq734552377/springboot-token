package com.pjb.springbootjjwt.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.pjb.springbootjjwt.entity.BaseResult;
import com.pjb.springbootjjwt.entity.RequestState;
import com.pjb.springbootjjwt.exceptions.AthException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author jinbin
 * @date 2018-07-08 22:37
 */
@ControllerAdvice
public class GloablExceptionHandler {
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        String msg = e.getMessage();
        if (msg == null || msg.equals("")) {
            msg = "服务器出错";
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", msg);
        return jsonObject;
    }

    @ResponseBody
    @ExceptionHandler(AthException.class)
    public BaseResult handleAthException(AthException e){
        BaseResult baseResult = new BaseResult();
        baseResult.setStatus(RequestState.FAIL);
        baseResult.setInfo(e.getMessage());
        return baseResult;
    }

}
