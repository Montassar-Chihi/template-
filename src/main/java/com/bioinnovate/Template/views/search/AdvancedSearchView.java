package com.bioinnovate.PreMediT.views.search;

import com.bioinnovate.PreMediT.backend.entities.*;
import com.bioinnovate.PreMediT.backend.services.*;
import com.bioinnovate.PreMediT.utils.components.SearchBar;
import com.bioinnovate.PreMediT.utils.security.AuthorizedOnlyView;
import com.bioinnovate.PreMediT.utils.security.Role;
import com.bioinnovate.PreMediT.views.diseases.diseaseUtils.MakeDiseasesList;
import com.bioinnovate.PreMediT.views.genes.geneUtils.MakeGenesList;
import com.bioinnovate.PreMediT.views.main.MainView;
import com.bioinnovate.PreMediT.views.mutations.mutationUtils.MakeMutationsList;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.*;

@Route(value = "advancedSearch", layout = MainView.class)
@PageTitle("Advanced Search")
@CssImport("./styles/views/main/main-view.css")
public class AdvancedSearchView extends AuthorizedOnlyView {

    private final VaadinSession session = VaadinSession.getCurrent();
    private User user;
    private SearchBar searchBar;
    private String searchTerm;
    private String searchField;
    private ComboBox<String> diseaseGenomicLocationFilter;
    private MultiselectComboBox<Symptom> diseaseSymptomFilter;
    private IntegerField diseaseOmimIdFilter;
    private ComboBox<String> diseaseInheritanceFilter;
    private MultiselectComboBox<Gene> diseaseGeneFilter;
    private ComboBox<String> diseaseCategoryFilter;
    private ComboBox<String> geneCategoryFilter;
    private ComboBox<Disease> geneDiseaseFilter;
    private ComboBox<String> geneGenomicLocationFilter;
    private IntegerField geneNcbiIdFilter;
    private TextField mutationDbSnpId;
    private TextField mutationClinVarId;
    private TextField mutationPharmGKBId;
    private TextField mutationDnaChangeHg19;
    private TextField mutationProteinChangeHg19;
    private TextField mutationHgvsHg19;
    private TextField mutationType;
    private TextField mutationClinVarClinicalSignificance;
    private TextField mutationGovernorate;
    private TextField mutationFrequencyInTunisia;
    private TextField mutationMenaRegionFrequency;
    private ComboBox<Gene> mutationGeneFilter;
    private ComboBox<Disease> mutationDiseaseFilter;
    private VerticalLayout searchResultView;
    private List<Gene> genes;
    private List<Mutation> mutations;
    private List<Disease> diseases;
    private VerticalLayout mutationFilterLayout;
    private VerticalLayout diseaseFilterLayout;
    private VerticalLayout geneFilterLayout;
    private HorizontalLayout filteringLayout;
    private H1 bigDiseaseTitle;
    private H1 bigMutationTitle;
    private H1 bigGeneTitle;
    private H1 bigTitle;

    public AdvancedSearchView(@Autowired UserService userService,@Autowired DiseaseService diseaseService,@Autowired GeneService geneService,@Autowired MutationService mutationService,@Autowired SymptomService symptomService){

        super(userService,new Role[]{Role.DOCTOR,Role.ADMIN,Role.RESEARCHER});
        setId("view-special-container");
        if(isLoggedIn() && isAuthorized()){
            buildLoggedInView(diseaseService, userService, mutationService, geneService,symptomService);
        }
    }
    public void handleNotLoggedIn(){
        UI.getCurrent().getPage().executeJs("window.location.replace('login');");
    }
    public void buildLoggedInView(@Autowired DiseaseService diseaseService, @Autowired UserService userService,@Autowired MutationService mutationService, @Autowired GeneService geneService,@Autowired SymptomService symptomService){
        user = getUser();
        getStyle().set("padding","0").set("margin","2rem 15% 2rem 5%").set("width","80%");
        createAdvancedSearchTools(userService,diseaseService,geneService,mutationService,symptomService);

    }
    private void createAdvancedSearchTools(UserService userService, DiseaseService diseaseService, GeneService geneService, MutationService mutationService, SymptomService symptomService){
        createUI(diseaseService, geneService,  mutationService,symptomService);
        searchBar.registration.remove();
        searchBar.search.addClickListener(event -> {
            session.setAttribute("searchTerm", searchBar.searchBar.getValue());
            session.setAttribute("searchField", searchBar.searchField.getValue());
            searchTerm = session.getAttribute("searchTerm").toString();
            searchField = session.getAttribute("searchField").toString();
            generateSearchResults(diseaseService, geneService,  mutationService);
            createSearchResults(userService,diseaseService, geneService,  mutationService,symptomService);
            filteringLayout.setVisible(!filteringLayout.isVisible());
        });
        Button showFilters = new Button(VaadinIcon.LIST.create());
        showFilters.addClickListener(event -> filteringLayout.setVisible(!filteringLayout.isVisible()));
        showFilters.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        showFilters.setId("diseases-view-showFilters-button");
        searchBar.add(showFilters);
    }

