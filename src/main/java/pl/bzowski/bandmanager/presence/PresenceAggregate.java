package pl.bzowski.bandmanager.presence;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import pl.bzowski.bandmanager.presence.queries.MusicianMusicEventPresenceChangedEvent;

import java.util.UUID;
@Aggregate
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
        AggregateLifecycle.apply(new PresenceCreatedEvent(
                command.getPresenceId(),
                command.getEventId(),
                command.getMusicianId(),
                command.isPresent()
        ));
    }

    @EventSourcingHandler
    public void on(PresenceCreatedEvent event) {
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
}
