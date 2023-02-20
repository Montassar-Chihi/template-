package com.bioinnovate.PreMediT.views.search.SearchUtils;

import com.bioinnovate.PreMediT.backend.entities.Disease;
import com.bioinnovate.PreMediT.backend.entities.Gene;
import com.bioinnovate.PreMediT.backend.entities.Mutation;
import com.bioinnovate.PreMediT.backend.services.DiseaseService;
import com.bioinnovate.PreMediT.backend.services.GeneService;
import com.bioinnovate.PreMediT.backend.services.MutationService;

import java.util.ArrayList;
import java.util.List;

public class SearchEngine {
    public static  List searchList(String searchTerm,String searchField,DiseaseService diseaseService, GeneService geneService, MutationService mutationService) {
        List finalList;
        if (searchField.equalsIgnoreCase("Diseases")){

            List listByName = diseaseService.searchByExpression(searchTerm);

            List<Gene> genes = geneService.searchByName(searchTerm);
            List listByGene = new ArrayList<>();
            for (Gene gene:genes){
                List<Disease> tempoList = diseaseService.searchByGene(gene);
                listByGene.addAll(tempoList);
            }

            List<Mutation> mutations = mutationService.findMutationsByName(searchTerm);
            List listByMutations = new ArrayList<>();
            for (Mutation mutation:mutations){
                List<Disease> tempoList = diseaseService.findDiseasesByMutation(mutation);
                listByMutations.addAll(tempoList);
            }

            if (listByName.isEmpty() && listByGene.isEmpty() && listByMutations.isEmpty()) {
                finalList = listByGene;
            }else{
                if (listByName.isEmpty()) {
                    listByName = diseaseService.findAll();
                }
                if (listByGene.isEmpty()) {
                    listByGene = diseaseService.findAll();
                }
                if (listByMutations.isEmpty()) {
                    listByMutations = diseaseService.findAll();
                }
                finalList = intersectionDiseases(listByName, listByGene,listByMutations);
            }

        }else if (searchField.equalsIgnoreCase("Genes")){

            List<Disease> diseases = diseaseService.searchByExpression(searchTerm);

            List listByDisease = new ArrayList<>();
            for (Disease disease:diseases){
                List<Gene> tempoList = geneService.findGeneByDiseases(disease);
                listByDisease.addAll(tempoList);
            }

            List<Mutation> mutations = mutationService.findMutationsByName(searchTerm);
            List listByMutations = new ArrayList<>();
            for (Mutation mutation:mutations){
                List<Gene> tempoList = geneService.searchByMutations(mutation);
                listByMutations.addAll(tempoList);
            }

            List listByName = geneService.searchByName(searchTerm);

            if(listByDisease.isEmpty() && listByName.isEmpty() && listByMutations.isEmpty()){
                finalList = listByDisease;
            }else{
                if (listByDisease.isEmpty()) {
                    listByDisease = geneService.findAll();
                }
                if (listByName.isEmpty()) {
                    listByName = geneService.findAll();
                }
                if (listByMutations.isEmpty()) {
                    listByMutations = geneService.findAll();
                }
                finalList = intersectionGenes(listByDisease, listByName,listByMutations);
            }

        }else{
            List listByName = mutationService.findMutationsByName(searchTerm);

            List<Gene> genes = geneService.searchByName(searchTerm);
            List listByGene = new ArrayList<>();
            for (Gene gene:genes){
                List<Mutation> tempoList = mutationService.findMutationsByGene(gene);
                listByGene.addAll(tempoList);
            }

            List<Disease> diseases = diseaseService.searchByExpression(searchTerm);
            List listByDisease = new ArrayList<>();
            for (Disease disease:diseases){
                List<Mutation> tempoList = mutationService.findMutationsByDisease(disease);
                listByDisease.addAll(tempoList);
            }

            if(listByDisease.isEmpty() && listByGene.isEmpty() && listByName.isEmpty()){
                finalList = listByDisease;
            }else {
                if (listByName.isEmpty()) {
                    listByName = mutationService.findAll();
                }
                if (listByDisease.isEmpty()) {
                    listByDisease = mutationService.findAll();
                }
                if (listByGene.isEmpty()) {
                    listByGene = mutationService.findAll();
                }
                finalList = intersectionMutations(listByName,listByDisease,listByGene);
            }
        }
        return finalList;
    }
    public static List<Disease> intersectionDiseases(List<Disease> list1, List<Disease> list2, List<Disease> list3) {
        List<Disease> list = new ArrayList<>();
        for (Disease disease : list1) {
            if(list2.contains(disease) && list3.contains(disease)) {
                list.add(disease);
            }
        }
        return list;
    }
    public static List<Gene> intersectionGenes(List<Gene> list1, List<Gene> list2, List<Gene> list3) {
        List<Gene> list = new ArrayList<>();
        for (Gene gene : list1) {
            if(list2.contains(gene) && list3.contains(gene)) {
                list.add(gene);
            }
        }
        return list;
    }
    public static List<Mutation> intersectionMutations(List<Mutation> list1, List<Mutation> list2, List<Mutation> list3) {
        List<Mutation> list = new ArrayList<>();
        for (Mutation mutation : list1) {
            if(list2.contains(mutation) && list3.contains(mutation)) {
                list.add(mutation);
            }
        }
        return list;
    }

}
