package com.quijano.reportlistener.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportDocument {

    @Id
    private String Id;
    private String content;
}
