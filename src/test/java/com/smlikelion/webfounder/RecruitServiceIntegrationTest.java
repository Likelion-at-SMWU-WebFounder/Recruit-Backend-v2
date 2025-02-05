package com.smlikelion.webfounder;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.DocsScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.smlikelion.webfounder.Recruit.Dto.Request.AnswerListRequest;
import com.smlikelion.webfounder.Recruit.Dto.Request.RecruitmentRequest;
import com.smlikelion.webfounder.Recruit.Dto.Request.StudentInfoRequest;
import com.smlikelion.webfounder.Recruit.Service.RecruitService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class RecruitServiceIntegrationTest {

    @Autowired
    private RecruitService recruitService;

    @BeforeAll
    static void setupGoogleCredentials() throws IOException {
        // 환경 변수 설정 (추천)
        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", "src/main/resources/credentials.json");

        // 파일 직접 로드 방식 (대안)
        File credentialsPath = new File("src/main/resources/credentials.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsPath))
                .createScoped(Collections.singleton(DocsScopes.DOCUMENTS));
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

        // GoogleDocsService가 필요하다면 설정
        Docs docsService = new Docs.Builder(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                requestInitializer
        ).setApplicationName("Recruitment System Test").build();
    }

    @Test
    public void testUploadToGoogleDocsRealAPI() {
        RecruitmentRequest request = new RecruitmentRequest();
        StudentInfoRequest studentInfo = new StudentInfoRequest();
        studentInfo.setStudentId("2024001");
        studentInfo.setName("Test User");
        studentInfo.setEmail("testuser@example.com");
        request.setStudentInfo(studentInfo);

        AnswerListRequest answerList = new AnswerListRequest();
        answerList.setA1("Integration Answer 1");
        answerList.setA2("Integration Answer 2");
        request.setAnswerList(answerList);

        String documentId = "1aMe9deXsLgNkfW4I1HpMED676sDi23h9dUkC2iZYYtk";

        assertDoesNotThrow(() -> recruitService.uploadToGoogleDocs(documentId, request));
        System.out.println("Google Docs에 정상적으로 업로드됨: " + documentId);
    }
}
