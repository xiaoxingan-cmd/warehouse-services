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

    public Object[] findProductAndCustomer(Long productId, Long customerId) {
        return getEntityManager()
                .createQuery("SELECT p, c FROM Product p, Customer c WHERE p.id = :productId AND c.id = :customerId", Object[].class)
                .setParameter("productId", productId)
                .setParameter("customerId", customerId)
                .getSingleResult();
    }
}
