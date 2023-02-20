package com.bioinnovate.PreMediT.views.diseases.diseaseUtils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import static com.bioinnovate.PreMediT.utils.components.ContactUs.contact;

public class DiseasesDisconnectedView {
    public static void buildDiseasesUIWhenDisconnected(Div div){

        H1 name = new H1();
        name.setText("PreMediT: Diseases' Database");
        name.setId("diseases-view-title");
        Image logo = new Image("/images/logo.png","logo");
        logo.setId("diseases-view-logo");
        Div stylingDiv = new Div();
        stylingDiv.setWidthFull();
        HorizontalLayout hLayout = new HorizontalLayout(name,stylingDiv,logo);
        hLayout.setId("diseases-view-hLayout");
        Span description = new Span("Using This tool, the user can access our database that contains information about rare diseases that one can find in Tunisia. They can use multiple filters to make it easy to find a specific disease/diseases. References are provided along with every single information. ");
        Span description2 = new Span("To get access to our analysis tool please check our offers and sign up now or contact us directly!");
        description.setId("diseases-view-description");
        description.setId("diseases-view-description-2");
        VerticalLayout v = new VerticalLayout(hLayout,description,description2);
        v.setWidth("60%");

        Image someImage = new Image("/images/diseaseImage.jpeg","image");
        someImage.setId("diseases-view-sideImage");
        VerticalLayout sideLayout = new VerticalLayout(someImage);
        sideLayout.setWidth("40%");

        HorizontalLayout layout = new HorizontalLayout(v,sideLayout);
        layout.setId("diseases-view-layout");
        Button contactUs = new Button("Contact Us");
        contactUs.setId("diseases-view-contactUs-button");

        contactUs.addClickListener(event -> contact(div));

        Anchor login = new Anchor("login","Have an account already? \n login now!");
        login.setId("diseases-view-login-anchor");
        div.add(layout);
        VerticalLayout verticalLayout = new VerticalLayout(contactUs,login);
        div.add(verticalLayout);

    }
}
