package com.eight.sailingship.service.email;

import com.eight.sailingship.service.redis.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;
    private int authNumber;

    public EmailService(JavaMailSender mailSender, RedisUtil redisUtil) {
        this.mailSender = mailSender;
        this.redisUtil = redisUtil;
    }

    //임의의 6자리 양수를 반환합니다.
    public int makeRandomNumber() {
        Random r = new Random();
        StringBuilder randomNumber = new StringBuilder();
        for(int i = 0; i < 6; i++) {
            randomNumber.append(Integer.toString(r.nextInt(10)));
        }

        this.authNumber = Integer.parseInt(randomNumber.toString());
        return authNumber;
    }


    //mail을 어디서 보내는지, 어디로 보내는지 , 인증 번호를 html 형식으로 어떻게 보내는지 작성합니다.
    public String joinEmail(String email) {
        makeRandomNumber();
        String authNum = String.valueOf(authNumber);
        redisUtil.setDataExpire(email, authNum, 1800000);
        String setFrom = "chickenchobab@gmail.com"; // email-config에 설정한 자신의 이메일 주소를 입력
        String toMail = email;
        String title = "회원 가입 인증 이메일 입니다."; // 이메일 제목
        String content =
                        "돛단배달의 민족에 가입해주셔서 감사합니다." + 	//html 형식으로 작성 !
                        "<br><br>" +
                        "인증 번호는 " + authNumber + "입니다." +
                        "<br>" +
                        "인증번호를 제대로 입력해주세요"; //이메일 내용 삽입
        mailSend(setFrom, toMail, title, content);
        return Integer.toString(authNumber);
    }

    //이메일을 전송합니다.
    public void mailSend(String setFrom, String toMail, String title, String content) {
        //JavaMailSender 객체를 사용하여 MimeMessage 객체를 생성
        MimeMessage message = mailSender.createMimeMessage();

        try {
            // 이메일 메시지와 관련된 설정을 수행
            // true를 전달하여 multipart 형식의 메시지를 지원하고, "utf-8"을 전달하여 문자 인코딩을 설정
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");
            helper.setFrom(setFrom); //이메일의 발신자 주소 설정
            helper.setTo(toMail); //이메일의 수신자 주소 설정
            helper.setSubject(title); //이메일의 제목을 설정
            helper.setText(content,true); //이메일의 내용 설정 두 번째 매개 변수에 true를 설정하여 html 설정.
            mailSender.send(message);
        } catch (MessagingException e) {
            // 이메일 서버에 연결할 수 없거나, 잘못된 이메일 주소를 사용하거나, 인증 오류가 발생하는 등 오류
            // 이러한 경우 MessagingException이 발생
            e.printStackTrace();
        }


    }

    // 검증 로직
    public boolean checkAuthNum(String email,String authNum){
        System.out.println(redisUtil.getData(email));

        if(redisUtil.getData(email) == null){
            return false;
        }
        else return redisUtil.getData(email).equals(authNum);
    }

}