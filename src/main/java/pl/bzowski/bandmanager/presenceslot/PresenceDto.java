package pl.bzowski.bandmanager.presenceslot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PresenceDto {

    private UUID presenceId;
    private UUID musicianId;
    private String fullName;
    private boolean checked;
}