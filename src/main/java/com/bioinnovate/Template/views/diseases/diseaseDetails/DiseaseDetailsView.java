package com.bioinnovate.Template.views.diseases.diseaseDetails;

import com.bioinnovate.Template.backend.entities.*;
import com.bioinnovate.Template.backend.services.DiseaseService;
import com.bioinnovate.Template.backend.services.UserService;

import com.bioinnovate.Template.views.diseases.DiseasesView;
import com.bioinnovate.Template.views.main.MainView;
import com.vaadin.componentfactory.Autocomplete;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.klaudeta.PaginatedGrid;

import java.util.ArrayList;
import java.util.List;

@Route(value = "disease", layout = MainView.class)
@PageTitle("Disease Details")
@CssImport("./styles/views/diseases/diseases-view.css")
public class DiseaseDetailsView extends Div implements HasUrlParameter<String> {

    private Autocomplete searchBar;
    private User user;
    private Disease disease;
    private UserService userService;
    private DiseaseService diseaseService;
    /*private GeneService geneService;*/

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        try{
            Integer diseaseId = Integer.parseInt(parameter);
            this.disease = diseaseService.searchByOmimID(diseaseId).get(0);
            createUI(disease, userService, diseaseService/*, geneService, mutationService*/);
        }catch (Exception e){
            UI.getCurrent().navigate("diseases");
            UI.getCurrent().getPage().reload();
        }
    }

    @Autowired
    public DiseaseDetailsView(@Autowired UserService userService, @Autowired DiseaseService diseaseService) {
        buildLoggedInView(diseaseService,userService);
    }
    public void buildLoggedInView(@Autowired DiseaseService diseaseService, @Autowired UserService userService/*,@Autowired MutationService mutationService, @Autowired GeneService geneService*/){

        user = userService.findByEmail(VaadinSession.getCurrent().getAttribute("user").toString());
        setId("view-special-container");
        this.userService = userService;
        this.diseaseService = diseaseService;
//        this.geneService = geneService
    }
    private void createUI(Disease disease, @Autowired UserService userService, @Autowired DiseaseService diseaseService/*, @Autowired GeneService geneService, @Autowired MutationService mutationService*/){
        getStyle().set("background","transparent").set("width","80%").set("margin","2rem 15% 10px 5%");
        H1 name = new H1();
        name.setText("PreMediT: Disease Database");
        name.setId("diseases-view-title");
        Image logo = new Image("/images/logo.png","logo");
        logo.setId("diseases-detailsView-logo");
        Div stylingDiv = new Div();
        stylingDiv.setWidthFull();
        HorizontalLayout hLayout = new HorizontalLayout(name,stylingDiv,logo);
        hLayout.setId("diseases-view-hLayout");

        searchBar = new Autocomplete();
        searchBar.addChangeListener(event -> searchBar.setOptions(findOptions(searchBar.getValue(),diseaseService)));
        searchBar.setWidthFull();
        searchBar.setPlaceholder("Search for disease...");
        Button searchButton = new Button(VaadinIcon.SEARCH.create());
        searchButton.setText("Search");
        searchButton.setId("diseases-view-search-button");
        searchButton.addClickListener(event -> {
            Disease newDisease = diseaseService.searchByExpression(searchBar.getValue()).get(0);
            UI.getCurrent().navigate("disease/"+newDisease.getOmimID());
        });
        Button backToDiseasesView = new Button(VaadinIcon.ARROW_BACKWARD.create());
        backToDiseasesView.setId("diseases-view-backToPrincipleView-button");
        backToDiseasesView.addClickListener(event -> UI.getCurrent().navigate(DiseasesView.class));

        HorizontalLayout searchLayout = new HorizontalLayout(backToDiseasesView,searchBar,searchButton);
        searchLayout.setWidthFull();

        add(hLayout,searchLayout);


        H1 diseaseName = new H1(disease.getDiseaseName());
        diseaseName.setId("diseases-view-disease-name");
        Button addDisease = new Button();
        addDisease.setWidth("20rem");
        addDisease.setText("Add to My List");
        addDisease.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        addDisease.setIcon(VaadinIcon.PLUS.create());
        Button removeDisease = new Button();
        removeDisease.setWidth("20rem");
        removeDisease.setText("Remove From My List");
        removeDisease.addThemeVariants(ButtonVariant.LUMO_ERROR);
        removeDisease.setIcon(VaadinIcon.TRASH.create());
        List<Disease> userList = user.getDiseases();
        if (userList.contains(disease)){
            addDisease.setVisible(false);
            removeDisease.setVisible(true);
        }else{
            addDisease.setVisible(true);
            removeDisease.setVisible(false);
        }
        addDisease.addClickListener(event -> {
            try{
                userList.add(disease);
                user.setDiseases(userList);
                userService.update(user);
                addDisease.setVisible(false);
                removeDisease.setVisible(true);
            }catch (Exception e){
                Notification.show("There was an error adding from list, please try again later").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        removeDisease.addClickListener(event -> {
            try{
                userList.remove(disease);
                user.setDiseases(userList);
                userService.update(user);
                addDisease.setVisible(true);
                removeDisease.setVisible(false);
            }catch (Exception e){
                Notification.show("There was an error removing from list, please try again later").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });


        HorizontalLayout nameLayout = new HorizontalLayout(diseaseName);
        nameLayout.setPadding(false);
        nameLayout.setMargin(false);
        nameLayout.setSpacing(false);
        nameLayout.setWidth("100%");
        nameLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout horizontalLayout = new HorizontalLayout(nameLayout, addDisease, removeDisease);
        horizontalLayout.setSpacing(true);
        horizontalLayout.setWidthFull();
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        add(horizontalLayout);

        Anchor aliasesAndClassificationsLink = new Anchor("disease/"+disease.getOmimID()+"#aliasesAndClassifications","Aliases And Classifications");
        aliasesAndClassificationsLink. setClassName("disease-view-internal-link");
        Anchor summaryLink = new Anchor("disease/"+disease.getOmimID()+"#summary","Summary");
        summaryLink. setClassName("disease-view-internal-link");
        Anchor symptomsLink = new Anchor("disease/"+disease.getOmimID()+"#symptoms","Symptoms");
        symptomsLink. setClassName("disease-view-internal-link");
//        Anchor genesLink = new Anchor("disease/"+disease.getOmimID()+"#genes","Genes");
//        genesLink. setClassName("disease-view-internal-link");
//        Anchor mutationsLink = new Anchor("disease/"+disease.getOmimID()+"#mutations","Mutations");
//        mutationsLink. setClassName("disease-view-internal-link");
        Anchor referencesLink = new Anchor("disease/"+disease.getOmimID()+"#references","References");
        referencesLink. setClassName("disease-view-internal-link");
        Anchor externalLinksLink = new Anchor("disease/"+disease.getOmimID()+"#externalLinks","External Links");
        externalLinksLink. setClassName("disease-view-internal-link");


        HorizontalLayout links = new HorizontalLayout(aliasesAndClassificationsLink,summaryLink,symptomsLink,referencesLink,externalLinksLink);
        links.setWidthFull();
        links.setAlignItems(FlexComponent.Alignment.START);
        add(links);

        addAliasesAndClassifications();
        addSummary();
        addSymptoms();
//        addGenes(userService,diseaseService,geneService,mutationService);
//        addMutations(userService,diseaseService,geneService,mutationService);
        addReferences();
        addExternalLinks();


    }

    private void addAliasesAndClassifications(){
        VerticalLayout aliasesAndClassifications = new VerticalLayout();
        aliasesAndClassifications.setId("aliasesAndClassifications");
        aliasesAndClassifications.setPadding(false);

        H1 aliasesAndClassificationsTitle = new H1("Aliases, Inheritance and Classifications");
        aliasesAndClassificationsTitle.setClassName("disease-view-sections-titles");
        aliasesAndClassifications.add(aliasesAndClassificationsTitle);
        H4 externalIds = new H4("External Ids");
        aliasesAndClassifications.add(externalIds);
        aliasesAndClassifications.add(new Span("OMIM ID : "+this.disease.getOmimID()));
        H4 Aliases = new H4("Aliases");
        aliasesAndClassifications.add(Aliases);
        aliasesAndClassifications.add(new Span("Disease symbol : "+this.disease.getDiseaseSymbol()));
        Span alternativeNames = new Span("Alternative Names : " + this.disease.getAlternativeNames().replaceAll("\\|\\$"," , "));
        aliasesAndClassifications.add(alternativeNames);
        H4 Inheritance = new H4("Inheritance");
        aliasesAndClassifications.add(Inheritance);
        aliasesAndClassifications.add(new Span("Disease Inheritance : "+this.disease.getInheritance()));
        H4 Classifications = new H4("Classifications");
        aliasesAndClassifications.add(Classifications);
        Span classifications = new Span(this.disease.getClassification().replaceAll("\\|\\$"," , "));
        aliasesAndClassifications.add(classifications);
        add(aliasesAndClassifications);
    }
    private void addSummary(){
        H1 summaryTitle = new H1("Summary");
        summaryTitle.setClassName("disease-view-sections-titles");
        VerticalLayout summary = new VerticalLayout(summaryTitle);
        for(String summaryContent:disease.getSummary().split("\\|\\$")){
            summary.add(new Span(summaryContent));
        }
        summary.setId("summary");
        summary.setPadding(false);
        add(summary);
    }
    private void addSymptoms(){
        H1 symptomsTitle = new H1("Symptoms");
        symptomsTitle.setClassName("disease-view-sections-titles");
        VerticalLayout symptoms = new VerticalLayout(symptomsTitle);
        PaginatedGrid<Symptom> symptomsGrid = new PaginatedGrid<>(Symptom.class);
        symptomsGrid.setPageSize(50);
        symptomsGrid.removeAllColumns();
        symptomsGrid.addColumn("description").setAutoWidth(true);
        symptomsGrid.addColumn("hpoTerm").setAutoWidth(true);
        symptomsGrid.addColumn("hpoID").setAutoWidth(true);
        symptomsGrid.addColumn("frequency").setAutoWidth(true);
        symptomsGrid.addColumn(new ComponentRenderer<>(symptom -> {
            Button link = new Button();
            link.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            link.setIcon(VaadinIcon.LINK.create());
            link.addClickListener(event -> {

            });
            return link;
        })).setHeader("External Link");
        symptomsGrid.setItems(this.disease.getSymptoms());
        symptomsGrid.setMultiSort(false);
        symptomsGrid.setWidthFull();
        symptoms.add(symptomsGrid);
        symptoms.setId("symptoms");
        symptoms.setPadding(false);
        add(symptoms);
    }
//    private void addGenes(UserService userService,DiseaseService diseaseService,  GeneService geneService,  MutationService mutationService){
//        H1 genesTitle = new H1("Genes");
//        genesTitle.setClassName("disease-view-sections-titles");
//        ListBox<Gene> genesList = new MakeGenesList(user,disease,userService,geneService,diseaseService,mutationService);
//        genesList.setItems(disease.getGenes());
//        VerticalLayout genes = new VerticalLayout(genesTitle,genesList);
//        genes.setId("genes");
//        genes.setPadding(false);
//        add(genes);
//    }
//    private void addMutations(UserService userService,DiseaseService diseaseService,  GeneService geneService, MutationService mutationService){
//        H1 mutationsTitle = new H1("Mutations");
//        mutationsTitle.setClassName("disease-view-sections-titles");
//        ListBox<Mutation> mutationsList = new MakeMutationsList(user,disease,userService,geneService,diseaseService,mutationService);
//        mutationsList.setItems(disease.getMutations());
//        VerticalLayout mutations = new VerticalLayout(mutationsTitle,mutationsList);
//        mutations.setId("mutations");
//        mutations.setPadding(false);
//        add(mutations);
//    }
    private void addReferences(){
        H1 referencesTitle = new H1("References");
        referencesTitle.setClassName("disease-view-sections-titles");
        VerticalLayout references = new VerticalLayout(referencesTitle);
        for(String reference:disease.getDiseasesReferences().split("\\|\\$")){
            references.add(new Anchor(reference));
        }
        references.setId("references");
        references.setPadding(false);
        add(references);
    }
    private void addExternalLinks(){
        H1 ExternalLinksTitle = new H1("External Links");
        ExternalLinksTitle.setClassName("disease-view-sections-titles");
        VerticalLayout ExternalLinks = new VerticalLayout(ExternalLinksTitle);
        for(String ExternalLink:disease.getLinks().split("\\|\\$")){
            ExternalLinks.add(new Anchor(ExternalLink));
        }
        ExternalLinks.setId("references");
        ExternalLinks.setPadding(false);
        add(ExternalLinks);
    }

    private List<String> findOptions(String searchTerm, DiseaseService diseaseService) {
        List<String> optionsList = new ArrayList<>();
        List<Disease> diseasesList = diseaseService.searchByExpression(searchTerm);
        for (Disease disease : diseasesList) {
            optionsList.add(disease.getDiseaseName());
        }
        return optionsList;
    }


}
