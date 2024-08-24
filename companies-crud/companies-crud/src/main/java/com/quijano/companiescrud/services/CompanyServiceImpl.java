package com.quijano.companiescrud.services;
import com.quijano.companiescrud.entities.Category;
import com.quijano.companiescrud.entities.Company;
import com.quijano.companiescrud.repositories.CompanyRepository;

import io.micrometer.tracing.Tracer;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService{

    private final CompanyRepository companyRepository;
    private final Tracer tracer;
    /**
     * @param company
     * @return
     */
    @Override
    public Company create(Company company) {
        company.getWebSites().forEach(webSite -> {
            if (Objects.isNull(webSite.getCategory())) {
                webSite.setCategory(Category.NONE);
            }        });
        return this.companyRepository.save(company);
    }

    /**
     * @param name
     * @return
     */
    @Override
    public Company readByName(String name) {
        var spam = tracer.nextSpan().name("readByName");
        try(Tracer.SpanInScope spanInScope = this.tracer.withSpan(spam.start())){
            log.info("Betting company from DB");
        }finally {
            spam.end();
        }
        return this.companyRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("Company not found"));
    }


    /**
     * @param company
     * @return
     */
    @Override
    public Company update(Company company, String name) {
        var companyToUpdate = this.companyRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("Company not found"));
        companyToUpdate.setLogo(company.getLogo());
        companyToUpdate.setFoundationDate(company.getFoundationDate());
        companyToUpdate.setFounder(company.getFounder());
        return this.companyRepository.save(companyToUpdate);
    }

    /**
     * @param name
     */
    @Override
    public void delete(String name) {
        var companyToUpdate = this.companyRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("Company not found"));
        this.companyRepository.delete(companyToUpdate);

    }
}
