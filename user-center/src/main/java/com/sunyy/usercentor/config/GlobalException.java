package com.sunyy.usercentor.config;

import com.sunyy.usercentor.common.Message;
import com.sunyy.usercentor.common.MyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * @author ovi
 * @since 2024/8/11
 */
@Slf4j
@ControllerAdvice
public class GlobalException {
    @ResponseBody
    @ExceptionHandler(value = Throwable.class)
    public Message globalExceptionHandler(Throwable e) {
        log.error(e.getMessage(), e);
        return Message.error("未知错误");
    }

    @ResponseBody
    @ExceptionHandler(value = MyException.class)
    public Message myExceptionHandler(MyException e) {
        log.error(e.getMessage(), e);
        return Message.error(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Message methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e, HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        BindingResult bindingResult = e.getBindingResult();
        log.error("请求[ {} ] {} 的参数校验发生错误", request.getMethod(), request.getRequestURL());
        for (ObjectError objectError : bindingResult.getAllErrors()) {
            FieldError fieldError = (FieldError) objectError;
            log.error("参数 {} = {} 校验错误：{}", fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage());
            sb.append("field: ").append(fieldError.getField())
                    .append(" value: ").append(fieldError.getRejectedValue())
                    .append(" msg: ").append(fieldError.getDefaultMessage()).append("\n");
        }
        sb.setLength(sb.length() - 1);
        return Message.error(sb.toString());
    }

    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Message constraintViolationExceptionHandler(ConstraintViolationException e, HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        log.error("请求[ {} ] {} 的参数校验发生错误", request.getMethod(), request.getRequestURL());
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            log.error("参数 {} = {} 校验错误：{}", constraintViolation.getPropertyPath(), constraintViolation.getInvalidValue(), constraintViolation.getMessage());
            sb.append("field: ").append(constraintViolation.getPropertyPath())
                    .append(" value: ").append(constraintViolation.getInvalidValue())
                    .append(" msg: ").append(constraintViolation.getMessage()).append("\n");
        }
        sb.setLength(sb.length() - 1);
        return Message.error(sb.toString());
    }
}
