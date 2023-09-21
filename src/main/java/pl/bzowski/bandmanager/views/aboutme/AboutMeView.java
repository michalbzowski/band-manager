package pl.bzowski.bandmanager.views.aboutme;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import jakarta.annotation.security.RolesAllowed;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.data.domain.PageRequest;
import pl.bzowski.bandmanager.data.entity.MusicEvent;
import pl.bzowski.bandmanager.data.entity.Musician;
import pl.bzowski.bandmanager.musician.queries.GetAllMusiciansQuery;
import pl.bzowski.bandmanager.musician.queries.GetMusicianHistory;
import pl.bzowski.bandmanager.musician.queries.MusicianHistoryDto;
import pl.bzowski.bandmanager.musician.queries.MusicianProjection;
import pl.bzowski.bandmanager.presence.PresenceDto;
import pl.bzowski.bandmanager.presence.queries.GetMusicianPresenceInMusicEventQuery;
import pl.bzowski.bandmanager.views.MainLayout;

import java.util.UUID;

@PageTitle("About Me")
@Route(value = "about-me", layout = MainLayout.class)
@RolesAllowed("USER")
@Uses(Icon.class)
public class AboutMeView extends Div {

    private final MusicianProjection personService;
    private final QueryGateway queryGateway;
    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private EmailField email = new EmailField("Email address");
    private DatePicker dateOfBirth = new DatePicker("Birthday");
    private PhoneNumberField phone = new PhoneNumberField("Phone number");
    private TextField occupation = new TextField("Occupation");

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private Binder<Musician> binder = new Binder<>(Musician.class);

    private ComboBox<Musician> musicianComboBox = new ComboBox<>();
    private final Grid<MusicianHistoryDto> grid = new Grid<>(MusicianHistoryDto.class, false);

    public AboutMeView(MusicianProjection personService, QueryGateway queryGateway) {
        this.personService = personService;
        this.queryGateway = queryGateway;
        addClassName("about-me-view");
        add(musicianComboBox());
        add(createTitle());
        add(createFormLayout());

        add(grid());
//        add(createButtonLayout());

        binder.bindInstanceFields(this);
        clearForm();

        cancel.addClickListener(e -> clearForm());
        save.addClickListener(e -> {
            personService.update(binder.getBean());
            Notification.show(binder.getBean().getClass().getSimpleName() + " details stored.");
            clearForm();
        });
    }

    private Component musicianComboBox() {
        var stream = queryGateway.query(new GetAllMusiciansQuery(), ResponseTypes.multipleInstancesOf(Musician.class)).join();
        this.musicianComboBox.setItems(stream);
        this.musicianComboBox.setLabel("Wybierz muzyka");
        var first = stream.stream().findFirst();
        if(first.isPresent()) {
            musicianComboBox.setValue(first.get());
        }
        musicianComboBox.setItemLabelGenerator(Musician::getLastName);
        musicianComboBox.addValueChangeListener(event -> {
            Musician selectedEvent = event.getValue();
            refreshGrid(selectedEvent.getId());
        });
        return musicianComboBox;
    }

    private void refreshGrid(UUID musicianId) {
        var pres = queryGateway.query(new GetMusicianHistory(musicianId), ResponseTypes.multipleInstancesOf(MusicianHistoryDto.class)).join();
        grid.setItems(pres);
    }

    private void clearForm() {
        binder.setBean(new Musician());
    }

    private Component createTitle() {
        return new H3("Personal information");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        email.setErrorMessage("Please enter a valid email address");
        formLayout.add(firstName, lastName, dateOfBirth, phone, email, occupation);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save);
        buttonLayout.add(cancel);
        return buttonLayout;
    }

    private static class PhoneNumberField extends CustomField<String> {
        private ComboBox<String> countryCode = new ComboBox<>();
        private TextField number = new TextField();

        public PhoneNumberField(String label) {
            setLabel(label);
            countryCode.setWidth("120px");
            countryCode.setPlaceholder("Country");
            countryCode.setAllowedCharPattern("[\\+\\d]");
            countryCode.setItems("+354", "+91", "+62", "+98", "+964", "+353", "+44", "+972", "+39", "+225");
            countryCode.addCustomValueSetListener(e -> countryCode.setValue(e.getDetail()));
            number.setAllowedCharPattern("\\d");
            HorizontalLayout layout = new HorizontalLayout(countryCode, number);
            layout.setFlexGrow(1.0, number);
            add(layout);
        }

        @Override
        protected String generateModelValue() {
            if (countryCode.getValue() != null && number.getValue() != null) {
                String s = countryCode.getValue() + " " + number.getValue();
                return s;
            }
            return "";
        }

        @Override
        protected void setPresentationValue(String phoneNumber) {
            String[] parts = phoneNumber != null ? phoneNumber.split(" ", 2) : new String[0];
            if (parts.length == 1) {
                countryCode.clear();
                number.setValue(parts[0]);
            } else if (parts.length == 2) {
                countryCode.setValue(parts[0]);
                number.setValue(parts[1]);
            } else {
                countryCode.clear();
                number.clear();
            }
        }
    }

    private Grid<MusicianHistoryDto> grid() {
        // Configure Grid
        grid.addColumn("firstName").setAutoWidth(true);
        grid.addColumn("lastName").setAutoWidth(true);
        grid.addColumn("email").setAutoWidth(true);
        grid.addColumn("phone").setAutoWidth(true);
        grid.addColumn("dateOfBirth").setAutoWidth(true);
        grid.addColumn("joinDate").setAutoWidth(true);
        grid.addColumn("active").setAutoWidth(true);
        grid.addColumn("address").setAutoWidth(true);

        grid.setItems(query -> {
            var page = query.getPage();
            var pageSize = query.getPageSize();
            var springDataSort = VaadinSpringDataHelpers.toSpringDataSort(query);
            var of = PageRequest.of(page, pageSize, springDataSort);
            var stream = queryGateway.query(new GetMusicianHistory(UUID.randomUUID()), ResponseTypes.multipleInstancesOf(MusicianHistoryDto.class)).join();

            return stream.stream();
        });

        return grid;
    }
}
