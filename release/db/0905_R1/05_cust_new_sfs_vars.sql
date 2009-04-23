
-- New Site Features

INSERT INTO ss_site_feature (id, title, smart_saving) VALUES ('C_SAVE_YF', 'C''n''T Save on Your Favorites', 1);
INSERT INTO ss_site_feature (id, title, smart_saving) VALUES ('C_SAVE_FDF', 'C''n''T Save on FreshDirect Favorites', 1);
INSERT INTO ss_site_feature (id, title) VALUES ('C_DEALS', 'C''n''T Don''t Miss Deals');
INSERT INTO ss_site_feature (id, title) VALUES ('C_PEAK_PRODUCE', 'C''n''T Peak Season Produce');
INSERT INTO ss_site_feature (id, title) VALUES ('C_PEAK_FRUIT', 'C''n''T Peak Season Fruit');
INSERT INTO ss_site_feature (id, title) VALUES ('C_HEALTHY_SNACKS', 'C''n''T Healthy Snacks');
INSERT INTO ss_site_feature (id, title) VALUES ('C_NEW_PRODUCTS', 'C''n''T New Products');
INSERT INTO ss_site_feature (id, title) VALUES ('C_YMAL', 'C''n''T You Might Also Like');
INSERT INTO ss_site_feature (id, title) VALUES ('C_YMAL_YF', 'C''n''T YMAL from Your Favorites');

-- Save on Your Blah-blah
-- THIS SECTION HAS TO BE CHANGED ACCORDINGLY WITH ACTUAL VALUES OF SAVE VARIANTS

INSERT INTO ss_variants (id, type, feature, alias_id) VALUES ('c_save_yf_1', 'alias', 'C_SAVE_YF', 'dyf-freqbought3');
INSERT INTO ss_variants (id, type, feature, alias_id) VALUES ('c_save_fdf_1', 'alias', 'C_SAVE_FDF', 'favorites-1');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_save_yf_1', 'prez_title', 'SAVE ON YOUR FAVORITES');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_save_fdf_1', 'prez_title', 'SAVE ON FRESHDIRECT FAVORITES');

INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C1', 'c_save_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C2', 'c_save_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C3', 'c_save_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C4', 'c_save_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C5', 'c_save_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C6', 'c_save_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C7', 'c_save_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C8', 'c_save_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C9', 'c_save_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C10', 'c_save_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C11', 'c_save_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C12', 'c_save_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C13', 'c_save_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C14', 'c_save_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C15', 'c_save_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C16', 'c_save_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C17', 'c_save_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C18', 'c_save_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C19', 'c_save_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C20', 'c_save_yf_1', sysdate);

INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C1', 'c_save_fdf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C2', 'c_save_fdf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C3', 'c_save_fdf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C4', 'c_save_fdf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C5', 'c_save_fdf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C6', 'c_save_fdf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C7', 'c_save_fdf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C8', 'c_save_fdf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C9', 'c_save_fdf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C10', 'c_save_fdf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C11', 'c_save_fdf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C12', 'c_save_fdf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C13', 'c_save_fdf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C14', 'c_save_fdf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C15', 'c_save_fdf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C16', 'c_save_fdf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C17', 'c_save_fdf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C18', 'c_save_fdf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C19', 'c_save_fdf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C20', 'c_save_fdf_1', sysdate);

-- Don't Miss Deals

INSERT INTO ss_variants (id, type, feature) VALUES ('c_deal_1', 'scripted', 'C_DEALS');

INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_deal_1', 'generator',
    'RecursiveNodes("FreshDirect"):atLeast(QualityRating_Discretized2,0):between(DealsPercentage,0.1,0.75)');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_deal_1', 'scoring', 'Recency_Discretized:top; DealsPercentage_Discretized; Popularity_Discretized');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_deal_1', 'sampling_strat', 'power');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_deal_1', 'top_n', '20');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_deal_1', 'top_perc', '2.0');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_deal_1', 'exponent', '0.4');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_deal_1', 'prez_title', 'DON''T-MISS DEALS');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_deal_1', 'prez_desc', 'Great deals on some of our most popular items.');
    
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C1', 'c_deal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C2', 'c_deal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C3', 'c_deal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C4', 'c_deal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C5', 'c_deal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C6', 'c_deal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C7', 'c_deal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C8', 'c_deal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C9', 'c_deal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C10', 'c_deal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C11', 'c_deal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C12', 'c_deal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C13', 'c_deal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C14', 'c_deal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C15', 'c_deal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C16', 'c_deal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C17', 'c_deal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C18', 'c_deal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C19', 'c_deal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C20', 'c_deal_1', sysdate);

