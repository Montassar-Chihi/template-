package com.bioinnovate.Template.views.search.SearchUtils;

import com.bioinnovate.Template.backend.services.DiseaseService;
// import com.bioinnovate.Template.backend.services.GeneService;

import java.util.List;

public class SearchEngine {
    public static  List searchList(String searchTerm,String searchField,DiseaseService diseaseService) {
        List finalList;
        finalList = diseaseService.searchByExpression(searchTerm);

        return finalList;
    }

}
