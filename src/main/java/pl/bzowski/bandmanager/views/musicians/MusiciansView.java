package pl.bzowski.bandmanager.views.musicians;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import jakarta.annotation.security.RolesAllowed;

import java.util.Optional;
import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import pl.bzowski.bandmanager.data.entity.Musician;
import pl.bzowski.bandmanager.musician.commands.CreateMusicianCommand;
import pl.bzowski.bandmanager.musician.queries.GetAllMusiciansQuery;
import pl.bzowski.bandmanager.musician.commands.UpdateMusicianCommand;
import pl.bzowski.bandmanager.views.MainLayout;

@PageTitle("Musicians")
@Route(value = "musicians/:musicianID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("ADMIN")
@Uses(Icon.class)
public class MusiciansView extends Div implements BeforeEnterObserver {

    private final String SAMPLEPERSON_ID = "musicianID";
    private final String SAMPLEPERSON_EDIT_ROUTE_TEMPLATE = "musicians/%s/edit";

    private final Grid<Musician> grid = new Grid<>(Musician.class, false);

    private TextField firstName;
    private TextField lastName;
    private TextField email;
    private TextField phone;
    private DatePicker dateOfBirth;
    private DatePicker joinDate;
    private Checkbox active;
    private TextField address;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<Musician> binder;

    private Musician musician;

    private final QueryGateway queryGateway;

    private final CommandGateway commandGateway;

    public MusiciansView(QueryGateway queryGateway, CommandGateway commandGateway) {
        this.queryGateway = queryGateway;
        this.commandGateway = commandGateway;
        addClassNames("musicians-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);
        add(splitLayout);

        // Configure Grid
        grid.addColumn("firstName").setAutoWidth(true);
        grid.addColumn("lastName").setAutoWidth(true);
        grid.addColumn("email").setAutoWidth(true);
        grid.addColumn("phone").setAutoWidth(true);
        grid.addColumn("dateOfBirth").setAutoWidth(true);
        grid.addColumn("joinDate").setAutoWidth(true);
        grid.addColumn("active").setAutoWidth(true);
        grid.addColumn("address").setAutoWidth(true);
//
        grid.setItems(query -> {
            var page = query.getPage();
            var pageSize = query.getPageSize();
            var springDataSort = VaadinSpringDataHelpers.toSpringDataSort(query);
            var of = PageRequest.of(page, pageSize, springDataSort);
            var stream = queryGateway.query(new GetAllMusiciansQuery(), ResponseTypes.multipleInstancesOf(Musician.class)).join();

            return stream.stream();
        });

//        grid.setItems(query -> queryGateway.query(new GetAllMusicicansQuery(PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query))), ResponseTypes.multipleInstancesOf(Musician.class)).join().stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(SAMPLEPERSON_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(MusiciansView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Musician.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.musician == null) {
                    this.musician = new Musician();
                }
                binder.writeBean(this.musician);
                if(this.musician.getId() == null) {
                    commandGateway.send(new CreateMusicianCommand(UUID.randomUUID(),
                            this.musician.getFirstName(),
                            this.musician.getLastName(),
                            this.musician.getEmail(),
                            this.musician.getPhone(),
                            this.musician.getDateOfBirth(),
                            this.musician.getJoinDate(),
                            this.musician.getActive(),
                            this.musician.getAddress())).join();
                } else {
                    commandGateway.send(new UpdateMusicianCommand(this.musician.getId(),
                            this.musician.getFirstName(),
                            this.musician.getLastName(),
                            this.musician.getEmail(),
                            this.musician.getPhone(),
                            this.musician.getDateOfBirth(),
                            this.musician.getJoinDate(),
                            this.musician.getActive(),
                            this.musician.getAddress())).join();
                }
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(MusiciansView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> musicianId = event.getRouteParameters().get(SAMPLEPERSON_ID).map(UUID::fromString);
        if (musicianId.isPresent()) {
            Optional<Musician> musicianFromBackend = queryGateway.query(new GetOneMusician(musicianId.get()), ResponseTypes.optionalInstanceOf(Musician.class)).join();
            if (musicianFromBackend.isPresent()) {
                populateForm(musicianFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested musician was not found, ID = %s", musicianId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(MusiciansView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        firstName = new TextField("First Name");
        lastName = new TextField("Last Name");
        email = new TextField("Email");
        phone = new TextField("Phone");
        dateOfBirth = new DatePicker("Date Of Birth");
        joinDate = new DatePicker("Join Date");
        active = new Checkbox("Active");
        address = new TextField("Address");
        formLayout.add(firstName, lastName, email, phone, dateOfBirth, joinDate, active, address);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Musician value) {
        this.musician = value;
        binder.readBean(this.musician);

    }
}
