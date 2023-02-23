package com.bioinnovate.Template.views.diseases.diseaseUtils;

import com.bioinnovate.Template.backend.entities.Disease;
import com.bioinnovate.Template.backend.entities.User;
import com.bioinnovate.Template.backend.services.DiseaseService;
// import com.bioinnovate.Template.backend.services.GeneService;
import com.bioinnovate.Template.backend.services.UserService;
import com.vaadin.componentfactory.Tooltip;
import com.vaadin.componentfactory.TooltipAlignment;
import com.vaadin.componentfactory.TooltipPosition;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MakeDiseasesList extends ListBox<Disease> {


    public MakeDiseasesList(User user ,List<Disease> diseasesList,@Autowired UserService userService){
        setWidth("100%");
        setRenderer(new ComponentRenderer<>(disease -> {
            HorizontalLayout row = new HorizontalLayout();
            row.setClassName("row");
            row.setAlignItems(FlexComponent.Alignment.START);

            Span namee = new Span(disease.getDiseaseName());
            Span category = new Span(disease.getClassification().replaceAll("\\|\\$"," , "));
            category.getStyle()
                    .set("color", "var(--lumo-secondary-text-color)")
                    .set("font-size", "var(--lumo-font-size-s)");

            VerticalLayout personalDetails = new VerticalLayout(namee, category);
            Hr line = new Hr();
            line.setWidth("80%");
            line.getStyle().set("margin","1rem 10%").set("color","black");
            personalDetails.add(line);
            personalDetails.setPadding(false);
            personalDetails.setSpacing(false);
            personalDetails.setWidth("100%");
            personalDetails.getStyle().set("margin-top","5px").set("padding-left","5px");

            List<Disease> myList = user.getDiseases();
            Button add = new Button(new Icon(VaadinIcon.PLUS));
            add.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            add.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
            add.addClickListener(event -> {
                myList.add(disease);
                user.setDiseases(myList);
                userService.update(user);
                setItems(diseasesList);
            });
            Button remove = new Button(new Icon(VaadinIcon.TRASH));
            remove.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            remove.addThemeVariants(ButtonVariant.LUMO_ERROR);
            remove.addClickListener(event -> {
                myList.remove(disease);
                user.setDiseases(myList);
                userService.update(user);
                setItems(diseasesList);
            });
            Tooltip tooltipOperations = new Tooltip();
            tooltipOperations.add(VaadinIcon.INFO_CIRCLE.create());
            tooltipOperations.setPosition(TooltipPosition.TOP);
            tooltipOperations.setAlignment(TooltipAlignment.LEFT);
            tooltipOperations.setThemeName("light");
            tooltipOperations.getStyle().set("margin","0px 0px 0px 5px");
            VerticalLayout operations = new VerticalLayout();
            if (myList.contains(disease)){
                tooltipOperations.add(new Span(" Remove " + disease.getDiseaseName() + " from My list!"));
                tooltipOperations.attachToComponent(remove);
                operations.add(remove,tooltipOperations);
            }else {
                tooltipOperations.add(new Span(" Add " + disease.getDiseaseName() + " from My list!"));
                tooltipOperations.attachToComponent(add);
                operations.add(add,tooltipOperations);
            }
            operations.setPadding(false);
            operations.setSpacing(false);
            operations.setWidth("auto");

            row.add(personalDetails,operations);
//            row.addClickListener(event -> {
//                Dialog dialog = new Dialog();
//                dialog.open();
//                dialog.setWidth("70%");
//                dialog.setModal(false);
//                dialog.setDraggable(true);
//                dialog.setResizable(true);
//                dialog.add(new DiseaseDetailsView(disease,false, userService, diseaseService,geneService,mutationService));
//                // Changes start here
//                DialogHeaderBar header = DialogHeaderBar.addTo(dialog).setCaption("Disease Details : "+disease.getDiseaseName());
//                dialog.addOpenedChangeListener(event1 -> {
//                    if (header.isMinimized()) {
//                        Button bubble = new Button();
//                        bubble.setId("bubble");
//                        bubble.getStyle().set("margin-left","calc(100% - 36px)").set("background-image","url('/images/disease.jpg')").set("height","64px").set("width","50px").set("border-radius","50%").set("box-shadow","1px 1px 10px 5px grey").set("color","#c93434");
//
//                        Tooltip tooltip = new Tooltip();
//                        tooltip.add(VaadinIcon.INFO_CIRCLE.create());
//                        tooltip.add(new Span("Disease : " + disease.getDiseaseName()));
//                        tooltip.attachToComponent(bubble);
//                        tooltip.setPosition(TooltipPosition.RIGHT);
//                        tooltip.setAlignment(TooltipAlignment.LEFT);
//                        tooltip.setThemeName("light");
//                        tooltip.getStyle().set("margin","0px 0px 0px 5px");
//
//                        Button closeBubble = new Button(VaadinIcon.CLOSE.create());
//                        closeBubble.setId("closeBubble");
//                        closeBubble.getStyle().set("background","#efefef").set("color","red").set("width","10px").set("border-radius","50%").set(
//                                "position","relative").set(
//                                "top","-0.7rem").set(
//                                "right","2.5rem");
//                        HorizontalLayout bubbleLayout = new HorizontalLayout(bubble,closeBubble);
//                        bubbleLayout.setId("bubbleLayout");
//                        closeBubble.addClickListener(event2 -> bubblesView.remove(bubbleLayout,tooltip));
//
//                        bubblesView.add(bubbleLayout,tooltip);
//
//                        bubble.addClickListener(event2 -> {
//                            bubblesView.remove(bubbleLayout,tooltip);
//                            header.setMinimized(false);
//                            dialog.setDraggable(true);
//                            dialog.setResizable(true);
//                            dialog.open();
//                        });
//                    }
//                });
//                add(dialog);
//            });
            personalDetails.addClickListener(event -> {
                UI.getCurrent().getPage().open("disease/"+disease.getOmimID());
            });
            return row;
        }));
    }

    public MakeDiseasesList(User user , List<Disease> diseasesList, H1 bigTitle, @Autowired UserService userService){
        setWidth("100%");
        setRenderer(new ComponentRenderer<>(disease -> {
            HorizontalLayout row = new HorizontalLayout();
            row.setClassName("row");
            row.setAlignItems(FlexComponent.Alignment.START);

            Span name = new Span(disease.getDiseaseName());
            Span category = new Span(disease.getClassification().replaceAll("\\|\\$"," , "));
            category.getStyle()
                    .set("color", "var(--lumo-secondary-text-color)")
                    .set("font-size", "var(--lumo-font-size-s)");

            VerticalLayout personalDetails = new VerticalLayout(name, category);
            Hr line = new Hr();
            line.setWidth("80%");
            line.getStyle().set("margin","1rem 10%").set("color","black");
            personalDetails.add(line);
            personalDetails.setPadding(false);
            personalDetails.setSpacing(false);
            personalDetails.setWidth("100%");
            personalDetails.getStyle().set("margin-top","5px").set("padding-left","5px");

            Button remove = new Button(new Icon(VaadinIcon.TRASH));
            remove.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            remove.addThemeVariants(ButtonVariant.LUMO_ERROR);
            remove.addClickListener(event -> {
                diseasesList.remove(disease);
                user.setDiseases(diseasesList);
                userService.update(user);
                bigTitle.setText("Diseases saved (" + diseasesList.size() + "):");
                setItems(diseasesList);
            });
            Tooltip tooltipRemove = new Tooltip();
            tooltipRemove.add(VaadinIcon.INFO_CIRCLE.create());
            tooltipRemove.add(new Span(" Remove " + disease.getDiseaseName() + " from My list!"));
            tooltipRemove.attachToComponent(remove);
            tooltipRemove.setPosition(TooltipPosition.TOP);
            tooltipRemove.setAlignment(TooltipAlignment.LEFT);
            tooltipRemove.setThemeName("light");
            tooltipRemove.getStyle().set("margin","0px 0px 0px 5px");
            VerticalLayout operations = new VerticalLayout(remove, tooltipRemove);
            operations.setPadding(false);
            operations.setSpacing(false);
            operations.setWidth("auto");

            row.add(personalDetails,operations);
            personalDetails.addClickListener(event -> {
                UI.getCurrent().getPage().open("disease/"+disease.getOmimID());
            });
            return row;
        }));
    }
}
