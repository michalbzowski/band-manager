package pl.bzowski.bandmanager.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;
import java.io.ByteArrayInputStream;
import java.util.Optional;
import org.vaadin.lineawesome.LineAwesomeIcon;
import pl.bzowski.bandmanager.data.entity.User;
import pl.bzowski.bandmanager.security.AuthenticatedUser;
import pl.bzowski.bandmanager.views.aboutme.AboutMeView;
import pl.bzowski.bandmanager.views.events.EventsView;
import pl.bzowski.bandmanager.views.instrumentcompanies.InstrumentCompaniesView;
import pl.bzowski.bandmanager.views.instrumentowners.InstrumentOwnersView;
import pl.bzowski.bandmanager.views.instruments.InstrumentsView;
import pl.bzowski.bandmanager.views.instrumenttypes.InstrumentTypesView;
import pl.bzowski.bandmanager.views.musicianform.MusicianFormView;
import pl.bzowski.bandmanager.views.musicians.MusiciansView;
import pl.bzowski.bandmanager.views.presence.PresenceView;
import pl.bzowski.bandmanager.views.queries.QueriesView;
import pl.bzowski.bandmanager.views.uniformparts.UniformPartsView;
import pl.bzowski.bandmanager.views.uniforms.UniformsView;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H2 viewTitle;

    private AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("Band Manager");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        if (accessChecker.hasAccess(PresenceView.class)) {
            nav.addItem(new SideNavItem("Presence", PresenceView.class, LineAwesomeIcon.CHECK_SQUARE_SOLID.create()));

        }
        if (accessChecker.hasAccess(MusiciansView.class)) {
            nav.addItem(new SideNavItem("Musicians", MusiciansView.class, LineAwesomeIcon.PERSON_BOOTH_SOLID.create()));

        }
        if (accessChecker.hasAccess(MusicianFormView.class)) {
            nav.addItem(new SideNavItem("Musician Form", MusicianFormView.class, LineAwesomeIcon.USER.create()));

        }
        if (accessChecker.hasAccess(EventsView.class)) {
            nav.addItem(new SideNavItem("Events", EventsView.class, LineAwesomeIcon.WOLF_PACK_BATTALION.create()));

        }
        if (accessChecker.hasAccess(InstrumentOwnersView.class)) {
            nav.addItem(new SideNavItem("Instrument Owners", InstrumentOwnersView.class,
                    LineAwesomeIcon.COLUMNS_SOLID.create()));

        }
        if (accessChecker.hasAccess(InstrumentTypesView.class)) {
            nav.addItem(new SideNavItem("Instrument Types", InstrumentTypesView.class,
                    LineAwesomeIcon.COLUMNS_SOLID.create()));

        }
        if (accessChecker.hasAccess(InstrumentCompaniesView.class)) {
            nav.addItem(new SideNavItem("Instrument Companies", InstrumentCompaniesView.class,
                    LineAwesomeIcon.COLUMNS_SOLID.create()));

        }
        if (accessChecker.hasAccess(InstrumentsView.class)) {
            nav.addItem(new SideNavItem("Instruments", InstrumentsView.class, LineAwesomeIcon.COLUMNS_SOLID.create()));

        }
        if (accessChecker.hasAccess(AboutMeView.class)) {
            nav.addItem(new SideNavItem("About Me", AboutMeView.class, LineAwesomeIcon.USER.create()));

        }
        if (accessChecker.hasAccess(UniformPartsView.class)) {
            nav.addItem(
                    new SideNavItem("Uniform Parts", UniformPartsView.class, LineAwesomeIcon.COLUMNS_SOLID.create()));

        }
        if (accessChecker.hasAccess(UniformsView.class)) {
            nav.addItem(new SideNavItem("Uniforms", UniformsView.class, LineAwesomeIcon.COLUMNS_SOLID.create()));

        }
        if (accessChecker.hasAccess(QueriesView.class)) {
            nav.addItem(new SideNavItem("Queries", QueriesView.class, LineAwesomeIcon.GLOBE_SOLID.create()));

        }

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();

            Avatar avatar = new Avatar(user.getName());
            StreamResource resource = new StreamResource("profile-pic",
                    () -> new ByteArrayInputStream(user.getProfilePicture()));
            avatar.setImageResource(resource);
            avatar.setThemeName("xsmall");
            avatar.getElement().setAttribute("tabindex", "-1");

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(avatar);
            div.add(user.getName());
            div.add(new Icon("lumo", "dropdown"));
            div.getElement().getStyle().set("display", "flex");
            div.getElement().getStyle().set("align-items", "center");
            div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
            userName.add(div);
            userName.getSubMenu().addItem("Sign out", e -> {
                authenticatedUser.logout();
            });

            layout.add(userMenu);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
