package com.bioinnovate.PreMediT.views.diseases;

import com.bioinnovate.PreMediT.backend.entities.Disease;
import com.bioinnovate.PreMediT.backend.entities.Gene;
import com.bioinnovate.PreMediT.backend.entities.User;
import com.bioinnovate.PreMediT.backend.services.DiseaseService;
import com.bioinnovate.PreMediT.backend.services.GeneService;
import com.bioinnovate.PreMediT.backend.services.UserService;
import com.bioinnovate.PreMediT.utils.security.AuthorizedOnlyView;
import com.bioinnovate.PreMediT.utils.security.Role;
import com.bioinnovate.PreMediT.views.main.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.klaudeta.PaginatedGrid;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.bioinnovate.PreMediT.views.diseases.diseaseUtils.DiseasesDisconnectedView.buildDiseasesUIWhenDisconnected;

@Route(value = "diseases", layout = MainView.class)
@PageTitle("Diseases")
@CssImport("./styles/views/diseases/diseases-view.css")
@Secured({"ADMIN","USER"})
public class DiseasesView extends Div {
    private ComboBox<String> genomicLocationFilter;
    private IntegerField omimIdFilter;
    private ComboBox<String> inheritanceFilter;
    private HorizontalLayout layout;
    private VerticalLayout mainLayout;
    private VaadinSession vaadinSession;
    private User user;
    /*private ComboBox<Gene> geneFilter;*/
    private ComboBox<String> categoryFilter;
    private TextField searchBar;
    private PaginatedGrid<Disease> grid ;
    private List<Disease> listDiseases;

    public DiseasesView(@Autowired UserService userService, @Autowired DiseaseService diseaseService/*, @Autowired GeneService geneService*/) {

        buildLoggedInView(diseaseService, geneService);
    }
    public void buildLoggedInView(@Autowired DiseaseService diseaseService/*, @Autowired GeneService geneService*/){

        user = getUser();H1 name = new H1();
        name.setText("PreMediT: Disease Database");
        name.setId("diseases-view-title");

        Image logo = new Image("/images/logo.png","logo");
        logo.setId("diseases-view-logo");
        Div stylingDiv = new Div();
        stylingDiv.setWidthFull();
        HorizontalLayout hLayout = new HorizontalLayout(name,stylingDiv,logo);
        hLayout.setId("diseases-view-hLayout");

        searchBar = new TextField();
        searchBar.setPlaceholder("Search for disease using its name or symbol ...");
        searchBar.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchBar.setClearButtonVisible(true);
        searchBar.setValueChangeMode(ValueChangeMode.EAGER);
        searchBar.addValueChangeListener(e -> updateListByFilters(diseaseService));
        searchBar.setWidthFull();

        categoryFilter = new ComboBox<>();
        categoryFilter.setAllowCustomValue(true);
        List<String> categories = diseaseService.findCategories();
        List<String> classifications = new ArrayList<>();
        for (String category:categories){
            classifications.addAll(List.of(category.split(" \\|\\$ ")));
        }
        Set<String> set = new HashSet<>(classifications);
        classifications.clear();
        classifications.addAll(set);
        categoryFilter.setItems(classifications);
        categoryFilter.setPlaceholder("Filter by category ...");
        categoryFilter.setClearButtonVisible(true);
        categoryFilter.addValueChangeListener(e -> updateListByFilters(diseaseService));
        categoryFilter.setWidth("50%");

//        geneFilter = new ComboBox<>();
//        geneFilter.setAllowCustomValue(true);
//        geneFilter.setItems(geneService.findAll());
//        geneFilter.setItemLabelGenerator(Gene::getName);
//        geneFilter.setPlaceholder("Filter by gene ...");
//        geneFilter.setClearButtonVisible(true);
//        geneFilter.addValueChangeListener(e -> updateListByFilters(diseaseService));
//        geneFilter.setWidth("50%");

        HorizontalLayout otherFilters = new HorizontalLayout(categoryFilter/*,geneFilter*/);
        otherFilters.setWidthFull();

        inheritanceFilter = new ComboBox<>();
        inheritanceFilter.setAllowCustomValue(true);
        inheritanceFilter.setItems(diseaseService.findInheritances());
        inheritanceFilter.setPlaceholder("Filter by inheritance ...");
        inheritanceFilter.setClearButtonVisible(true);
        inheritanceFilter.addValueChangeListener(e -> updateListByFilters(diseaseService));
        inheritanceFilter.setWidth("50%");

        genomicLocationFilter = new ComboBox<>();
        genomicLocationFilter.setAllowCustomValue(true);
        genomicLocationFilter.setItems(diseaseService.findGenomicLocations());
        genomicLocationFilter.setPlaceholder("Filter by genomic location ...");
        genomicLocationFilter.setClearButtonVisible(true);
        genomicLocationFilter.addValueChangeListener(e -> updateListByFilters(diseaseService));
        genomicLocationFilter.setWidth("50%");

        HorizontalLayout otherFilters1 = new HorizontalLayout(inheritanceFilter,genomicLocationFilter);
        otherFilters1.setWidthFull();

        omimIdFilter = new IntegerField();
        omimIdFilter.setPlaceholder("Search using OMIM id ...");
        omimIdFilter.setClearButtonVisible(true);
        omimIdFilter.addValueChangeListener(e -> updateListByFilters(diseaseService));
        omimIdFilter.setWidth("50%");

        HorizontalLayout otherFilters2 = new HorizontalLayout(omimIdFilter);
        otherFilters2.setWidthFull();

        VerticalLayout allFilters = new VerticalLayout(otherFilters,otherFilters1,otherFilters2);
        allFilters.setPadding(false);
        allFilters.setWidthFull();
        allFilters.setVisible(false);

        Button showFilters = new Button(VaadinIcon.LIST.create());
        showFilters.addClickListener(event -> allFilters.setVisible(!allFilters.isVisible()));
        showFilters.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        showFilters.setId("diseases-view-showFilters-button");
        HorizontalLayout searchBarLayout = new HorizontalLayout(searchBar,showFilters);
        searchBarLayout.setWidthFull();

        mainLayout = new VerticalLayout(hLayout,searchBarLayout,allFilters);
        addGrid(diseaseService);
        mainLayout.setWidthFull();
        layout = new HorizontalLayout(mainLayout);
        layout.setId("diseases-view-grid-layout");
        add(layout);
    }

