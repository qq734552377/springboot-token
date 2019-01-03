package com.pjb.springbootjjwt.entity;

/**
 * Created by pj on 2018/12/18.
 */
public class BaseResult {
    Object Data;
    int Size;
    String Info;
    String Status;

    public Object getData() {
        return Data;
    }

    public void setData(Object data) {
        Data = data;
    }

    public int getSize() {
        return Size;
    }

    public void setSize(int size) {
        Size = size;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
