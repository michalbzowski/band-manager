package pl.bzowski.bandmanager.data.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.bzowski.bandmanager.data.entity.CompanyBrand;

@Service
public class CompanyBrandService {

    private final CompanyBrandRepository repository;

    public CompanyBrandService(CompanyBrandRepository repository) {
        this.repository = repository;
    }

    public Optional<CompanyBrand> get(Long id) {
        return repository.findById(id);
    }

    public CompanyBrand update(CompanyBrand entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<CompanyBrand> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<CompanyBrand> list(Pageable pageable, Specification<CompanyBrand> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
