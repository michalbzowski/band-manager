package pl.bzowski.bandmanager.musicevent;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import pl.bzowski.bandmanager.musicevent.commands.CreateMusicEventCommand;
import pl.bzowski.bandmanager.musicevent.commands.UpdateMusicEventCommand;
import pl.bzowski.bandmanager.musicevent.events.MusicEventCreatedEvent;
import pl.bzowski.bandmanager.musicevent.events.MusicEventUpdatedEvent;

import java.time.LocalDateTime;
import java.util.UUID;

@Aggregate
public class MusicEventAggregate {

    @AggregateIdentifier
    private UUID id;
    private String name;
    private String address;
    private LocalDateTime dateTime;

    public MusicEventAggregate() {

    }

    @CommandHandler
    public MusicEventAggregate(CreateMusicEventCommand command) {
        AggregateLifecycle.apply(new MusicEventCreatedEvent(
                command.getId(),
                command.getName(),
                command.getAddress(),
                command.getDateTime()));
    }

    @EventSourcingHandler
    public void on(MusicEventCreatedEvent event) {
        this.id = event.getMusicEventId();
        this.name = event.getName();
        this.address = event.getAddress();
        this.dateTime = event.getDateTime();
    }

    @CommandHandler
    public void handle(UpdateMusicEventCommand command) {
        AggregateLifecycle.apply(new MusicEventCreatedEvent(
                command.getId(),
                command.getName(),
                command.getAddress(),
                command.getDateTime()));
    }

    @EventSourcingHandler
    public void on(MusicEventUpdatedEvent event) {
        this.name = event.getName();
        this.address = event.getAddress();
        this.dateTime = event.getDateTime();
    }
}
