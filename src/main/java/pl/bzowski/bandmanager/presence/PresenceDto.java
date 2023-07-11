package pl.bzowski.bandmanager.presence;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PresenceDto {

    private UUID presenceId;
    private UUID musicianId;
    private String fullName;
    private boolean checked;
}