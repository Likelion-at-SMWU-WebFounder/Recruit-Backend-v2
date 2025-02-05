package com.smlikelion.webfounder.Recruit.Service;

import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.DocsScopes;
import com.google.api.services.docs.v1.model.*;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.smlikelion.webfounder.Recruit.Dto.Request.RecruitmentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

@Slf4j
@Service
public class GoogleDocsService {
    private final Docs docsService;
    private final String documentId;

    public GoogleDocsService(@Value("${google.docs.document-id}") String documentId) throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                        new ClassPathResource("credentials.json").getInputStream())
                .createScoped(Collections.singleton(DocsScopes.DOCUMENTS));

        this.docsService = new Docs.Builder(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName("Recruitment System")
                .build();

        this.documentId = documentId;
    }

    /**
     * 📌 지원자 정보를 Google Docs에 업로드
     */
    public void uploadRecruitmentToGoogleDocs(String documentId, RecruitmentRequest request) throws IOException {
        int docLength = getDocumentEndIndex();

        log.info("Google Docs에 서류 업로드 중: 문서 ID={}, 현재 길이={}", documentId, docLength);

        // ✅ 문서에 데이터 추가
        List<Request> requests = new ArrayList<>();
        requests.add(insertText("지원자 정보", true));
        requests.add(insertText("이름: " + request.getStudentInfo().getName(), false));
        requests.add(insertText("학번: " + request.getStudentInfo().getStudentId(), false));
        requests.add(insertText("전공: " + request.getStudentInfo().getMajor(), false));
        requests.add(insertText("이메일: " + request.getStudentInfo().getEmail(), false));
        requests.add(insertText("전화번호: " + request.getStudentInfo().getPhoneNumber(), false));
        requests.add(insertText("트랙: " + request.getStudentInfo().getTrack(), false));
        requests.add(insertText("포트폴리오: " + request.getStudentInfo().getPortfolio(), false));
        requests.add(insertText("졸업 예정 연도: " + request.getStudentInfo().getGraduatedYear(), false));
        requests.add(insertText("프로그래머스 인증: " + request.getStudentInfo().getProgrammersImg(), false));

        // ✅ answerList 추가 (toAnswerListMap() 사용)
        requests.add(insertText("\n[지원서 문항 및 답변]", true));
        request.getAnswerListRequest().toAnswerListMap().forEach((question, answer) -> {
            requests.add(insertText(question + ": " + answer, false));
        });

        // 🔹 Google Docs 업데이트 실행
        BatchUpdateDocumentRequest body = new BatchUpdateDocumentRequest().setRequests(requests);
        docsService.documents().batchUpdate(documentId, body).execute();

        log.info("Google Docs 업로드 완료: {}", documentId);
    }

    /**
     * 📌 문서의 끝 위치(문자 개수) 가져오기
     */
    private int getDocumentEndIndex() throws IOException {
        try {
            Document document = docsService.documents().get(documentId).execute();
            List<StructuralElement> elements = document.getBody().getContent();
            if (elements.isEmpty()) return 1;
            return elements.get(elements.size() - 1).getEndIndex();
        } catch (GoogleJsonResponseException e) {
            log.error("Google Docs 문서를 찾을 수 없습니다. 문서 ID={} 에러 메시지={}", documentId, e.getDetails().getMessage());
            throw new RuntimeException("Google Docs 문서 ID가 존재하지 않습니다: " + documentId, e);
        }
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
                        .setEndOfSegmentLocation(new EndOfSegmentLocation())  // 🔹 문서 끝에 삽입
        );
    }
}
