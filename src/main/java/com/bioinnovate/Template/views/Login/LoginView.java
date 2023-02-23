package com.bioinnovate.Template.views.Login;

import com.bioinnovate.Template.backend.entities.User;
import com.bioinnovate.Template.backend.services.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

@Route("login")
@PageTitle("Login | PreMediT")
public class LoginView extends Div {

    private VaadinSession vaadinSession;
    private LoginForm login = new LoginForm();
    private  User user;
    public LoginView(@Autowired UserService userService){

        vaadinSession = VaadinSession.getCurrent();
        try{
            user = userService.findByEmail(vaadinSession.getAttribute("user").toString());
            UI.getCurrent().getPage().executeJs("window.location.replace('home')");
        }catch (Exception exception) {

            getStyle().set("padding", "0").set("margin", "0");

            VerticalLayout loginLayout = new VerticalLayout();
            H1 name = new H1();
            name.setText("PreMediT");
            name.getStyle().set("align-self", "center").set(
                    "color", "#cb3434").set(
                    "font-family", "Eczar").set(
                    "font-size", "30px").set(
                    "text-align", "center");
            loginLayout.addClassName("login-view");
            loginLayout.setHeightFull();
            loginLayout.setWidthFull();
            loginLayout.setAlignItems(FlexComponent.Alignment.CENTER);
            loginLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
            login.setI18n(createLoginI18n());
            login.addLoginListener(event -> {
                vaadinSession = VaadinSession.getCurrent();
                User user = userService.findByEmail(event.getUsername());
                if (user == null) {
                    Notification.show("Wrong Password and/or Email, please verify !").addThemeVariants(NotificationVariant.LUMO_ERROR);
                } else {
                    vaadinSession.setAttribute("logged", "yes");
                    vaadinSession.setAttribute("user", user.getEmail());
                    UI.getCurrent().getPage().executeJs("window.location.replace('home')");
                }
            });
            Image logo = new Image("images/logo.png", "PreMediT logo");
            logo.setMaxWidth("133px");
            loginLayout.add(logo, name, login);

            VerticalLayout imageLayout = new VerticalLayout();
            imageLayout.getStyle().set("box-shadow", " 0 0px 5px 10px grey").set("background-image", "url(/images/sideImage.jpg)").set("background-repeat", "no-repeat").set("background-size", "cover");

            HorizontalLayout mainLayout = new HorizontalLayout(imageLayout, loginLayout);
            mainLayout.getStyle().set("padding", "0").set("margin", "0").set("width","100vw").set("height","100vh");
            add(mainLayout);
        }
    }

    private LoginI18n createLoginI18n(){

        LoginI18n i18n = LoginI18n.createDefault();

        i18n.getForm().setUsername("Em@il");
        i18n.getForm().setTitle("");
        i18n.getForm().setSubmit("Login");
        i18n.getForm().setPassword("Password");
        i18n.getForm().setForgotPassword("Forgot your password?");
        i18n.getErrorMessage().setTitle("Wrong password/email !");
        i18n.getErrorMessage().setMessage("Wrong password/email !");
        return i18n;
    }
}
