package pl.bzowski.bandmanager.presenceslot;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import pl.bzowski.bandmanager.data.entity.MusicEvent;
import pl.bzowski.bandmanager.data.entity.Musician;
import pl.bzowski.bandmanager.data.entity.Presence;
import pl.bzowski.bandmanager.musicevent.MusicEventRepository;
import pl.bzowski.bandmanager.musicevent.commands.CreateMusicEventCommand;
import pl.bzowski.bandmanager.musicevent.events.MusicEventCreatedEvent;
import pl.bzowski.bandmanager.musicevent.events.MusicEventDateCreated;
import pl.bzowski.bandmanager.musicevent.events.MusicEventDateUpdated;
import pl.bzowski.bandmanager.musician.commands.CreateMusicianCommand;
import pl.bzowski.bandmanager.musician.events.MusicianJoinDateCreatedEvent;
import pl.bzowski.bandmanager.musician.events.MusicianJoinDateUpdatedEvent;
import pl.bzowski.bandmanager.musician.events.MusicianSignedUpEvent;
import pl.bzowski.bandmanager.musician.events.MusicianUpdatedEvent;
import pl.bzowski.bandmanager.musician.queries.MusicianRepository;
import pl.bzowski.bandmanager.presenceslot.commands.CreatePresenceSlotCommand;
import pl.bzowski.bandmanager.presenceslot.commands.RemovePresenceCommand;
import pl.bzowski.bandmanager.presenceslot.events.PresenceRemovedEvent;
import pl.bzowski.bandmanager.presenceslot.events.PresenceSlotCreatedEvent;
import pl.bzowski.bandmanager.presenceslot.queries.GetMusicianPresenceInMusicEventQuery;
import pl.bzowski.bandmanager.presenceslot.queries.MusicianMusicEventPresenceChangedEvent;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class PresenceProjector {

    private final PresenceRepository presenceRepository;
    private final MusicianRepository musicianRepository;
    private final MusicEventRepository musicEventRepository;
    private final CommandGateway commandGateway;

    public PresenceProjector(PresenceRepository presenceRepository,
            MusicianRepository musicianRepository,
            MusicEventRepository musicEventRepository,
            CommandGateway commandGateway) {
        this.presenceRepository = presenceRepository;
        this.musicianRepository = musicianRepository;
        this.musicEventRepository = musicEventRepository;
        this.commandGateway = commandGateway;
    }

    @EventHandler
    public void on(MusicianJoinDateCreatedEvent event) {
        if (!event.isMusicianActive()) {
            return;
        }
        var musicianId = event.getMusicianId();
        var joinDate = event.getCurrentJoinDate();
        var musicEvents = musicEventRepository.findAllAfter(joinDate.atStartOfDay());

        for (MusicEvent musicEvent : musicEvents) {
            var presenceId = UUID.randomUUID();
            commandGateway.send(
                    new CreatePresenceSlotCommand(
                            presenceId,
                            musicEvent.getId(),
                            musicianId,
                            Boolean.FALSE));
        }
    }

    @EventHandler
    public void on(MusicianJoinDateUpdatedEvent event) {
        var musicianId = event.getMusicianId();
        var earlier = event.getCurrentJoinDate().isBefore(event.getNewJoinDate()) ? event.getCurrentJoinDate() : event.getNewJoinDate();
        var further = event.getCurrentJoinDate().isAfter(event.getNewJoinDate()) ? event.getCurrentJoinDate() : event.getNewJoinDate();
        if (event.getCurrentJoinDate().isBefore(event.getNewJoinDate())) {        
            var musicEvents = musicEventRepository.findAllBetween(earlier.atStartOfDay(), further.plusDays(1).atStartOfDay());
            for (MusicEvent musicEvent : musicEvents) {
            var presenceId = UUID.randomUUID();
            commandGateway.send(
                    new CreatePresenceSlotCommand(
                            presenceId,
                            musicEvent.getId(),
                            musicianId,
                            Boolean.FALSE));
            }
        } else {
            var presenceList = presenceRepository.findAllBetween(earlier, further);
            for(Presence p : presenceList) {
                commandGateway.send (new RemovePresenceCommand(p.getId()));
            }
        }
    }
        
    @EventHandler
    public void on(MusicEventDateCreated event) {
        var musicEventId = event.getMusicEventId();
        var eventDateTime = event.getDateTime();

        var musicians = musicianRepository.findAllActiveOnMusicEventTime(eventDateTime.toLocalDate());
        for (Musician musician : musicians) {
            var presenceSlotId = UUID.randomUUID();
            log.info("PresenceProjector@EventHandler.onMusicEventCreatedEvent - sending CreatePresenceEntryCommand");
            commandGateway.send(
                    new CreatePresenceSlotCommand(
                            presenceSlotId,
                            musicEventId,
                            musician.getId(),
                            Boolean.FALSE));
        }
    }

    @EventHandler
    public void on(MusicEventDateUpdated event) {
        var musicEventId = event.getMusicEventId();
        var earlier = event.getCurrentDateTime().isBefore(event.getNewDateTime()) ? event.getCurrentDateTime() : event.getNewDateTime();
        var further = event.getCurrentDateTime().isAfter(event.getNewDateTime()) ? event.getCurrentDateTime() : event.getNewDateTime();
        
         if (event.getCurrentDateTime().isBefore(event.getNewDateTime())) {     
            var musicians = musicianRepository.findAllActiveOnMusicEventTime(event.getNewDateTime().toLocalDate());
            for (Musician musician : musicians) {
                var presenceSlotId = UUID.randomUUID();
                log.info("PresenceProjector@EventHandler.onMusicEventCreatedEvent - sending CreatePresenceEntryCommand");
                commandGateway.send(
                        new CreatePresenceSlotCommand(
                                presenceSlotId,
                                musicEventId,
                                musician.getId(),
                                Boolean.FALSE));
            }
         } else {
            var presenceList = presenceRepository.findAllBetween(earlier.toLocalDate(), further.plusDays(1).toLocalDate());
            for(Presence p : presenceList) {
                commandGateway.send (new RemovePresenceCommand(p.getId()));
            }
         }

    }

    @EventHandler
    public void on(PresenceSlotCreatedEvent event) {
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
