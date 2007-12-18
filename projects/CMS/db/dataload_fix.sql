Fix Department --> Categorypriority
declare 
  CURSOR cur_depts is
                   select content_name 
                   from fdstore.contentnode 
                   where content_type = 'D'
                   order by content_name asc; 
  CURSOR cur_cats (dept_in VARCHAR2) 
  IS 
      select 'Category:' || cat.content_name catName,c.priority 
                  from fdstore.category c, fdstore.contentnode cat, fdstore.contentnode dept
                  where c.department_id = dept.id
                  and c.content_id = cat.id
                  and dept.content_name = dept_in
                  and c.store_id = '41781199'
                  order by c.priority asc, cat.full_name asc; 
  curCat VARCHAR2(2000) := ''; 
  dept VARCHAR2(2000) := '';
  counter NUMBER := 0; 
  prio number;
begin 
  open cur_depts;
  loop
      FETCH cur_depts into dept;
      exit when cur_depts%NOTFOUND;
      open cur_cats(dept); 
      counter := 0; 
      loop 
       FETCH cur_cats into curCat, prio; 
        exit when cur_cats%NOTFOUND;
        update relationship set ordinal = counter 
         where def_contenttype = 'Category' 
         and def_name = 'categories'
         and parent_contentnode_id = 'Department:' || dept
         and child_contentnode_id = curCat; 
        counter := counter + 1; 
       end loop;
         close cur_cats;
  end loop; 
end;


Fix Category --> Product priority

-- Get all categories
select 	   parentcategory.content_name parent_name, parentcategory.full_name parent_full_name,
		   category.content_name cat_name, category.full_name cat_full_name,
	   department.content_name dept_name, department.full_name dept_full_name
 from fdstore.category cat, fdstore.contentnode category, 
 	  fdstore.contentnode parentcategory, fdstore.contentnode department
where cat.content_id = category.id
and cat.parent_category_id = parentcategory.id (+)
and cat.department_id = department.id (+)
and cat.store_id = '41781199'
connect by parent_category_id =  content_id
	
