package com.freshdirect.content.nutrition.panel;


/**
 *	Static helper class to generate various kinds of proto-panels (default nutrition panels)
 * 
 * @author treer
 */
public final class ProtoPanel {
	
	public static NutritionPanel createProtoPanel(NutritionPanelType type, boolean sample) {
		switch ( type ) {
			case DRUG:
				return sample ? createSampleDrug() : createProtoDrug();
			case PET:
				return sample ? createSamplePet() : createProtoPet();
			case BABY:
				return sample ? createSampleBaby() : createProtoBaby();
			case SUPPL:
				return sample ? createSampleSupplement() : createProtoSupplement();
			default:
				return new NutritionPanel();
		}
	}

	private static NutritionPanel createProtoBaby() {
		return new PanelBuilder( NutritionPanelType.BABY ).
		addSection( NutritionSectionType.FREETEXT, "Nutrition Facts ", 4 ).
			addItem( "N" ).setItemValue( "" ).
		addSection( NutritionSectionType.INGREDIENT, "Nutrients", 0 ).
			addSeparator().
			addItem( "N" ).setItemNutrition(   0.0, "" ).setItemValues( "", " " ).
		addSection( NutritionSectionType.INGREDIENT, "Vitamins", 0 ).
			addSeparator().
			addItem( "N" ).setItemNutrition(   0.0, "" ).setItemValues( "", " " ).
		addSection( NutritionSectionType.INGREDIENT, "Minerals", 0 ).
			addSeparator().
			addItem( "N" ).setItemNutrition(   0.0, "" ).setItemValues( "", " " ).
		build();
	}

	private static NutritionPanel createSampleBaby() {
		return new PanelBuilder( NutritionPanelType.BABY ).
		addSection( NutritionSectionType.FREETEXT, "Nutrition Facts SAMPLE", 4 ).
			addItem( "N" ).setItemValue( "(Normal dilution); per 100 Calories (5 fl oz)" ).
		addSection( NutritionSectionType.INGREDIENT, "Nutrients", 0 ).
			addSeparator().
			addItem( "N" ).setItemNutrition(0.0, "g" ).setItemValues( "PROTEIN", " " ).
			addItem( "N" ).setItemNutrition(0.0, "g" ).setItemValues( "FAT", " " ).
			addItem( "N" ).setItemNutrition(0.0, "g" ).setItemValues( "CARBOHYDRATE", " " ).
			addItem( "N" ).setItemNutrition(0.0, "g" ).setItemValues( "WATER", " " ).
			addItem( "N" ).setItemNutrition(0.0, "mg" ).setItemValues( "LINOLEIC ACID", " " ).
		addSection( NutritionSectionType.INGREDIENT, "Vitamins", 0 ).
			addSeparator().
			addItem( "N" ).setItemNutrition(0.0, "IU" ).setItemValues( "A", " " ).
			addItem( "N" ).setItemNutrition(0.0, "IU" ).setItemValues( "D", " " ).
			addItem( "N" ).setItemNutrition(0.0, "IU" ).setItemValues( "E", " " ).
			addItem( "N" ).setItemNutrition(0.0, "mcg" ).setItemValues( "K", " " ).
			addItem( "N" ).setItemNutrition(0.0, "mcg" ).setItemValues( "THIAMIN(B1)", " " ).
			addItem( "N" ).setItemNutrition(0.0, "mcg" ).setItemValues( "RIBOFLAVIN(B2)", " " ).
			addItem( "N" ).setItemNutrition(0.0, "mcg" ).setItemValues( "B6", " " ).
			addItem( "N" ).setItemNutrition(0.0, "mcg" ).setItemValues( "B12", " " ).
			addItem( "N" ).setItemNutrition(0.0, "mcg" ).setItemValues( "NIACIN", " " ).
			addItem( "N" ).setItemNutrition(0.0, "mcg" ).setItemValues( "FOLIC ACID (FOLACIN)", " " ).
			addItem( "N" ).setItemNutrition(0.0, "mcg" ).setItemValues( "PANTOTHENIC ACID", " " ).
			addItem( "N" ).setItemNutrition(0.0, "mcg" ).setItemValues( "BIOTIN", " " ).
			addItem( "N" ).setItemNutrition(0.0, "mg" ).setItemValues( "C (ASCORBIC ACID)", " " ).
			addItem( "N" ).setItemNutrition(0.0, "mg" ).setItemValues( "CHOLINE", " " ).
			addItem( "N" ).setItemNutrition(0.0, "mg" ).setItemValues( "INOSITOL", " " ).		
		addSection( NutritionSectionType.INGREDIENT, "Minerals", 0 ).
			addSeparator().
			addItem( "N" ).setItemNutrition(0.0, "mg" ).setItemValues( "CALCIUM", " " ).
			addItem( "N" ).setItemNutrition(0.0, "mg" ).setItemValues( "PHOSPHORUS", " " ).		
			addItem( "N" ).setItemNutrition(0.0, "mg" ).setItemValues( "MAGNESIUM", " " ).
			addItem( "N" ).setItemNutrition(0.0, "mg" ).setItemValues( "IRON", " " ).		
			addItem( "N" ).setItemNutrition(0.0, "mg" ).setItemValues( "ZINC", " " ).
			addItem( "N" ).setItemNutrition(0.0, "mcg" ).setItemValues( "MANGANESE", " " ).		
			addItem( "N" ).setItemNutrition(0.0, "mcg" ).setItemValues( "COPPER", " " ).		
			addItem( "N" ).setItemNutrition(0.0, "mcg" ).setItemValues( "IODINE", " " ).		
			addItem( "N" ).setItemNutrition(0.0, "mcg" ).setItemValues( "SELENIUM", " " ).		
			addItem( "N" ).setItemNutrition(0.0, "mg" ).setItemValues( "SODIUM", " " ).		
			addItem( "N" ).setItemNutrition(0.0, "mg" ).setItemValues( "POTASSIUM", " " ).		
			addItem( "N" ).setItemNutrition(0.0, "mg" ).setItemValues( "CHLORIDE", " " ).		
		build();
	}
	
