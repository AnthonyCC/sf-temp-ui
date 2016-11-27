package com.freshdirect.content.nutrition.panel;

/**
 * Nutrition panel builder
 * 
 * Follows the Builder pattern, combines building panels/sections/items.
 * 
 * For examples see the ProtoPanel class.
 * 
 * @author treer
 */
public class PanelBuilder {

	NutritionPanel panel;
	
	NutritionSection currentSection;
	int sectionPosition = 0;
	
	NutritionItem currentItem;
	int itemPosition = 0;
	
	public PanelBuilder( NutritionPanelType type ) {
		panel = new NutritionPanel();
		panel.setType( type );
	}

	public NutritionPanel build() {
		return panel;
	}
	
	// SECTIONS
	
	public PanelBuilder addSection( NutritionSectionType type, String title, int importance ) {
		currentSection = new NutritionSection();
		currentSection.setPosition( sectionPosition++ );
		currentSection.setType( type );
		currentSection.setTitle( title );
		currentSection.setImportance( importance );
		panel.getSections().add( currentSection );
		itemPosition = 0;
		return this;
	}

	
	// ITEMS
	
	public PanelBuilder addItem( String flags ) {
		currentItem = new NutritionItem();
		currentSection.getItems().add( currentItem );
		currentItem.setPosition( itemPosition++ );
		currentItem.populateFlags( flags );
		return this;
	}
	
	public PanelBuilder addItem( String flags, String value ) {
		return addItem( flags ).setItemValue( value );
	}
	
	public PanelBuilder addItem( String flags, String value1, String value2 ) {
		return addItem( flags ).setItemValues( value1, value2 );
	}
	
	public PanelBuilder addSeparator() {
		return addItem( "S" );
	}

	public PanelBuilder setItemValue( String value1 ) {
		currentItem.setValue1( value1 );
		return this;
	}
	
	public PanelBuilder setItemValues( String value1, String value2 ) {
		currentItem.setValue1( value1 );
		currentItem.setValue2( value2 );
		return this;
	}

	public PanelBuilder setItemNutrition( double ingredientValue, String uom ) {
		currentItem.setIngredientValue( ingredientValue );
		currentItem.setUom( uom );
		return this;
	}
	
}
