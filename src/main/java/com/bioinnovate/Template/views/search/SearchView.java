package com.bioinnovate.Template.views.search;

import com.bioinnovate.Template.backend.entities.Disease;
import com.bioinnovate.Template.backend.entities.Gene;
import com.bioinnovate.Template.backend.entities.User;
import com.bioinnovate.Template.backend.services.DiseaseService;
// import com.bioinnovate.Template.backend.services.GeneService;
import com.bioinnovate.Template.backend.services.UserService;
import com.bioinnovate.Template.utils.components.SearchBar;

import com.bioinnovate.Template.views.diseases.diseaseUtils.MakeDiseasesList;
import com.bioinnovate.Template.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.bioinnovate.Template.views.search.SearchUtils.SearchEngine.searchList;

@Route(value = "Search", layout = MainView.class)
@PageTitle("Search Results")
@CssImport("./styles/views/main/main-view.css")
public class SearchView extends Div {

    private VaadinSession session;
    private User user;
    private String searchTerm;
    private String searchField;

    public SearchView(@Autowired DiseaseService diseaseService, @Autowired UserService userService){
        buildLoggedInView(diseaseService, userService);
        
    }

    public void buildLoggedInView(@Autowired DiseaseService diseaseService, @Autowired UserService userService){
        setId("view-special-container");
        try{
            session = VaadinSession.getCurrent();
            user = userService.findByEmail(VaadinSession.getCurrent().getAttribute("user").toString());
            searchTerm = session.getAttribute("searchTerm").toString();
            searchField = session.getAttribute("searchField").toString();

            getStyle().set("padding","0").set("margin","0").set("margin-top","1rem").set("width","100%");
            VerticalLayout searchResultView = new VerticalLayout();
            searchResultView.setWidth("90%");
            searchResultView.getStyle().set("margin","1rem auto");
            HorizontalLayout wholeView = new HorizontalLayout(searchResultView);
            wholeView.setSizeFull();
            add(wholeView);

            if (searchField.equalsIgnoreCase("Diseases")) {
                List<Disease> diseasesList = searchList(searchTerm, searchField, diseaseService);
                H1 bigTitle = new H1("Search Results ("+diseasesList.size()+" diseases found) :");
                bigTitle.getStyle().set("align-self","center").set(
                        "color","#cb3434").set(
                        "font-family","Eczar").set(
                        "font-size","30px").set(
                        "text-align","center").set("margin-right","15rem");
                searchResultView.add(bigTitle);
                searchResultView.add(addDiseasesList(diseasesList,userService));
            }

        }catch (Exception exception){
            exception.printStackTrace();
            add(buildUIWhenAccessingWithoutSearching(diseaseService));

        }
    }
    private VerticalLayout buildUIWhenAccessingWithoutSearching(DiseaseService diseaseService){
        H1 name = new H1();
        name.setText("PreMediT");
        name.getStyle().set("align-self","center").set(
                "color","#cb3434").set(
                "font-family","Eczar").set(
                "font-size","2.6rem").set(
                "text-align","center");

        Image logo = new Image("/images/logo.png","logo");
        logo.getStyle().set("max-width","95px").set("align-self","center").set("width","20%").set("margin","10px auto");
        Div stylingDiv = new Div();
        stylingDiv.setWidthFull();
        HorizontalLayout hLayout = new HorizontalLayout(name,stylingDiv,logo);
        hLayout.setWidthFull();
        hLayout.setHeight("100px");

        Span description = new Span("PreMediT is an attempt to collate all known (published) gene lesions responsible for human inherited disease in Tunisia (highly curated database by experts in the field).\n" +
                "This platform gives access to the latest articles related to human genome mutation and genetic diseases \n" +
                "as well as statistics and data analysis to spread awareness about these diseases in Tunisia.\n" +
                "PreMediT is an intelligent platform to help doctors with diagnostics and decision-making process, also diet propositions extracted from peer reviewed articles and predictions through our algorithms\n" +
                "It is also an intelligent solution for drug repurposing and discovery for different mutation (for pharma companies) \n" +
                "The purpose of PreMediT is to empower life science and healthcare professionals with effective and faster bioinformatics solutions to turn their complex biological datasets into actionable insights in the medical and research fields. \n");
        description.getStyle().set("width","100%").set("font-size","1rem").set("text-align","justify").set("margin","10px auto");

        SearchBar searchLayout = new SearchBar(diseaseService);

        VerticalLayout mainLayout = new VerticalLayout(hLayout,description,searchLayout);
        mainLayout.setWidth("80%");
        mainLayout.getStyle().set("margin","1rem 15% 1rem 5%");
        mainLayout.setPadding(true);
        return mainLayout;
    }


    private ListBox<Disease> addDiseasesList(List<Disease> diseasesList,UserService userService){
        ListBox<Disease> diseases = new MakeDiseasesList(user,diseasesList, userService);
        diseases.setItems(diseasesList);
        return diseases;
    }
}
