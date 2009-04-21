
-- YMAL/YF
-- site feature is built in

INSERT INTO ss_variants (id, type, feature) VALUES ('ymal-yf-var', 'ymal-yf', 'YMAL_YF');

INSERT INTO ss_variant_params (id, key, value)
	VALUES ('ymal-yf-var', 'prez_title', 'You Might Also Like');

INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C1', 'ymal-yf-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C2', 'ymal-yf-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C3', 'ymal-yf-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C4', 'ymal-yf-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C5', 'ymal-yf-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C6', 'ymal-yf-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C7', 'ymal-yf-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C8', 'ymal-yf-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C9', 'ymal-yf-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C10', 'ymal-yf-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C11', 'ymal-yf-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C12', 'ymal-yf-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C13', 'ymal-yf-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C14', 'ymal-yf-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C15', 'ymal-yf-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C16', 'ymal-yf-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C17', 'ymal-yf-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C18', 'ymal-yf-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C19', 'ymal-yf-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C20', 'ymal-yf-var', sysdate);

-- New Products

INSERT INTO ss_site_feature (id, title) VALUES ('NEW_PRODUCTS', 'New Products');

INSERT INTO ss_variants (id, type, feature) VALUES ('new-prods-var', 'scripted', 'NEW_PRODUCTS');

INSERT INTO ss_variant_params (id, key, value)
	VALUES ('new-prods-var', 'generator', 'RecursiveNodes("FreshDirect"):atLeast(Newness,0-1250)');
INSERT INTO ss_variant_params (id, key, value)
	VALUES ('new-prods-var', 'scoring', 'DealsPercentage_Discretized; Newness;');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('new-prods-var', 'sampling_strat', 'power');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('new-prods-var', 'top_n', '20');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('new-prods-var', 'top_perc', '2.0');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('new-prods-var', 'exponent', '0.4');

INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C1', 'new-prods-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C2', 'new-prods-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C3', 'new-prods-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C4', 'new-prods-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C5', 'new-prods-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C6', 'new-prods-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C7', 'new-prods-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C8', 'new-prods-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C9', 'new-prods-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C10', 'new-prods-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C11', 'new-prods-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C12', 'new-prods-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C13', 'new-prods-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C14', 'new-prods-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C15', 'new-prods-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C16', 'new-prods-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C17', 'new-prods-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C18', 'new-prods-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C19', 'new-prods-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C20', 'new-prods-var', sysdate);

-- Peak Produce
INSERT INTO ss_site_feature (id, title) VALUES ('PEAK_PRODUCE', 'Peak Produce');

INSERT INTO ss_variants (id, type, feature) VALUES ('peak-prod-var', 'scripted', 'PEAK_PRODUCE');

INSERT INTO ss_variant_params (id, key, value)
    VALUES ('peak-prod-var', 'generator', 'RecursiveNodes("fru","veg"):atLeast(QualityRating_Discretized2,0)');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('peak-prod-var', 'scoring', 'Recency_Discretized; QualityRating; Popularity_Discretized; ReorderRate_DepartmentNormalized');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('peak-prod-var', 'sampling_strat', 'power');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('peak-prod-var', 'top_n', '20');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('peak-prod-var', 'top_perc', '2.0');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('peak-prod-var', 'exponent', '0.4');
    
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C1', 'peak-prod-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C2', 'peak-prod-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C3', 'peak-prod-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C4', 'peak-prod-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C5', 'peak-prod-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C6', 'peak-prod-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C7', 'peak-prod-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C8', 'peak-prod-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C9', 'peak-prod-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C10', 'peak-prod-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C11', 'peak-prod-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C12', 'peak-prod-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C13', 'peak-prod-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C14', 'peak-prod-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C15', 'peak-prod-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C16', 'peak-prod-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C17', 'peak-prod-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C18', 'peak-prod-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C19', 'peak-prod-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C20', 'peak-prod-var', sysdate);

-- Don't Miss Deals
INSERT INTO ss_site_feature (id, title) VALUES ('GREAT_DEALS', 'Don''t Miss Deals');

