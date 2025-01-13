package com.smlikelion.webfounder.Recruit.Dto.Request;
import com.smlikelion.webfounder.Recruit.Entity.Joiner;
import com.smlikelion.webfounder.Recruit.Entity.Programmers;
import com.smlikelion.webfounder.Recruit.Entity.SchoolStatus;
import com.smlikelion.webfounder.Recruit.Entity.Track;
import lombok.Getter;
 import lombok.Setter;
 import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
public class StudentInfoRequest {
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "트랙을 입력해주세요.")
    private String track; //pm , fe, be

    @NotBlank(message = "전화번호를 입력해주세요.")
    private String phoneNumber;

    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotNull(message = "학번을 입력해주세요.")
    private String studentId;

    @NotBlank(message = "전공을 입력해주세요.")
    private String major;

    @NotNull(message = "수료학기를 선택해주세요.")
    private int completedSem;

    @NotNull(message = "포트폴리오를 작성해주세요.")
    private String Portfolio;

    @NotNull(message = "재/휴학 여부를 선택해주세요.")
    private String schoolStatus; // ENROLLED, ON_LEAVE

    @NotNull(message = "프로그래머스 수강 여부를 선택해주세요.")
    private String programmers; //NOT_ENROLLED , ENROLLED

    private String programmersImg;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "졸업년도를 입력해주세요.")
    private String graduatedYear;

    @NotNull(message = "개인정보 동의 여부를 선택해주세요.")
    private boolean agreeToTerms;

    @NotNull(message = "행사 필수참여 동의 여부를 선택해주세요.")
    private boolean agreeToEventParticipation;


    public Joiner toJoiner() {
        Joiner joiner = new Joiner();
        joiner.setName(this.getName());
        joiner.setTrack(convertToTrackEnum(this.getTrack()));  // Track 열거형으로 변환
        joiner.setPhoneNum(this.getPhoneNumber());
        joiner.setEmail(this.getEmail());
        joiner.setStudentId(String.valueOf(this.getStudentId()));
        joiner.setMajor(this.getMajor());
        joiner.setCompletedSem(this.getCompletedSem());
        joiner.setPortfolio(this.getPortfolio());
        joiner.setSchoolStatus(convertToSchoolStatusEnum(this.getSchoolStatus())); // SchoolStatus 열거형으로 변환
        joiner.setProgrammers(convertToProgrammersEnum(this.getProgrammers())); // Programmers 열거형으로 변환
        joiner.setPassword(this.getPassword());
        joiner.setProgrammersImageUrl(this.getProgrammersImg());
        joiner.setGraduatedYear(this.getGraduatedYear());
        joiner.setAgreeToTerms(this.isAgreeToTerms()); // 개인정보 동의 여부 설정
        joiner.setAgreeToEventParticipation(this.isAgreeToEventParticipation()); // 행사 필수참여 동의 여부 설정
        return joiner;
    }

    private Track convertToTrackEnum(String track) {
        try {
            System.out.println("Input track value: " + track); //  로그
            return Track.valueOf(track.toUpperCase());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return Track.PLANDESIGN; //
        }
    }
    private SchoolStatus convertToSchoolStatusEnum(String schoolStatus) {
        try {
            return SchoolStatus.valueOf(schoolStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            return SchoolStatus.ENROLLED; // 예를 들어, DEFAULT_SCHOOL_STATUS를 사용하거나 다른 기본값으로 변경
        }
    }
    private Programmers convertToProgrammersEnum(String programmers) {
        try {
            return Programmers.valueOf(programmers.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Programmers.NOT_ENROLLED; // 예를 들어, DEFAULT_PROGRAMMERS를 사용하거나 다른 기본값으로 변경
        }
    }


}

