package pl.bzowski.bandmanager.musicevent;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;
import pl.bzowski.bandmanager.data.entity.MusicEvent;

import java.util.List;
import java.util.Optional;

@Component
public class MusicEventsProjector {

    private MusicEventRepository musicEventRepository;

    public MusicEventsProjector(MusicEventRepository musicEventRepository) {
        this.musicEventRepository = musicEventRepository;
    }

    @EventHandler
    public void on(MusicEventCreatedEvent event) {
        var entity = new MusicEvent();
        entity.setId(event.getMusicEventId());
        entity.setAddress(event.getAddress());
        entity.setName(event.getName());
        entity.setDateTime(event.getDateTime());
        musicEventRepository.save(entity);
    }

    @EventHandler
    public void on(MusicEventUpdatedEvent event) {
        var entity = musicEventRepository.findById(event.getMusicEventId()).orElseThrow();
        entity.setAddress(event.getAddress());
        entity.setName(event.getName());
        entity.setDateTime(event.getDateTime());
        musicEventRepository.save(entity);
    }

    @QueryHandler
    public List<MusicEvent> query(GetAllMusicEventsQuery query) {
        return musicEventRepository.findAll();
    }

    @QueryHandler
    public Optional<MusicEvent> query(GetMusicEventQuery query) {
        return musicEventRepository.findById(query.getMusicEventId());
    }

}