-- PRIORITY ORDER for products
declare 
   CURSOR cur_get_cats is
	select content_name from fdstore.contentnode where content_type = 'C' and store_id = '41781199' ;
   CURSOR cur_get_list_as(cat_in VARCHAR2) is 
	select value from (
		select * from
		(select child_contentnode_id nodeId, level levelNumber from all_nodes 
			start with child_contentnode_id = 'Category:' || cat_in
			connect by prior parent_contentnode_id = child_contentnode_id) nodes,
		(select contentnode_id nodeId, value from attribute where def_name = 'LIST_AS') listAs
		where nodes.nodeId = listAs.nodeId
		 and rownum = 1
		 order by nodes.levelNumber asc
		);
   CURSOR cur_get_layout(cat_in VARCHAR2) is 
	select value from (
		select * from
		(select child_contentnode_id nodeId, level levelNumber from all_nodes 
			start with child_contentnode_id = 'Category:' || cat_in
			connect by prior parent_contentnode_id = child_contentnode_id) nodes,
		(select contentnode_id nodeId, value from attribute where def_name = 'LAYOUT') listAs
		where nodes.nodeId = listAs.nodeId
		 and rownum = 1
		 order by nodes.levelNumber asc
		);
   CURSOR cur_prodcats_by_full(cat_in VARCHAR2) is 
             select 'Category:' || category.content_name as catName, 
                        'Product:' || product.content_name prodName,
                    pc.priority
             from fdstore.productcategory pc, fdstore.contentnode product, fdstore.contentnode category 
             where product.id = pc.product_id 
             and product.store_id = '41781199'
             and category.id = pc.category_id
             and category.content_name = cat_in
             order by category.content_name, pc.priority asc, product.full_name asc, product.content_name asc; 
   CURSOR cur_prodcats_by_full_and_price(cat_in VARCHAR2) is 
   		  select catName, prodName, priority
		  from (
             select 'Category:' || category.content_name as catName, 
                    'Product:' || product.content_name prodName,
                    pc.priority,
					product.full_name productName,
					product.content_name product_content_name,
					min(skuPrice.default_price) defaultPrice
             from fdstore.productcategory pc, fdstore.contentnode product, fdstore.contentnode category, fdstore.sku sku,
			 	  (select p.sku_code, p.default_price
				  	from erps.product p, erps.material m, erps.materialproxy mp
					where p.id = mp.product_id
					  and m.id = mp.mat_id
					    and p.version = (select max(version) from erps.product where sku_code = p.sku_code and unavailability_status is null)) skuPrice
             where product.id = pc.product_id 
             and product.store_id = '41781199'
             and category.id = pc.category_id
             and category.content_name = cat_in
			 and sku.product_id = product.id
			 and sku.skucode = skuPrice.sku_code (+)
			 group by 'Category:' || category.content_name, 
                    'Product:' || product.content_name,
                    pc.priority,
					product.full_name,
					product.content_name
		  ) order by catName asc, priority asc, productName asc, defaultPrice asc, product_content_name asc;
  CURSOR cur_prodcats_by_glance(cat_in VARCHAR2) is 
             select 'Category:' || category.content_name as catName, 
                        'Product:' || product.content_name prodName,
                    pc.priority
             from fdstore.productcategory pc, fdstore.contentnode product, fdstore.contentnode category 
             where product.id = pc.product_id 
             and product.store_id = '41781199'
             and category.id = pc.category_id
             and category.content_name = cat_in
             order by category.content_name, pc.priority asc, nvl(product.glance_name, product.full_name) asc, product.content_name asc; 
   CURSOR cur_prodcats_by_gl_and_price(cat_in VARCHAR2) is 
   		  select catName, prodName, priority
		  from (
             select 'Category:' || category.content_name as catName, 
                    'Product:' || product.content_name prodName,
                    pc.priority,
					nvl(product.glance_name, product.full_name) productName,
					product.content_name product_content_name,
					min(skuPrice.default_price) defaultPrice
             from fdstore.productcategory pc, fdstore.contentnode product, fdstore.contentnode category, fdstore.sku sku,
			 	  (select p.sku_code, p.default_price
				  	from erps.product p, erps.material m, erps.materialproxy mp
					where p.id = mp.product_id
					  and m.id = mp.mat_id
					    and p.version = (select max(version) from erps.product where sku_code = p.sku_code and unavailability_status is null)) skuPrice
             where product.id = pc.product_id 
             and product.store_id = '41781199'
             and category.id = pc.category_id
             and category.content_name = cat_in
			 and sku.product_id = product.id
			 and sku.skucode = skuPrice.sku_code (+)
			 group by 'Category:' || category.content_name, 
                    'Product:' || product.content_name,
                    pc.priority,
					nvl(product.glance_name, product.full_name),
					product.content_name
		  ) order by catName asc, priority asc, productName asc, defaultPrice asc, product_content_name asc;
  CURSOR cur_prodcats_by_nav(cat_in VARCHAR2) is 
             select 'Category:' || category.content_name as catName, 
                        'Product:' || product.content_name prodName,
                    pc.priority
             from fdstore.productcategory pc, fdstore.contentnode product, fdstore.contentnode category 
             where product.id = pc.product_id 
             and product.store_id = '41781199'
             and category.id = pc.category_id
             and category.content_name = cat_in
             order by category.content_name, pc.priority asc, nvl(product.nav_name, product.full_name) asc, product.content_name asc; 
   CURSOR cur_prodcats_by_nav_and_price(cat_in VARCHAR2) is 
   		  select catName, prodName, priority
		  from (
             select 'Category:' || category.content_name as catName, 
                    'Product:' || product.content_name prodName,
                    pc.priority,
					nvl(product.nav_name, product.full_name) productName,
					product.content_name product_content_name,
					min(skuPrice.default_price) defaultPrice
             from fdstore.productcategory pc, fdstore.contentnode product, fdstore.contentnode category, fdstore.sku sku,
			 	  (select p.sku_code, p.default_price
				  	from erps.product p, erps.material m, erps.materialproxy mp
					where p.id = mp.product_id
					  and m.id = mp.mat_id
					    and p.version = (select max(version) from erps.product where sku_code = p.sku_code and unavailability_status is null)) skuPrice
             where product.id = pc.product_id 
             and product.store_id = '41781199'
             and category.id = pc.category_id
             and category.content_name = cat_in
			 and sku.product_id = product.id
			 and sku.skucode = skuPrice.sku_code (+)
			 group by 'Category:' || category.content_name, 
                    'Product:' || product.content_name,
                    pc.priority,
					nvl(product.nav_name, product.full_name),
					product.content_name
		  ) order by catName asc, priority asc, productName asc, defaultPrice asc, product_content_name asc;
  curCat VARCHAR2(2000) := ''; 
  listAs VARCHAR2(2000) := ''; 
  horizontal VARCHAR2(2000) := ''; 
  prod VARCHAR2(2000) := ''; 
  cat VARCHAR2(2000) := ''; 
  counter NUMBER := 0; 
  priority NUMBER := 0;
