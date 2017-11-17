package com.freshdirect.storeapi.search.service;

import java.util.ArrayList;
import java.util.List;

public class ApproximationsPermuter {

    private final List<String> term;

    public ApproximationsPermuter(List<String> term) {
        super();
        this.term = term;
    }

    public List<List<List<String>>> permute() {
        List<List<List<String>>> permutations = new ArrayList<List<List<String>>>();
        List<String> tokens = term;

        // one word missing permutation
        List<List<String>> permutation = new ArrayList<List<String>>();
        for (int i = 0; i < tokens.size(); i++) {
            List<String> permTokens = new ArrayList<String>();
            for (int k = 0; k < tokens.size(); k++)
                if (k != i)
                    permTokens.add(tokens.get(k));
            permutation.add(new ArrayList<String>(permTokens));
        }
        permutations.add(permutation);

        // two words missing permutation
        if (tokens.size() > 3) {
            permutation = new ArrayList<List<String>>();
            for (int i = 0; i < tokens.size(); i++)
                for (int j = i + 1; j < tokens.size(); j++) {
                    List<String> permTokens = new ArrayList<String>();
                    for (int k = 0; k < tokens.size(); k++)
                        if (k != i && k != j)
                            permTokens.add(tokens.get(k));
                    permutation.add(new ArrayList<String>(permTokens));
                }
            permutations.add(permutation);
        }

        return permutations;
    }
}
