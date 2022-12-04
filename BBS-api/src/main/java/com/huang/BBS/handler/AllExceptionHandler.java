package com.huang.BBS.handler;

import com.huang.BBS.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//对加了@Controller注解的方法进行拦截校验处理 AOP的实现

@ControllerAdvice
public class AllExceptionHandler {

    //进行异常处理 处理Exception.class的异常
    @ExceptionHandler(Exception.class)
    @ResponseBody   //返回json数据
    public Result doException(Exception ex){
        ex.printStackTrace();
        return Result.fail(-999,"系统异常");
    }
}
