package com.xiaoxingan.services;

import com.xiaoxingan.dto.ProductDTO;
import com.xiaoxingan.exceptions.companies.CompanyNotFoundException;
import com.xiaoxingan.exceptions.products.ProductNotFoundException;
import com.xiaoxingan.exceptions.products.ProductUpdateFailureException;
import com.xiaoxingan.exceptions.products.ProductsHaveDifferentCompanyNameException;
import com.xiaoxingan.models.Company;
import com.xiaoxingan.models.Product;
import com.xiaoxingan.repositories.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@ApplicationScoped
public class ProductService {
    @Inject
    ProductRepository productRepository;
    @Inject
    CompanyService companyService;

    public List<Product> findAllProducts() {
        log.debug("Пытаюсь найти все товары в {}", productRepository.getClass().getName());
        return productRepository.findAllProducts();
    }

    public Product findProductByName(String name) {
        log.debug("Пытаюсь найти товар по имени {} в {}", name, productRepository.getClass().getName());
        return productRepository.findProductByName(name);
    }

    public List<Product> findAllCompanyProducts(String name) {
        log.debug("Пытаюсь найти все товары компании в {}", productRepository.getClass().getName());
        return productRepository.findAllCompanyProducts(name);
    }

    public Product findProductById(Long id) {
        log.debug("Пытаюсь найти товар по идентификатору {} в {}", id, productRepository.getClass().getName());
        return productRepository.findProductById(id);
    }

    @Transactional
    public void addProduct(ProductDTO productDTO) {
        Company company = companyService.findCompanyByName(productDTO.getCompany());
        if (company == null) {
            log.warn("Компания не найдена в процессе добавления товара для {} в {}", productDTO.getCompany(), productRepository.getClass().getName());
            throw new CompanyNotFoundException("Компания " + productDTO.getCompany() + " не найдена.");
        }

        Product newProduct = new Product();
        newProduct.setCompany(company);
        newProduct.setName(productDTO.getName());
        newProduct.setPrice(productDTO.getPrice());
        newProduct.setDescription(productDTO.getDescription());
        newProduct.setQuantity(productDTO.getQuantity());
        newProduct.setCreatedAt(LocalDateTime.now());
        newProduct.setUpdatedAt(LocalDateTime.now());

        try {
            log.debug("Пытаюсь сохранить новый товар для компании {}", newProduct.getCompany());
            productRepository.persist(newProduct);
        } catch (Exception e) {
            log.error("Произошла ошибка в процессе сохранения нового товара для компании {}. " + e.getMessage(), newProduct.getCompany());
            throw new ProductUpdateFailureException(e.getMessage());
        }
    }

    @Transactional
    public void addProducts(List<ProductDTO> productDTO) {
        log.debug("Проверяю список товаров на соответствие одной компании...");
        boolean sameCompanies = productDTO.stream()
                .map(ProductDTO::getCompany)
                .distinct()
                .count() == 1;

        if (sameCompanies) {
            LinkedList<Product> products = new LinkedList<>();
            Company company = companyService.findCompanyByName(productDTO.getFirst().getCompany());
            if (company == null) {
                log.warn("Компания {} для группы товаров не найдена.", productDTO.getFirst().getCompany());
                throw new CompanyNotFoundException("Компания " + productDTO.getFirst().getCompany() + " не найдена");
            }

            for (ProductDTO dto : productDTO) {
                Product newProduct = new Product();
                newProduct.setCompany(company);
                newProduct.setName(dto.getName());
                newProduct.setPrice(dto.getPrice());
                newProduct.setDescription(dto.getDescription());
                newProduct.setQuantity(dto.getQuantity());
                newProduct.setCreatedAt(LocalDateTime.now());
                newProduct.setUpdatedAt(LocalDateTime.now());

                products.add(newProduct);
            }

            try {
                log.debug("Добавляю спискок товаров используя {}...", productRepository.getClass().getName());
                productRepository.persist(products);
            } catch (Exception e) {
                log.error("Произошла ошибка при добавлении группы товаров в batch-запросе. " + e.getMessage());
                throw new ProductUpdateFailureException("Произошла ошибка при добавлении товаров." + " " + e.getMessage());
            }
        } else {
            log.warn("Товары имели разные названия компаний.");
            throw new ProductsHaveDifferentCompanyNameException("Произошла ошибка при добавлении товаров. Товары имеют разные наименования компаний.");
        }
    }

    @Transactional
    public void updateProduct(Long id, ProductDTO productDTO) {
        log.debug("Пытаюсь обновить товар по идентификатору {} используя {}", id, productRepository.getClass().getName());
        Product oldProduct = productRepository.findById(id);

        if (oldProduct == null) {
            log.warn("Товар {} для обновления не был найден!", id);
            throw new ProductNotFoundException("Товар " + id + " не найден.");
        }

        try {
            oldProduct.setName(productDTO.getName());
            oldProduct.setPrice(productDTO.getPrice());
            oldProduct.setDescription(productDTO.getDescription());
            oldProduct.setQuantity(productDTO.getQuantity());
            oldProduct.setUpdatedAt(LocalDateTime.now());

            log.debug("Обновляю товар {}...", id);
            productRepository.persist(oldProduct);
        } catch (Exception e) {
            throw new ProductUpdateFailureException("Произошла ошибка при обновлении товара " + oldProduct.getName() + "." + " " + e.getMessage());
        }
    }

    @Transactional
    public void deleteProduct(Long id) {
        log.debug("Пытаюсь удалить товар по идентификатору {}", id);
        productRepository.deleteById(id);
    }
}
