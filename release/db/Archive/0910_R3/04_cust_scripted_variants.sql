INSERT INTO "CUST"."SS_VARIANTS" (ID, FEATURE, TYPE) VALUES ('featured-candidate-s', 'FEATURED_ITEMS', 'scripted');

INSERT INTO "CUST"."SS_VARIANT_PARAMS" (ID, KEY, VALUE) VALUES ('featured-candidate-s', 'exponent', '0.5');
INSERT INTO "CUST"."SS_VARIANT_PARAMS" (ID, KEY, VALUE) VALUES ('featured-candidate-s', 'fi_label', 'NEW! Top Sellers');
INSERT INTO "CUST"."SS_VARIANT_PARAMS" (ID, KEY, VALUE) VALUES ('featured-candidate-s', 'sampling_strat', 'power');
INSERT INTO "CUST"."SS_VARIANT_PARAMS" (ID, KEY, VALUE) VALUES ('featured-candidate-s', 'top_n', '10');
INSERT INTO "CUST"."SS_VARIANT_PARAMS" (ID, KEY, VALUE) VALUES ('featured-candidate-s', 'top_perc', '20');
INSERT INTO "CUST"."SS_VARIANT_PARAMS" (ID, KEY, VALUE) VALUES ('featured-candidate-s', 'generator', 'CandidateLists');
INSERT INTO "CUST"."SS_VARIANT_PARAMS" (ID, KEY, VALUE) VALUES ('featured-candidate-s', 'scoring', 'Popularity');


INSERT INTO "CUST"."SS_VARIANTS" (ID, FEATURE, TYPE) VALUES ('featured-cms-s', 'FEATURED_ITEMS', 'scripted');
INSERT INTO "CUST"."SS_VARIANTS" (ID, FEATURE, TYPE) VALUES ('featured-popular-s', 'FEATURED_ITEMS', 'scripted');
INSERT INTO "CUST"."SS_VARIANTS" (ID, FEATURE, TYPE) VALUES ('featured-pop2-s', 'FEATURED_ITEMS', 'scripted');


INSERT INTO "CUST"."SS_VARIANT_PARAMS" (ID, KEY, VALUE) VALUES ('featured-cms-s', 'sampling_strat', 'deterministic');
INSERT INTO "CUST"."SS_VARIANT_PARAMS" (ID, KEY, VALUE) VALUES ('featured-cms-s', 'generator', 'FeaturedItems');

INSERT INTO "CUST"."SS_VARIANT_PARAMS" (ID, KEY, VALUE) VALUES ('featured-popular-s', 'exponent', '0.5');
INSERT INTO "CUST"."SS_VARIANT_PARAMS" (ID, KEY, VALUE) VALUES ('featured-popular-s', 'fi_label', 'NEW! Top Sellers');
INSERT INTO "CUST"."SS_VARIANT_PARAMS" (ID, KEY, VALUE) VALUES ('featured-popular-s', 'sampling_strat', 'power');
INSERT INTO "CUST"."SS_VARIANT_PARAMS" (ID, KEY, VALUE) VALUES ('featured-popular-s', 'top_n', '10');
INSERT INTO "CUST"."SS_VARIANT_PARAMS" (ID, KEY, VALUE) VALUES ('featured-popular-s', 'top_perc', '20');
INSERT INTO "CUST"."SS_VARIANT_PARAMS" (ID, KEY, VALUE) VALUES ('featured-popular-s', 'generator', 'ManuallyOverriddenSlotsP(currentNode) + CandidateLists');
INSERT INTO "CUST"."SS_VARIANT_PARAMS" (ID, KEY, VALUE) VALUES ('featured-popular-s', 'scoring', 'Popularity');

INSERT INTO "CUST"."SS_VARIANT_PARAMS" (ID, KEY, VALUE) VALUES ('featured-pop2-s', 'exponent', '0.5');
INSERT INTO "CUST"."SS_VARIANT_PARAMS" (ID, KEY, VALUE) VALUES ('featured-pop2-s', 'fi_label', 'NEW! Top Sellers');
INSERT INTO "CUST"."SS_VARIANT_PARAMS" (ID, KEY, VALUE) VALUES ('featured-pop2-s', 'sampling_strat', 'power');
INSERT INTO "CUST"."SS_VARIANT_PARAMS" (ID, KEY, VALUE) VALUES ('featured-pop2-s', 'top_n', '10');
INSERT INTO "CUST"."SS_VARIANT_PARAMS" (ID, KEY, VALUE) VALUES ('featured-pop2-s', 'top_perc', '2');
INSERT INTO "CUST"."SS_VARIANT_PARAMS" (ID, KEY, VALUE) VALUES ('featured-pop2-s', 'generator', 'Top(RecursiveNodes(currentNode), Frequency, 1):atLeast(Frequency, 1):prioritize() + ManuallyOverriddenSlotsP(currentNode) + CandidateLists');
INSERT INTO "CUST"."SS_VARIANT_PARAMS" (ID, KEY, VALUE) VALUES ('featured-pop2-s', 'scoring', 'Popularity');

INSERT INTO "CUST"."SS_VARIANTS" (ID, FEATURE, TYPE, ALIAS_ID) VALUES ('featured-cms-bu', 'FEATURED_ITEMS', 'alias', 'featured-cms');
INSERT INTO "CUST"."SS_VARIANTS" (ID, FEATURE, TYPE, ALIAS_ID) VALUES ('featured-popular-bu', 'FEATURED_ITEMS', 'alias', 'featured-popular');
INSERT INTO "CUST"."SS_VARIANTS" (ID, FEATURE, TYPE, ALIAS_ID) VALUES ('featured-pop2-bu', 'FEATURED_ITEMS', 'alias', 'featured-pop2');

INSERT INTO "CUST"."SS_VARIANT_PARAMS" (ID, KEY, VALUE) VALUES ('featured-cms-bu', 'brand_uniq_sort', 'true');
INSERT INTO "CUST"."SS_VARIANT_PARAMS" (ID, KEY, VALUE) VALUES ('featured-popular-bu', 'brand_uniq_sort', 'true');
INSERT INTO "CUST"."SS_VARIANT_PARAMS" (ID, KEY, VALUE) VALUES ('featured-pop2-bu', 'brand_uniq_sort', 'true');

INSERT INTO "CUST"."SS_VARIANTS" (ID, FEATURE, TYPE, ALIAS_ID) VALUES ('featured-cms-s-bu', 'FEATURED_ITEMS', 'alias', 'featured-cms-s');
INSERT INTO "CUST"."SS_VARIANTS" (ID, FEATURE, TYPE, ALIAS_ID) VALUES ('featured-popular-s-bu', 'FEATURED_ITEMS', 'alias', 'featured-popular-s');
INSERT INTO "CUST"."SS_VARIANTS" (ID, FEATURE, TYPE, ALIAS_ID) VALUES ('featured-pop2-s-bu', 'FEATURED_ITEMS', 'alias', 'featured-pop2-s');

INSERT INTO "CUST"."SS_VARIANT_PARAMS" (ID, KEY, VALUE) VALUES ('featured-cms-s-bu', 'brand_uniq_sort', 'true');
INSERT INTO "CUST"."SS_VARIANT_PARAMS" (ID, KEY, VALUE) VALUES ('featured-popular-s-bu', 'brand_uniq_sort', 'true');
INSERT INTO "CUST"."SS_VARIANT_PARAMS" (ID, KEY, VALUE) VALUES ('featured-pop2-s-bu', 'brand_uniq_sort', 'true');

