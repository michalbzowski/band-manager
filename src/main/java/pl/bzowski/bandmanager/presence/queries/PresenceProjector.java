package pl.bzowski.bandmanager.presence.queries;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import pl.bzowski.bandmanager.data.entity.MusicEvent;
import pl.bzowski.bandmanager.data.entity.Musician;
import pl.bzowski.bandmanager.data.entity.Presence;
import pl.bzowski.bandmanager.musicevent.MusicEventCreatedEvent;
import pl.bzowski.bandmanager.musicevent.MusicEventRepository;
import pl.bzowski.bandmanager.musician.events.MusicianSignedUpEvent;
import pl.bzowski.bandmanager.musician.events.MusicianUpdatedEvent;
import pl.bzowski.bandmanager.musician.queries.MusicianRepository;
import pl.bzowski.bandmanager.presence.PresenceDto;
import pl.bzowski.bandmanager.presence.PresenceRepository;
import pl.bzowski.bandmanager.presence.commands.CreatePresenceEntryCommand;
import pl.bzowski.bandmanager.presence.events.PresenceCreatedEvent;
import pl.bzowski.bandmanager.presence.events.PresenceRemovedEvent;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class PresenceProjector {

    private final PresenceRepository presenceRepository;
    private final MusicianRepository musicianRepository;
    private final MusicEventRepository musicEventRepository;
    private final EventGateway  eventGateway;

    public PresenceProjector(PresenceRepository presenceRepository,
            MusicianRepository musicianRepository,
            MusicEventRepository musicEventRepository,
            EventGateway eventGateway) {
        this.presenceRepository = presenceRepository;
        this.musicianRepository = musicianRepository;
        this.musicEventRepository = musicEventRepository;
        this.eventGateway = eventGateway;
    }

    @EventHandler
    public void on(MusicianSignedUpEvent event) {
        if (!event.getActive()) {
            return;
        }
        var musicianId = event.getMusicianId();
        var joinDate = event.getJoinDate();
        var musicEvents = musicEventRepository.findAllAfter(joinDate.atStartOfDay());

        for (MusicEvent musicEvent : musicEvents) {
            var presenceId = UUID.randomUUID();
            // eventGateway.publish(
            //         new PresenceCreatedEvent(
            //                 presenceId,
            //                 musicEvent.getId(),
            //                 musicianId,
            //                 Boolean.FALSE));
        }
    }

    @EventHandler
    public void on(MusicEventCreatedEvent event) {
        var musicEventId = event.getMusicEventId();
        var eventDateTime = event.getDateTime();

        var musicians = musicianRepository.findAllActiveOnMusicEventTime(eventDateTime.toLocalDate());
        for (Musician musician : musicians) {
            var presenceId = UUID.randomUUID();
            log.info("PresenceProjector@EventHandler.onMusicEventCreatedEvent - sending CreatePresenceEntryCommand");
            eventGateway.publish(
                    new PresenceCreatedEvent(
                            presenceId,
                            musicEventId,
                            musician.getId(),
                            Boolean.FALSE));
        }
    }

    @EventHandler
    public void on(PresenceCreatedEvent event) {
        log.info("PresenceProjector@EventHandler.onPresenceCreatedEvent: " + event);
        var musician = musicianRepository.findById(event.getMusicianId());
        var musicianFullName = musician.get().getFullName();
        var p = new Presence(event.getPresenceId(), event.getMusicianId(), musicianFullName, event.getEventId());
        presenceRepository.save(p);
    }

    @EventHandler
    public void on(PresenceRemovedEvent event) {
        presenceRepository.deleteById(event.getId());
    }

    @EventHandler
    public void on(MusicianMusicEventPresenceChangedEvent event) {
        Presence presence = presenceRepository.findForMusicianIdAndEventId(event.getMusicianId(), event.getEventId());
        presence.setPresent(event.isPresent());
        presenceRepository.save(presence);
    }

    @QueryHandler
    public List<PresenceDto> handle(GetMusicianPresenceInMusicEventQuery query) {
        return presenceRepository.findAllForEventId(query.getMusicEventId());
    }
}