	private static NutritionPanel createProtoPet() {
		return new PanelBuilder( NutritionPanelType.PET ).
		addSection( NutritionSectionType.FREETEXT, "Pet Nutrition Facts ", 4 ).
		addSection( NutritionSectionType.FREETEXT, "Ingredients: ", 0 ).
			addItem( "", "" ).
		addSection( NutritionSectionType.INGREDIENT, "Guaranteed Analysis:", 0 ).
			addItem( "N" ).setItemNutrition( 0.0, "" ).setItemValues( "", "" ).
		addSection( NutritionSectionType.FREETEXT, "AAFCO Statement: ", 0 ).
			addItem( "", "" ).
		build();
	}
	
	private static NutritionPanel createSamplePet() {
		return new PanelBuilder( NutritionPanelType.PET ).
		addSection( NutritionSectionType.FREETEXT, "Pet Nutrition Facts SAMPLE", 4 ).
		addSection( NutritionSectionType.FREETEXT, "Ingredients: ", 0 ).
			addItem( "", "Organic Chicken, Sufficient Water For Processing, CHicken Liver, Ocean Whitefish, Brown Rice, Oat Bran, Flaxseed, Tricalcium Phosphate, Guar Gum, Dried Kelp, Carrageenan, Minerals (Iron Amino Acid Chelate, Zinc Amino Acid Chelate, Cobalt Amino Acid Chelate, Copper Amino Acid Chelate, Manganese Amino Acid" ).
			addItem( "NB", "With all the goodness of chicken...	" ).
		addSection( NutritionSectionType.INGREDIENT, "Guaranteed Analysis:", 0 ).
			addItem( "N" ).setItemNutrition( 0.0, "%" ).setItemValues( "Crude Protein", "Min." ).
			addItem( "N" ).setItemNutrition( 0.0, "%" ).setItemValues( "Crude Fat", "Min." ).
			addItem( "N" ).setItemNutrition( 0.0, "%" ).setItemValues( "Crude Fiber", "Max." ).
			addItem( "N" ).setItemNutrition( 0.0, "%" ).setItemValues( "Moisture", "Max." ).
			addItem( "N" ).setItemNutrition( 0.0, "%" ).setItemValues( "Calcium", "Min." ).
			addItem( "N" ).setItemNutrition( 0.0, "%" ).setItemValues( "Phosphorus", "Min." ).
			addItem( "N" ).setItemNutrition( 0.0, "%" ).setItemValues( "Phosphorus", "Max." ).			
			addItem( "N" ).setItemNutrition( 0.0, "IU/kg" ).setItemValues( "Vitamin E", "Min." ).			
			addItem( "N" ).setItemNutrition( 0.0, "mg/kg" ).setItemValues( "Ascorbic Acid* (Vitamin C)", "Min." ).
			addItem( "N" ).setItemNutrition( 0.0, "%" ).setItemValues( "Taurine*", "Min." ).
			addItem( "N" ).setItemNutrition( 0.0, "%" ).setItemValues( "Total Omega-6 & 3 Fatty Acids*", "Min." ).
			addItem( "NB" ).setItemValues( "*Not recognized as an essential nutrient by the AAFCO Dog Food Nutrient Profiles.", "" ).
		addSection( NutritionSectionType.FREETEXT, "AAFCO Statement: ", 0 ).
			addItem( "", "Animal feeding tests using AAFCO procedures substantiate that [name of product] provides complete and balanced nutrition for [specific life stage]." ).
			addItem( "NB", "Protect from moisture." ).
			addItem( "B", "Store in a cool, dry place." ).
		build();
	}
	
