package com.freshdirect.smartstore.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Trigger;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.dsl.CompileException;
import com.freshdirect.smartstore.fdstore.ScoreProvider;
import com.freshdirect.smartstore.sampling.RankedContent;
import com.freshdirect.smartstore.scoring.DataAccess;
import com.freshdirect.smartstore.scoring.DataGenerator;
import com.freshdirect.smartstore.scoring.Score;
import com.freshdirect.smartstore.scoring.ScoringAlgorithm;

/**
 * This recommendation service returns items based on a domain specific language.
 * 
 * @author zsombor
 *
 */
public class ScriptedRecommendationService extends AbstractRecommendationService {
    public final static String   CKEY_GENERATOR = "generator";
    public final static String   CKEY_SCORING = "scoring";

    private DataGenerator dataGenerator;
    private ScoringAlgorithm scoring;

    public ScriptedRecommendationService(Variant variant, String generator, String scoring) throws CompileException {
        super(variant);
        if (generator == null) {
            throw new NullPointerException("generator");
        }
        this.dataGenerator = GlobalCompiler.getInstance().createDataGenerator(generator);
        if (scoring != null) {
            this.scoring = GlobalCompiler.getInstance().createScoringAlgorithm(scoring);
        }
    }
    
    public ScriptedRecommendationService(Variant variant, String generator) throws CompileException {
        this(variant, generator, null);
    }
    
    public ScriptedRecommendationService(Variant variant) throws CompileException {
        this(variant, variant.getServiceConfig().get(CKEY_GENERATOR), variant.getServiceConfig().get(CKEY_SCORING));
    }

    public List recommendNodes(Trigger trigger, SessionInput input) {
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
                // if multiple number is the returning statement, we can't interpret as a probability.
                TreeSet scores = new TreeSet();
                for (Iterator iter = result.iterator(); iter.hasNext();) {
                    ContentNodeModel contentNode = (ContentNodeModel) iter.next();
                    double[] values = dataAccess.getVariables(userId, contentNode, variableNames);
                    double[] score = scoring.getScores(values);
                    Score sc = new Score(contentNode, score);
                    scores.add(sc);
                }
                int max = scores.size();
                int i = 0;
                for (Iterator iter = scores.iterator(); iter.hasNext();) {
                    Score sc = (Score) iter.next();
                    rankedContents.add(new RankedContent.Single(max + 1 - i, sc.getNode()));
                    i++;
                }
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
        List sample = RankedContent.getContentNodeModel(getSampler(input.isNoShuffle()).sample(rankedContents, input.getCartContents(), rankedContents.size()));
        return sample;
    }

    
    /**
     * Collect needed factors into the buffer
     * @param buffer Collection<String>
     * @return the original buffer.
     */
    public Collection collectFactors(Collection buffer) {
        buffer.addAll(dataGenerator.getFactors());
        if (scoring!=null) {
            String[] variableNames = scoring.getVariableNames();
            for (int i=0;i<variableNames.length;i++) {
                buffer.add(variableNames[i]);
            }
        }
        return buffer;
    }
    
    protected Map appendConfiguration(Map configMap) {
        configMap.put(CKEY_GENERATOR, this.dataGenerator.toString());
        if (scoring!=null) {
            configMap.put(CKEY_SCORING, scoring.toString());
        }
        return super.appendConfiguration(configMap);
    }
    
    public String getDescription() {
        return "generator:"+this.dataGenerator+", scoring:"+this.scoring;
    }

}