INSERT INTO ss_variants (id, type, feature) VALUES ('great-deal-var', 'scripted', 'GREAT_DEALS');

INSERT INTO ss_variant_params (id, key, value)
    VALUES ('great-deal-var', 'generator',
    'RecursiveNodes("FreshDirect"):atLeast(QualityRating_Discretized2,0):between(DealsPercentage,0.1,0.75)');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('great-deal-var', 'scoring', 'Recency_Discretized:top; DealsPercentage_Discretized; Popularity_Discretized');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('great-deal-var', 'sampling_strat', 'power');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('great-deal-var', 'top_n', '20');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('great-deal-var', 'top_perc', '2.0');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('great-deal-var', 'exponent', '0.4');
    
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C1', 'great-deal-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C2', 'great-deal-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C3', 'great-deal-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C4', 'great-deal-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C5', 'great-deal-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C6', 'great-deal-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C7', 'great-deal-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C8', 'great-deal-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C9', 'great-deal-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C10', 'great-deal-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C11', 'great-deal-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C12', 'great-deal-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C13', 'great-deal-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C14', 'great-deal-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C15', 'great-deal-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C16', 'great-deal-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C17', 'great-deal-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C18', 'great-deal-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C19', 'great-deal-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C20', 'great-deal-var', sysdate);

-- Peak Season Fruits
INSERT INTO ss_site_feature (id, title) VALUES ('PEAK_FRUITS', 'Peak Season Fruits');

INSERT INTO ss_variants (id, type, feature) VALUES ('peak-fruits-var', 'scripted', 'PEAK_FRUITS');

INSERT INTO ss_variant_params (id, key, value)
    VALUES ('peak-fruits-var', 'generator', 'RecursiveNodes("fru"):atLeast(QualityRating_Discretized2,0)');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('peak-fruits-var', 'scoring', 'Recency_Discretized; QualityRating; Popularity_Discretized; ReorderRate_DepartmentNormalized');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('peak-fruits-var', 'sampling_strat', 'power');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('peak-fruits-var', 'top_n', '20');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('peak-fruits-var', 'top_perc', '2.0');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('peak-fruits-var', 'exponent', '0.4');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('peak-fruits-var', 'cos_filter', 'CORPORATE');
    
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C1', 'peak-fruits-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C2', 'peak-fruits-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C3', 'peak-fruits-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C4', 'peak-fruits-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C5', 'peak-fruits-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C6', 'peak-fruits-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C7', 'peak-fruits-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C8', 'peak-fruits-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C9', 'peak-fruits-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C10', 'peak-fruits-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C11', 'peak-fruits-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C12', 'peak-fruits-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C13', 'peak-fruits-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C14', 'peak-fruits-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C15', 'peak-fruits-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C16', 'peak-fruits-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C17', 'peak-fruits-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C18', 'peak-fruits-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C19', 'peak-fruits-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C20', 'peak-fruits-var', sysdate);


-- Healthy Snacks
INSERT INTO ss_site_feature (id, title) VALUES ('HEALTHY_SNACKS', 'Healthy Snacks');

INSERT INTO ss_variants (id, type, feature) VALUES ('fdf-hs-var', 'favorites', 'HEALTHY_SNACKS');

INSERT INTO ss_variant_params (id, key, value)
    VALUES ('fdf-hs-var', 'favorite_list_id', 'fd_favs_nb');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('fdf-hs-var', 'cos_filter', 'CORPORATE');
    
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C1', 'fdf-hs-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C2', 'fdf-hs-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C3', 'fdf-hs-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C4', 'fdf-hs-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C5', 'fdf-hs-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C6', 'fdf-hs-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C7', 'fdf-hs-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C8', 'fdf-hs-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C9', 'fdf-hs-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C10', 'fdf-hs-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C11', 'fdf-hs-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C12', 'fdf-hs-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C13', 'fdf-hs-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C14', 'fdf-hs-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C15', 'fdf-hs-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C16', 'fdf-hs-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C17', 'fdf-hs-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C18', 'fdf-hs-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C19', 'fdf-hs-var', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C20', 'fdf-hs-var', sysdate);

