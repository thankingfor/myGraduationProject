package vip.bzsy.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vip.bzsy.common.CommonResponse;
import vip.bzsy.common.CommonStatus;
import vip.bzsy.exception.CommonException;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(value = Exception.class)
    public CommonResponse handlerException(HttpServletRequest req,
                                           CommonException ex) {
        CommonResponse response = new CommonResponse(CommonStatus.EXCEPTION);
        log.info(ex.getMessage());
        return response;
    }

    @ExceptionHandler(value = CommonException.class)
    public CommonResponse handlerCommonException(HttpServletRequest req,
                                             CommonException ex) {
        CommonResponse response = new CommonResponse(CommonStatus.EXCEPTION);
        response.setMsg(ex.getMessage());
        return response;
    }


}
