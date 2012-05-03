delete from ATTRIBUTEDEFINITION where ID = 'Category.PRODUCT_PROMOTION_TYPE';

delete from LOOKUP where LOOKUPTYPE_CODE = 'Category.PRODUCT_PROMOTION_TYPE';

delete from LOOKUPTYPE where CODE = 'Category.PRODUCT_PROMOTION_TYPE';



delete from LOOKUP where LOOKUPTYPE_CODE ='Product.LAYOUT' and LABEL='President''s Picks';
