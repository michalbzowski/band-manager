package pl.bzowski.bandmanager.presenceslot.commands;

import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Data
public class ChangeMusicianPresenceCommand {

    @TargetAggregateIdentifier
    private final UUID presenceId;
    private final UUID eventId;
    private final UUID musicianId;
    private final Boolean present;
}
