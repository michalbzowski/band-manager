package pl.bzowski.bandmanager.views.musicians;

import java.util.UUID;

public class GetOneMusician {
    private final UUID id;

    public GetOneMusician(UUID aLong) {
        this.id = aLong;
    }

    public UUID getId() {
        return id;
    }
}
