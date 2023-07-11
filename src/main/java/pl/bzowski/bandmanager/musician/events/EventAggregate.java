package pl.bzowski.bandmanager.musician.events;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.time.LocalDateTime;
import java.util.UUID;

@Aggregate
public class EventAggregate {

    @TargetAggregateIdentifier
    private UUID id;

    private String name;
    private String address;
    private LocalDateTime dateTime;

}
