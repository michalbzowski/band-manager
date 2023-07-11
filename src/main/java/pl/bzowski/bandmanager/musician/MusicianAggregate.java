package pl.bzowski.bandmanager.musician;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.axonframework.eventsourcing.EventSourcingHandler;
import pl.bzowski.bandmanager.musician.commands.CreateMusicianCommand;
import pl.bzowski.bandmanager.musician.commands.UpdateMusicianCommand;
import pl.bzowski.bandmanager.musician.events.MusicianCreatedEvent;
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
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private LocalDate joinDate;
    private Boolean active;
    private String address;

    protected MusicianAggregate() {

    }

    @CommandHandler
    public MusicianAggregate(CreateMusicianCommand command) {
        apply(new MusicianCreatedEvent(command.musicianId(),
                command.firstName(),
                command.lastName(),
                command.email(),
                command.phone(),
                command.dateOfBirth(),
                command.joinDate(),
                command.active(),
                command.address()
        ));
    }

    @EventSourcingHandler
    public void on(MusicianCreatedEvent evt) {
        this.musicianId = evt.getMusicianId();
        this.firstName = evt.getFirstName();
        this.lastName = evt.getLastName();
        this.email = evt.getEmail();
        this.phone = evt.getPhone();
        this.dateOfBirth = evt.getDateOfBirth();
        this.joinDate = evt.getJoinDate();
        this.active = evt.getActive();
        this.address = evt.getAddress();
    }

    @CommandHandler
    public void on(UpdateMusicianCommand command) {
        apply(new MusicianUpdatedEvent(command.id(),
                command.firstName(),
                command.lastName(),
                command.email(),
                command.phone(),
                command.dateOfBirth(),
                command.joinDate(),
                command.active(),
                command.address()
        ));
    }

    @EventSourcingHandler
    public void on(MusicianUpdatedEvent evt) {
        this.musicianId = evt.getId();
        this.firstName = evt.getFirstName();
        this.lastName = evt.getLastName();
        this.email = evt.getEmail();
        this.phone = evt.getPhone();
        this.dateOfBirth = evt.getDateOfBirth();
        this.joinDate = evt.getJoinDate();
        this.active = evt.getActive();
        this.address = evt.getAddress();
    }
}
