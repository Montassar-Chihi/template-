package com.bioinnovate.PreMediT.views.home;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NcbiApi {
    public static List<Journal> getJournals() throws UnsupportedEncodingException {
        String searchTerm = "(\"rare diseases\") OR (\"genes\" OR \"mutations\") AND (\"Tunisia\")";
        String searchTermEncoded = java.net.URLEncoder.encode(searchTerm, "UTF-8");
        String apiUrl = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&term=" + searchTermEncoded + "&retmax=100&format=json&usehistory=y";
        List<Journal> journalList = new ArrayList<>();
        try {
            URL url = new URL(apiUrl);
            System.out.println(url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output = "";
            String line;
            while ((line = br.readLine()) != null) {
                output += line;
            }

            JSONObject searchResults = new JSONObject(output);
            String webEnv = searchResults.getJSONObject("esearchresult").getString("webenv");
            String queryKey = searchResults.getJSONObject("esearchresult").getString("querykey");
            int count = 10;

            apiUrl = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&query_key=" + queryKey + "&WebEnv=" + webEnv + "&retmax=" + count;
            url = new URL(apiUrl);
            System.out.println(url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            output = "";
            while ((line = br.readLine()) != null) {
                output += line;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(output)));
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("PubmedArticle");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String title = element.getElementsByTagName("ArticleTitle").item(0).getTextContent();
                    String abstractText = element.getElementsByTagName("AbstractText").item(0).getTextContent();
                    String author = element.getElementsByTagName("Author").item(0).getTextContent();
                    String date = element.getElementsByTagName("PubDate").item(0).getTextContent();
                    String link = element.getElementsByTagName("PMID").item(0).getTextContent();
                    String type = element.getElementsByTagName("PublicationTypeList").item(0).getTextContent();
                    String subject = "";
                    Journal journal = new Journal(title,link, type, author,subject,abstractText, date);
                    journalList.add(journal);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return journalList;
    }
}
