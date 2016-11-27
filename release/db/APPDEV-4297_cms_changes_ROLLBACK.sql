-- remove Holiday Meal Bundle product layout
DELETE FROM CMS.LOOKUP WHERE LOOKUPTYPE_CODE='Product.PRODUCT_LAYOUT' AND CODE='10';

-- remove meal include products of side box product
DELETE FROM cms.relationshipdestination WHERE ID = 'Product.MEAL_INCLUDE_PRODUCTS.Product';
DELETE FROM cms.relationshipdefinition WHERE ID = 'Product.MEAL_INCLUDE_PRODUCTS';
