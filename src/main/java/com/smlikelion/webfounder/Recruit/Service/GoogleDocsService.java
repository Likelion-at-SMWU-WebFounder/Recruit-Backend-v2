package com.smlikelion.webfounder.Recruit.Service;


import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.DocsScopes;
import com.google.api.services.docs.v1.model.*;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleDocsService {
    private final Docs docsService;

    public GoogleDocsService() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                        new ClassPathResource("credentials.json").getInputStream())
                .createScoped(Collections.singleton(DocsScopes.DOCUMENTS));

        docsService = new Docs.Builder(
                new com.google.api.client.http.javanet.NetHttpTransport(),
                com.google.api.client.json.jackson2.JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName("Recruitment System")
                .build();
    }

    public Docs getDocsService() {
        return docsService;
    }

    public void appendTextToDocument(String documentId, String content) throws IOException {
        List<Request> requests = List.of(
                new Request().setInsertText(
                        new InsertTextRequest()
                                .setText(content + "\n")
                                .setEndOfSegmentLocation(new EndOfSegmentLocation())
                )
        );

        BatchUpdateDocumentRequest body = new BatchUpdateDocumentRequest().setRequests(requests);
        docsService.documents().batchUpdate(documentId, body).execute();
    }
}