begin 
  open cur_get_cats;
  loop 
    FETCH cur_get_cats into curCat;
      exit when cur_get_cats%NOTFOUND;
      listAs := 'full';
      open cur_get_list_as(curCat);
      FETCH cur_get_list_as into listAs;
	  close cur_get_list_as;
      open cur_get_layout(curCat);
      FETCH cur_get_layout into horizontal;
      CLOSE cur_get_layout;
      if listAs is null then
         listAs := 'full';
      end if;
      counter := 0;
      if listAs = 'full' then
	  	if horizontal = '1' then
	        open cur_prodcats_by_full_and_price(curCat);
	        loop
	          fetch cur_prodcats_by_full_and_price into cat, prod, priority;
	          exit when cur_prodcats_by_full_and_price%NOTFOUND;
	          update relationship set ordinal = counter
	           where def_contenttype = 'Product' 
	             and def_name = 'products' 
	             and parent_contentnode_id = cat
	             and child_contentnode_id = prod; 
	          counter := counter + 1;
	        end loop;
	        close cur_prodcats_by_full_and_price;
		 else
	        open cur_prodcats_by_full(curCat);
	        loop
	          fetch cur_prodcats_by_full into cat, prod, priority;
	          exit when cur_prodcats_by_full%NOTFOUND;
	          update relationship set ordinal = counter
	           where def_contenttype = 'Product' 
	             and def_name = 'products' 
	             and parent_contentnode_id = cat
	             and child_contentnode_id = prod; 
	          counter := counter + 1;
	        end loop;
	        close cur_prodcats_by_full;
		 end if;
      end if;
      if listAs = 'glance' then
	  	if horizontal = '1' then
	        open cur_prodcats_by_gl_and_price(curCat);
	        loop
	          fetch cur_prodcats_by_gl_and_price into cat, prod, priority;
	          exit when cur_prodcats_by_gl_and_price%NOTFOUND;
	          update relationship set ordinal = counter
	           where def_contenttype = 'Product' 
	             and def_name = 'products' 
	             and parent_contentnode_id = cat
	             and child_contentnode_id = prod; 
	          counter := counter + 1;
	        end loop;
	        close cur_prodcats_by_gl_and_price;
		 else
	        open cur_prodcats_by_glance(curCat);
	        loop
	          fetch cur_prodcats_by_glance into cat, prod, priority;
	          exit when cur_prodcats_by_glance%NOTFOUND;
	          update relationship set ordinal = counter
	           where def_contenttype = 'Product' 
	             and def_name = 'products' 
	             and parent_contentnode_id = cat
	             and child_contentnode_id = prod; 
	          counter := counter + 1;
	        end loop;
	        close cur_prodcats_by_glance;
		end if;
      end if;
      if listAs = 'nav' then
	  	if horizontal = '1' then
	        open cur_prodcats_by_nav_and_price(curCat);
	        loop
	          fetch cur_prodcats_by_nav_and_price into cat, prod, priority;
	          exit when cur_prodcats_by_nav_and_price%NOTFOUND;
	          update relationship set ordinal = counter
	           where def_contenttype = 'Product' 
	             and def_name = 'products' 
	             and parent_contentnode_id = cat
	             and child_contentnode_id = prod; 
	          counter := counter + 1;
	        end loop;
	        close cur_prodcats_by_nav_and_price;
		 else
	        open cur_prodcats_by_nav(curCat);
	        loop
	          fetch cur_prodcats_by_nav into cat, prod, priority;
	          exit when cur_prodcats_by_nav%NOTFOUND;
	          update relationship set ordinal = counter
	           where def_contenttype = 'Product' 
	             and def_name = 'products' 
	             and parent_contentnode_id = cat
	             and child_contentnode_id = prod; 
	          counter := counter + 1;
	        end loop;
	        close cur_prodcats_by_nav;
		end if;
      end if;
  end loop; 
end;



Fix Category --> SubCategory priority
declare 
  CURSOR cur_cats is
                   select content_name 
                   from fdstore.contentnode 
                   where content_type = 'C'
                   order by content_name asc; 
  CURSOR cur_subcats (cat_in VARCHAR2) 
  IS 
      select 'Category:' || cat.content_name catName,c.priority 
                  from fdstore.category c, fdstore.contentnode cat, fdstore.contentnode parentcat
                  where c.parent_category_id = parentcat.id
                  and c.content_id = cat.id
                  and parentcat.content_name = cat_in
                  and c.store_id = '41781199'
                  order by priority asc, cat.full_name asc; 
  curCat VARCHAR2(2000) := ''; 
  parentCat VARCHAR2(2000) := '';
  counter NUMBER := 0; 
  prio number;
begin 
  open cur_cats;
  loop
      FETCH cur_cats into parentCat;
      exit when cur_cats%NOTFOUND;
      open cur_subcats(parentCat); 
      counter := 0; 
      loop 
       FETCH cur_subcats into curCat, prio; 
        exit when cur_subcats%NOTFOUND;
        update relationship set ordinal = counter 
         where def_contenttype = 'Category' 
         and def_name = 'subcategories' 
         and parent_contentnode_id = 'Category:' || parentCat
         and child_contentnode_id = curCat; 
        counter := counter + 1; 
       end loop;
         close cur_subcats;
  end loop; 
