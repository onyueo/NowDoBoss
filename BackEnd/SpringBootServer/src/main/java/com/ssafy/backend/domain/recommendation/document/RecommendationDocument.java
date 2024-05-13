package com.ssafy.backend.domain.recommendation.document;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "recommendation")
@Data
@CompoundIndex(def = "{'userId': 1, 'commercialCode': 1}", unique = true)
public class RecommendationDocument {
    @Id
    private Long userId;
    private String commercialCode;

    public RecommendationDocument(Long id, String commercialCode) {
        this.userId = id;
        this.commercialCode = commercialCode;
    }
}