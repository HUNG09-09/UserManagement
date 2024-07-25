package project.java.qlsv.jobscheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import project.java.qlsv.entity.User;
import project.java.qlsv.repository.UserRepo;
import project.java.qlsv.service.EmailService;

import java.util.Calendar;
import java.util.List;

@Component
@Slf4j
public class JobSchedule {

//    @Scheduled(fixedDelay = 6000)
//    public void hello() {
//        log.info("Hello");
//    }


//    @Autowired
//    UserRepo userRepo;
//
//    @Autowired
//    EmailService emailService;
//
//    @Scheduled(fixedDelay = 60000)
//    public void hello() {
//        log.info("Hello");
//        emailService.testEmail();
//
//        emailService.sendBirthdayEmail("thai20082002@gmail.com","Quang Thai");
//    }
//
//    // Giay - phut - gio - ngay - thang - thu
//    @Scheduled(cron = "0 37 10 * * *")
//    public void morning() {
//
//        // lay danh sach nhung nguoi co ngay sinh nhat hom nay tu User, va cmsn luc 10h37
//        Calendar cal = Calendar.getInstance();
//        int date = cal.get(Calendar.DATE);
//        int month = cal.get(Calendar.MONTH) + 1;
//     List<User> users = userRepo.searchByBirthday(date,month);
//
//        for (User u : users) {
//            emailService.sendBirthdayEmail(u.getEmail(), u.getName());
//            log.info("Happy Birthday" + u.getName());
//        }
//
//        // test thu 10h37 log ra good mn
//        log.info("Good Morning");
//    }
}