end;


-- Fix DEPT_NAV, FEATURED_BRANDS priority
declare 
  CURSOR cur_attr is 
      select distinct rdf.contenttype_id parent, rdf.name, rd.contenttype_id ref, cn.id 
      from relationshipdefinition rdf, relationshipdestination rd, contentnode cn 
      where rdf.cardinality_code = 'Many' 
       and rdf.contenttype_id in ('Store','Product','Department','Category','Brand') 
       and rd.contenttype_id in ('Store','Product','Department','Category', 'Brand') 
       and rdf.id = rd.relationshipdefinition_id 
       and cn.contenttype_id = rdf.contenttype_id 
       and rdf.name in ('DEPT_NAV', 'FEATURED_BRANDS') 
       order by rdf.name asc; 
  CURSOR cur_rel (attr VARCHAR2, parent VARCHAR2, cType VARCHAR2) is 
      select c.content_name, a.priority 
      from fdstore.attribute a, fdstore.contentnode p, fdstore.contentnode c 
      where p.store_id = '41781199' 
      and c.store_id = '41781199' 
      and p.id = a.content_id 
      and a.ref_name = c.content_name 
      and a.attribute_name = attr 
      and cType || ':' || p.content_name = parent 
      order by a.priority asc, c.full_name asc; 
  parentType VARCHAR2(2000) := ''; 
  childType VARCHAR2(2000) := ''; 
  parentNode VARCHAR2(2000) := ''; 
  attrName VARCHAR2(2000) := ''; 
  cNode VARCHAR2(2000) := ''; 
  counter NUMBER := 0; 
  prio number; 
begin 
  open cur_attr; 
  loop 
      FETCH cur_attr into parentType, attrName, childType, parentNode; 
      exit when cur_attr%NOTFOUND; 
      open cur_rel(attrName, parentNode, parentType); 
      counter := 0; 
      loop 
       FETCH cur_rel into cNode, prio; 
        exit when cur_rel%NOTFOUND; 
        update relationship set ordinal = counter 
         where def_name = attrName 
         and parent_contentnode_id = parentNode 
         and child_contentnode_id = childType || ':' || cNode; 
        counter := counter + 1; 
       end loop; 
         close cur_rel; 
  end loop; 
end;


-- Fix FEATURED_PRODUCTS priority
declare 
  CURSOR cur_attr is 
      select distinct rdf.contenttype_id parent, rdf.name, rd.contenttype_id ref, cn.id 
      from relationshipdefinition rdf, relationshipdestination rd, contentnode cn 
      where rdf.cardinality_code = 'Many' 
       and rdf.contenttype_id in ('Store','Product','Department','Category') 
       and rd.contenttype_id in ('Store','Product','Department','Category') 
       and rdf.id = rd.relationshipdefinition_id 
       and cn.contenttype_id = rdf.contenttype_id 
       and rdf.name in ('FEATURED_PRODUCTS')
       order by rdf.name asc; 
  CURSOR cur_rel (attr VARCHAR2, parent VARCHAR2, cType VARCHAR2) is 
      select c.content_name, a.priority 
      from fdstore.attribute a, fdstore.contentnode p, fdstore.contentnode c 
      where p.store_id = '41781199' 
      and c.store_id = '41781199' 
      and p.id = a.content_id 
      and a.ref_name2 = c.content_name 
      and a.attribute_name = attr 
      and cType || ':' || p.content_name = parent 
      order by a.priority asc, c.full_name asc; 
  parentType VARCHAR2(2000) := ''; 
  childType VARCHAR2(2000) := ''; 
  parentNode VARCHAR2(2000) := ''; 
  attrName VARCHAR2(2000) := ''; 
  cNode VARCHAR2(2000) := ''; 
  counter NUMBER := 0; 
  prio number; 
begin 
  open cur_attr; 
  loop 
      FETCH cur_attr into parentType, attrName, childType, parentNode; 
      exit when cur_attr%NOTFOUND; 
      open cur_rel(attrName, parentNode, parentType); 
      counter := 0; 
      loop 
       FETCH cur_rel into cNode, prio; 
        exit when cur_rel%NOTFOUND; 
        update relationship set ordinal = counter 
         where def_name = attrName 
         and parent_contentnode_id = parentNode 
         and child_contentnode_id = childType || ':' || cNode; 
        counter := counter + 1; 
       end loop; 
         close cur_rel; 
  end loop; 
end;

