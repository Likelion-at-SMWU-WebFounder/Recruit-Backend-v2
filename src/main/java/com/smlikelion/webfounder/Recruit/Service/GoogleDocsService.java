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

        List<Request> requests = new ArrayList<>();

        // ✅ 문서에 제목 추가 (굵게, 큰 글씨)
        requests.add(insertText("[지원자 정보]", true));
        requests.add(insertStyledText("이름: " + request.getStudentInfo().getName(), false));
        requests.add(insertStyledText("학번: " + request.getStudentInfo().getStudentId(), false));
        requests.add(insertStyledText("전공: " + request.getStudentInfo().getMajor(), false));
        requests.add(insertStyledText("이메일: " + request.getStudentInfo().getEmail(), false));
        requests.add(insertStyledText("전화번호: " + request.getStudentInfo().getPhoneNumber(), false));
        requests.add(insertStyledText("트랙: " + request.getStudentInfo().getTrack(), false));
        requests.add(insertStyledText("포트폴리오: " + request.getStudentInfo().getPortfolio(), false));
        requests.add(insertStyledText("졸업 예정 연도: " + request.getStudentInfo().getGraduatedYear(), false));
        requests.add(insertStyledText("수료 학기: " + request.getStudentInfo().getCompletedSem() + "학기", false));

        // ✅ 추가된 부분: 재/휴학 여부, 프로그래머스 수강 여부
        requests.add(insertStyledText("재/휴학 여부: " + request.getStudentInfo().getSchoolStatus(), false));
        requests.add(insertStyledText("프로그래머스 수강 여부: " + request.getStudentInfo().getProgrammers(), false));

        // ✅ 개인정보 및 행사 참여 동의 여부 추가
        requests.add(insertStyledText("개인정보 동의 여부: " + (request.getStudentInfo().isAgreeToTerms() ? "동의" : "비동의"), false));
        requests.add(insertStyledText("행사 필수참여 동의 여부: " + (request.getStudentInfo().isAgreeToEventParticipation() ? "동의" : "비동의"), false));

        // ✅ 문항 & 답변 추가
        requests.add(insertText("\n[지원서 문항 및 답변]", true));
        request.getAnswerListRequest().toAnswerListMap().forEach((question, answer) -> {
            requests.add(insertStyledText(question + ": " + answer, false));
        });

        // ✅ 추가된 부분: 면접 시간을 날짜 순으로 정렬하여 추가
        if (request.getInterview_time() != null && !request.getInterview_time().isEmpty()) {
            requests.add(insertText("\n[면접 가능 시간]", true));

            // 날짜 정렬
            List<String> sortedDates = new ArrayList<>(request.getInterview_time().keySet());
            Collections.sort(sortedDates);

            for (String date : sortedDates) {
                String time = request.getInterview_time().get(date);
                requests.add(insertStyledText(date + ": " + time, false));
            }
        }

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
     * 📌 Google Docs에 일반 텍스트 추가 (제목 여부 선택)
     */
    private Request insertText(String content, boolean isTitle) {
        return new Request().setInsertText(
                new InsertTextRequest()
                        .setText(content + "\n")
                        .setEndOfSegmentLocation(new EndOfSegmentLocation())
        );
    }

    /**
     * 📌 Google Docs에 스타일이 적용된 텍스트 추가
     */
    private Request insertStyledText(String content, boolean isTitle) {
        TextStyle textStyle = new TextStyle()
                .setFontSize(new Dimension().setMagnitude(isTitle ? 16.0 : 12.0)) // 제목은 크게
                .setBold(isTitle); // 제목이면 굵게

        return new Request().setInsertText(
                new InsertTextRequest()
                        .setText(content + "\n")
                        .setEndOfSegmentLocation(new EndOfSegmentLocation())
        );
    }
}
