package com.smlikelion.webfounder.Recruit.Service;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Lists;
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.DocsScopes;
import com.google.api.services.docs.v1.model.*;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.smlikelion.webfounder.Recruit.Dto.Request.RecruitmentRequest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleDocsService {
    private final Docs docsService;

    public GoogleDocsService() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                        new ClassPathResource("credentials.json").getInputStream())
                .createScoped(Collections.singleton(DocsScopes.DOCUMENTS));

        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

        this.docsService = new Docs.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), requestInitializer)
                .setApplicationName("Recruitment System")
                .build();
    }

    public void uploadRecruitmentToGoogleDocs(String documentId, RecruitmentRequest request) throws IOException {
        List<Request> requests = new ArrayList<>();

        // 🔹 1. 지원자 기본 정보 추가 (모든 필드 포함)
        requests.add(insertText("지원자 정보", true));
        requests.add(insertText("이름: " + request.getStudentInfo().getName(), false));
        requests.add(insertText("학번: " + request.getStudentInfo().getStudentId(), false));
        requests.add(insertText("전공: " + request.getStudentInfo().getMajor(), false));
        requests.add(insertText("이메일: " + request.getStudentInfo().getEmail(), false));
        requests.add(insertText("전화번호: " + request.getStudentInfo().getPhoneNumber(), false));
        requests.add(insertText("재학 상태: " + request.getStudentInfo().getSchoolStatus(), false));
        requests.add(insertText("졸업 연도: " + request.getStudentInfo().getGraduatedYear(), false));
        requests.add(insertText("이수 학기: " + request.getStudentInfo().getCompletedSem(), false));
        requests.add(insertText("트랙: " + request.getStudentInfo().getTrack(), false));
        requests.add(insertText("Programmers 인증 상태: " + request.getStudentInfo().getProgrammers(), false));
        requests.add(insertText("Programmers 인증 이미지: " + request.getStudentInfo().getProgrammersImg(), false));
        requests.add(insertText("포트폴리오: " + request.getStudentInfo().getPortfolio(), false));
        requests.add(insertText("이용 약관 동의 여부: " + (request.getStudentInfo().isAgreeToTerms() ? "동의함" : "동의하지 않음"), false));
        requests.add(insertText("이벤트 참여 동의 여부: " + (request.getStudentInfo().isAgreeToEventParticipation() ? "동의함" : "동의하지 않음"), false));

        // 줄바꿈 추가
        requests.add(insertText("", false));

        // 🔹 2. 자소서 문항 및 답변 추가
        List<String> answers = request.getAnswerListRequest().toAnswerList();
        for (int i = 0; i < answers.size(); i++) {
            requests.add(insertText("문항 " + (i + 1) + ": " + answers.get(i), false));
        }

        // 🔹 3. Google Docs 업데이트
        BatchUpdateDocumentRequest body = new BatchUpdateDocumentRequest().setRequests(requests);
        docsService.documents().batchUpdate(documentId, body).execute();
    }

    /**
     * 📌 Google Docs에 텍스트 추가하는 메서드
     */
    private Request insertText(String content, boolean isTitle) {
        TextStyle textStyle = new TextStyle();
        if (isTitle) {
            textStyle.setBold(true).setFontSize(new Dimension().setMagnitude(14.0));
        }

        return new Request().setInsertText(
                new InsertTextRequest()
                        .setText(content + "\n")
                        .setEndOfSegmentLocation(new EndOfSegmentLocation()) // 🔹 문서 끝에 삽입
        );
    }
}