-- FIX STRINGS
declare 
  CURSOR cur_node is 
    select EDITORIAL_TITLE, BLURB, ALT_TEXT, FULL_NAME, NAV_NAME, GLANCE_NAME, 
           decode(content_type, 'B', 'Brand', 'D', 'Department', 'P', 'Product', 'C', 'Category', null) || ':' || content_name
    from fdstore.contentnode where store_id = '41781199';
  editorial_title VARCHAR2(2000);
  blurb VARCHAR2(2000);
  alt_text VARCHAR2(2000);
  full_name VARCHAR2(2000);
  nav_name VARCHAR2(2000);
  glance_name VARCHAR2(2000);
  content_name VARCHAR2(2000);
begin 
  open cur_node;
  loop
    FETCH cur_node into EDITORIAL_TITLE, BLURB, ALT_TEXT, FULL_NAME, NAV_NAME, GLANCE_NAME, CONTENT_NAME;
    exit when cur_node%NOTFOUND;
    update attribute set value = EDITORIAL_TITLE 
	where def_name = 'EDITORIAL_TITLE' 
          and contentnode_id = CONTENT_NAME;
    update attribute set value = BLURB
	where def_name = 'BLURB' 
          and contentnode_id = CONTENT_NAME;
    update attribute set value = ALT_TEXT 
	where def_name = 'ALT_TEXT' 
          and contentnode_id = CONTENT_NAME;
    update attribute set value = FULL_NAME
	where def_name = 'FULL_NAME' 
          and contentnode_id = CONTENT_NAME;
    update attribute set value = NAV_NAME 
	where def_name = 'NAV_NAME' 
          and contentnode_id = CONTENT_NAME;
    update attribute set value = GLANCE_NAME
	where def_name = 'GLANCE_NAME' 
          and contentnode_id = CONTENT_NAME;
  end loop;
end;

-- FIX THE MISSING FILTER_LIST ATTRIBUTE
insert into attribute 
     SELECT 'Category:'||content_name,system_seq.nextval,trim(attribute_value),0,'FILTER_LIST','Category' 
     FROM fdstore.ATTRIBUTE,fdstore.contentnode 
      WHERE ATTRIBUTE_NAME ='FILTER_LIST' and attribute.store_id = '41781199'
      and content_id = id;

