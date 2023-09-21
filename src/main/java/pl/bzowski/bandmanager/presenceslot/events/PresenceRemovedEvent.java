package pl.bzowski.bandmanager.presenceslot.events;

import java.time.LocalDate;
import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class PresenceRemovedEvent {

    @TargetAggregateIdentifier
    private UUID id;
    
}
