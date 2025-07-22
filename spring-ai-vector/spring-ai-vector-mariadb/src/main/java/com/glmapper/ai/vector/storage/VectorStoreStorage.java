package com.glmapper.ai.vector.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @ClassName VectorStoreStorage
 * @Description TODO
 * @Author masen.27
 * @Date 2025/7/22 13:24
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class VectorStoreStorage {

    private final VectorStore vectorStore;


    public void delete(Set<String> ids) {
        vectorStore.delete(new ArrayList<>(ids));
    }

    public void store(List<Document> documents) {
        if (documents == null || documents.isEmpty()) {
            return;
        }
        vectorStore.add(documents);
    }

    public List<Document> search(String query) {
        return vectorStore.similaritySearch(SearchRequest.builder().query(query).topK(5).build());
    }
}
