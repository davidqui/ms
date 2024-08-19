package com.quijano.companiescrud.repositories;

import com.quijano.companiescrud.entities.WebSite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebSiteRepository extends JpaRepository<WebSite, Long> {
}
