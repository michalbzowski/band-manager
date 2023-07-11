package pl.bzowski.bandmanager.data.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.bzowski.bandmanager.data.entity.MusicEvent;

@Service
public class EventService {

    private final EventRepository repository;

    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public Optional<MusicEvent> get(UUID id) {
        return repository.findById(id);
    }

    public MusicEvent update(MusicEvent entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<MusicEvent> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<MusicEvent> list(Pageable pageable, Specification<MusicEvent> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
