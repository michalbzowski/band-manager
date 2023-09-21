package pl.bzowski.bandmanager.musicevent.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CreateMusicEventCommand {
    private UUID musicEventId;
    private String name;
    private String address;
    private LocalDateTime dateTime;
}
