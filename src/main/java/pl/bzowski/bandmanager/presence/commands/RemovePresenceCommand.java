package pl.bzowski.bandmanager.presence.commands;

import java.time.LocalDate;
import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class RemovePresenceCommand {

    @TargetAggregateIdentifier
    private UUID id;
    
}
