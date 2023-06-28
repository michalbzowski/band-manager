package pl.bzowski.bandmanager.data.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.bzowski.bandmanager.data.entity.InstrumentType;

@Service
public class InstrumentTypeService {

    private final InstrumentTypeRepository repository;

    public InstrumentTypeService(InstrumentTypeRepository repository) {
        this.repository = repository;
    }

    public Optional<InstrumentType> get(Long id) {
        return repository.findById(id);
    }

    public InstrumentType update(InstrumentType entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<InstrumentType> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<InstrumentType> list(Pageable pageable, Specification<InstrumentType> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
