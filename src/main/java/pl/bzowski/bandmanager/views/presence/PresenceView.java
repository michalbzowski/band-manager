package pl.bzowski.bandmanager.views.presence;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import pl.bzowski.bandmanager.data.entity.Event;
import pl.bzowski.bandmanager.data.entity.Musician;
import pl.bzowski.bandmanager.data.service.EventRepository;
import pl.bzowski.bandmanager.views.MainLayout;

import java.util.List;
import java.util.stream.Collectors;

@PageTitle("Presence")
@Route(value = "presence", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@RolesAllowed("USER")
public class PresenceView extends VerticalLayout {


    private final EventRepository eventRepository;

    private Grid<Presence> grid;
    ComboBox<String> eventComboBox;

    public PresenceView(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
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
        grid.addComponentColumn(this::createCheckBox).setHeader("Checkbox");
    }

    private ComboBox<String> comboBox() {
        this.eventComboBox = new ComboBox<>();
        var all = eventRepository.findAll().stream().map(e -> e.getName()).collect(Collectors.toSet());
        eventComboBox.setItems(all);
        eventComboBox.setLabel("Wybierz wydarzenie");
        eventComboBox.addValueChangeListener(event -> {
            String selectedEvent = event.getValue();
            refreshGrid(selectedEvent);
        });
        return eventComboBox;
    }

    private void refreshGrid(String selectedEvent) {
        // Pobranie listy osób dla wybranego wydarzenia z repozytorium
        // Dodanie przykładowych danych do siatki
        Presence person1 = new Presence("Jan Kowalski");
        Presence person2 = new Presence("Anna Nowak");
        grid.setItems(person1, person2);
//        List<Presence> people = personRepository.findByEvent(selectedEvent);
//        grid.setItems(people);
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
