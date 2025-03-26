package com.xiaoxingan.services;

import com.xiaoxingan.exceptions.companies.CompanyNotFoundException;
import com.xiaoxingan.exceptions.companies.CompanyUpdateFailureException;
import com.xiaoxingan.models.Company;
import com.xiaoxingan.repositories.CompanyRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@ApplicationScoped
public class CompanyService {
    @Inject
    CompanyRepository companyRepository;

    public List<Company> findAllCompanies() {
        log.debug("Выполняю поиск по всем компаниям в {}", companyRepository.getClass().getName());
        return companyRepository.listAll();
    }

    public Company findCompanyByName(String name) {
        log.debug("Выполняю поиск по имени компании {} в {}", name, companyRepository.getClass().getName());
        return companyRepository.findByName(name);
    }

    @Transactional
    public void addCompany(Company company) {
        try {
            log.debug("Пытаюсь добавить компанию: {} в {}", company.getId(), companyRepository.getClass().getName());
            companyRepository.persist(company);
        } catch (Exception e) {
            log.error("Произошла ошибка в процессе добавления новой компании в {}! " + e.getMessage(), companyRepository.getClass().getName());
            throw new CompanyUpdateFailureException(e.getMessage());
        }
    }

    @Transactional
    public void deleteCompany(Long id) {
        log.debug("Пытаюсь удалить компанию: {} в {}", id, companyRepository.getClass().getName());
        companyRepository.deleteById(id);
    }

    @Transactional
    public void updateCompany(Long id, Company company) {
        log.debug("Пытаюсь обновить компанию: {} в {}", id, companyRepository.getClass().getName());
        Company oldCompany = companyRepository.findById(id);

        if (oldCompany == null) {
            log.warn("Попытка обновить компанию {} вернула null", id);
            throw new CompanyNotFoundException("Компания " + company.getName() + " не найдена.");
        }

        try {
            oldCompany.setName(company.getName());
            oldCompany.setCountry(company.getCountry());
            oldCompany.setDescription(company.getDescription());
            oldCompany.setUpdatedAt(company.getUpdatedAt());

            companyRepository.persist(oldCompany);
        } catch (Exception e) {
            log.error("Попытка обновить компанию {} завершилась неудачно! " + e.getMessage(), id);
            throw new CompanyUpdateFailureException("Произошла ошибка при обновлении компании " + company.getName() + "." + " " + e.getMessage());
        }
    }
}
