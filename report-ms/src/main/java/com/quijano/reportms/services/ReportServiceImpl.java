package com.quijano.reportms.services;

import com.netflix.discovery.EurekaClient;
import com.quijano.reportms.helpers.ReportHelper;
import com.quijano.reportms.models.Company;
import com.quijano.reportms.models.WebSite;
import com.quijano.reportms.repositories.CompaniesFallbackRepository;
import com.quijano.reportms.repositories.CompaniesRepository;
import com.quijano.reportms.streams.ReportPublisher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService{

    private final CompaniesRepository companiesRepository;
    private final ReportHelper reportHelper;
    private final CompaniesFallbackRepository fallbackRepository;
    private final Resilience4JCircuitBreakerFactory circuitBreakerFactory;
    private final ReportPublisher reportPublisher;

    @Override
    public String makeReport(String name) {
       var circuitBreaker = circuitBreakerFactory.create("compamies-circuitBreaker");

       return circuitBreaker.run(
               () -> this.makeReportMain(name),
               throwable -> this.makeReportFallback(name, throwable)
       );
    }

    @Override
    public String saveReport(String report) {
       var format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
       var placeholders = this.reportHelper.getPlaceholdersFromTemplate(report);
       var webSites = Stream.of(placeholders.get(3))
               .map(website -> WebSite.builder().name(website).build())
               .collect(toList());

       var company = Company.builder()
               .name(placeholders.get(0))
               .foundationDate(LocalDate.parse(placeholders.get(1), format))
               .logo(placeholders.get(2))
               .webSites(webSites)
               .build();
       this.reportPublisher.publishReport(report);
       this.companiesRepository.postByName(company);

        return "Saved";
    }

    @Override
    public void deleteReport(String name) {
        this.companiesRepository.deleteByName(name);
        log.info("Deleted company: {}", name);

    }


    private String makeReportMain(String name) {
        return reportHelper.readTemplate(this.companiesRepository.getByName(name).orElseThrow());
    }
    private String makeReportFallback(String name, Throwable error) {
        log.warn(error.getMessage());
        return reportHelper.readTemplate(this.fallbackRepository.getByName(name));
    }
}
