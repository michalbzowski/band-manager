package pl.bzowski.bandmanager.data.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.bzowski.bandmanager.data.entity.Musician;

@Service
public class MusicianService {

    private final MusicianRepository repository;

    public MusicianService(MusicianRepository repository) {
        this.repository = repository;
    }

    public Optional<Musician> get(Long id) {
        return repository.findById(id);
    }

    public Musician update(Musician entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Musician> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Musician> list(Pageable pageable, Specification<Musician> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