-- Fix VIRTUAL_GROUPS
-- NOTE this is copy / paste from fix for products
declare 
   CURSOR cur_get_cats is
	select content_name from fdstore.contentnode 
	where id in (select content_id from fdstore.attribute where attribute_name = 'VIRTUAL_GROUP' and store_id = '41781199');
   CURSOR cur_get_list_as(cat_in VARCHAR2) is 
	select value from (
		select * from
		(select child_contentnode_id nodeId, level levelNumber from all_nodes 
			start with child_contentnode_id = 'Category:' || cat_in
			connect by prior parent_contentnode_id = child_contentnode_id) nodes,
		(select contentnode_id nodeId, value from attribute where def_name = 'LIST_AS') listAs
		where nodes.nodeId = listAs.nodeId
		 and rownum = 1
		 order by nodes.levelNumber asc
		);
   CURSOR cur_get_layout(cat_in VARCHAR2) is 
	select value from (
		select * from
		(select child_contentnode_id nodeId, level levelNumber from all_nodes 
			start with child_contentnode_id = 'Category:' || cat_in
			connect by prior parent_contentnode_id = child_contentnode_id) nodes,
		(select contentnode_id nodeId, value from attribute where def_name = 'LAYOUT') listAs
		where nodes.nodeId = listAs.nodeId
		 and rownum = 1
		 order by nodes.levelNumber asc
		);
   CURSOR cur_prodcats_by_full(cat_in VARCHAR2) is 
             select 'Category:' || category.content_name as catName, 
                        'Product:' || product.content_name prodName,
                    pc.priority
             from fdstore.productcategory pc, fdstore.contentnode product, fdstore.contentnode category 
             where product.id = pc.product_id 
             and product.store_id = '41781199'
             and category.id = pc.category_id
             and category.content_name = cat_in
             order by category.content_name, pc.priority asc, product.full_name asc, product.content_name asc; 
   CURSOR cur_prodcats_by_full_and_price(cat_in VARCHAR2) is 
   		  select catName, prodName, priority
		  from (
             select 'Category:' || category.content_name as catName, 
                    'Product:' || product.content_name prodName,
                    pc.priority,
					product.full_name productName,
					product.content_name product_content_name,
					min(skuPrice.default_price) defaultPrice
             from fdstore.productcategory pc, fdstore.contentnode product, fdstore.contentnode category, fdstore.sku sku,
			 	  (select p.sku_code, p.default_price
				  	from erps.product p, erps.material m, erps.materialproxy mp
					where p.id = mp.product_id
					  and m.id = mp.mat_id
					    and p.version = (select max(version) from erps.product where sku_code = p.sku_code and unavailability_status is null)) skuPrice
             where product.id = pc.product_id 
             and product.store_id = '41781199'
             and category.id = pc.category_id
             and category.content_name = cat_in
			 and sku.product_id = product.id
			 and sku.skucode = skuPrice.sku_code (+)
			 group by 'Category:' || category.content_name, 
                    'Product:' || product.content_name,
                    pc.priority,
					product.full_name,
					product.content_name
		  ) order by catName asc, priority asc, productName asc, defaultPrice asc, product_content_name asc;
  CURSOR cur_prodcats_by_glance(cat_in VARCHAR2) is 
             select 'Category:' || category.content_name as catName, 
                        'Product:' || product.content_name prodName,
                    pc.priority
             from fdstore.productcategory pc, fdstore.contentnode product, fdstore.contentnode category 
             where product.id = pc.product_id 
             and product.store_id = '41781199'
             and category.id = pc.category_id
             and category.content_name = cat_in
             order by category.content_name, pc.priority asc, nvl(product.glance_name, product.full_name) asc, product.content_name asc; 
   CURSOR cur_prodcats_by_gl_and_price(cat_in VARCHAR2) is 
   		  select catName, prodName, priority
		  from (
             select 'Category:' || category.content_name as catName, 
                    'Product:' || product.content_name prodName,
                    pc.priority,
					nvl(product.glance_name, product.full_name) productName,
					product.content_name product_content_name,
					min(skuPrice.default_price) defaultPrice
             from fdstore.productcategory pc, fdstore.contentnode product, fdstore.contentnode category, fdstore.sku sku,
			 	  (select p.sku_code, p.default_price
				  	from erps.product p, erps.material m, erps.materialproxy mp
					where p.id = mp.product_id
					  and m.id = mp.mat_id
					    and p.version = (select max(version) from erps.product where sku_code = p.sku_code and unavailability_status is null)) skuPrice
             where product.id = pc.product_id 
             and product.store_id = '41781199'
             and category.id = pc.category_id
             and category.content_name = cat_in
			 and sku.product_id = product.id
			 and sku.skucode = skuPrice.sku_code (+)
			 group by 'Category:' || category.content_name, 
                    'Product:' || product.content_name,
                    pc.priority,
					nvl(product.glance_name, product.full_name),
					product.content_name
		  ) order by catName asc, priority asc, productName asc, defaultPrice asc, product_content_name asc;
  CURSOR cur_prodcats_by_nav(cat_in VARCHAR2) is 
             select 'Category:' || category.content_name as catName, 
                        'Product:' || product.content_name prodName,
                    pc.priority
             from fdstore.productcategory pc, fdstore.contentnode product, fdstore.contentnode category 
             where product.id = pc.product_id 
             and product.store_id = '41781199'
             and category.id = pc.category_id
             and category.content_name = cat_in
             order by category.content_name, pc.priority asc, nvl(product.nav_name, product.full_name) asc, product.content_name asc; 
   CURSOR cur_prodcats_by_nav_and_price(cat_in VARCHAR2) is 
   		  select catName, prodName, priority
		  from (
             select 'Category:' || category.content_name as catName, 
                    'Product:' || product.content_name prodName,
                    pc.priority,
					nvl(product.nav_name, product.full_name) productName,
					product.content_name product_content_name,
					min(skuPrice.default_price) defaultPrice
             from fdstore.productcategory pc, fdstore.contentnode product, fdstore.contentnode category, fdstore.sku sku,
			 	  (select p.sku_code, p.default_price
				  	from erps.product p, erps.material m, erps.materialproxy mp
					where p.id = mp.product_id
					  and m.id = mp.mat_id
					    and p.version = (select max(version) from erps.product where sku_code = p.sku_code and unavailability_status is null)) skuPrice
             where product.id = pc.product_id 
             and product.store_id = '41781199'
             and category.id = pc.category_id
             and category.content_name = cat_in
			 and sku.product_id = product.id
			 and sku.skucode = skuPrice.sku_code (+)
			 group by 'Category:' || category.content_name, 
                    'Product:' || product.content_name,
                    pc.priority,
					nvl(product.nav_name, product.full_name),
					product.content_name
		  ) order by catName asc, priority asc, productName asc, defaultPrice asc, product_content_name asc;
  curCat VARCHAR2(2000) := ''; 
  listAs VARCHAR2(2000) := ''; 
  horizontal VARCHAR2(2000) := ''; 
  prod VARCHAR2(2000) := ''; 
  cat VARCHAR2(2000) := ''; 
  counter NUMBER := 0; 
  priority NUMBER := 0;
