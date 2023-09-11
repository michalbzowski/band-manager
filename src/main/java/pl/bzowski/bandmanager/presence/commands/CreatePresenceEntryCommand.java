package pl.bzowski.bandmanager.presence.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CreatePresenceEntryCommand {

    @TargetAggregateIdentifier
    private UUID presenceId;
    private UUID eventId;
    private UUID musicianId;
    private boolean present;
}
