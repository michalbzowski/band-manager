package pl.bzowski.bandmanager.views.presence;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.RolesAllowed;
import pl.bzowski.bandmanager.data.service.EventRepository;
import pl.bzowski.bandmanager.musician.queries.MusicianRepository;
import pl.bzowski.bandmanager.views.MainLayout;

import java.util.Comparator;
import java.util.stream.Collectors;

@PageTitle("Presence")
@Route(value = "presence", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@RolesAllowed("USER")
public class PresenceView extends VerticalLayout {
    private final EventRepository eventRepository;
    private final MusicianRepository musicianRepository;

    private Grid<Presence> grid;
    ComboBox<String> eventComboBox;

    public PresenceView(EventRepository eventRepository, MusicianRepository musicianRepository) {
        this.eventRepository = eventRepository;
        this.musicianRepository = musicianRepository;

        comboBox();
        grid();
        add(eventComboBox, grid);

        // Pobranie i wyświetlenie domyślnego wydarzenia
        String selectedEvent = eventComboBox.getValue();
        refreshGrid(selectedEvent);
    }

    private void grid() {
        grid = new Grid<>();
        grid.addColumn(Presence::getFullName).setHeader("Imię i nazwisko");
        grid.addComponentColumn(this::createCheckBox).setHeader("Obecna / obecny");
    }

    private ComboBox<String> comboBox() {
        this.eventComboBox = new ComboBox<>();
        var all = eventRepository.findAll().stream().map(e -> e.getName()).collect(Collectors.toSet());
        eventComboBox.setItems(all);
        eventComboBox.setLabel("Wybierz wydarzenie");
        eventComboBox.setValue(all.stream().findFirst().get());
        eventComboBox.addValueChangeListener(event -> {
            String selectedEvent = event.getValue();
            refreshGrid(selectedEvent);
        });
        return eventComboBox;
    }

    private void refreshGrid(String selectedEvent) {
        var pres = musicianRepository.findAll()
                .stream()
                .map(m -> new Presence(m.getFirstName() + " " + m.getLastName()))
                .sorted(Comparator.comparing(Presence::getFullName))
                .collect(Collectors.toList());
        grid.setItems(pres);
    }

    private Checkbox createCheckBox(Presence presence) {
        Checkbox checkbox = new Checkbox();
        checkbox.addValueChangeListener(event -> {
            presence.setChecked(checkbox.getValue());
//            personRepository.save(person); // Zapisz zmienione dane osoby w repozytori
        });

        return checkbox;
    }

    public static class Presence {
        private String fullName;
        private boolean checked;

        public Presence(String fullName) {
            this.fullName = fullName;
        }

        public String getFullName() {
            return fullName;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }
    }

}
