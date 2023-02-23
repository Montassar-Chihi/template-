package com.bioinnovate.Template.views.main;

import com.bioinnovate.Template.backend.entities.User;
import com.bioinnovate.Template.backend.services.DiseaseService;
// import com.bioinnovate.Template.backend.services.GeneService;
import com.bioinnovate.Template.backend.services.UserService;
import com.bioinnovate.Template.utils.components.Footer;
import com.bioinnovate.Template.utils.components.SearchBar;
import com.bioinnovate.Template.views.Login.LoginView;
import com.bioinnovate.Template.views.diseases.DiseasesView;
import com.bioinnovate.Template.views.home.HomeView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The main view is a top-level placeholder for other views.
 */
@CssImport("./styles/views/main/main-view.css")
@PWA(name = "PreMediT", shortName = "PMT", enableInstallPrompt = true)
@JsModule("./styles/shared-styles.js")
public class MainView extends AppLayout {
    private Tab homeTab;
    private Tab diseasesTab;
    private final Tabs menu;
    private VaadinSession vaadinSession;


    public MainView(@Autowired UserService userService, @Autowired DiseaseService diseaseService) {

        setId("app-layout");
        menu = createMenu(userService);
        createHeaderContent(menu,diseaseService);
        UI.getCurrent().addAfterNavigationListener(event -> {
            try{
                switch (event.getLocation().getPath()) {
                    case "home":
                    case "":
                        menu.setSelectedTab(homeTab);
                        break;
                    case "diseases":
                        menu.setSelectedTab(diseasesTab);
                        break;
                    default:
                        menu.setSelectedTab(null);
                        break;
                }
            }catch(Exception ex){
               UI.getCurrent().navigate(HomeView.class);
            }
            if ((event.getLocation().getPath().equalsIgnoreCase("home")) || (event.getLocation().getPath().equalsIgnoreCase(""))
                    || (event.getLocation().getPath().equalsIgnoreCase("DataAccess"))){
                setId("app-layout");
            }else {
                setId("app-alternative-layout");
            }
        });
    }

    private Component createHeaderContent(Tabs menu,DiseaseService diseaseService) {
        VerticalLayout headerLayout = new VerticalLayout();
        headerLayout.setId("navbar");

        HorizontalLayout topHeaderLayout = new HorizontalLayout();
        topHeaderLayout.setId("header");
        topHeaderLayout.getThemeList().set("dark", false);
        topHeaderLayout.setSpacing(false);
        topHeaderLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        Image logo = new Image("/images/logo.png","logo");
        logo.setId("header-logo");
        H1 name = new H1();
        name.setText("PreMediT");
        name.setId("header-premedit");
        HorizontalLayout h = new HorizontalLayout(logo,name);
        h.setId("header-name-logo-layout");

        Image bio = new Image("/images/bioinnov8.jpg","BioInnov8 logo");
        bio.setId("header-bioinnov8");
        Image ipt = new Image("/images/logo-Institut-Pasteur-Tunis.png","IPT logo");
        ipt.setId("header-ipt");
        Div stylingDiv = new Div();
        stylingDiv.setWidthFull();
        HorizontalLayout h2 = new HorizontalLayout(bio,stylingDiv,ipt);
        h2.setId("header-logos-layout");

        SearchBar searchLayout = new SearchBar(diseaseService);
        Anchor advancedSearchAnchor = new Anchor("advancedSearch","Advanced Search");
        advancedSearchAnchor.getStyle().set("width","10rem").set("color","white").set("align-self","center");
        if (vaadinSession.getAttribute("logged") != null){
            searchLayout.add(advancedSearchAnchor);
        }

        VerticalLayout v = new VerticalLayout(h2,searchLayout);
        v.setWidth("60%");
        v.getStyle().set("margin","5px auto").set("background","transparent");

        topHeaderLayout.add(h,v);
        topHeaderLayout.setHeightFull();

        HorizontalLayout menuLayout = new HorizontalLayout();
        menuLayout.setId("menu");
        menuLayout.getThemeList().set("dark", false);
        menuLayout.setWidthFull();
        menuLayout.setSpacing(true);
        menuLayout.getStyle().set("padding","0px").set("background","transparent");
        menuLayout.add(menu);

        createLoginLogoutButtons(menuLayout);

        headerLayout.add(topHeaderLayout, menuLayout);
        return headerLayout;
    }

    private void createLoginLogoutButtons(HorizontalLayout menuLayout){
        Button login = new Button("Login");
        login.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        login.setIcon(VaadinIcon.SIGN_IN_ALT.create());
        login.setWidthFull();
        login.getStyle().set("color","white");
        login.addClickListener(event -> getUI().get().navigate(LoginView.class));
        HorizontalLayout buttons = new HorizontalLayout(login);
        buttons.setAlignItems(FlexComponent.Alignment.CENTER);
        buttons.getStyle().set("margin","auto 2rem auto auto").set(
                "background","transparent").set(
                "align-items","center").set(
                "position","relative").set(
                "right","0px").set(
                "width","12rem").set(
                "bottom","5px");

        vaadinSession = VaadinSession.getCurrent();
       menuLayout.add(buttons);

    }

    private Tabs createMenu(UserService userService) {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        homeTab = createTab("Home" , HomeView.class);
        diseasesTab = createTab("Diseases" , DiseasesView.class);
        tabs.add(homeTab);
        tabs.add(diseasesTab);

        return tabs;
    }

    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        Tab tab;
        if (text.equalsIgnoreCase("profile")){
            tab = new Tab(VaadinIcon.USER_CARD.create());
        }else{
            tab = new Tab();
        }
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;

    }

}