    private void createUI(DiseaseService diseaseService, GeneService geneService, MutationService mutationService, SymptomService symptomService){
        bigTitle = new H1("Advanced Search");
        bigTitle.getStyle().set("align-self","center").set(
                "color","#cb3434").set(
                "font-family","Eczar").set(
                "font-size","2rem").set(
                "text-align","center").set("margin","2rem auto");
        searchBar = new SearchBar(diseaseService, geneService,  mutationService);
        add(bigTitle,searchBar);

        diseaseCategoryFilter = new ComboBox<>();
        List<String> categories = diseaseService.findCategories();
        List<String> classifications = new ArrayList<>();
        for (String category:categories){
            classifications.addAll(List.of(category.split(" \\|\\$ ")));
        }
        Set<String> set = new HashSet<>(classifications);
        classifications.clear();
        classifications.addAll(set);
        diseaseCategoryFilter.setItems(classifications);
        diseaseCategoryFilter.setPlaceholder("Filter by category ...");
        diseaseCategoryFilter.setClearButtonVisible(true);
        diseaseCategoryFilter.setWidth("100%");

        diseaseGeneFilter = new MultiselectComboBox<>();
        diseaseGeneFilter.setItemLabelGenerator(Gene::getName);
        diseaseGeneFilter.setPlaceholder("Filter by gene ...");
        diseaseGeneFilter.setClearButtonVisible(true);
        diseaseGeneFilter.setWidth("100%");

        diseaseInheritanceFilter = new ComboBox<>();
        diseaseInheritanceFilter.setPlaceholder("Filter by inheritance ...");
        diseaseInheritanceFilter.setClearButtonVisible(true);
        diseaseInheritanceFilter.setWidth("100%");

        diseaseGenomicLocationFilter = new ComboBox<>();
        diseaseGenomicLocationFilter.setPlaceholder("Filter by genomic location ...");
        diseaseGenomicLocationFilter.setClearButtonVisible(true);
        diseaseGenomicLocationFilter.setWidth("100%");

        diseaseOmimIdFilter = new IntegerField();
        diseaseOmimIdFilter.setPlaceholder("Search using OMIM id ...");
        diseaseOmimIdFilter.setClearButtonVisible(true);
        diseaseOmimIdFilter.setWidth("100%");

        diseaseSymptomFilter = new MultiselectComboBox<>();
        diseaseSymptomFilter.setPlaceholder("Filter by symptom ...");
        diseaseSymptomFilter.setItemLabelGenerator(Symptom::getHpoTerm);
        diseaseSymptomFilter.setClearButtonVisible(true);
        diseaseSymptomFilter.setWidth("100%");

        bigDiseaseTitle = new H1("Filter By Disease fields");
        bigDiseaseTitle.getStyle().set("align-self","center").set(
                "color","#cb3434").set(
                "font-family","Eczar").set(
                "font-size","1.5rem").set(
                "text-align","center").set("margin","3rem auto 1rem");

        diseaseFilterLayout = new VerticalLayout(bigDiseaseTitle, diseaseCategoryFilter, diseaseGeneFilter, diseaseInheritanceFilter, diseaseGenomicLocationFilter,diseaseSymptomFilter, diseaseOmimIdFilter);
        diseaseFilterLayout.setWidth("30%");
        diseaseFilterLayout.setPadding(false);
        diseaseFilterLayout.setSpacing(true);

        mutationDbSnpId = new TextField();
        mutationDbSnpId.setPlaceholder("Filter with mutation DbSnp Id ...");
        mutationDbSnpId.setClearButtonVisible(true);
        mutationDbSnpId.setWidthFull();

        mutationClinVarId  = new TextField();
        mutationClinVarId.setPlaceholder("Filter with mutation ClinVar Id ...");
        mutationClinVarId.setClearButtonVisible(true);
        mutationClinVarId.setWidthFull();

        mutationPharmGKBId = new TextField();
        mutationPharmGKBId.setPlaceholder("Filter with mutation PharmGKB Id ...");
        mutationPharmGKBId.setClearButtonVisible(true);
        mutationPharmGKBId.setWidthFull();

        mutationDnaChangeHg19 = new TextField();
        mutationDnaChangeHg19.setPlaceholder("Filter with mutation Dna Change (Hg19) ...");
        mutationDnaChangeHg19.setClearButtonVisible(true);
        mutationDnaChangeHg19.setWidthFull();

        mutationProteinChangeHg19 = new TextField();
        mutationProteinChangeHg19.setPlaceholder("Filter with mutation Protein Change (Hg19) ...");
        mutationProteinChangeHg19.setClearButtonVisible(true);
        mutationProteinChangeHg19.setWidthFull();

        mutationHgvsHg19 = new TextField();
        mutationHgvsHg19.setPlaceholder("Filter with mutation Hgvs (Hg19) ...");
        mutationHgvsHg19.setClearButtonVisible(true);
        mutationHgvsHg19.setWidthFull();

        mutationType = new TextField();
        mutationType.setPlaceholder("Filter with mutation Type ...");
        mutationType.setClearButtonVisible(true);
        mutationType.setWidthFull();

        mutationClinVarClinicalSignificance = new TextField();
        mutationClinVarClinicalSignificance.setPlaceholder("Filter with mutation ClinVar Clinical Significance ...");
        mutationClinVarClinicalSignificance.setClearButtonVisible(true);
        mutationClinVarClinicalSignificance.setWidthFull();

        mutationGovernorate = new TextField();
        mutationGovernorate.setPlaceholder("Filter with mutation Governorate ...");
        mutationGovernorate.setClearButtonVisible(true);
        mutationGovernorate.setWidthFull();

        mutationFrequencyInTunisia = new TextField();
        mutationFrequencyInTunisia.setPlaceholder("Filter with mutation Frequency In Tunisia ...");
        mutationFrequencyInTunisia.setClearButtonVisible(true);
        mutationFrequencyInTunisia.setWidthFull();

        mutationMenaRegionFrequency = new TextField();
        mutationMenaRegionFrequency.setPlaceholder("Filter with Mena Region Mutation Frequency ...");
        mutationMenaRegionFrequency.setClearButtonVisible(true);
        mutationMenaRegionFrequency.setWidthFull();

        mutationGeneFilter = new ComboBox<>();
        mutationGeneFilter.setItems(geneService.findAll());
        mutationGeneFilter.setItemLabelGenerator(Gene::getName);
        mutationGeneFilter.setPlaceholder("Filter by gene ...");
        mutationGeneFilter.setClearButtonVisible(true);
        mutationGeneFilter.setWidthFull();

        mutationDiseaseFilter = new ComboBox<>();
        mutationDiseaseFilter.setItems(diseaseService.findAll());
        mutationDiseaseFilter.setItemLabelGenerator(Disease::getDiseaseName);
        mutationDiseaseFilter.setPlaceholder("Filter by disease ...");
        mutationDiseaseFilter.setClearButtonVisible(true);
        mutationDiseaseFilter.setWidthFull();

        bigMutationTitle = new H1("Filter By Mutation fields");
        bigMutationTitle.getStyle().set("align-self","center").set(
                "color","#cb3434").set(
                "font-family","Eczar").set(
                "font-size","1.5rem").set(
                "text-align","center").set("margin","3rem auto 1rem");

        mutationFilterLayout = new VerticalLayout(bigMutationTitle,mutationDbSnpId,mutationClinVarId,mutationPharmGKBId,mutationDnaChangeHg19,mutationProteinChangeHg19,
                mutationHgvsHg19,mutationType,mutationClinVarClinicalSignificance,mutationGovernorate,mutationFrequencyInTunisia,mutationMenaRegionFrequency,mutationGeneFilter,
                mutationDiseaseFilter);
        mutationFilterLayout.setWidth("30%");
        mutationFilterLayout.setPadding(false);
        mutationFilterLayout.setSpacing(true);

        geneCategoryFilter = new ComboBox<>();
        geneCategoryFilter.setItems(geneService.findType());
        geneCategoryFilter.setPlaceholder("Filter by gene type ...");
        geneCategoryFilter.setClearButtonVisible(true);
        geneCategoryFilter.setWidthFull();

        geneDiseaseFilter = new ComboBox<>();
        geneDiseaseFilter.setItems(diseaseService.findAll());
        geneDiseaseFilter.setItemLabelGenerator(Disease::getDiseaseName);
        geneDiseaseFilter.setPlaceholder("Filter by disease ...");
        geneDiseaseFilter.setClearButtonVisible(true);
        geneDiseaseFilter.setWidthFull();

        geneGenomicLocationFilter = new ComboBox<>();
        geneGenomicLocationFilter.setItems(geneService.findGenomicLocations());
        geneGenomicLocationFilter.setPlaceholder("Filter by genomic location ...");
        geneGenomicLocationFilter.setClearButtonVisible(true);
        geneGenomicLocationFilter.setWidthFull();

        geneNcbiIdFilter = new IntegerField();
        geneNcbiIdFilter.setPlaceholder("Search using NCBI id ...");
        geneNcbiIdFilter.setClearButtonVisible(true);
        geneNcbiIdFilter.setWidthFull();

        bigGeneTitle = new H1("Filter By Gene fields");
        bigGeneTitle.getStyle().set("align-self","center").set(
                "color","#cb3434").set(
                "font-family","Eczar").set(
                "font-size","1.5rem").set(
                "text-align","center").set("margin","3rem auto 1rem");;

        geneFilterLayout = new VerticalLayout(bigGeneTitle,geneCategoryFilter, geneDiseaseFilter,geneGenomicLocationFilter, geneNcbiIdFilter);
        geneFilterLayout.setWidth("30%");
        geneFilterLayout.setPadding(false);
        geneFilterLayout.setSpacing(true);

        filteringLayout = new HorizontalLayout(diseaseFilterLayout,geneFilterLayout,mutationFilterLayout);
        filteringLayout.setWidthFull();
        filteringLayout.getStyle().set("display","flex").set("justify-content","center");
        filteringLayout.setPadding(false);
        filteringLayout.setSpacing(true);
        filteringLayout.setMargin(false);
        try {
            session.getAttribute("searchField").toString();
            filteringLayout.setVisible(true);
        }catch (Exception e){
            filteringLayout.setVisible(false);
        }

        fillComboBoxes(diseaseService,geneService,symptomService);

        add(filteringLayout);


    }
    public void fillComboBoxes(DiseaseService diseaseService,GeneService geneService,SymptomService symptomService) {

        diseaseGenomicLocationFilter.setItems(diseaseService.findDistinctGenomicLocation());
        diseaseInheritanceFilter.setItems(diseaseService.findDistinctInheritance());
        geneCategoryFilter.setItems(geneService.findType());
        geneGenomicLocationFilter.setItems(geneService.findGenomicLocations());

        diseaseGeneFilter.setItems(geneService.findAll());
        diseaseSymptomFilter.setItems(symptomService.findAll());
        geneDiseaseFilter.setItems(diseaseService.findAll());
        mutationGeneFilter.setItems(geneService.findAll());
        mutationDiseaseFilter.setItems(diseaseService.findAll());
    }
    private void createSearchResults(@Autowired UserService userService,@Autowired DiseaseService diseaseService,@Autowired GeneService geneService,@Autowired MutationService mutationService,@Autowired SymptomService symptomService){
        removeAll();

        createAdvancedSearchTools(userService,diseaseService,geneService,mutationService,symptomService);
        searchResultView = new VerticalLayout();
        searchResultView.setId("searchResultView");
        searchResultView.setWidth("100%");
        add(searchResultView);

        if (searchField.equalsIgnoreCase("Diseases")) {
            H1 bigTitle = new H1("Search Results ("+diseases.size()+" diseases found) :");
            bigTitle.getStyle().set("align-self","center").set(
                    "color","#cb3434").set(
                    "font-family","Eczar").set(
                    "font-size","30px").set(
                    "text-align","center").set("margin-right","15rem");

            searchResultView.add(bigTitle);
            searchResultView.add(addDiseasesList(diseases,userService));
        } else if (searchField.equalsIgnoreCase("genes")) {
            H1 bigTitle = new H1("Search Results ("+genes.size()+" genes found) :");
            bigTitle.getStyle().set("align-self","center").set(
                    "color","#cb3434").set(
                    "font-family","Eczar").set(
                    "font-size","30px").set(
                    "text-align","center").set("margin-right","15rem");

            searchResultView.setWidthFull();
            searchResultView.add(bigTitle);
            searchResultView.add(addGenesList(genes,userService));
        } else {
            H1 bigTitle = new H1("Search Results ("+mutations.size()+" mutations found) :");
            bigTitle.getStyle().set("align-self","center").set(
                    "color","#cb3434").set(
                    "font-family","Eczar").set(
                    "font-size","30px").set(
                    "text-align","center").set("margin-right","15rem");
            searchResultView.add(bigTitle);
            searchResultView.add(addMutationsList(mutations,userService));
        }
    }
    private void generateSearchResults(DiseaseService diseaseService, GeneService geneService, MutationService mutationService){
        System.out.println(mutationFrequencyInTunisia.getValue());
        if (searchField.equalsIgnoreCase("diseases")) {
            mutations = intersectionMutations(
                    mutationGeneFilter.getValue() != null ?
                            mutationService.findMutationsByGene(mutationGeneFilter.getValue()) :
                            mutationService.findAll(),
                    mutationDiseaseFilter.getValue() != null ?
                            mutationService.findMutationsByDisease(mutationDiseaseFilter.getValue()) :
                            mutationService.findAll(),
                    !mutationDbSnpId.getValue().equals("") ?
                            mutationService.findMutationsByDBSnpId(mutationDbSnpId.getValue()) :
                            mutationService.findAll(),
                    !mutationClinVarId.getValue().equals("") ?
                            mutationService.findMutationsByClinVarId(mutationClinVarId.getValue()) :
                            mutationService.findAll(),
                    !mutationPharmGKBId.getValue().equals("") ?
                            mutationService.findMutationsByPharmGKBId(mutationPharmGKBId.getValue()) :
                            mutationService.findAll(),
                    !mutationDnaChangeHg19.getValue().equals("") ?
                            mutationService.findMutationsByDnaChangeHg19(mutationDnaChangeHg19.getValue()) :
                            mutationService.findAll(),
                    !mutationProteinChangeHg19.getValue().equals("") ?
                            mutationService.findMutationsByProteinChangeHg19(mutationProteinChangeHg19.getValue()) :
                            mutationService.findAll(),
                    !mutationHgvsHg19.getValue().equals("") ?
                            mutationService.findMutationsByHgvsHg19(mutationHgvsHg19.getValue()) :
                            mutationService.findAll(),
                    !mutationFrequencyInTunisia.getValue().equals("") ?
                            mutationService.findMutationsByFrequencyInTunisia(mutationFrequencyInTunisia.getValue()) :
                            mutationService.findAll(),
                    !mutationMenaRegionFrequency.getValue().equals("") ?
                            mutationService.findMutationsByMenaRegionFrequency(mutationMenaRegionFrequency.getValue()) :
                            mutationService.findAll(),
                    !mutationGovernorate.getValue().equals("") ?
                            mutationService.findMutationsByGovernorate(mutationGovernorate.getValue()) :
                            mutationService.findAll(),
                    !mutationType.getValue().equals("") ?
                            mutationService.findMutationsByMutationType(mutationType.getValue()) :
                            mutationService.findAll(),
                    !mutationClinVarClinicalSignificance.getValue().equals("") ?
                            mutationService.findMutationsByClinVarClinicalSignificance(mutationClinVarClinicalSignificance.getValue()) :
                            mutationService.findAll());
            genes = intersectionGenes(
                    geneCategoryFilter.getValue() != null
                            ? geneService.searchByType(geneCategoryFilter.getValue())
                            : geneService.findAll(),
                    geneGenomicLocationFilter.getValue() != null
                            ? geneService.findByGenomicLocation(geneGenomicLocationFilter.getValue())
                            : geneService.findAll(),
                    geneNcbiIdFilter.getValue() != null
                            ? geneService.findByNcbiIDContaining(String.valueOf(geneNcbiIdFilter.getValue()))
                            : geneService.findAll(),
                    geneDiseaseFilter.getValue() != null
                            ? geneService.findGeneByDiseases(geneDiseaseFilter.getValue())
                            : geneService.findAll());
            diseases = intersectionDiseases(
                    diseaseInheritanceFilter.getValue() != null
                            ? diseaseService.searchByInheritance(diseaseInheritanceFilter.getValue())
                            : diseaseService.findAll(),
                    diseaseGenomicLocationFilter.getValue() != null
                            ? diseaseService.searchByGenomicLocation(diseaseGenomicLocationFilter.getValue())
                            : diseaseService.findAll(),
                    diseaseOmimIdFilter.getValue() != null
                            ? diseaseService.findByOmimID(diseaseOmimIdFilter.getValue())
                            : diseaseService.findAll(),
                    !diseaseGeneFilter.getSelectedItems().isEmpty()
                            ? diseaseService.findByGenes((List<Gene>) diseaseGeneFilter.getSelectedItems())
                            : diseaseService.findByGenes(genes),
                    diseaseCategoryFilter.getValue() != null
                            ? diseaseService.findByClassification(diseaseCategoryFilter.getValue())
                            : diseaseService.findAll(),
                    diseaseService.findByMutations(mutations)
            );
        } else if (searchField.equalsIgnoreCase("genes")) {
            mutations = intersectionMutations(
                    mutationGeneFilter.getValue() != null ?
                            mutationService.findMutationsByGene(mutationGeneFilter.getValue()) :
                            mutationService.findAll(),
                    mutationDiseaseFilter.getValue() != null ?
                            mutationService.findMutationsByDisease(mutationDiseaseFilter.getValue()) :
                            mutationService.findAll(),
                    !mutationDbSnpId.getValue().equals("") ?
                            mutationService.findMutationsByDBSnpId(mutationDbSnpId.getValue()) :
                            mutationService.findAll(),
                    !mutationClinVarId.getValue().equals("") ?
                            mutationService.findMutationsByClinVarId(mutationClinVarId.getValue()) :
                            mutationService.findAll(),
                    !mutationPharmGKBId.getValue().equals("") ?
                            mutationService.findMutationsByPharmGKBId(mutationPharmGKBId.getValue()) :
                            mutationService.findAll(),
                    !mutationDnaChangeHg19.getValue().equals("") ?
                            mutationService.findMutationsByDnaChangeHg19(mutationDnaChangeHg19.getValue()) :
                            mutationService.findAll(),
                    !mutationProteinChangeHg19.getValue().equals("") ?
                            mutationService.findMutationsByProteinChangeHg19(mutationProteinChangeHg19.getValue()) :
                            mutationService.findAll(),
                    !mutationHgvsHg19.getValue().equals("") ?
                            mutationService.findMutationsByHgvsHg19(mutationHgvsHg19.getValue()) :
                            mutationService.findAll(),
                    !mutationFrequencyInTunisia.getValue().equals("") ?
                            mutationService.findMutationsByFrequencyInTunisia(mutationFrequencyInTunisia.getValue()) :
                            mutationService.findAll(),
                    !mutationMenaRegionFrequency.getValue().equals("") ?
                            mutationService.findMutationsByMenaRegionFrequency(mutationMenaRegionFrequency.getValue()) :
                            mutationService.findAll(),
                    !mutationGovernorate.getValue().equals("") ?
                            mutationService.findMutationsByGovernorate(mutationGovernorate.getValue()) :
                            mutationService.findAll(),
                    !mutationType.getValue().equals("") ?
                            mutationService.findMutationsByMutationType(mutationType.getValue()) :
                            mutationService.findAll(),
                    !mutationClinVarClinicalSignificance.getValue().equals("") ?
                            mutationService.findMutationsByClinVarClinicalSignificance(mutationClinVarClinicalSignificance.getValue()) :
                            mutationService.findAll());
            diseases = intersectionDiseases(
                    diseaseInheritanceFilter.getValue() != null
                            ? diseaseService.searchByInheritance(diseaseInheritanceFilter.getValue())
                            : diseaseService.findAll(),
                    diseaseGenomicLocationFilter.getValue() != null
                            ? diseaseService.searchByGenomicLocation(diseaseGenomicLocationFilter.getValue())
                            : diseaseService.findAll(),
                    diseaseOmimIdFilter.getValue() != null
                            ? diseaseService.findByOmimID(diseaseOmimIdFilter.getValue())
                            : diseaseService.findAll(),
                    !diseaseGeneFilter.getSelectedItems().isEmpty()
                            ? diseaseService.findByGenes((List<Gene>) diseaseGeneFilter.getSelectedItems())
                            : diseaseService.findAll(),
                    diseaseCategoryFilter.getValue() != null
                            ? diseaseService.findByClassification(diseaseCategoryFilter.getValue())
                            : diseaseService.findAll());
            genes = intersectionGenes(
                    geneCategoryFilter.getValue() != null
                            ? geneService.searchByType(geneCategoryFilter.getValue())
                            : geneService.findAll(),
                    geneGenomicLocationFilter.getValue() != null
                            ? geneService.findByGenomicLocation(geneGenomicLocationFilter.getValue())
                            : geneService.findAll(),
                    geneNcbiIdFilter.getValue() != null
                            ? geneService.findByNcbiIDContaining(String.valueOf(geneNcbiIdFilter.getValue()))
                            : geneService.findAll(),
                    geneDiseaseFilter.getValue() != null
                            ? geneService.findGeneByDiseases(geneDiseaseFilter.getValue())
                            : geneService.findByDiseasesIn(diseases),
                    geneService.findGeneByMutationsIn(mutations)
            );
        } else if (searchField.equalsIgnoreCase("mutations")) {
            diseases = intersectionDiseases(
                    diseaseInheritanceFilter.getValue() != null
                            ? diseaseService.searchByInheritance(diseaseInheritanceFilter.getValue())
                            : diseaseService.findAll(),
                    diseaseGenomicLocationFilter.getValue() != null
                            ? diseaseService.searchByGenomicLocation(diseaseGenomicLocationFilter.getValue())
                            : diseaseService.findAll(),
                    diseaseOmimIdFilter.getValue() != null
                            ? diseaseService.findByOmimID(diseaseOmimIdFilter.getValue())
                            : diseaseService.findAll(),
                    !diseaseGeneFilter.getSelectedItems().isEmpty()
                            ? diseaseService.findByGenes((List<Gene>) diseaseGeneFilter.getSelectedItems())
                            : diseaseService.findAll(),
                    diseaseCategoryFilter.getValue() != null
                            ? diseaseService.findByClassification(diseaseCategoryFilter.getValue())
                            : diseaseService.findAll());
            genes = intersectionGenes(
                    geneCategoryFilter.getValue() != null
                            ? geneService.searchByType(geneCategoryFilter.getValue())
                            : geneService.findAll(),
                    geneGenomicLocationFilter.getValue() != null
                            ? geneService.findByGenomicLocation(geneGenomicLocationFilter.getValue())
                            : geneService.findAll(),
                    geneNcbiIdFilter.getValue() != null
                            ? geneService.findByNcbiIDContaining(String.valueOf(geneNcbiIdFilter.getValue()))
                            : geneService.findAll(),
                    geneDiseaseFilter.getValue() != null
                            ? geneService.findGeneByDiseases(geneDiseaseFilter.getValue())
                            : geneService.findAll());
            mutations = intersectionMutations(
                    mutationGeneFilter.getValue() != null ?
                            mutationService.findMutationsByGene(mutationGeneFilter.getValue()) :
                            mutationService.findMutationsByGenes(genes),
                    mutationDiseaseFilter.getValue() != null ?
                            mutationService.findMutationsByDisease(mutationDiseaseFilter.getValue()) :
                            mutationService.findMutationsByDiseases(diseases),
                    !mutationDbSnpId.getValue().equals("") ?
                            mutationService.findMutationsByDBSnpId(mutationDbSnpId.getValue()) :
                            mutationService.findAll(),
                    !mutationClinVarId.getValue().equals("") ?
                            mutationService.findMutationsByClinVarId(mutationClinVarId.getValue()) :
                            mutationService.findAll(),
                    !mutationPharmGKBId.getValue().equals("") ?
                            mutationService.findMutationsByPharmGKBId(mutationPharmGKBId.getValue()) :
                            mutationService.findAll(),
                    !mutationDnaChangeHg19.getValue().equals("") ?
                            mutationService.findMutationsByDnaChangeHg19(mutationDnaChangeHg19.getValue()) :
                            mutationService.findAll(),
                    !mutationProteinChangeHg19.getValue().equals("") ?
                            mutationService.findMutationsByProteinChangeHg19(mutationProteinChangeHg19.getValue()) :
                            mutationService.findAll(),
                    !mutationHgvsHg19.getValue().equals("") ?
                            mutationService.findMutationsByHgvsHg19(mutationHgvsHg19.getValue()) :
                            mutationService.findAll(),
                    !mutationFrequencyInTunisia.getValue().equals("") ?
                            mutationService.findMutationsByFrequencyInTunisia(mutationFrequencyInTunisia.getValue()) :
                            mutationService.findAll(),
                    !mutationMenaRegionFrequency.getValue().equals("") ?
                            mutationService.findMutationsByMenaRegionFrequency(mutationMenaRegionFrequency.getValue()) :
                            mutationService.findAll(),
                    !mutationGovernorate.getValue().equals("") ?
                            mutationService.findMutationsByGovernorate(mutationGovernorate.getValue()) :
                            mutationService.findAll(),
                    !mutationType.getValue().equals("") ?
                            mutationService.findMutationsByMutationType(mutationType.getValue()) :
                            mutationService.findAll(),
                    !mutationClinVarClinicalSignificance.getValue().equals("") ?
                            mutationService.findMutationsByClinVarClinicalSignificance(mutationClinVarClinicalSignificance.getValue()) :
                            mutationService.findAll());
        }
        System.out.println(Arrays.toString(genes.toArray()));
        System.out.println(Arrays.toString(diseases.toArray()));
        System.out.println(Arrays.toString(mutations.toArray()));
    }


