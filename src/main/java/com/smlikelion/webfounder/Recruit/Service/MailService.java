package com.smlikelion.webfounder.Recruit.Service;

import com.smlikelion.webfounder.Recruit.Dto.Request.MailRequestDto;
import com.smlikelion.webfounder.Recruit.Entity.Mail;
import com.smlikelion.webfounder.Recruit.Repository.MailRepository;
import com.smlikelion.webfounder.Recruit.exception.ApplyMailSendException;
import com.smlikelion.webfounder.Recruit.exception.NotFoundEmailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {
    private final MailRepository mailRepository;
    private final JavaMailSender javaMailSender;

    public String mailSubmit(MailRequestDto requestDto) {
        Mail mail=mailRepository.save(
                Mail.builder()
                        .emailAdd(requestDto.getEmailAdd())
                        .build()
        );
        return mail.getEmailAdd();
    }

    public List<String> findAllmail(){
        try{
            List<Mail> mailList=mailRepository.findAll();
            List<String> result=new ArrayList<>();

            for(Mail mail:mailList){
                result.add(mail.getEmailAdd());
            }
            return result;
        }catch (Exception e){
            throw new NotFoundEmailException("이메일 전체 조회 실패");
        }
    }

    public List<String> getEmailsOnly() {
        List<Mail> emailsWithId = mailRepository.findAll(); // 모든 이메일을 가져옴
        return emailsWithId.stream()
                .map(Mail::getEmailAdd) // 이메일만 추출
                .collect(Collectors.toList());
    }

    public String sendMail(){
        // 어떤 메일에, 총 몇개가 발송되었는지 로그 찍어주면 좋을 듯?!
        List<String> mailList=getEmailsOnly();
        SimpleMailMessage message=new SimpleMailMessage();

        message.setSubject("[멋쟁이사자처럼 숙명여대] 13기 모집이 시작되었습니다."); // 메일 제목
        message.setTo(mailList.toArray(new String[mailList.size()]));
        message.setText("멋쟁이사자처럼 숙명여대의 13기 모집이 공식적으로 시작되었음을 알려드리고자 합니다. \n" +
                "\n" +
                "모집에 관심을 가지고 계신 분들께서는 인스타그램 및 공식 홈페이지의 공지사항을 확인하여 자세한 정보를 얻어주시기 바랍니다.\n" +
                "\n" +
                "이번 기회를 통해 멋쟁이사자처럼 숙명여대와 함께할 수 있는 여러분을 기다리고 있습니다.\n" +
                "\n" +
                "감사합니다.\n" +
                "\n" +
                "멋쟁이 사자처럼 숙명여대 운영진 일동 드림"); // 메일 내용
        javaMailSender.send(message);

        return "지원자에게 메일 발송이 완료되었습니다.";
    }

    public void sendApplyStatusMail(String receiver){
        SimpleMailMessage message=new SimpleMailMessage();

        try{
            message.setSubject("[멋쟁이사자처럼 숙명여대] 12기 지원서류 접수가 정상적으로 처리되었습니다."); // 메일 제목
            message.setTo(receiver);
            message.setText("안녕하세요,\n" +
                    "\n" +
                    "저희 멋쟁이사자처럼 숙명여대 12기 지원해 주신 여러분께 이 메일을 보냅니다. \n" +
                    "\n" +
                    "먼저, 지원에 참여해 주셔서 진심으로 감사드립니다. 여러분의 열정과 관심에 깊이 감사드립니다.\n" +
                    "\n" +
                    "지원서를 작성하시며 많은 노력과 시간을 투자해 주셨음을 알고 있습니다. \n" +
                    "\n" +
                    "멋쟁이 사자처럼에서는 모든 지원자분들의 노력을 높이 평가하고 있으며, 각 분의 역량과 가능성에 대해 큰 관심을 가지고 있습니다.\n" +
                    "\n" +
                    "이와 함께, 지원하신 서류가 정상적으로 접수되었음을 안내해드리고자 합니다. \n" +
                    "\n" +
                    "추가적인 질문이나 문의사항이 있으시면 언제든지 연락 주시기 바랍니다. \n" +
                    "\n" +
                    "저희 멋쟁이사자처럼 숙명여대를 향한 여러분의 관심에 다시 한 번 감사드립니다.\n" +
                    "\n" +
                    "이 메일이 여러분에게 좋은 소식이 되기를 바랍니다.\n" +
                    "\n" +
                    "감사합니다.\n" +
                    "\n" +
                    "멋쟁이사자처럼 숙명여대 운영진 일동 드림"); // 메일 내용
            javaMailSender.send(message);
        }
        catch(MailSendException e) {
            log.error("메일 전송 중 오류 발생: {}", e.getMessage());
            throw new ApplyMailSendException("메일 전송 중 오류가 발생했습니다. 이메일이 유효한지 다시 확인해주세요.");
        }

    }
}
