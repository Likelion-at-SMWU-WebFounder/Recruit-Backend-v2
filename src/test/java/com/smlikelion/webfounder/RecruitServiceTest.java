package com.smlikelion.webfounder;

import com.smlikelion.webfounder.Recruit.Dto.Request.AnswerListRequest;
import com.smlikelion.webfounder.Recruit.Dto.Request.RecruitmentRequest;
import com.smlikelion.webfounder.Recruit.Dto.Request.StudentInfoRequest;
import com.smlikelion.webfounder.Recruit.Service.GoogleDocsService;
import com.smlikelion.webfounder.Recruit.Service.RecruitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.testng.Assert.*;

public class RecruitServiceTest {

    @Mock
    private GoogleDocsService googleDocsService;

    @InjectMocks
    private RecruitService recruitService;

    private final String testDocumentId = "1aBcDeFgHiJkLmNoPqRsTuVwXyZ0123456789AbCdEfGhIjKlMnOpQrStUvWxYz";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadToExistingGoogleDoc() throws Exception {
        // Mocking GoogleDocsService behavior
        doNothing().when(googleDocsService).appendTextToDocument(anyString(), anyString());

        // Mock recruitment request object
        RecruitmentRequest request = new RecruitmentRequest();
        request.setStudentInfo(new StudentInfoRequest());
        request.getStudentInfo().setStudentId("2024001");
        request.getStudentInfo().setName("홍길동");
        request.getStudentInfo().setEmail("hong@example.com");

        AnswerListRequest answerList = new AnswerListRequest();
        answerList.setA1("First answer");
        answerList.setA2("Second answer");
        answerList.setA3("Third answer");
        answerList.setA4("Fourth answer");
        answerList.setA5("Fifth answer");
        answerList.setA6("Sixth answer");
        answerList.setA7("Seventh answer");
        request.setAnswerList(answerList);

        // Verify no exceptions are thrown and the method works correctly
        assertDoesNotThrow(() -> recruitService.uploadToGoogleDocs(testDocumentId, request));

        // Verify that the GoogleDocsService method was called once with expected arguments
        verify(googleDocsService, times(1)).appendTextToDocument(eq(testDocumentId), anyString());
    }

    @Test
    void testUploadToGoogleDocsNullRequest() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> recruitService.uploadToGoogleDocs(testDocumentId, null));

        assertEquals("RecruitmentRequest cannot be null", exception.getMessage());
    }


    @Test
    void testUploadToGoogleDocsIOExceptionHandling() throws IOException {
        // Mock을 사용하여 IOException을 던지도록 설정
        doThrow(new IOException("Google Docs API failure"))
                .when(googleDocsService).appendTextToDocument(anyString(), anyString());

        // RecruitmentRequest 객체 초기화
        RecruitmentRequest request = new RecruitmentRequest();

        // 필수 필드인 StudentInfoRequest 설정
        StudentInfoRequest studentInfo = new StudentInfoRequest();
        studentInfo.setStudentId("2024001");
        studentInfo.setName("John Doe");
        studentInfo.setEmail("johndoe@example.com");
        request.setStudentInfo(studentInfo);

        // 필수 필드인 AnswerListRequest 설정
        AnswerListRequest answerList = new AnswerListRequest();
        answerList.setA1("Answer 1");
        answerList.setA2("Answer 2");
        answerList.setA3("Answer 3");
        answerList.setA4("Answer 4");
        answerList.setA5("Answer 5");
        answerList.setA6("Answer 6");
        answerList.setA7("Answer 7");
        request.setAnswerList(answerList);

        // 테스트 실행 및 예외 처리 확인
        Exception exception = assertThrows(RuntimeException.class,
                () -> recruitService.uploadToGoogleDocs(testDocumentId, request));

        // 예외 메시지를 출력하여 디버깅
        System.out.println("Exception message: " + exception.getMessage());

        // 예외 메시지가 예상대로 포함되어 있는지 검증
        assertTrue(exception.getMessage().contains("Failed to upload to Google Docs"));

        // Mock이 정확히 한 번 호출되었는지 확인
        verify(googleDocsService, times(1)).appendTextToDocument(eq(testDocumentId), anyString());
    }
}
