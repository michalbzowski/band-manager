package pl.bzowski.bandmanager.presenceslot;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;

import org.axonframework.spring.stereotype.Aggregate;

import lombok.extern.slf4j.Slf4j;
import pl.bzowski.bandmanager.presenceslot.commands.ChangeMusicianPresenceCommand;
import pl.bzowski.bandmanager.presenceslot.commands.CreatePresenceSlotCommand;
import pl.bzowski.bandmanager.presenceslot.commands.RemovePresenceCommand;
import pl.bzowski.bandmanager.presenceslot.events.PresenceRemovedEvent;
import pl.bzowski.bandmanager.presenceslot.events.PresenceSlotCreatedEvent;
import pl.bzowski.bandmanager.presenceslot.queries.MusicianMusicEventPresenceChangedEvent;

import java.util.UUID;
@Aggregate
@Slf4j
public class PresenceSlotAggregate {

    @AggregateIdentifier
    private UUID presenceId;
    private UUID eventId;
    private UUID musicianId;
    private boolean present;

    public PresenceSlotAggregate() {

    }

    @CommandHandler
    public PresenceSlotAggregate(CreatePresenceSlotCommand command) {
        log.info("CreatePresenceEntryCommand: " + command);
        AggregateLifecycle.apply(new PresenceSlotCreatedEvent(
                command.getPresenceId(),
                command.getEventId(),
                command.getMusicianId(),
                command.isPresent()
        ));
    }

    @EventSourcingHandler
    public void on(PresenceSlotCreatedEvent event) {
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
