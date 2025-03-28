package com.xiaoxingan.repositories;

import com.xiaoxingan.models.Product;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {
    private final EntityManager entityManager;

    public ProductRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

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

    @Transactional
    public Product findByIdWithLock(Long productId) {
        return entityManager.find(Product.class, productId, LockModeType.PESSIMISTIC_WRITE);
    }

}
