package com.xiaoxingan.repositories;

import com.xiaoxingan.models.Product;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {
    public List<Product> findAllProducts() {
        return find("SELECT p FROM Product p JOIN FETCH p.company").list();
    }

    public Product findProductByName(String name) {
        return find("SELECT p FROM Product p JOIN FETCH p.company WHERE p.name = ?1", name)
                .firstResult();
    }

    public List<Product> findAllCompanyProducts(String companyName) {
        return find("SELECT p FROM Product p JOIN FETCH p.company c WHERE c.name = ?1", companyName)
                .list();
    }

    public Product findProductById(Long id) {
        return find("SELECT p FROM Product p JOIN FETCH p.company WHERE p.id = ?1", id)
                .firstResult();
    }
}
