package com.kk.service;

import java.util.Random;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.kk.dao.EmailAuthDAO;
import com.kk.domain.EmailAuthVO;

@Service("mss")
public class MailSendService {
    
    @Autowired
    private EmailAuthDAO emailAuthDAO;
    
    // DB에 이메일이 있는지 확인하는 메소드
    public EmailAuthVO getEmail(EmailAuthVO vo) {
    	return emailAuthDAO.getEmail(vo);
    }
    
    // DB에 넣기
    public int insert(EmailAuthVO vo) {
    	return emailAuthDAO.insert(vo);
    }
    
    // DB 업데이트 하기
    public int update(EmailAuthVO vo) {
    	return emailAuthDAO.update(vo);
    }

    
    /////////////// 이하 비즈니스 로직  - 키 생성 및 메일 전송
    
    @Autowired
    private JavaMailSenderImpl mailSender;
    
    int size;
    
    //인증키 생성
    private String getKey(int size) {
        this.size = size;
        return getAuthCode();
    }

    //인증코드 난수 발생
    private String getAuthCode() {
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        int num = 0;

        while(buffer.length() < size) {
            num = random.nextInt(10);
            buffer.append(num);
        }

        return buffer.toString();
    }

    //인증메일 보내기
    public String sendAuthMail(String email) {
    	
    	String authKey = getKey(12);
    	
    	MimeMessage mail = mailSender.createMimeMessage();
        String mailContent = "<h1>[이메일 인증]</h1><br><p>아래 링크를 클릭하시면 이메일 인증이 완료됩니다.</p>"
                            + "<a href='http://localhost:8180/kkini_kkili/main/signUpConfirm?email=" 
                            + email + "&authKey=" + authKey + "' target='_blenk'>이메일 인증 확인</a>";

        try {
            mail.setSubject("회원가입 이메일 인증 ", "utf-8");
            mail.setText(mailContent, "utf-8", "html");
            mail.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            Address[] addr = {new InternetAddress("kkinikkili@gmail.com")};
            mail.addFrom(addr);
            mailSender.send(mail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        
        return authKey;
    }
    
    
    
}
