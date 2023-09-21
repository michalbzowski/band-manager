package pl.bzowski.bandmanager.views.presence;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.RolesAllowed;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import pl.bzowski.bandmanager.data.entity.MusicEvent;
import pl.bzowski.bandmanager.musicevent.queries.GetAllMusicEventsQuery;
import pl.bzowski.bandmanager.presenceslot.PresenceDto;
import pl.bzowski.bandmanager.presenceslot.commands.ChangeMusicianPresenceCommand;
import pl.bzowski.bandmanager.presenceslot.queries.GetMusicianPresenceInMusicEventQuery;
import pl.bzowski.bandmanager.views.MainLayout;

import java.util.*;

@PageTitle("Presence")
@Route(value = "presence", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@RolesAllowed("USER")
public class PresenceView extends VerticalLayout {
    private final QueryGateway queryGateway;
    private final CommandGateway commandGateway;
    private Grid<PresenceDto> grid;
    private ComboBox<MusicEvent> eventComboBox;

    private final Map<Checkbox, UUID> checkboxMusicianIdMap = new HashMap<>();

    public PresenceView(QueryGateway queryGateway, CommandGateway commandGateway) {
        this.queryGateway = queryGateway;
        this.commandGateway = commandGateway;


        createComboBox();
        grid();
        add(eventComboBox, grid);

        // Pobranie i wyświetlenie domyślnego wydarzenia
        MusicEvent selectedEvent = eventComboBox.getValue();
        if(selectedEvent != null) {
            refreshGrid(selectedEvent.getId());
        }
    }

    private void grid() {
        grid = new Grid<>();
        grid.addColumn(PresenceDto::getFullName).setHeader("Imię i nazwisko");
        grid.addComponentColumn(this::createCheckBox).setHeader("Obecna / obecny");
    }

    private ComboBox<MusicEvent> createComboBox() {
        this.eventComboBox = new ComboBox<>();
        this.eventComboBox.setWidthFull();
        List<MusicEvent> all = queryGateway.query(new GetAllMusicEventsQuery(), ResponseTypes.multipleInstancesOf(MusicEvent.class)).join();
//        List<MusicEvent> all = List.of();
        eventComboBox.setItems(all);
        eventComboBox.setLabel("Wybierz wydarzenie");
        var first = all.stream().findFirst();
        if(first.isPresent()) {
            eventComboBox.setValue(first.get());
        }
        eventComboBox.setItemLabelGenerator(MusicEvent::getDescription);
        eventComboBox.addValueChangeListener(event -> {
            MusicEvent selectedEvent = event.getValue();
            refreshGrid(selectedEvent.getId());
        });
        return eventComboBox;
    }

    private void refreshGrid(UUID eventId) {
        var pres = queryGateway.query(new GetMusicianPresenceInMusicEventQuery(eventId), ResponseTypes.multipleInstancesOf(PresenceDto.class)).join();
        checkboxMusicianIdMap.clear();
        grid.setItems(pres);
    }

    private Checkbox createCheckBox(PresenceDto presence) {
        Checkbox checkbox = new Checkbox();
        checkboxMusicianIdMap.put(checkbox, presence.getMusicianId());
        checkbox.setValue(presence.isChecked());
        checkbox.addValueChangeListener(event -> {
            var present = checkbox.getValue();
            presence.setChecked(present);
            var eventId = eventComboBox.getValue().getId();
            var musicianId = checkboxMusicianIdMap.get(event.getSource());
            commandGateway.send(new ChangeMusicianPresenceCommand(presence.getPresenceId(), eventId, musicianId, present)).join();
        });
        return checkbox;
    }

}
