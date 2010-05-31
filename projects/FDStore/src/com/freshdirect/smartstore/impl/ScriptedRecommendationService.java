package com.freshdirect.smartstore.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.dsl.CompileException;
import com.freshdirect.smartstore.fdstore.FactorRequirer;
import com.freshdirect.smartstore.sampling.ImpressionSampler;
import com.freshdirect.smartstore.sampling.RankedContent;
import com.freshdirect.smartstore.scoring.CachingDataGenerator;
import com.freshdirect.smartstore.scoring.DataAccess;
import com.freshdirect.smartstore.scoring.DataGenerator;
import com.freshdirect.smartstore.scoring.OrderingFunction;
import com.freshdirect.smartstore.scoring.PrioritizedDataAccess;
import com.freshdirect.smartstore.scoring.ScoringAlgorithm;

/**
 * This recommendation service returns items based on a domain specific
 * language.
 * 
 * @author zsombor
 * 
 */
public class ScriptedRecommendationService extends AbstractRecommendationService implements FactorRequirer {

	private DataGenerator generator;
	private ScoringAlgorithm scoring;

	public ScriptedRecommendationService(Variant variant, ImpressionSampler sampler,
			boolean includeCartItems, String generator) throws CompileException {
		this(variant, sampler, includeCartItems, generator, null);
	}

	public ScriptedRecommendationService(Variant variant, ImpressionSampler sampler,
			boolean includeCartItems, String generator, String scoring) throws CompileException {
		super(variant, sampler, includeCartItems);
		if (generator == null) {
			throw new IllegalArgumentException("generator cannot be null");
		}
		this.generator = GlobalCompiler.getInstance().createDataGenerator(generator);
		if (scoring != null && scoring.trim().length() != 0) {
			this.scoring = GlobalCompiler.getInstance().createScoringAlgorithm(scoring);
		}
	}

	public List<ContentNodeModel> doRecommendNodes(SessionInput input) {
		return recommendNodes(input, new PrioritizedDataAccess(input.getExclusions(), input.isUseAlternatives(), input.isShowTemporaryUnavailable()));
	}

	public List<ContentNodeModel> recommendNodes(SessionInput input, DataAccess dataAccess) {
		// generate content node list based on the 'generator' expression.
		List<ContentNodeModel> result = generator.generate(input, dataAccess);

		String userId = input.getCustomerId();
                PricingContext pricingCtx = input.getPricingContext();
		List<RankedContent.Single> rankedContents;

		boolean aggregatable;
		if (scoring != null && scoring.getReturnSize() > 0) {
			String[] variableNames = scoring.getVariableNames();

			if (scoring.getReturnSize() > 1) {
				OrderingFunction orderingFunction = scoring.createOrderingFunction();
				for (Iterator<ContentNodeModel> iter = result.iterator(); iter.hasNext();) {
					ContentNodeModel contentNode = iter.next();
					double[] values = dataAccess.getVariables(userId, pricingCtx, contentNode, variableNames);
					double[] score = scoring.getScores(values);
					orderingFunction.addScore(contentNode, score);
				}
				rankedContents = orderingFunction.getRankedContents();
				aggregatable = false;
			} else {
				// one score computed, interpret as 'weight' or probability.
				TreeSet<RankedContent.Single> scores = new TreeSet<RankedContent.Single>();
				rankedContents = new ArrayList<RankedContent.Single>(result.size());

				for (Iterator<ContentNodeModel> iter = result.iterator(); iter.hasNext();) {
					ContentNodeModel contentNode = iter.next();
					double[] values = dataAccess.getVariables(userId, pricingCtx, contentNode, variableNames);
					double[] score = scoring.getScores(values);
					scores.add(new RankedContent.Single(score[0], contentNode));
				}
				for (Iterator<RankedContent.Single> iter = scores.iterator(); iter.hasNext();) {
					rankedContents.add(iter.next());
				}
				aggregatable = true;
			}
		} else {
			rankedContents = rankListByOrder(result);
			aggregatable = false;
		}

		List<ContentNodeModel> sample;
		List<ContentNodeModel> prioritized = dataAccess.getPrioritizedNodes();
		sample = sample(input, rankedContents, aggregatable, prioritized);

		List<ContentNodeModel> appended = new ArrayList<ContentNodeModel>(prioritized.size() + sample.size());
		appended.addAll(prioritized);
		appended.addAll(sample);
		return appended;
	}

	/**
	 * Collect needed factors into the buffer
	 * 
	 * @param buffer
	 *            Collection<String>
	 * @return the original buffer.
	 */
	public void collectFactors(Collection<String> buffer) {
		buffer.addAll(generator.getFactors());
		if (scoring != null) {
			String[] variableNames = scoring.getVariableNames();
			for (int i = 0; i < variableNames.length; i++) {
				buffer.add(variableNames[i]);
			}
		}
	}

	public String getDescription() {
		return "generator:" + this.generator + ", scoring:" + this.scoring;
	}

	public boolean isCacheable() {
		return generator instanceof CachingDataGenerator;
	}

	public boolean isCacheEnabled() {
		return generator instanceof CachingDataGenerator && ((CachingDataGenerator) generator).isCacheEnabled();
	}

	public ScoringAlgorithm getScoring() {
		return scoring;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[sampler=" + sampler
				+ ", includeCartItems=" + isIncludeCartItems()
				+ ", generator=" + generator
				+ ", scoring=" + scoring + "]";
	}

}
