DELETE FROM cms.attributedefinition where ID='Category.featuredRecommenderTitle';
DELETE FROM cms.attributedefinition where ID='Category.featuredRecommenderRandomizeProducts';
DELETE FROM cms.attributedefinition where ID='Category.featuredRecommenderSiteFeature';
DELETE FROM cms.relationshipdefinition WHERE ID = 'Category.featuredRecommenderSourceCategory';
DELETE FROM cms.relationshipdestination WHERE ID = 'Category.featuredRecommenderSourceCategory.Category';