    private void addGrid(DiseaseService diseaseService){
        listDiseases = diseaseService.findAll();
        vaadinSession = VaadinSession.getCurrent();

        grid = new PaginatedGrid<>(Disease.class);
        grid.setPageSize(20);
        grid.removeAllColumns();
        grid.addColumn("diseaseName");
        grid.addColumn(new ComponentRenderer<>(disease -> new Span(disease.getClassification().replaceAll("\\|\\$"," , ")))).setHeader("Classification");

        grid.setItems(listDiseases);
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                Disease diseaseFromBackend = event.getValue();
                UI.getCurrent().navigate("disease/"+diseaseFromBackend.getOmimID());
            }
        });
        mainLayout.add(grid);
    }
    private void updateListByFilters(DiseaseService diseaseService) {
        List listByCategory ;
        /*List listByGene ;*/
        List listByName ;
        List listByOmimID;
        List listByInheritance;
        List listByGenomicLocation;
        try{
            listByCategory = diseaseService.searchByClassification(categoryFilter.getValue());
            if (categoryFilter.getValue() == null){
                listByCategory = diseaseService.findAll();
            }
        }catch (Exception e){listByCategory = diseaseService.findAll();}
        try {
            listByName = diseaseService.searchByExpression(searchBar.getValue());
            if (searchBar.getValue() == null){
                listByName = diseaseService.findAll();
            }
        }catch (Exception e){
            listByName = diseaseService.findAll();}
        /*try {
            listByGene = diseaseService.searchByGene(geneFilter.getValue());
            if (geneFilter.getValue() == null){
                listByGene = diseaseService.findAll();
            }
        }catch (Exception e){listByGene = diseaseService.findAll();}*/
        try{
            listByOmimID = diseaseService.searchByOmimID(omimIdFilter.getValue());
            if (omimIdFilter.getValue() == null){
                listByOmimID = diseaseService.findAll();
            }
        }catch (Exception e){listByOmimID = diseaseService.findAll();}
        try {
            listByInheritance = diseaseService.searchByInheritance(inheritanceFilter.getValue());
            if (inheritanceFilter.getValue() == null){
                listByInheritance = diseaseService.findAll();
            }
        }catch (Exception e){listByInheritance = diseaseService.findAll();}
        try {
            listByGenomicLocation = diseaseService.searchByGenomicLocation(genomicLocationFilter.getValue());
            if (genomicLocationFilter.getValue() == null){
                listByGenomicLocation = diseaseService.findAll();
            }
        }catch (Exception e){listByGenomicLocation = diseaseService.findAll();}

        List<Disease> finalList = intersection(listByCategory, listByName,listByOmimID,listByOmimID,listByInheritance,listByGenomicLocation);
        grid.setItems(finalList);
    }
    private List<Disease> intersection(List<Disease> list1, List<Disease> list2, List<Disease> list3,
                                      List<Disease> list4,List<Disease> list5,List<Disease> list6) {
        List<Disease> list = new ArrayList<>();
        for (Disease disease : list1) {
            if(list2.contains(disease) && list3.contains(disease) && list4.contains(disease) && list5.contains(disease) && list6.contains(disease)) {
                list.add(disease);
            }
        }
        return list;
    }

}
