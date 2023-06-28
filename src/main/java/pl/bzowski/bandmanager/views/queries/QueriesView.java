package pl.bzowski.bandmanager.views.queries;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import pl.bzowski.bandmanager.views.MainLayout;

/**
 * A Designer generated component for the stub-tag template.
 *
 * Designer will add and remove fields with @Id mappings but does not overwrite
 * or otherwise change this file.
 */
@PageTitle("Queries")
@Route(value = "queries", layout = MainLayout.class)
@RolesAllowed("USER")
@Tag("queries-view")
@JsModule("./views/queries/queries-view.ts")
public class QueriesView extends LitTemplate {

    @Id
    private TextField name;

    @Id
    private Button sayHello;

    public QueriesView() {
        addClassName("block");
        sayHello.addClickListener(e -> {
            Notification.show("Hello " + name.getValue());
        });
    }
}
