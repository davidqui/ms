package com.quijano.reportlistener.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportDocument extends MongoRepository<ReportDocument, String> {
}
