package pl.bzowski.bandmanager.data.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.bzowski.bandmanager.data.entity.UniformPart;

@Service
public class UniformPartService {

    private final UniformPartRepository repository;

    public UniformPartService(UniformPartRepository repository) {
        this.repository = repository;
    }

    public Optional<UniformPart> get(Long id) {
        return repository.findById(id);
    }

    public UniformPart update(UniformPart entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<UniformPart> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<UniformPart> list(Pageable pageable, Specification<UniformPart> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
