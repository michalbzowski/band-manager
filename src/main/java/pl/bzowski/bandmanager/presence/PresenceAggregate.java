package pl.bzowski.bandmanager.presence;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;

import org.axonframework.spring.stereotype.Aggregate;

import lombok.extern.slf4j.Slf4j;
import pl.bzowski.bandmanager.presence.commands.ChangeMusicianPresenceCommand;
import pl.bzowski.bandmanager.presence.commands.CreatePresenceEntryCommand;
import pl.bzowski.bandmanager.presence.commands.RemovePresenceCommand;
import pl.bzowski.bandmanager.presence.events.PresenceCreatedEvent;
import pl.bzowski.bandmanager.presence.events.PresenceRemovedEvent;
import pl.bzowski.bandmanager.presence.queries.MusicianMusicEventPresenceChangedEvent;

import java.util.UUID;
@Aggregate
@Slf4j
public class PresenceAggregate {

    @AggregateIdentifier
    private UUID presenceId;
    private UUID eventId;
    private UUID musicianId;
    private boolean present;

    public PresenceAggregate() {

    }

    @CommandHandler
    public PresenceAggregate(CreatePresenceEntryCommand command) {
        log.info("CreatePresenceEntryCommand: " + command);
        AggregateLifecycle.apply(new PresenceCreatedEvent(
                command.getPresenceId(),
                command.getEventId(),
                command.getMusicianId(),
                command.isPresent()
        ));
    }

    @EventSourcingHandler
    public void on(PresenceCreatedEvent event) {
        log.info("PresenceCreatedEvent: " + event);
        this.presenceId = event.getPresenceId();
        this.eventId = event.getEventId();
        this.musicianId = event.getMusicianId();
        this.present = event.isPresent();
    }

    @CommandHandler
    public void handle(ChangeMusicianPresenceCommand command) {
        AggregateLifecycle.apply(new MusicianMusicEventPresenceChangedEvent(
                command.getPresenceId(),
                command.getMusicianId(),
                command.getEventId(),
                command.getPresent()
        ));
    }

    @EventSourcingHandler
    public void on(MusicianMusicEventPresenceChangedEvent event) {
        this.present = event.isPresent();
    }

    @CommandHandler
    public void handle(RemovePresenceCommand command) {
        AggregateLifecycle.apply(new PresenceRemovedEvent((command.getId())));
    }

    @EventSourcingHandler
    public void handle(PresenceRemovedEvent event) {
        AggregateLifecycle.markDeleted();
    }

    

}
