package com.atguigu.cloud.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Accessors(chain = true) //开启链式编程
public class ResultData<T> {

    //状态码
    public String code;
    //返回消息
    public String message;
    //返回数据
    public T data;
    //时间戳
    public String timestamp;

    //每次调用接口创建返回对象的时候，都更新一下时间戳，记录流水时间
    public ResultData() {
        this.timestamp = dateBrush();
    }

    //操作成功返回的结果对象
    public static <T> ResultData<T> success(T data){

        ResultData<T> resultData = new ResultData<>();

        resultData.setCode(ReturnCodeEnum.RC200.getCode());
        resultData.setMessage(ReturnCodeEnum.RC200.getMessage());
        resultData.setData(data);

        return resultData;
    }

    public static ResultData<String> success(){

        ResultData<String> resultData = new ResultData();

        resultData.setCode(ReturnCodeEnum.RC200.getCode());
        resultData.setMessage(ReturnCodeEnum.RC200.getMessage());
        resultData.setData("Operation succeeded!");

        return resultData;
    }

    //操作失败返回的结果对象
    public static <T> ResultData<T> fail(String code, String message){

        ResultData<T> resultData = new ResultData<>();

        resultData.setCode(ReturnCodeEnum.RC500.getCode());
        resultData.setMessage(message);
        resultData.setData(null);

        return resultData;
    }

    public static <T> ResultData<T> fail(String message){

        ResultData<T> resultData = new ResultData<>();

        resultData.setCode(ReturnCodeEnum.RC500.getCode());
        resultData.setMessage(message);
        resultData.setData(null);

        return resultData;
    }


    /**
     * 时间格式刷
     * @return
     */
    public static String dateBrush(){
        //获取格式刷
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long currentTime = System.currentTimeMillis();

        //将时间戳转换为时间类型
        Date date = new Date(currentTime);

        //调用格式刷
        String dataStr = simpleDateFormat.format(date);

        return dataStr;
    }

}