begin 
  open cur_get_cats;
  loop 
    FETCH cur_get_cats into curCat;
      exit when cur_get_cats%NOTFOUND;
      -- delete all products so we can reinsert
      delete from relationship where parent_contentnode_id = 'Category:' || curCat and def_name = 'products';
      listAs := 'full';
      open cur_get_list_as(curCat);
      FETCH cur_get_list_as into listAs;
	  close cur_get_list_as;
      open cur_get_layout(curCat);
      FETCH cur_get_layout into horizontal;
      CLOSE cur_get_layout;
      if listAs is null then
         listAs := 'full';
      end if;
      counter := 0;
      if listAs = 'full' then
	  	if horizontal = '1' then
	        open cur_prodcats_by_full_and_price(curCat);
	        loop
	          fetch cur_prodcats_by_full_and_price into cat, prod, priority;
	          exit when cur_prodcats_by_full_and_price%NOTFOUND;
	          insert into relationship values (cat,
					counter,
					system_seq.nextVal,
                              'products',
                              'Product',
					prod);
	          counter := counter + 1;
	        end loop;
	        close cur_prodcats_by_full_and_price;
		 else
	        open cur_prodcats_by_full(curCat);
	        loop
	          fetch cur_prodcats_by_full into cat, prod, priority;
	          exit when cur_prodcats_by_full%NOTFOUND;
	          insert into relationship values (cat,
					counter,
					system_seq.nextVal,
                              'products',
                              'Product',
					prod);
	          counter := counter + 1;
	        end loop;
	        close cur_prodcats_by_full;
		 end if;
      end if;
      if listAs = 'glance' then
	  	if horizontal = '1' then
	        open cur_prodcats_by_gl_and_price(curCat);
	        loop
	          fetch cur_prodcats_by_gl_and_price into cat, prod, priority;
	          exit when cur_prodcats_by_gl_and_price%NOTFOUND;
	          insert into relationship values (cat,
					counter,
					system_seq.nextVal,
                              'products',
                              'Product',
					prod);
	          counter := counter + 1;
	        end loop;
	        close cur_prodcats_by_gl_and_price;
		 else
	        open cur_prodcats_by_glance(curCat);
	        loop
	          fetch cur_prodcats_by_glance into cat, prod, priority;
	          exit when cur_prodcats_by_glance%NOTFOUND;
	          insert into relationship values (cat,
					counter,
					system_seq.nextVal,
                              'products',
                              'Product',
					prod);
	          counter := counter + 1;
	        end loop;
	        close cur_prodcats_by_glance;
		end if;
      end if;
      if listAs = 'nav' then
	  	if horizontal = '1' then
	        open cur_prodcats_by_nav_and_price(curCat);
	        loop
	          fetch cur_prodcats_by_nav_and_price into cat, prod, priority;
	          exit when cur_prodcats_by_nav_and_price%NOTFOUND;
	          insert into relationship values (cat,
					counter,
					system_seq.nextVal,
                              'products',
                              'Product',
					prod);
	          counter := counter + 1;
	        end loop;
	        close cur_prodcats_by_nav_and_price;
		 else
	        open cur_prodcats_by_nav(curCat);
	        loop
	          fetch cur_prodcats_by_nav into cat, prod, priority;
	          exit when cur_prodcats_by_nav%NOTFOUND;
	          insert into relationship values (cat,
					counter,
					system_seq.nextVal,
                              'products',
                              'Product',
					prod);
	          counter := counter + 1;
	        end loop;
	        close cur_prodcats_by_nav;
		end if;
      end if;
  end loop; 
end;

-- For FDSTORE.ATTRIBUTE table...remember to add indexes to:
    CONTENT_ID
    ATTRIBUTE_NAME

-- Fix RATING
declare 
  CURSOR cur_attr is 
      select distinct rdf.contenttype_id parent, rdf.name, rd.contenttype_id ref, cn.id 
      from relationshipdefinition rdf, relationshipdestination rd, contentnode cn 
      where rdf.cardinality_code = 'Many' 
       and rdf.contenttype_id in ('Product') 
       and rd.contenttype_id in ('DomainValue') 
       and rdf.id = rd.relationshipdefinition_id 
       and cn.contenttype_id = rdf.contenttype_id 
       and rdf.name in ('RATING') 
       order by rdf.name asc; 
  CURSOR cur_rel (attr VARCHAR2, parent VARCHAR2, cType VARCHAR2) is 
      select c.content_name, a.priority 
      from fdstore.attribute a, fdstore.contentnode p, fdstore.contentnode c 
      where p.store_id = '41781199' 
      and c.store_id = '41781199' 
      and p.id = a.content_id 
      and a.ref_name = c.content_name 
      and a.attribute_name = attr 
      and cType || ':' || p.content_name = parent 
      order by a.priority asc, c.full_name asc; 
  parentType VARCHAR2(2000) := ''; 
  childType VARCHAR2(2000) := ''; 
  parentNode VARCHAR2(2000) := ''; 
  attrName VARCHAR2(2000) := ''; 
  cNode VARCHAR2(2000) := ''; 
  counter NUMBER := 0; 
  prio number; 
