package com.xiaoxingan.repositories;

import com.xiaoxingan.models.Company;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CompanyRepository implements PanacheRepository<Company> {
    public Company findByName(String name) {
        return find("name", name).firstResult();
    }
}
