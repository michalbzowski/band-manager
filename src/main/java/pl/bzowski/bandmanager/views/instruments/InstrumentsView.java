package pl.bzowski.bandmanager.views.instruments;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import jakarta.annotation.security.RolesAllowed;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import pl.bzowski.bandmanager.data.entity.Instrument;
import pl.bzowski.bandmanager.data.service.InstrumentService;
import pl.bzowski.bandmanager.views.MainLayout;

@PageTitle("Instruments")
@Route(value = "instruments/:instrumentID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("USER")
@Uses(Icon.class)
@Uses(Icon.class)
public class InstrumentsView extends Div implements BeforeEnterObserver {

    private final String INSTRUMENT_ID = "instrumentID";
    private final String INSTRUMENT_EDIT_ROUTE_TEMPLATE = "instruments/%s/edit";

    private final Grid<Instrument> grid = new Grid<>(Instrument.class, false);

    private TextField type;
    private TextField brand;
    private TextField model;
    private TextField condition;
    private TextField description;
    private Checkbox toLearn;
    private Checkbox toFix;
    private TextField owner;
    private TextField tenant;
    private Upload picture;
    private Image picturePreview;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<Instrument> binder;

    private Instrument instrument;

    private final InstrumentService instrumentService;

    public InstrumentsView(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
        addClassNames("instruments-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("type").setAutoWidth(true);
        grid.addColumn("brand").setAutoWidth(true);
        grid.addColumn("model").setAutoWidth(true);
        grid.addColumn("condition").setAutoWidth(true);
        grid.addColumn("description").setAutoWidth(true);
        LitRenderer<Instrument> toLearnRenderer = LitRenderer.<Instrument>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", toLearn -> toLearn.isToLearn() ? "check" : "minus").withProperty("color",
                        toLearn -> toLearn.isToLearn()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(toLearnRenderer).setHeader("To Learn").setAutoWidth(true);

        LitRenderer<Instrument> toFixRenderer = LitRenderer.<Instrument>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", toFix -> toFix.isToFix() ? "check" : "minus").withProperty("color",
                        toFix -> toFix.isToFix()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(toFixRenderer).setHeader("To Fix").setAutoWidth(true);

        grid.addColumn("owner").setAutoWidth(true);
        grid.addColumn("tenant").setAutoWidth(true);
        LitRenderer<Instrument> pictureRenderer = LitRenderer
                .<Instrument>of("<img style='height: 64px' src=${item.picture} />").withProperty("picture", item -> {
                    if (item != null && item.getPicture() != null) {
                        return "data:image;base64," + Base64.getEncoder().encodeToString(item.getPicture());
                    } else {
                        return "";
                    }
                });
        grid.addColumn(pictureRenderer).setHeader("Picture").setWidth("68px").setFlexGrow(0);

        grid.setItems(query -> instrumentService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(INSTRUMENT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(InstrumentsView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Instrument.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        attachImageUpload(picture, picturePreview);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.instrument == null) {
                    this.instrument = new Instrument();
                }
                binder.writeBean(this.instrument);
                instrumentService.update(this.instrument);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(InstrumentsView.class);
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
        Optional<Long> instrumentId = event.getRouteParameters().get(INSTRUMENT_ID).map(Long::parseLong);
        if (instrumentId.isPresent()) {
            Optional<Instrument> instrumentFromBackend = instrumentService.get(instrumentId.get());
            if (instrumentFromBackend.isPresent()) {
                populateForm(instrumentFromBackend.get());
            } else {
                Notification.show(String.format("The requested instrument was not found, ID = %s", instrumentId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(InstrumentsView.class);
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
        type = new TextField("Type");
        brand = new TextField("Brand");
        model = new TextField("Model");
        condition = new TextField("Condition");
        description = new TextField("Description");
        toLearn = new Checkbox("To Learn");
        toFix = new Checkbox("To Fix");
        owner = new TextField("Owner");
        tenant = new TextField("Tenant");
        Label pictureLabel = new Label("Picture");
        picturePreview = new Image();
        picturePreview.setWidth("100%");
        picture = new Upload();
        picture.getStyle().set("box-sizing", "border-box");
        picture.getElement().appendChild(picturePreview.getElement());
        formLayout.add(type, brand, model, condition, description, toLearn, toFix, owner, tenant, pictureLabel,
                picture);

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

    private void attachImageUpload(Upload upload, Image preview) {
        ByteArrayOutputStream uploadBuffer = new ByteArrayOutputStream();
        upload.setAcceptedFileTypes("image/*");
        upload.setReceiver((fileName, mimeType) -> {
            uploadBuffer.reset();
            return uploadBuffer;
        });
        upload.addSucceededListener(e -> {
            StreamResource resource = new StreamResource(e.getFileName(),
                    () -> new ByteArrayInputStream(uploadBuffer.toByteArray()));
            preview.setSrc(resource);
            preview.setVisible(true);
            if (this.instrument == null) {
                this.instrument = new Instrument();
            }
            this.instrument.setPicture(uploadBuffer.toByteArray());
        });
        preview.setVisible(false);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Instrument value) {
        this.instrument = value;
        binder.readBean(this.instrument);
        this.picturePreview.setVisible(value != null);
        if (value == null || value.getPicture() == null) {
            this.picture.clearFileList();
            this.picturePreview.setSrc("");
        } else {
            this.picturePreview.setSrc("data:image;base64," + Base64.getEncoder().encodeToString(value.getPicture()));
        }

    }
}
