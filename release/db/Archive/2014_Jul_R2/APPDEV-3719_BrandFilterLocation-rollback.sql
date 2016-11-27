delete cms.attributedefinition where id = 'Category.brandFilterLocation';

delete from cms.lookup where lookuptype_code like 'Category.brandFilterLocation';
delete from cms.lookuptype where code like 'Category.brandFilterLocation';