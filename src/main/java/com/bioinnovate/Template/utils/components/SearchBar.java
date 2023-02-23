package com.bioinnovate.Template.utils.components;

import com.bioinnovate.Template.backend.entities.Disease;
import com.bioinnovate.Template.backend.entities.Gene;
import com.bioinnovate.Template.backend.services.DiseaseService;
// import com.bioinnovate.Template.backend.services.GeneService;
import com.vaadin.componentfactory.Autocomplete;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;
import java.util.List;

public class SearchBar extends HorizontalLayout{

    private final VaadinSession vaadinSession = VaadinSession.getCurrent();
    public final Autocomplete searchBar;
    public final ComboBox<String> searchField;
    public final Button search;
    public Registration registration ;

    public SearchBar(DiseaseService diseaseService){
        searchBar = new Autocomplete();

        searchBar.addChangeListener(event -> searchBar.setOptions(findOptions(searchBar.getValue(),diseaseService)));
        searchBar.setWidth("60%");
        searchBar.setPlaceholder("Search using diseases/mutations/genes names...");

        search = new Button("Search");
        search.setEnabled(false);
        search.getStyle().set("background", "#cb3434");
        search.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        search.setIcon(VaadinIcon.SEARCH.create());
        search.setWidth("20%");
        searchField = new ComboBox<>();
        searchField.addValueChangeListener(event -> {
            search.setEnabled((searchBar.getValue() != null) || (searchField.getValue() != null));
            if (search.isEnabled()){
                search.getStyle().set("background", "#cb3434");
                search.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            }else{
                search.getStyle().set("color", "#cb3434");
                search.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            }
        });
        searchBar.addValueChangeListener(event -> {
            search.setEnabled((searchBar.getValue() != null) || (searchField.getValue() != null));
            if (search.isEnabled()){
                search.getStyle().set("background", "#cb3434");
                search.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            }else{
                search.getStyle().set("color", "#cb3434");
                search.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            }
        });
        searchField.setPlaceholder("Choose Field");
        searchField.setAllowCustomValue(false);
        searchField.setItems("Genes","Mutations","Diseases");
        searchField.setWidth("20%");
        searchField.setValue("Mutations");
        add(searchBar,searchField,search);
        setWidthFull();
        getStyle().set("background","transparent").set("margin-top","0");

        search.addClickListener(event -> {
            vaadinSession.setAttribute("searchTerm", searchBar.getValue());
            vaadinSession.setAttribute("searchField", searchField.getValue());
        });
        registration = search.addClickListener(event -> UI.getCurrent().getPage().executeJs("window.location.replace('Search')"));
    }

    private List<String> findOptions(String searchTerm, DiseaseService diseaseService){
        List<String> optionsList = new ArrayList<>();
        List<Disease> diseasesList = diseaseService.searchByExpression(searchTerm);
        for (Disease disease : diseasesList) {
            optionsList.add(disease.getDiseaseName());
        }
        return optionsList;
    }
}
