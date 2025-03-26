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

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@ApplicationScoped
public class ProductService {
    @Inject
    ProductRepository productRepository;
    @Inject
    CompanyService companyService;

    public List<Product> findAllProducts() {
        return productRepository.findAllProducts();
    }

    public Product findProductByName(String name) {
        return productRepository.findProductByName(name);
    }

    public List<Product> findAllCompanyProducts(String name) {
        return productRepository.findAllCompanyProducts(name);
    }

    public Product findProductById(Long id) {
        return productRepository.findProductById(id);
    }

    @Transactional
    public void addProduct(ProductDTO productDTO) {
        Company company = companyService.findCompanyByName(productDTO.getCompany());
        if (company == null) {
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
            productRepository.persist(newProduct);
        } catch (Exception e) {
            throw new ProductUpdateFailureException(e.getMessage());
        }
    }

    @Transactional
    public void addProducts(List<ProductDTO> productDTO) {
        boolean sameCompanies = productDTO.stream()
                .map(ProductDTO::getCompany)
                .distinct()
                .count() == 1;

        if (sameCompanies) {
            LinkedList<Product> products = new LinkedList<>();
            Company company = companyService.findCompanyByName(productDTO.getFirst().getCompany());
            if (company == null) {
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
                productRepository.persist(products);
            } catch (Exception e) {
                throw new ProductUpdateFailureException("Произошла ошибка при обновлении товаров." + " " + e.getMessage());
            }
        } else {
            throw new ProductsHaveDifferentCompanyNameException("Произошла ошибка при обновлении товаров. Товары имеют разные наименования компаний.");
        }
    }

    @Transactional
    public void updateProduct(Long id, ProductDTO productDTO) {
        Product oldProduct = productRepository.findById(id);

        if (oldProduct == null) {
            throw new ProductNotFoundException("Товар " + id + " не найден.");
        }

        try {
            oldProduct.setName(productDTO.getName());
            oldProduct.setPrice(productDTO.getPrice());
            oldProduct.setDescription(productDTO.getDescription());
            oldProduct.setQuantity(productDTO.getQuantity());
            oldProduct.setUpdatedAt(LocalDateTime.now());

            productRepository.persist(oldProduct);
        } catch (Exception e) {
            throw new ProductUpdateFailureException("Произошла ошибка при обновлении товара " + oldProduct.getName() + "." + " " + e.getMessage());
        }
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
