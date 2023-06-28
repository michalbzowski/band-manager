package pl.bzowski.bandmanager.data.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.bzowski.bandmanager.data.entity.CompanyBrand;

public interface CompanyBrandRepository
        extends
            JpaRepository<CompanyBrand, Long>,
            JpaSpecificationExecutor<CompanyBrand> {

}
