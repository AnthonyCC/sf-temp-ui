package com.freshdirect.smartstore.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.dsl.CompileException;
import com.freshdirect.smartstore.fdstore.FactorRequirer;
import com.freshdirect.smartstore.fdstore.ScoreProvider;
import com.freshdirect.smartstore.sampling.ImpressionSampler;
import com.freshdirect.smartstore.sampling.RankedContent;
import com.freshdirect.smartstore.scoring.DataAccess;
import com.freshdirect.smartstore.scoring.DataGenerator;
import com.freshdirect.smartstore.scoring.OrderingFunction;
import com.freshdirect.smartstore.scoring.ScoringAlgorithm;

/**
 * This recommendation service returns items based on a domain specific language.
 * 
 * @author zsombor
 *
 */
public class ScriptedRecommendationService extends AbstractRecommendationService implements FactorRequirer {
    private DataGenerator dataGenerator;
    private ScoringAlgorithm scoring;

    public ScriptedRecommendationService(Variant variant, ImpressionSampler sampler,
    		boolean catAggr, boolean includeCartItems, String generator, String scoring) throws CompileException {
        super(variant, sampler, catAggr, includeCartItems);
        if (generator == null) {
            throw new IllegalArgumentException("generator cannot be null");
        }
        this.dataGenerator = GlobalCompiler.getInstance().createDataGenerator(generator);
        if (scoring != null) {
            this.scoring = GlobalCompiler.getInstance().createScoringAlgorithm(scoring);
        }
    }
    
    public ScriptedRecommendationService(Variant variant, ImpressionSampler sampler,
    		boolean catAggr, boolean includeCartItems, String generator) throws CompileException {
        this(variant, sampler, catAggr, includeCartItems, generator, null);
    }

    public List recommendNodes(SessionInput input) {
        return recommendNodes(input, ScoreProvider.getInstance());
    }
    
    public List recommendNodes(SessionInput input, DataAccess dataAccess) {
        // generate content node list based on the 'generator' expression.
        List result = dataGenerator.generate(input, dataAccess);

        String userId = input.getCustomerId();
        List rankedContents = new ArrayList(result.size());
        
        if (scoring != null) {
            String[] variableNames = scoring.getVariableNames();
             
            if (scoring.getReturnSize() > 1) {
                
                OrderingFunction orderingFunction = scoring.createOrderingFunction();
                for (Iterator iter = result.iterator(); iter.hasNext();) {
                    ContentNodeModel contentNode = (ContentNodeModel) iter.next();
                    double[] values = dataAccess.getVariables(userId, contentNode, variableNames);
                    double[] score = scoring.getScores(values);
                    orderingFunction.addScore(contentNode, score);
                }
                rankedContents = orderingFunction.getRankedContents();
            } else {
                // one score computed, interpret as 'weight' or probability.
                TreeSet scores = new TreeSet();

                for (Iterator iter = result.iterator(); iter.hasNext();) {
                    ContentNodeModel contentNode = (ContentNodeModel) iter.next();
                    double[] values = dataAccess.getVariables(userId, contentNode, variableNames);
                    double[] score = scoring.getScores(values);
                    scores.add(new RankedContent.Single(score[0], contentNode));
                }
                for (Iterator iter = scores.iterator(); iter.hasNext();) {
                    rankedContents.add(iter.next());
                }
            }
        } else {
            // no scoring, go ahead and prepare for sampling
            int max = result.size()+1;
            int i=0;
            for (Iterator iter = result.iterator();iter.hasNext();) {
                ContentNodeModel sc = (ContentNodeModel) iter.next();
                rankedContents.add(new RankedContent.Single(max - i, sc));
                i++;
            }
        }
        
        // aggregate content nodes
        if (aggregateAtCategoryLevel) {
            rankedContents = aggregateContentList(rankedContents);
        }
        List sample = RankedContent.getContentNodeModel(getSampler(input.isNoShuffle()).sample(rankedContents,
        		includeCartItems ? Collections.EMPTY_SET : input.getCartContents(), rankedContents.size()));
        return sample;
    }

    
    /**
     * Collect needed factors into the buffer
     * @param buffer Collection<String>
     * @return the original buffer.
     */
    public void collectFactors(Collection buffer) {
        buffer.addAll(dataGenerator.getFactors());
        if (scoring!=null) {
            String[] variableNames = scoring.getVariableNames();
            for (int i=0;i<variableNames.length;i++) {
                buffer.add(variableNames[i]);
            }
        }
    }
    
    public String getDescription() {
        return "generator:"+this.dataGenerator+", scoring:"+this.scoring;
    }

}
