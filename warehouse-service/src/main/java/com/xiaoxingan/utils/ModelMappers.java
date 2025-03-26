package com.xiaoxingan.utils;

import com.xiaoxingan.dto.CompanyDTO;
import com.xiaoxingan.dto.ProductDTO;
import com.xiaoxingan.models.Company;
import com.xiaoxingan.models.Product;
import jakarta.enterprise.context.ApplicationScoped;
import org.modelmapper.ModelMapper;

@ApplicationScoped
public class ModelMappers {
    private final ModelMapper modelMapper;

    public ModelMappers() {
        this.modelMapper = new ModelMapper();
    }

    public Company mapToCompany(CompanyDTO companyDTO) {
        return modelMapper.map(companyDTO, Company.class);
    }

    public Product mapToProduct(ProductDTO productDTO) {
        return modelMapper.map(productDTO, Product.class);
    }
}