	private static NutritionPanel createProtoDrug() {
		return new PanelBuilder( NutritionPanelType.DRUG ).
		addSection( NutritionSectionType.INGREDIENT, "Drug Facts", 4 ).
			addSeparator().
			addItem( "I", "Active ingredient", "Purpose" ).
			addItem( "" ).
		addSection( NutritionSectionType.FREETEXT, "Uses ", 0 ).
			addItem( "N" ).
			addItem( "N" ).
		addSection( NutritionSectionType.FREETEXT, "Warnings ", 0 ).
			addSeparator().
			addItem( "I", "Ask a doctor or pharmacist before use if you are " ).
			addItem( "" ).
			addSeparator().
			addItem( "I", "When using this product " ).
			addItem( "" ).
			addSeparator().
			addItem( "I", "If pregnant or breast feeding, " ).
			addItem( "" ).
			addSeparator().
			addItem( "NB", "Keep out of reach of children." ).
		addSection( NutritionSectionType.TABLE, "Directions", 0 ).
			addItem( "", "adults and children 12 years and over" ).
			addItem( "", "children 6 years to under 12 years" ).
			addItem( "", "children under 6 years" ).
		addSection( NutritionSectionType.FREETEXT, "Other information ", 1 ).
			addItem( "N" ).	
		addSection( NutritionSectionType.FREETEXT, "Inactive ingredients ", 0 ).
			addItem( "N" ).
		build();
	}

	private static NutritionPanel createSampleDrug() {
		return new PanelBuilder( NutritionPanelType.DRUG ).
		addSection( NutritionSectionType.INGREDIENT, "Drug Facts SAMPLE", 4 ).
			addSeparator().
			addItem( "I", "Active ingredient (in each tablet)", "Purpose" ).
			addItem( "N" ).setItemNutrition(   0.0, "mg" ).setItemValues( "Chlorpheniramine maleate", "Antihistamine" ).
		addSection( NutritionSectionType.FREETEXT, "Uses ", 0 ).
			addItem( "", "temporarily relieves these symptoms due to hay fever or other upper respiratory allergies:" ).
			addItem( "NB", "sneezing" ).
			addItem( "B", "runny nose" ).
			addItem( "B", "itchy, watery eyes" ).
			addItem( "B", "itchy  throat" ).
		addSection( NutritionSectionType.FREETEXT, "Warnings ", 0 ).
			addItem( "I", "Ask a doctor before use if you have" ).
			addItem( "NB", "glaucoma" ).
			addItem( "B", "a breathing problem such as emphysema or chronic bronchitis" ).
			addItem( "B", "trouble urinating due to an enlarged prostate gland" ).
			addSeparator().
			addItem( "I", "Ask a doctor or pharmacist before use if you are " ).
			addItem( "", "taking tranquilizers or sedatives" ).
			addSeparator().
			addItem( "I", "When using this product " ).
			addItem( "NB", "You may get drowsy" ).
			addItem( "B", "avoid alcoholic drinks" ).
			addItem( "NB", "alcohol, sedatives, and tranquilizers may increase drowsiness" ).
			addItem( "NB", "be careful when driving a motor vehicle or operating machinery" ).
			addItem( "NB", "excitability may occur, especially in children" ).
			addSeparator().
			addItem( "I", "If pregnant or breast feeding, " ).
			addItem( "", "ask a health professional before use." ).
			addItem( "NI", "Keep out of reach of children. " ).
			addItem( "", "In case of overdose, get medical help or contact a Poison Control Center right away." ).
		addSection( NutritionSectionType.TABLE, "Directions", 0 ).
			addItem( "", "adults and children 12 years and over", "take 2 tablets every 4 to 6 hours; not more than 12 tablets in 24 hours" ).
			addItem( "", "children 6 years to under 12 years", "take 1 tablet every 4 to 6 hours; not more than 6 tablets in 24 hours" ).
			addItem( "", "children under 6 years", "ask a doctor" ).
		addSection( NutritionSectionType.FREETEXT, "Other information ", 1 ).
			addItem( "", "store at 20-25\u00b0C (68-77\u00b0F)" ).
			addItem( "B", "protect from excessive moisture." ).
		addSection( NutritionSectionType.FREETEXT, "Inactive ingredients ", 0 ).
			addItem( "", "D&C yellow no. 10, lactose, magnesium stearate, microcrystalline, cellulose, pregelatinized starch" ).
		build();
	}

	
	private static NutritionPanel createProtoSupplement() {
		return new PanelBuilder( NutritionPanelType.SUPPL ).
		addSection( NutritionSectionType.FREETEXT, "Supplement Facts ", 4 ).
			addItem( "N" ).setItemValue( "Serving Size " ).
		addSection( NutritionSectionType.INGREDIENT, "", 0 ).		
			addItem( "I", "Amount Per Serving", "% Daily Value" ).			
			addSeparator().			
			addItem( "N" ).setItemNutrition( 0.0, "" ).setItemValues( "", "" ).
		addSection( NutritionSectionType.FREETEXT, "Other ingredients: ", 0 ).
			addItem( "", "" ).
		build();
	}
	
