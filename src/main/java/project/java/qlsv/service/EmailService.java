package project.java.qlsv.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;

@Service
public class EmailService {
    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    SpringTemplateEngine templateEngine;
//
    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body,true);
            helper.setFrom("syhungg0909@gmail.com");

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

//    public void testEmail() {
//        String to = "thai20082002@gmail.com";
//        String subject = "Dich vu ca do bong da truc tuyen";
//        String body = "<h1> Chao anh Thái</h1>";
//        sendEmail(to, subject, body);
//
//    }


    public void sendBirthdayEmail(String to, String name) {
        String subject = "Happy Birthday <3" + name;

        Context ctx = new Context(); //thymleaf
        ctx.setVariable("name", name);
        String body = templateEngine.process("email/hpbd.html", ctx);
        sendEmail(to, subject, body);
    }
}
