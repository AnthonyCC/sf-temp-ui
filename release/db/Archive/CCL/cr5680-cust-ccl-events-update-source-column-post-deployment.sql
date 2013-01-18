update cust.events set source = 'Browse' where URL = '/category.jsp' and (source is  null or source = 'Unknown');

update cust.events set source = 'Browse' where URL = '/product.jsp'and (source is null or source = 'Unknown');

update cust.events set source = 'Browse' where URL = '/order/product.jsp' and (source is null or source = 'Unknown');

update cust.events set source = 'Recipe' where URL = '/recipe.jsp' and (source is  null or source = 'Unknown');

update cust.events set source = 'Recipe' where URL = '/order/recipe.jsp' and (source is null or source = 'Unknown');

update cust.events set source = 'Quickshop' where URL like '/quickshop/%' and (source is null or source = 'Unknown');

update cust.events set source = 'Quickshop' where URL like '/order/quickshop/%' and (source is null or source = 'Unknown');

update cust.events set source = 'TxYmal' where URL = '/cart_confirm.jsp' and (source is null or source = 'Unknown');

update cust.events set source = 'TxYmal' where URL = '/grocery_cart_confirm.jsp' and (source is  null or source = 'Unknown');

update cust.events set source='Unknown' where source is null;

alter table cust.events modify (source varchar2(16) default 'Unknown' not null);