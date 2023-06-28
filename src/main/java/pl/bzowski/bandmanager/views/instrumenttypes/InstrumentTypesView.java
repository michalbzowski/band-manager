package pl.bzowski.bandmanager.views.instrumenttypes;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import pl.bzowski.bandmanager.data.entity.InstrumentType;
import pl.bzowski.bandmanager.data.service.InstrumentTypeService;
import pl.bzowski.bandmanager.views.MainLayout;

@PageTitle("Instrument Types")
@Route(value = "instrument-types/:instrumentTypeID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("USER")
public class InstrumentTypesView extends Div implements BeforeEnterObserver {

    private final String INSTRUMENTTYPE_ID = "instrumentTypeID";
    private final String INSTRUMENTTYPE_EDIT_ROUTE_TEMPLATE = "instrument-types/%s/edit";

    private final Grid<InstrumentType> grid = new Grid<>(InstrumentType.class, false);

    private TextField name;
    private TextField type;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<InstrumentType> binder;

    private InstrumentType instrumentType;

    private final InstrumentTypeService instrumentTypeService;

    public InstrumentTypesView(InstrumentTypeService instrumentTypeService) {
        this.instrumentTypeService = instrumentTypeService;
        addClassNames("instrument-types-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("name").setAutoWidth(true);
        grid.addColumn("type").setAutoWidth(true);
        grid.setItems(query -> instrumentTypeService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(INSTRUMENTTYPE_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(InstrumentTypesView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(InstrumentType.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.instrumentType == null) {
                    this.instrumentType = new InstrumentType();
                }
                binder.writeBean(this.instrumentType);
                instrumentTypeService.update(this.instrumentType);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(InstrumentTypesView.class);
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
        Optional<Long> instrumentTypeId = event.getRouteParameters().get(INSTRUMENTTYPE_ID).map(Long::parseLong);
        if (instrumentTypeId.isPresent()) {
            Optional<InstrumentType> instrumentTypeFromBackend = instrumentTypeService.get(instrumentTypeId.get());
            if (instrumentTypeFromBackend.isPresent()) {
                populateForm(instrumentTypeFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested instrumentType was not found, ID = %s", instrumentTypeId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(InstrumentTypesView.class);
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
        type = new TextField("Type");
        formLayout.add(name, type);

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

    private void populateForm(InstrumentType value) {
        this.instrumentType = value;
        binder.readBean(this.instrumentType);

    }
}
