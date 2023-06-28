package pl.bzowski.bandmanager.data.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.bzowski.bandmanager.data.entity.Instrument;

@Service
public class InstrumentService {

    private final InstrumentRepository repository;

    public InstrumentService(InstrumentRepository repository) {
        this.repository = repository;
    }

    public Optional<Instrument> get(Long id) {
        return repository.findById(id);
    }

    public Instrument update(Instrument entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Instrument> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Instrument> list(Pageable pageable, Specification<Instrument> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