-- Peak Produce

INSERT INTO ss_variants (id, type, feature) VALUES ('c_peak_prod_1', 'scripted', 'C_PEAK_PRODUCE');

INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_peak_prod_1', 'generator', 'RecursiveNodes("fru","veg"):atLeast(QualityRating_Discretized2,3)');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_peak_prod_1', 'scoring', 'Recency_Discretized:top; QualityRating; Popularity_Discretized; ReorderRate_DepartmentNormalized');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_peak_prod_1', 'sampling_strat', 'power');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_peak_prod_1', 'top_n', '20');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_peak_prod_1', 'top_perc', '2.0');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_peak_prod_1', 'exponent', '0.4');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_peak_prod_1', 'prez_title', 'PEAK SEASON PRODUCE');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_peak_prod_1', 'prez_desc', 'We test every item every day to find produce that will be great for delivery tomorrow.<br><a href="javascript:pop(''/brandpop.jsp?brandId=fd_ratings'',400,585)">Click here to learn about our Daily Quality Ratings.</a>');
    
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C1', 'c_peak_prod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C2', 'c_peak_prod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C3', 'c_peak_prod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C4', 'c_peak_prod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C5', 'c_peak_prod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C6', 'c_peak_prod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C7', 'c_peak_prod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C8', 'c_peak_prod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C9', 'c_peak_prod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C10', 'c_peak_prod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C11', 'c_peak_prod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C12', 'c_peak_prod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C13', 'c_peak_prod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C14', 'c_peak_prod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C15', 'c_peak_prod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C16', 'c_peak_prod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C17', 'c_peak_prod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C18', 'c_peak_prod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C19', 'c_peak_prod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C20', 'c_peak_prod_1', sysdate);

-- Peak Season Fruit

INSERT INTO ss_variants (id, type, feature) VALUES ('c_peak_fru_1', 'scripted', 'C_PEAK_FRUIT');

INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_peak_fru_1', 'generator', 'RecursiveNodes("fru"):atLeast(QualityRating_Discretized2,3)');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_peak_fru_1', 'scoring', 'Recency_Discretized:top; QualityRating; Popularity_Discretized; ReorderRate_DepartmentNormalized');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_peak_fru_1', 'sampling_strat', 'power');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_peak_fru_1', 'top_n', '20');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_peak_fru_1', 'top_perc', '2.0');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_peak_fru_1', 'exponent', '0.4');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_peak_fru_1', 'cos_filter', 'CORPORATE');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_peak_fru_1', 'prez_title', 'PEAK SEASON FRUIT');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_peak_fru_1', 'prez_desc', 'We test every item every day to find produce that will be great for delivery tomorrow.<br><a href="javascript:pop(''/brandpop.jsp?brandId=fd_ratings'',400,585)">Click here to learn about our Daily Quality Ratings.</a>');

INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C1', 'c_peak_fru_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C2', 'c_peak_fru_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C3', 'c_peak_fru_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C4', 'c_peak_fru_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C5', 'c_peak_fru_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C6', 'c_peak_fru_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C7', 'c_peak_fru_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C8', 'c_peak_fru_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C9', 'c_peak_fru_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C10', 'c_peak_fru_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C11', 'c_peak_fru_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C12', 'c_peak_fru_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C13', 'c_peak_fru_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C14', 'c_peak_fru_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C15', 'c_peak_fru_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C16', 'c_peak_fru_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C17', 'c_peak_fru_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C18', 'c_peak_fru_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C19', 'c_peak_fru_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C20', 'c_peak_fru_1', sysdate);

-- Healthy Snacks

INSERT INTO ss_variants (id, type, feature) VALUES ('c_hea_snck_1', 'favorites', 'C_HEALTHY_SNACKS');

INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_hea_snck_1', 'favorite_list_id', 'fd_favs_healthy_snacks');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_hea_snck_1', 'cos_filter', 'CORPORATE');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_hea_snck_1', 'prez_title', 'HEALTHY SNACKS');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_hea_snck_1', 'prez_desc', 'Keep the office healthy and happy with favorite fruit and nut snacks.');

INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C1', 'c_hea_snck_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C2', 'c_hea_snck_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C3', 'c_hea_snck_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C4', 'c_hea_snck_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C5', 'c_hea_snck_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C6', 'c_hea_snck_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C7', 'c_hea_snck_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C8', 'c_hea_snck_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C9', 'c_hea_snck_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C10', 'c_hea_snck_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C11', 'c_hea_snck_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C12', 'c_hea_snck_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C13', 'c_hea_snck_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C14', 'c_hea_snck_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C15', 'c_hea_snck_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C16', 'c_hea_snck_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C17', 'c_hea_snck_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C18', 'c_hea_snck_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C19', 'c_hea_snck_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C20', 'c_hea_snck_1', sysdate);

-- New Products

INSERT INTO ss_variants (id, type, feature) VALUES ('c_newprod_1', 'scripted', 'C_NEW_PRODUCTS');

INSERT INTO ss_variant_params (id, key, value)
	VALUES ('c_newprod_1', 'generator', 'RecursiveNodes("FreshDirect"):atLeast(Newness,0-30)');
INSERT INTO ss_variant_params (id, key, value)
	VALUES ('c_newprod_1', 'scoring', 'DealsPercentage_Discretized; Newness;');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_newprod_1', 'sampling_strat', 'power');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_newprod_1', 'top_n', '20');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_newprod_1', 'top_perc', '2.0');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_newprod_1', 'exponent', '0.4');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_newprod_1', 'prez_title', 'NEW PRODUCTS');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_newprod_1', 'prez_desc', 'We''re constantly adding new brands and products!');

INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C1', 'c_newprod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C2', 'c_newprod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C3', 'c_newprod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C4', 'c_newprod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C5', 'c_newprod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C6', 'c_newprod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C7', 'c_newprod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C8', 'c_newprod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C9', 'c_newprod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C10', 'c_newprod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C11', 'c_newprod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C12', 'c_newprod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C13', 'c_newprod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C14', 'c_newprod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C15', 'c_newprod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C16', 'c_newprod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C17', 'c_newprod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C18', 'c_newprod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C19', 'c_newprod_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C20', 'c_newprod_1', sysdate);

-- YMAL and YMAL/YF

INSERT INTO ss_variants (id, type, feature) VALUES ('c_ymal_1', 'smartYMAL', 'C_YMAL');

INSERT INTO ss_variant_params (id, key, value)
	VALUES ('c_ymal_1', 'prez_title', 'YOU MIGHT ALSO LIKE...');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_ymal_1', 'prez_desc', 'Based on the items in your cart, we recommend:');

INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C1', 'c_ymal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C2', 'c_ymal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C3', 'c_ymal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C4', 'c_ymal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C5', 'c_ymal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C6', 'c_ymal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C7', 'c_ymal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C8', 'c_ymal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C9', 'c_ymal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C10', 'c_ymal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C11', 'c_ymal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C12', 'c_ymal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C13', 'c_ymal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C14', 'c_ymal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C15', 'c_ymal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C16', 'c_ymal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C17', 'c_ymal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C18', 'c_ymal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C19', 'c_ymal_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C20', 'c_ymal_1', sysdate);


INSERT INTO ss_variants (id, type, feature) VALUES ('c_ymal_yf_1', 'ymal-yf', 'C_YMAL_YF');

INSERT INTO ss_variant_params (id, key, value)
	VALUES ('c_ymal_yf_1', 'prez_title', 'YOU MIGHT ALSO LIKE...');
INSERT INTO ss_variant_params (id, key, value)
    VALUES ('c_ymal_yf_1', 'prez_desc', 'Based on the items you''ve purchased, we recommend:');

INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C1', 'c_ymal_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C2', 'c_ymal_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C3', 'c_ymal_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C4', 'c_ymal_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C5', 'c_ymal_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C6', 'c_ymal_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C7', 'c_ymal_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C8', 'c_ymal_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C9', 'c_ymal_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C10', 'c_ymal_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C11', 'c_ymal_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C12', 'c_ymal_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C13', 'c_ymal_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C14', 'c_ymal_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C15', 'c_ymal_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C16', 'c_ymal_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C17', 'c_ymal_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C18', 'c_ymal_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C19', 'c_ymal_yf_1', sysdate);
INSERT INTO ss_variant_assignment (cohort_id, variant_id, "DATE") values ('C20', 'c_ymal_yf_1', sysdate);

