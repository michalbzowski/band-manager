package pl.bzowski.bandmanager.presence.queries;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;
import pl.bzowski.bandmanager.data.entity.MusicEvent;
import pl.bzowski.bandmanager.data.entity.Musician;
import pl.bzowski.bandmanager.data.entity.Presence;
import pl.bzowski.bandmanager.musicevent.MusicEventCreatedEvent;
import pl.bzowski.bandmanager.musicevent.MusicEventRepository;
import pl.bzowski.bandmanager.musician.events.MusicianSignedUpEvent;
import pl.bzowski.bandmanager.musician.queries.MusicianRepository;
import pl.bzowski.bandmanager.presence.CreatePresenceEntryCommand;
import pl.bzowski.bandmanager.presence.PresenceDto;
import pl.bzowski.bandmanager.presence.PresenceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class PresenceProjector {

    private final PresenceRepository presenceRepository;
    private final MusicianRepository musicianRepository;
    private final MusicEventRepository musicEventRepository;
    private final CommandGateway commandGateway;

    public PresenceProjector(PresenceRepository presenceRepository,
                             MusicianRepository musicianRepository,
                             MusicEventRepository musicEventRepository,
                             CommandGateway commandGateway
    ) {
        this.presenceRepository = presenceRepository;
        this.musicianRepository = musicianRepository;
        this.musicEventRepository = musicEventRepository;
        this.commandGateway = commandGateway;
    }

    @EventHandler
    public void on(MusicianSignedUpEvent event) {
        if (!event.getActive()) {
            return;
        }
        var musicianId = event.getMusicianId();
        var musicianFullName = event.getFirstName() + " " + event.getLastName();

        var joinDate = event.getJoinDate();
        var musicEvents = musicEventRepository.findAllAfter(joinDate.atStartOfDay());

        List<Presence> entities = new ArrayList<>();
        for (MusicEvent musicEvent : musicEvents) {
            var presenceId = UUID.randomUUID();
            entities.add(new Presence(presenceId, musicianId, musicianFullName, musicEvent.getId()));
            commandGateway.send(
                    new CreatePresenceEntryCommand(
                            presenceId,
                            musicEvent.getId(),
                            musicianId,
                            Boolean.FALSE
                    ));
        }
        presenceRepository.saveAll(entities);
    }


    @EventHandler
    public void on(MusicEventCreatedEvent event) {
        var musicEventId = event.getMusicEventId();
        var eventDateTime = event.getDateTime();

        var musicians = musicianRepository.findAllActiveOnMusicEventTime(eventDateTime.toLocalDate());

        List<Presence> entities = new ArrayList<>();
        for (Musician musician : musicians) {
            String musicianFullName = musician.getFirstName() + " " + musician.getLastName();
            var presenceId = UUID.randomUUID();
            entities.add(new Presence(presenceId, musician.getId(), musicianFullName, musicEventId));
            commandGateway.send(
                    new CreatePresenceEntryCommand(
                            presenceId,
                            musicEventId,
                            musician.getId(),
                            Boolean.FALSE
                    ));
        }
        presenceRepository.saveAll(entities);
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
