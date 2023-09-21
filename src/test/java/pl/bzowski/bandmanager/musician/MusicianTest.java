package pl.bzowski.bandmanager.musician;

 
import java.time.LocalDate;
import java.util.UUID;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.bzowski.bandmanager.musician.commands.CreateMusicianCommand;
import pl.bzowski.bandmanager.musician.events.MusicianSignedUpEvent;

 

public class MusicianTest {
     
    private FixtureConfiguration<MusicianAggregate> fixture;

    @BeforeEach
    public void setUp() {
        fixture = new AggregateTestFixture<>(MusicianAggregate.class);
    }

     @Test
    public void testFirstFixture() {
        var lol = UUID.randomUUID();
        var musician1 = MusicianSignedUpEvent
            .builder()
            .musicianId(lol)
            .firstName("Michal")
            .lastName("Bzowski")
            .address("WK")
            .active(true)
            .dateOfBirth(LocalDate.of(1989, 07, 12))
            .joinDate(LocalDate.of(2000, 01, 01))
            .build();

        var musician2 = CreateMusicianCommand
            .builder()
            .musicianId(UUID.randomUUID())
            .firstName("Patryk")
            .lastName("Nowy")
            .address("WK")
            .active(true)
            .dateOfBirth(LocalDate.of(1989, 07, 12))
            .joinDate(LocalDate.of(2010, 01, 01))
            .build();

        var musician3 = MusicianSignedUpEvent
            .builder()
            .musicianId(lol)
            .firstName("Patryk")
            .lastName("Nowy")
            .address("WK")
            .active(true)
            .dateOfBirth(LocalDate.of(1989, 07, 12))
            .joinDate(LocalDate.of(2010, 01, 01))
            .build();
            

        fixture.given(musician1)
               .when(musician2)
               .expectSuccessfulHandlerExecution()
               .expectEvents(musician3);
        /*
        These four lines define the actual scenario and its expected
        result. The first line defines the events that happened in the
        past. These events define the state of the aggregate under test.
        In practical terms, these are the events that the event store
        returns when an aggregate is loaded. The second line defines the
        command that we wish to execute against our system. Finally, we
        have two more methods that define expected behavior. In the
        example, we use the recommended void return type. The last method
        defines that we expect a single event as result of the command
        execution.
        */
    }

}
