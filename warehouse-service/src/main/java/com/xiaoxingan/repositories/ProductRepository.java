package com.xiaoxingan.repositories;

import com.xiaoxingan.models.Product;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {
    public Product findByName(String name) {
        return find("name", name).firstResult();
    }

    public List<Product> findByCompanyName(String companyName) {
        return find("company.name", companyName).list();
    }
}
