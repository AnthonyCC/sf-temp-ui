-- Category Carousel Setup
delete cms.attributedefinition where id = 'Category.carouselPositionCLP';
delete cms.attributedefinition where id = 'Category.carouselPositionPLP';
delete cms.attributedefinition where id = 'Category.carouselRatioCLP';
delete cms.attributedefinition where id = 'Category.carouselRatioPLP';

-- Department Carousel Setup
delete cms.attributedefinition where id = 'Department.carouselPosition';
delete cms.attributedefinition where id = 'Department.carouselRatio';

-- SuperDepartment Carousel Setup
delete cms.attributedefinition where id = 'SuperDepartment.carouselPosition';
delete cms.attributedefinition where id = 'SuperDepartment.carouselRatio';

-- Carousel Location
delete from cms.lookup where lookuptype_code like 'Category.carouselPosition';
delete from cms.lookuptype where code like 'Category.carouselPosition';

-- Carousel Ratio
delete from cms.lookup where lookuptype_code like 'Category.carouselRatio';
delete from cms.lookuptype where code like 'Category.carouselRatio';