package pl.bzowski.bandmanager.musician;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.axonframework.eventsourcing.EventSourcingHandler;

import pl.bzowski.bandmanager.musician.commands.CreateMusicianCommand;
import pl.bzowski.bandmanager.musician.commands.UpdateMusicianCommand;
import pl.bzowski.bandmanager.musician.events.MusicianJoinDateCreatedEvent;
import pl.bzowski.bandmanager.musician.events.MusicianJoinDateUpdatedEvent;
import pl.bzowski.bandmanager.musician.events.MusicianSignedUpEvent;
import pl.bzowski.bandmanager.musician.events.MusicianUpdatedEvent;

import java.time.LocalDate;
import java.util.UUID;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class MusicianAggregate {

    @AggregateIdentifier
    private UUID musicianId;
    private String firstName;
    private String lastName;
    private String phone;
    private LocalDate dateOfBirth;
    private LocalDate joinDate;
    private Boolean active;
    private String address;

    protected MusicianAggregate() {

    }

    @CommandHandler
    public MusicianAggregate(CreateMusicianCommand command) {
         apply(new MusicianSignedUpEvent(command.getMusicianId(),
                command.getFirstName(),
                command.getLastName(),
                command.getPhone(),
                command.getDateOfBirth(),
                command.getJoinDate(),
                command.getActive(),
                command.getAddress()
        )).andThenApply(() -> new MusicianJoinDateCreatedEvent(command.getMusicianId(), 
            command.getJoinDate(),
            command.getFirstName() + " " + command.getLastName(),
            command.getActive()));
    }

    @EventSourcingHandler
    public void on(MusicianSignedUpEvent evt) {
        if (evt.getJoinDate() == null) {
            throw new MusicianJoinDateIsEmptyException();
        }
        this.musicianId = evt.getMusicianId();
        this.firstName = evt.getFirstName();
        this.lastName = evt.getLastName();
        this.phone = evt.getPhone();
        this.dateOfBirth = evt.getDateOfBirth();
        this.joinDate = evt.getJoinDate();
        this.active = evt.getActive();
        this.address = evt.getAddress();
    }

    @CommandHandler
    public void on(UpdateMusicianCommand command) {
        var currentJoinDate = this.joinDate;
        apply(new MusicianUpdatedEvent(command.getId(),
                command.getFirstName(),
                command.getLastName(),
                command.getPhone(),
                command.getDateOfBirth(),
                command.getJoinDate(),
                command.getActive(),
                command.getAddress()
        )).andThenApplyIf(
            () -> !currentJoinDate.equals(command.getJoinDate()), 
            () -> new MusicianJoinDateUpdatedEvent(command.getId(), currentJoinDate, command.getJoinDate()));
    }

    @EventSourcingHandler
    public void on(MusicianUpdatedEvent evt) {
        this.musicianId = evt.getMusicianId();
        this.firstName = evt.getFirstName();
        this.lastName = evt.getLastName();
        this.phone = evt.getPhone();
        this.dateOfBirth = evt.getDateOfBirth();
        this.joinDate = evt.getJoinDate();
        this.active = evt.getActive();
        this.address = evt.getAddress();
    }
}
