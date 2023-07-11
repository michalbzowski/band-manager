package pl.bzowski.bandmanager.musician.queries;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.bzowski.bandmanager.data.entity.Musician;
import pl.bzowski.bandmanager.musician.events.MusicianCreatedEvent;
import pl.bzowski.bandmanager.musician.events.MusicianUpdatedEvent;
import pl.bzowski.bandmanager.views.musicians.GetOneMusician;

import java.util.List;

@Service
public class MusicianService {

    private final MusicianRepository repository;


    public MusicianService(MusicianRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void on(MusicianCreatedEvent event) {
        var entity = new Musician();
        entity.setId(event.getMusicianId());
        entity.setFirstName(event.getFirstName());
        entity.setLastName(event.getLastName());
        entity.setPhone(event.getPhone());
        entity.setEmail(event.getEmail());
        entity.setDateOfBirth(event.getDateOfBirth());
        entity.setJoinDate(event.getJoinDate());
        entity.setActive(event.getActive());
        entity.setAddress(event.getAddress());
        repository.save(entity);
    }

    @EventHandler
    public void on(MusicianUpdatedEvent event) {
        var byId = repository.findById(event.getId()).orElse(null);
        byId.setId(event.getId());
        byId.setFirstName(event.getFirstName());
        byId.setLastName(event.getLastName());
        byId.setPhone(event.getPhone());
        byId.setEmail(event.getEmail());
        byId.setDateOfBirth(event.getDateOfBirth());
        byId.setJoinDate(event.getJoinDate());
        byId.setActive(event.getActive());
        byId.setAddress(event.getAddress());
        repository.save(byId);
    }


//    public Optional<Musician> get(Long id) {
//        return repository.findById(id);
//    }

    public Musician update(Musician entity) {
        return repository.save(entity);
    }

//    public void delete(Long id) {
//        repository.deleteById(id);
//    }

    public Page<Musician> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Musician> list(Pageable pageable, Specification<Musician> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    @QueryHandler
    public List<Musician> on(GetAllMusiciansQuery query) {
        return repository.findAll();
    }

    @QueryHandler
    public Musician on(GetOneMusician query) {
        return repository.findById(query.getId()).orElseThrow();
    }

}
