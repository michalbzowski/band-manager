package pl.bzowski.bandmanager.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.util.UUID;

@Entity
public class Presence extends AbstractEntity {

    private UUID musicianId;

    private String musicianFullName;
    @ManyToOne
    private MusicEvent musicEvent;

    private boolean present;

    public Presence() {

    }

    public Presence(UUID presenceId, UUID musicianId, String musicianFullName, UUID musicEventId) {
        super.setId(presenceId);
        this.musicianId = musicianId;
        this.musicianFullName = musicianFullName;
        this.musicEvent = new MusicEvent(musicEventId);
        this.present = Boolean.FALSE;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }
}