    private ListBox<Mutation> addMutationsList(List<Mutation> mutationsList, UserService userService){
        ListBox<Mutation> mutations = new MakeMutationsList(user,mutationsList, userService);
        mutations.setItems(mutationsList);
        return mutations;
    }

    private ListBox<Disease> addDiseasesList(List<Disease> diseasesList,UserService userService){
        ListBox<Disease> diseases = new MakeDiseasesList(user,diseasesList,userService);
        diseases.setItems(diseasesList);
        return diseases;
    }
    private ListBox<Gene> addGenesList(List<Gene> genesList,UserService userService){
        ListBox<Gene> genes = new MakeGenesList(user,genesList, userService);
        genes.setItems(genesList);
        return genes;
    }

    @SafeVarargs
    private List<Mutation> intersectionMutations(List<Mutation>... lists) {
        List<Mutation> result = new ArrayList<>(lists[0]);
        for (int i = 1; i < lists.length; i++) {
            result.retainAll(lists[i]);
        }
        return result;
    }
    @SafeVarargs
    private List<Disease> intersectionDiseases(List<Disease>... lists) {
        List<Disease> result = new ArrayList<>(lists[0]);
        for (int i = 1; i < lists.length; i++) {
            result.retainAll(lists[i]);
        }
        return result;
    }
    @SafeVarargs
    private List<Gene> intersectionGenes(List<Gene>... lists) {
        List<Gene> result = new ArrayList<>(lists[0]);
        for (int i = 1; i < lists.length; i++) {
            result.retainAll(lists[i]);
        }
        return result;
    }

}
