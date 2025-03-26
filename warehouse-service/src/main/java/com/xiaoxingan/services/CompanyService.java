package com.xiaoxingan.services;

import com.xiaoxingan.exceptions.companies.CompanyNotFoundException;
import com.xiaoxingan.exceptions.companies.CompanyUpdateFailureException;
import com.xiaoxingan.models.Company;
import com.xiaoxingan.repositories.CompanyRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class CompanyService {
    @Inject
    CompanyRepository companyRepository;

    public List<Company> findAllCompanies() {
        return companyRepository.listAll();
    }

    public Company findCompanyByName(String name) {
        return companyRepository.findByName(name);
    }

    @Transactional
    public void addCompany(Company company) {
        try {
            companyRepository.persist(company);
        } catch (Exception e) {
            throw new CompanyUpdateFailureException(e.getMessage());
        }
    }

    @Transactional
    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }

    @Transactional
    public void updateCompany(Long id, Company company) {
        Company oldCompany = companyRepository.findById(id);

        if (oldCompany == null) {
            throw new CompanyNotFoundException("Компания " + company.getName() + " не найдена.");
        }

        try {
            oldCompany.setName(company.getName());
            oldCompany.setCountry(company.getCountry());
            oldCompany.setDescription(company.getDescription());
            oldCompany.setUpdatedAt(company.getUpdatedAt());

            companyRepository.persist(oldCompany);
        } catch (Exception e) {
            throw new CompanyUpdateFailureException("Произошла ошибка при обновлении компании " + company.getName() + "." + " " + e.getMessage());
        }
    }
}
