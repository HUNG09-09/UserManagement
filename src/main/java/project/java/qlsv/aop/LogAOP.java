package project.java.qlsv.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAOP {

    // ham nay chay chan trươc khi goi ham project.java.qlsv.service.DepartmentService.getById(*))
    @Before("execution(* project.java.qlsv.service.DepartmentService.getById(*))")
    public void getByDepartmentId(JoinPoint joinPoint) {
        int id = (Integer) joinPoint.getArgs()[0];
        log.info("JOIN POINT" + id);
    }
}
