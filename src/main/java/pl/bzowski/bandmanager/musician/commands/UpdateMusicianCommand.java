package pl.bzowski.bandmanager.musician.commands;


import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public final class UpdateMusicianCommand {
    @TargetAggregateIdentifier
    private final UUID id;
    private final String firstName;
    private final String lastName;
    private final String phone;
    private final LocalDate dateOfBirth;
    private final LocalDate joinDate;
    private final Boolean active;
    private final String address;
}