begin 
  open cur_attr; 
  loop 
      FETCH cur_attr into parentType, attrName, childType, parentNode; 
      exit when cur_attr%NOTFOUND; 
      open cur_rel(attrName, parentNode, parentType); 
      counter := 0; 
      loop 
       FETCH cur_rel into cNode, prio; 
        exit when cur_rel%NOTFOUND; 
        update relationship set ordinal = counter 
         where def_name = attrName 
         and parent_contentnode_id = parentNode 
         and child_contentnode_id = childType || ':' || cNode; 
        counter := counter + 1; 
       end loop; 
         close cur_rel; 
  end loop; 
end;


-- Fix empty FILTER_LIST
update attribute set value = 
   (select attribute_value 
   	from fdstore.attribute a, fdstore.contentnode c 
	where a.content_id = c.id 
	 and  'Category' || ':' || c.content_name = contentnode_id 
	 and  a.attribute_name = 'FILTER_LIST' 
	 and c.store_id = '41781199') 
	 where contentnode_id like 'Category:%' 
	  and def_name = 'FILTER_LIST'

-- FIX FOR BAD RELATIONSHIPS
  update relationship set def_contenttype = 'Domain', child_contentnode_id = 'Domain:stinky'
  where def_name in ('RG_TASTE_PRICE', 'RATING', 'RATING_HOME') and child_contentnode_id = 'Category:stinky';

  update relationship set def_contenttype = 'Domain', child_contentnode_id = 'Domain:organic'
  where def_name in ('RG_TASTE_PRICE', 'RATING', 'RATING_HOME') and child_contentnode_id = 'Category:organic';

  update relationship set def_contenttype = 'Domain', child_contentnode_id = 'Domain:firm'
  where def_name in ('RG_TASTE_PRICE', 'RATING', 'RATING_HOME') and child_contentnode_id = 'Category:firm';

  update relationship set def_contenttype = 'Domain', child_contentnode_id = 'Domain:firm'
  where def_name in ('RG_TASTE_PRICE', 'RATING', 'RATING_HOME') and child_contentnode_id = 'Category:firm';


-- INsert the missig WINE_COUNTRY RELATIONSHIPS
insert into relationship 
select 'Product:'||content_name,0,system_seq.nextval,'WINE_COUNTRY','DomainValue', 
replace('DomainValue:'||trim(ref_name)||'_'||ref_name2,' ','_') AS CN 
FROM FDSTORE.attribute,FDSTORE.contentnode 
where attribute_name ='WINE_COUNTRY' AND attribute.store_id = '41781199' 
  and FDSTORE.CONTENTNODE.ID = CONTENT_ID


-- Fix special characters
declare 
  CURSOR cur_node is 
   select decode(content_type, 'D', 'Domain', 'B', 'Brand', 'D', 'Department', 'P', 'Product', 'C', 'Category', null) || ':' || content_name, 
          a.attribute_name, a.attribute_value 
   from fdstore.attribute a, fdstore.contentnode c
   where a.attribute_name in ('RATING_PROD_NAME')
   and a.store_id = '41781199'
   and a.content_id = c.id;
  cname VARCHAR2(2000);
  aname VARCHAR2(2000);
  avalue VARCHAR2(2000);
begin 
  open cur_node;
  loop
    FETCH cur_node into cname, aname, avalue;
    exit when cur_node%NOTFOUND;
    update attribute set value = avalue
	where def_name = aname
          and contentnode_id = cname;
  end loop;
end;


FINAL ISSUES:
DONE / TEST 1.  Check EDITORIAL inheritance logic - ContentNodeModel.java
DONE / TEST 2.  For Featured Products, the catId and prodCatId isn't looking for the 
one closest; instead it looks like it's taking primary home - THIS IS BIG
  department_bottom_featured.jsp AND DepartmentModel.java add getFeaturedProducts
DONE / TEST 3.  Still have not implemented the final sort by content name if no other sorts work - ContentNodeComparator.java
  THIS NEEDS TO BE DONE IN THE MAIN DEV BRANCH FOR THE OLD STYLE STORE FOR COMPARISON
  CR4313
DONE / TEST 4.  YMAL incorrect - ContentFactory.java - getProductByName(catId, prodId) - always returning bogus product 


DONE / TEST 5. Side nav sort incorrect if nav_name equal
  SideNav.java
  NavigationElement.java

More fixes for inheritance: ContentNodeModel.java