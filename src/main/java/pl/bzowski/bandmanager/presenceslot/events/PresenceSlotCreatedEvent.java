package pl.bzowski.bandmanager.presenceslot.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PresenceSlotCreatedEvent {

    @TargetAggregateIdentifier
    private UUID presenceId;
    private UUID eventId;
    private UUID musicianId;
    private boolean present;
}
