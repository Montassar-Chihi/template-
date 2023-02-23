package com.bioinnovate.Template.utils.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

public class ContactUs {
    public static void contact(Div div){
        Dialog dialog = new Dialog();
        dialog.open();
        dialog.setWidth("50%");
        Button closeDialog = new Button(new Icon(VaadinIcon.CLOSE));
        closeDialog.addClickListener(e -> dialog.close());
        closeDialog.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        closeDialog.getStyle().set("float","right").set("color","red");
        dialog.add(closeDialog);
        H1 title = new H1("Contact Us");
        title.getStyle().set("margin","10px auto");
        dialog.add(title);
        Div content = new Div();

        TextField name = new TextField("Full Name");
        name.setWidthFull();
        TextField company = new TextField("Company");
        company.setWidthFull();
        EmailField emailField = new EmailField("Email");
        emailField.setWidthFull();
        TextField reason = new TextField("Motivation");
        reason.setWidthFull();
        TextArea message = new TextArea("Message");
        message.setWidthFull();

        VerticalLayout verticalLayout = new VerticalLayout(name,company,emailField,reason,message);
        Button send = new Button("Send");
        send.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        send.setWidthFull();
        send.setIcon(new Icon(VaadinIcon.CHECK));
        send.addClickListener(event1 -> dialog.close());
        Button cancel = new Button("Cancel");
        cancel.setWidthFull();
        cancel.setIcon(new Icon(VaadinIcon.CLOSE));
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancel.addClickListener(event1 -> dialog.close());
        HorizontalLayout buttonLayout = new HorizontalLayout(send,cancel);
        content.add(verticalLayout,buttonLayout);
        dialog.add(content);
        System.out.println(dialog.isOpened());
        div.add(dialog);
    }
}
