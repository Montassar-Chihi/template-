package com.bioinnovate.PreMediT.views.home;

import com.bioinnovate.PreMediT.views.main.MainView;
import com.bioinnovate.PreMediT.views.signUp.SignUpView;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.VaadinSession;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.bioinnovate.PreMediT.utils.components.ContactUs.contact;
import static com.bioinnovate.PreMediT.views.home.NcbiApi.getJournals;


@Route(value = "", layout = MainView.class)
@RouteAlias(value = "home" , layout = MainView.class)
@PageTitle("Home")
@CssImport("./styles/views/home/home-view.css")
public class HomeView extends Div {

    private final HorizontalLayout layout;
    private final VerticalLayout bodyOfArticle;
    VaadinSession vaadinSession;

    public HomeView(){

        H1 name = new H1();
        name.setText("PreMediT");
        name.setClassName("premedit");

        Image logo = new Image("/images/logo.png","logo");
        logo.setClassName("premedit-logo");
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
        description.setClassName("premedit-description");

        TextField searchBar = new TextField();
        searchBar.setPlaceholder("Search for genes/mutations/disease ...");
        searchBar.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchBar.setWidthFull();

        VerticalLayout mainLayout = new VerticalLayout(hLayout,description);
        mainLayout.setWidth("65%");
        mainLayout.setPadding(true);

        Div hr = new Div();
        hr.setClassName("hr");

        Image someImage = new Image("/images/sideImage.jpg","some image");
        someImage.setId("home-genes-image");
        Span text = new Span("To have unlimited access to all our data as well as our analysis tools, check our offers and sign up now!");
        Button signUp = new Button("Sign up");
        signUp.addClickListener(event -> getUI().get().navigate(SignUpView.class));
        signUp.setId("home-signup");
        Anchor login = new Anchor("login","Have an account already? \n login now!");
        login.getStyle().set("text-decoration","none").set("color","black").set("margin","auto");
        Button contactUs = new Button("Contact Us");
        contactUs.setMinHeight("40px");
        contactUs.setId("home-signup");
        contactUs.addClickListener(event -> contact(this));
        VerticalLayout sideLayout;
        vaadinSession = VaadinSession.getCurrent();
        if (vaadinSession.getAttribute("logged") != null) {
            sideLayout = new VerticalLayout(someImage,contactUs);
        }else {
            sideLayout = new VerticalLayout(someImage,text,signUp,login);
        }
        sideLayout.setWidth("35%");
        sideLayout.setPadding(true);
        layout = new HorizontalLayout(mainLayout, hr, sideLayout);
        layout.setId("home-description-layout");
        add(layout);

        Div redBand3 = new Div();
        redBand3.setClassName("left-band");
        Div redBand4 = new Div();
        redBand4.setClassName("right-band");
        H1 title1 = new H1("Did you know ?");
        title1.setClassName("home-big-titles");
        HorizontalLayout titleBand = new HorizontalLayout(redBand3,title1,redBand4);
        titleBand.setClassName("home-titles-layout");
        titleBand.getStyle().set("background","transparent");

        Span number = new Span("600.000 affected");
        number.setId("home-number");
        Div value = new Div(number);
        value.setId("home-value");
        Span descriptionOfValue = new Span("About 600,000 Tunisians suffer from a rare disease, i.e. one in twenty, Hela Boudabous, president of the Tunisian Association of Lysosomal Diseases (ATML) said at 2019.\n" +
                "\n" +
                "Rare diseases are often chronic, progressive and generally serious. Their expression is extremely diverse: neuromuscular, metabolic, infectious, immune and cancerous.\n" +
                "\n" +
                "More than 400 rare diseases are recorded in Tunisia, she said at a press conference held in Tunis on the Rare Disease Day observed on the last day of February each year.\n" +
                "\n" +
                "She added that 70% of the medical researches in Tunisia relate to rare diseases.\n" +
                "\n" +
                "Among the rare diseases, the president of the ATML spoke about lysosomal diseases that are generally genetic and affect both adults and children.\n" +
                "\n" +
                "According to Hela Boudabous, rare diseases remain unknown in Tunisia, which explains the error and the delay of diagnosis");
        descriptionOfValue.setId("home-description-of-value");

        HorizontalLayout pourcentage = new HorizontalLayout(value,descriptionOfValue);
        pourcentage.setId("home-percentage");

        Image image = new Image("/images/data.jpg","data about diseases' category");
        image.setId("diseases-graph");
        H3 imageTitle = new H3("Genetic diseases in Tunisia");
        imageTitle.setId("diseases-title");
        Anchor source = new Anchor("https://www.ncbi.nlm.nih.gov/pmc/articles/PMC8617973/","source : ncbi");
        source.setId("diseases-graph-source");
        Span someParagraph = new Span("Those classification revealed that congenital malformations, deformations, and chromosomal abnormalities are the most commonly reported (29.54%). Endocrine, nutritional, and metabolic diseases are the next most frequent group of diseases (22%), followed by diseases of the nervous system (15.45%) (Figure 2). We report a slight increase in the following disease groups: diseases of the ear and mastoid process and diseases of the eye and adnexa. 243 new genetic disease were discovered since 2011 to the total of 589.");
        someParagraph.setId("diseases-graph-description");
        VerticalLayout paragraphLayout = new VerticalLayout(imageTitle,someParagraph);
        paragraphLayout.setId("stats-paragraph-layout");
        HorizontalLayout statsSmallLayout = new HorizontalLayout(paragraphLayout,image);
        statsSmallLayout.setId("stats-small-layout");
        VerticalLayout statsLayout = new VerticalLayout(titleBand,pourcentage,statsSmallLayout,source);
        statsLayout.setId("stats-layout");

        Div redBand1 = new Div();
        redBand1.setClassName("left-band");
        Div redBand2 = new Div();
        redBand2.setClassName("right-band");
        H1 title = new H1("Latest Articles");
        title.setClassName("home-big-titles");
        HorizontalLayout titleBand2 = new HorizontalLayout(redBand1,title,redBand2);
        titleBand2.setClassName("home-titles-layout");

        bodyOfArticle = new VerticalLayout();
        bodyOfArticle.setWidth("60%");
        Div hr1 = new Div();
        hr1.setClassName("hr");
        VerticalLayout articles = createArticles();
        articles.setWidth("35%");
        HorizontalLayout articlesLayout = new HorizontalLayout(articles,bodyOfArticle);
        articlesLayout.setId("diseases-genetic-articles-layout");

        VerticalLayout all = new VerticalLayout(layout,statsLayout,titleBand2,articlesLayout);
        all.setSizeFull();
        all.setId("home-page");
        add(all);

    }

