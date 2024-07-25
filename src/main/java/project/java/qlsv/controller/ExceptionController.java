package project.java.qlsv.controller;

import jakarta.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.java.qlsv.dto.ResponseDTO;

import java.nio.file.AccessDeniedException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler({NoResultException.class})
    public ResponseDTO<String> notFound(NoResultException e) {
        log.info("INFO", e);
        return ResponseDTO.<String>builder().status(404).msg("No data").build();
    }

    @ExceptionHandler({BindException.class})
    public ResponseDTO<String> badRequest(BindException e) {
        log.info("Bad Request");
        List<ObjectError> errors = e.getBindingResult().getAllErrors();

        String msg = "";
        for (ObjectError err : errors) {
            FieldError fieldError = (FieldError) err;
            msg += fieldError.getField() + ":" + err.getDefaultMessage() + ";";
        }
        return ResponseDTO.<String>builder().status(400).msg(msg).build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ResponseDTO<String> unauthorized(Exception e) {
        log.info("INFO", e);
        return ResponseDTO.<String>builder().status(401).msg(e.getMessage()).build();
    }


    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public ResponseDTO<String> accessDeny(Exception e) {
        log.info("INFO", e);
        return ResponseDTO.<String>builder().status(403).msg("Access Deny").build();
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ResponseDTO<String> duplicate(Exception e) {
        log.info("INFO", e);
        return ResponseDTO.<String>builder().status(409).msg("Duplicated Data").build();
    }



}
