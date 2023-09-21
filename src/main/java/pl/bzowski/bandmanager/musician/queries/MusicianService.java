package pl.bzowski.bandmanager.musician.queries;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import pl.bzowski.bandmanager.data.entity.Musician;
import pl.bzowski.bandmanager.musicevent.MusicEventRepository;
import pl.bzowski.bandmanager.musician.events.MusicianSignedUpEvent;
import pl.bzowski.bandmanager.musician.events.MusicianUpdatedEvent;
import pl.bzowski.bandmanager.presenceslot.PresenceRepository;
import pl.bzowski.bandmanager.presenceslot.commands.CreatePresenceSlotCommand;
import pl.bzowski.bandmanager.presenceslot.commands.RemovePresenceCommand;
import pl.bzowski.bandmanager.views.musicians.GetOneMusician;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class MusicianService {

    private final MusicianRepository musicianRepository;
    private final PresenceRepository presenceRepository;
    private final MusicEventRepository musicEventRepository;
    private final EventGateway eventGateway;

    public MusicianService(MusicianRepository musicianRepository, PresenceRepository presenceRepository, MusicEventRepository musicEventRepository, EventGateway eventGateway) {
        this.musicianRepository = musicianRepository;
        this.presenceRepository = presenceRepository;
        this.eventGateway = eventGateway;
        this.musicEventRepository = musicEventRepository;
    }

    @EventHandler
    public void on(MusicianSignedUpEvent event) {
        var entity = new Musician();
        entity.setId(event.getMusicianId());
        entity.setFirstName(event.getFirstName());
        entity.setLastName(event.getLastName());
        entity.setPhone(event.getPhone());
        entity.setDateOfBirth(event.getDateOfBirth());
        entity.setJoinDate(event.getJoinDate());
        entity.setActive(event.getActive());
        entity.setAddress(event.getAddress());
        musicianRepository.save(entity);
    }

    @EventHandler
    public void on(MusicianUpdatedEvent event) {
        log.info("On MusicianUpdatedEvent");
        var byId = musicianRepository.findById(event.getMusicianId()).orElse(null);
        byId.setId(event.getMusicianId());
        byId.setFirstName(event.getFirstName());
        byId.setLastName(event.getLastName());
        byId.setPhone(event.getPhone());
        byId.setDateOfBirth(event.getDateOfBirth());
        byId.setJoinDate(event.getJoinDate());
        byId.setActive(event.getActive());
        byId.setAddress(event.getAddress());
        musicianRepository.save(byId);
    }

    public Musician update(Musician entity) {
        return musicianRepository.save(entity);
    }

    public Page<Musician> list(Pageable pageable) {
        return musicianRepository.findAll(pageable);
    }

    public Page<Musician> list(Pageable pageable, Specification<Musician> filter) {
        return musicianRepository.findAll(filter, pageable);
    }

    public int count() {
        return (int) musicianRepository.count();
    }

    @QueryHandler
    public List<Musician> on(GetAllMusiciansQuery query) {
        return musicianRepository.findAll();
    }

    @QueryHandler
    public Musician on(GetOneMusician query) {
        return musicianRepository.findById(query.getId()).orElseThrow();
    }

}