	private static NutritionPanel createSampleSupplement() {
		return new PanelBuilder( NutritionPanelType.SUPPL ).
		addSection( NutritionSectionType.FREETEXT, "Supplement Facts SAMPLE", 4 ).
			addItem( "N" ).setItemValue( "Serving Size 1 Tablet" ).
		addSection( NutritionSectionType.INGREDIENT, "", 0 ).		
			addItem( "I", "Amount Per Serving", "% Daily Value" ).			
			addSeparator().			
			addItem( "N" ).setItemNutrition( 5000.0, "IU" ).setItemValues( "Vitamin A (as retinyl acetate and 50% as beta-carotene)", "100%" ).
			addItem( "N" ).setItemNutrition( 60.0, "mg" ).setItemValues( "Vitamin C (as ascorbic acid)", "100%" ).
			addItem( "N" ).setItemNutrition( 400.0, "IU" ).setItemValues( "Vitamin D (as cholecalciferol)", "100%" ).
			addItem( "N" ).setItemNutrition( 30.0, "IU" ).setItemValues( "Vitamin E (as dl-alpha tocopheryl acetate)", "100%" ).
			addItem( "N" ).setItemNutrition( 1.5, "mg" ).setItemValues( "Thiamin (as thiamin mononitrate)", "100%" ).
			addItem( "N" ).setItemNutrition( 1.7, "mg" ).setItemValues( "Riboflavin", "100%" ).
			addItem( "N" ).setItemNutrition( 20.0, "mg" ).setItemValues( "Niacin (as niacinamide)", "100%" ).			
			addItem( "N" ).setItemNutrition( 2.0, "mg" ).setItemValues( "Vitamin B6 (as pyridoxine hydrochloride)", "100%" ).			
			addItem( "N" ).setItemNutrition( 400.0, "mcg" ).setItemValues( "Folate (as folic acid)", "100%" ).
			addItem( "N" ).setItemNutrition( 6.0, "mcg" ).setItemValues( "Vitamin B12 (as cyanocobalamin)", "100%" ).
			addItem( "N" ).setItemNutrition( 30.0, "mcg" ).setItemValues( "Biotin", "10%" ).
			addItem( "N" ).setItemNutrition( 10.0, "mg" ).setItemValues( "Pantothenic Acid (as calcium pantothenate)", "100%" ).
		addSection( NutritionSectionType.FREETEXT, "Other ingredients: ", 0 ).
			addItem( "", "Gelatin, lactose, magnesium stearate, microcrystalline cellulose, FD&C Yellow No. 6, propylene glycol, propylparaben, and sodium benzoate." ).
		build();
	}
	
	
}
