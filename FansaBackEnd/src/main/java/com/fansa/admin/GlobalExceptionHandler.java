package com.fansa.admin;



import com.fansa.admin.user.UserNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{


    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO handleGenericException(HttpServletRequest request,Exception exception) {
        ErrorDTO error = new ErrorDTO();

        error.setTimestamp(new Date());
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setPath(request.getServletPath());
        error.addError(exception.getMessage());

        LOGGER.error(exception.getMessage(),exception);
        return error;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO handleBadRequestException(HttpServletRequest request,Exception exception) {
        ErrorDTO error = new ErrorDTO();

        error.setTimestamp(new Date());
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setPath(request.getServletPath());
        error.addError(exception.getMessage());

        LOGGER.error(exception.getMessage(),exception);
        return error;
    }



    @ExceptionHandler({ConstraintViolationException.class, DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO handleConstraintViolationException(HttpServletRequest request, Exception exception) {
        ErrorDTO error = new ErrorDTO();

        error.setTimestamp(new Date());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setPath(request.getServletPath());

        if (exception instanceof ConstraintViolationException violationException) {
            violationException.getConstraintViolations().forEach(
                    constraint -> {
                        error.addError(constraint.getPropertyPath() + ": " + constraint.getMessage());
                    }
            );
        }
        if (exception instanceof DataIntegrityViolationException) {
            error.addError(exception.getMessage());
        }
        // Logging lỗi
        LOGGER.error(exception.getMessage(), exception);

        return error;
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorDTO handleUnAuthorizedException(HttpServletRequest request,Exception ex) {

        ErrorDTO error = new ErrorDTO();

        if (ex instanceof BadCredentialsException) {
            error.setTimestamp(new Date());
            error.setStatus(HttpStatus.UNAUTHORIZED.value());
            error.setPath(request.getServletPath());
            error.addError("Authentication Failure");
        }
        LOGGER.error(ex.getMessage(),ex);
        return error;
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorDTO handleExpiredOrSignatureException(HttpServletRequest request,Exception ex) {

        ErrorDTO error = new ErrorDTO();

        error.setTimestamp(new Date());
        error.setStatus(HttpStatus.FORBIDDEN.value());
        error.setPath(request.getServletPath());
        error.addError("Authentication JWT Expired Or Signature");

        LOGGER.error(ex.getMessage(),ex);
        return error;
    }


    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorDTO handleAccessDeniedException(HttpServletRequest request,Exception ex) {

        ErrorDTO error = new ErrorDTO();

        if (ex instanceof AccessDeniedException) {
            error.setTimestamp(new Date());
            error.setStatus(HttpStatus.FORBIDDEN.value());
            error.setPath(request.getServletPath());
            error.addError("Authentication accessdenied");
        }
        LOGGER.error(ex.getMessage(),ex);
        return error;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    public ErrorDTO handleUserNotFoundException(HttpServletRequest request,Exception ex) {

        ErrorDTO error = new ErrorDTO();


        error.setTimestamp(new Date());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setPath(request.getServletPath());
        error.addError(ex.getMessage());

        LOGGER.error(ex.getMessage(),ex);
        return error;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorDTO error = new ErrorDTO();
        error.setTimestamp(new Date());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setPath(((ServletWebRequest) request).getRequest().getServletPath());

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        fieldErrors.forEach(
                fieldError -> {
                    error.addError(fieldError.getDefaultMessage());
                }
        );
        LOGGER.error(ex.getMessage(),ex);
        return new ResponseEntity<>(error,headers,status);
    }

}
