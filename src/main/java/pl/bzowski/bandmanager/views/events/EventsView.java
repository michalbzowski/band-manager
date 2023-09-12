package pl.bzowski.bandmanager.views.events;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import jakarta.annotation.security.RolesAllowed;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import pl.bzowski.bandmanager.data.entity.MusicEvent;
import pl.bzowski.bandmanager.musicevent.commands.CreateMusicEventCommand;
import pl.bzowski.bandmanager.musicevent.queries.GetAllMusicEventsQuery;
import pl.bzowski.bandmanager.musicevent.queries.GetMusicEventQuery;
import pl.bzowski.bandmanager.musicevent.commands.UpdateMusicEventCommand;
import pl.bzowski.bandmanager.views.MainLayout;

@PageTitle("Events")
@Route(value = "events/:eventID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("USER")
public class EventsView extends Div implements BeforeEnterObserver {

    private final String EVENT_ID = "eventID";
    private final String EVENT_EDIT_ROUTE_TEMPLATE = "events/%s/edit";

    private final Grid<MusicEvent> grid = new Grid<>(MusicEvent.class, false);
    private final QueryGateway queryGateway;
    private final CommandGateway commandGateway;

    private TextField name;
    private TextField address;
    private DateTimePicker dateTime;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<MusicEvent> binder;

    private MusicEvent event;

    public EventsView(QueryGateway queryGateway, CommandGateway commandGateway) {
        this.queryGateway = queryGateway;
        this.commandGateway = commandGateway;
        addClassNames("events-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("name").setAutoWidth(true);
        grid.addColumn("address").setAutoWidth(true);
        grid.addColumn("dateTime").setAutoWidth(true);

        grid.setItems(query -> {
            var page = query.getPage();
            var pageSize = query.getPageSize();
            var springDataSort = VaadinSpringDataHelpers.toSpringDataSort(query);
            var of = PageRequest.of(page, pageSize, springDataSort);
            var stream = queryGateway.query(new GetAllMusicEventsQuery(), ResponseTypes.multipleInstancesOf(MusicEvent.class)).join();
            return stream.stream();
        });
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(EVENT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(EventsView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(MusicEvent.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.event == null) {
                    this.event = new MusicEvent();
                }
                binder.writeBean(this.event);
                if (this.event.getId() == null) {
                    commandGateway.send(new CreateMusicEventCommand(
                            UUID.randomUUID(),
                            event.getName(),
                            event.getAddress(),
                            event.getDateTime()
                            )
                    ).join();
                } else {
                    commandGateway.send(new UpdateMusicEventCommand(
                            event.getId(),
                            event.getName(),
                            event.getAddress(),
                            event.getDateTime()
                            )
                    ).join();
                }
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(EventsView.class);
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
        Optional<UUID> eventId = event.getRouteParameters().get(EVENT_ID).map(UUID::fromString);
        if (eventId.isPresent()) {
            Optional<MusicEvent> eventFromBackend = queryGateway.query(new GetMusicEventQuery(eventId.get()), ResponseTypes.optionalInstanceOf(MusicEvent.class)).join();
            if (eventFromBackend.isPresent()) {
                populateForm(eventFromBackend.get());
            } else {
                Notification.show(String.format("The requested event was not found, ID = %s", eventId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(EventsView.class);
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
        name = new TextField("Name");
        address = new TextField("Address");
        dateTime = new DateTimePicker("Date Time");
        dateTime.setStep(Duration.ofMinutes(1));
        formLayout.add(name, address, dateTime);

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

    private void populateForm(MusicEvent value) {
        this.event = value;
        binder.readBean(this.event);
    }
}