    private VerticalLayout createArticles(){
        H1 articlesTitle = new H1();
        articlesTitle.setText("Related Articles");
        articlesTitle.setWidthFull();
        articlesTitle.getStyle().set("align-self","center").set(
                "color","#cb3434").set(
                "font-family","Eczar").set(
                "font-size","2.6rem").set(
                "text-align","center");
        ListBox<Journal> journals = new ListBox<>();
        journals.setSizeFull();
        journals.setRenderer(new ComponentRenderer<>(journal -> {
            HorizontalLayout row = new HorizontalLayout();
            row.setAlignItems(FlexComponent.Alignment.CENTER);

            Avatar avatar = new Avatar();
            if (journal.getType().equalsIgnoreCase("magazine")){
                avatar.setImage("/images/magazine.png");
            } else if (journal.getType().equalsIgnoreCase("book")){
                avatar.setImage("/images/book.png");
            } else {
                avatar.setImage("/images/article.png");
            }
            avatar.setWidth("50px");

            Span name = new Span(journal.getName());
            Span author = new Span(journal.getAuthor());
            Span date = new Span(journal.getDate());
            author.getStyle()
                    .set("color", "var(--lumo-secondary-text-color)")
                    .set("font-size", "var(--lumo-font-size-s)");
            date.getStyle()
                    .set("color", "var(--lumo-secondary-text-color)")
                    .set("font-size", "var(--lumo-font-size-s)");

            VerticalLayout personalDetails = new VerticalLayout(name, author, date);
            personalDetails.setPadding(false);
            personalDetails.setSpacing(false);
            personalDetails.setWidth("65%");

            Span subject = new Span(journal.getSubject());
            subject.getStyle()
                    .set("color", "var(--lumo-secondary-text-color)")
                    .set("font-size", "var(--lumo-font-size-s)")
                    .set("align-self","flex-end");
            subject.setWidth("30%");

            row.add(avatar,personalDetails,subject,new Hr(),new Hr());
            row.addClickListener(event -> openJournal(journal));
            row.setId("journal");

            row.setSizeFull();
            return row;
        }));
        List<Journal> journalList = null;
        try {
            journalList = getJournals();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        journals.setItems(journalList);
        openJournal(journalList.get(0));
        VerticalLayout finalLayout = new VerticalLayout(journals);
        finalLayout.getStyle().set("contain","strict");
        return finalLayout;
    }

    private void openJournal(Journal journal){
        bodyOfArticle.removeAll();
        Span dateAndSubject = new Span(journal.getSubject()+" | "+journal.getDate());
        dateAndSubject.setWidthFull();
        dateAndSubject.getStyle().set("display","block");
        H1 title = new H1(journal.getName());
        title.getStyle().set("align-self","center").set(
                "font-family","Eczar").set(
                "font-size","2.6rem");
        Span author = new Span(journal.getAuthor());
        author.getStyle().set("align-self","center").set(
                "font-family","Merriweather").set(
                "font-size","1rem").set(
                "margin","10px 0").set("display","block");
        Span abstractTitle = new Span("Abstract");
        abstractTitle.getStyle().set("align-self","center").set(
                "font-family","BlinkMacSystemFont,-apple-system,Segoe UI,Roboto,Oxygen,Ubuntu,Cantarell,Fira Sans,Droid Sans,Helvetica Neue,sans-serif").set(
                "font-size","1rem").set(
                "margin","5px 0").set("display","block");
        Span paragraph = new Span(journal.getAbstractOfJournal());
        paragraph.getStyle().set("align-self","center").set(
                "font-family","BlinkMacSystemFont,-apple-system,Segoe UI,Roboto,Oxygen,Ubuntu,Cantarell,Fira Sans,Droid Sans,Helvetica Neue,sans-serif").set(
                "font-size","1rem").set(
                "margin","5px 0").set("display","block");
        Span link = new Span("To read the whole article please check this link : ");
        link.getStyle().set("align-self","center").set(
                "font-family","BlinkMacSystemFont,-apple-system,Segoe UI,Roboto,Oxygen,Ubuntu,Cantarell,Fira Sans,Droid Sans,Helvetica Neue,sans-serif").set(
                "font-size","1rem").set(
                "margin","5px 0").set("display","block");
        Anchor realLink = new Anchor("https://pubmed.ncbi.nlm.nih.gov/"+journal.getLink(),"Full Article!");
        realLink.setTarget("_blank");
        realLink.setVisible(true);
        realLink.getStyle().set("text-decoration","none").set("display","block").set("color","black").set("align-self","center");
        bodyOfArticle.add(dateAndSubject,title,author,abstractTitle,paragraph,link,realLink);
    }
}
