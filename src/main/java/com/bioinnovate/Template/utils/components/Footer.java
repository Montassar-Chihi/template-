package com.bioinnovate.Template.utils.components;

import com.bioinnovate.Template.views.Login.LoginView;
import com.bioinnovate.Template.views.home.HomeView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinSession;

import static com.bioinnovate.Template.utils.components.ContactUs.contact;

public class Footer extends VerticalLayout {

    private final Div stylingDiv;
    private final Button contactUs;
    private final HorizontalLayout mainLayout;
    private HorizontalLayout copyRightLayout;
    private HorizontalLayout socialMediaLayout;
    private HorizontalLayout createLogoLayout;

    public Footer(Div div){
        Div innerFooter = new Div();
        innerFooter.getStyle().set("border-radius","0 !important");
        VaadinSession vaadinSession = VaadinSession.getCurrent();

        contactUs = new Button("Contact Us");
        contactUs.addClickListener(event -> contact(div));
        contactUs.getStyle().set(
                "width","10rem").set(
                "margin","0 auto").set(
                "color","#d11818").set(
                "border","1px solid red").set(
                "border-bottom","none").set("position","relative").set("bottom","-4px");

        stylingDiv = new Div();
        stylingDiv.getStyle().set("border","1px solid #cb3434").set("width","90%").set("margin","auto").set("position","relative").set("bottom","-1px");

        VerticalLayout layout1 = new VerticalLayout(createLogoLayout(),createSocialMediaLayout(),createCopyRightLayout());
        layout1.setWidth("50%");
        layout1.getStyle().set("background","transparent");

        Span titleQuick = new Span("Quick Links");
        titleQuick.getStyle().set(
                "color","white").set(
                "font-family","Eczar").set(
                "font-size","20px");
        Span home = new Span();
        home.setText("Home");
        home.addClickListener(event -> UI.getCurrent().navigate(HomeView.class));
        Span myList = new Span();
        if (vaadinSession.getAttribute("user") != null){
            myList.setText("My List");
            myList.addClickListener(event -> {});
        }else {
            myList.setText("Login");
            myList.addClickListener(event -> UI.getCurrent().navigate(LoginView.class));
        }
        Span userGuid = new Span();
        userGuid.setText("User Guide");
        userGuid.addClickListener(event -> {});
        Span downloads = new Span();
        if (vaadinSession.getAttribute("user") != null){
            downloads.setText("Downloads");
            downloads.addClickListener(event -> {});
        }else {
            downloads.setText("Data Access");
            downloads.addClickListener(event -> {});
        }
        VerticalLayout layout2 = new VerticalLayout(titleQuick,home,myList,userGuid,downloads);
        layout2.setWidth("25%");
        layout2.setId("footer");
        layout2.getStyle().set("background","transparent");

        Span titleAbout = new Span("Know Us More");
        titleAbout.getStyle().set(
                "color","white").set(
                "font-family","Eczar").set(
                "font-size","20px");
        Span faq = new Span("FAQ");
        faq.addClickListener(event ->{});
        Span terms = new Span("Terms & Use Conditions");
        terms.addClickListener(event -> {});
        Span about = new Span("About Us");
        about.addClickListener(event -> {});
        VerticalLayout layout3 = new VerticalLayout(titleAbout,faq,about,terms);
        layout3.setWidth("25%");
        layout3.setId("footer");
        layout3.getStyle().set("background","transparent");

        Div stylingDiv1 = new Div();
        Div stylingDiv2 = new Div();
        stylingDiv1.getStyle().set("width","1px").set("border-left","1px solid white").set("margin","1rem");
        stylingDiv2.getStyle().set("width","1px").set("border-left","1px solid white").set("margin","1rem");

        mainLayout = new HorizontalLayout(layout1,stylingDiv1,layout2,stylingDiv2,layout3);
        mainLayout.setWidthFull();
        mainLayout.getStyle().set("margin","0").set("background","var(--general-color)").set("box-shadow","var(--lumo-box-shadow-l)").set("position","sticky").set("bottom","-100vh");
        innerFooter.getStyle().set("margin","0").set("padding","0");
        add(contactUs,stylingDiv,mainLayout);
        getStyle().set("padding","0").set("margin-top","50px").set("background","transparent");
    }

    private HorizontalLayout createCopyRightLayout(){
        Image copyright = new Image("/images/copyright.png","copyright");
        copyright.setWidth("30px");
        copyright.getStyle().set("margin","auto").set("margin-right","0px");
        Span text = new Span("All right reserved - 2022");
        text.getStyle().set("font-size","13px").set("margin","auto auto auto 0px").set("font-weight","bold");
        copyRightLayout = new HorizontalLayout(copyright,text);
        copyRightLayout.setWidthFull();
        copyRightLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        copyRightLayout.getStyle().set("background","transparent");
        return copyRightLayout;
    }

    private HorizontalLayout createLogoLayout(){
        Image logo = new Image("/images/logo.png","logo");
        logo.getStyle().set("max-width","90px").set("margin","0px calc(20% - 30px) 0px 2rem");
        H1 name = new H1();
        name.setText("PreMediT");
        name.getStyle().set(
                "color","#cb3434").set(
                "font-family","Eczar").set(
                "width","35%").set(
                "font-size","30px").set("margin-left","calc(25% - 30px)");
        createLogoLayout = new HorizontalLayout(name,logo);
        createLogoLayout.setWidthFull();
        createLogoLayout.getStyle().set("background","transparent").set("margin","2rem 0");
        return createLogoLayout;
    }

    private HorizontalLayout createSocialMediaLayout(){
        Image facebook = new Image("/images/facebook.png","facebook page");
        facebook.setWidth("22.5px");
        facebook.addClickListener(event -> UI.getCurrent().getPage().executeJs("window.location.replace('home')"));
        facebook.getStyle().set("margin","auto");
        Image linkedIn = new Image("/images/linkedin.png","linkedin page");
        linkedIn.setWidth("30px");
        linkedIn.addClickListener(event -> UI.getCurrent().getPage().executeJs("window.location.replace('home')"));
        linkedIn.getStyle().set("margin","auto");
        Image twitter = new Image("/images/twitter.png","twitter page");
        twitter.setWidth("22.5px");
        twitter.addClickListener(event -> UI.getCurrent().getPage().executeJs("window.location.replace('home')"));
        twitter.getStyle().set("margin","auto");
        Image email = new Image("/images/email.png","email");
        email.addClickListener(event -> UI.getCurrent().getPage().executeJs("window.location.replace('home')"));
        email.setWidth("22.5px");
        email.getStyle().set("margin","auto");
        Span text = new Span("Connect with us");
        text.setWidth("30%");
        text.getStyle().set("margin","auto").set("font-weight","bold");

        socialMediaLayout = new HorizontalLayout(text,facebook,linkedIn,twitter,email);
        socialMediaLayout.setId("social-media-layout");
        socialMediaLayout.setWidthFull();
        socialMediaLayout.getStyle().set("margin","auto").set("margin-top","5px").set("background","transparent");
        socialMediaLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        return socialMediaLayout;
    }
}
