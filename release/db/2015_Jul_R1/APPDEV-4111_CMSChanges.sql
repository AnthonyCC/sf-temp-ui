alter table CMS.PUBLISHMESSAGES modify MESSAGE  varchar2(1024);
--contenttype 
insert into cms.contenttype values ('WebPage','WebPage','WebPage for Mobile and Storefront','T'); 
insert into cms.contenttype values('Stopwords','Stopwords','Stopwords for Search Engine', 'F');

--attributedefinition 
insert into cms.attributedefinition values ('PAGE_TITLE', 'Category.PAGE_TITLE', 'Category','S', 'F', 'F','Page Title','One', null); 
insert into cms.attributedefinition values ('SEO_META_DESC', 'Category.SEO_META_DESC', 'Category','S', 'F', 'F','SEO Meta Description','One', null); 

insert into cms.attributedefinition values ('PAGE_TITLE', 'Product.PAGE_TITLE', 'Product','S', 'F', 'F','Page Title','One', null); 
insert into cms.attributedefinition values ('SEO_META_DESC', 'Product.SEO_META_DESC', 'Product','S', 'F', 'F','SEO Meta Description','One', null); 

insert into cms.attributedefinition values ('PAGE_TITLE', 'Department.PAGE_TITLE', 'Department','S', 'F', 'F','Page Title','One', null); 
insert into cms.attributedefinition values ('SEO_META_DESC', 'Department.SEO_META_DESC', 'Department','S', 'F', 'F','SEO Meta Description','One', null); 

insert into cms.attributedefinition values ('PAGE_TITLE', 'WebPage.PAGE_TITLE', 'WebPage','S', 'F', 'F','Page Title','One', null); 
insert into cms.attributedefinition values ('SEO_META_DESC', 'WebPage.SEO_META_DESC', 'WebPage','S', 'F', 'F','SEO Meta Description','One', null); 

insert into cms.attributedefinition values('word', 'Stopwords.word', 'Stopwords','S','F','T', 'Words(s)', 'One', null);

insert into cms.attributedefinition values ('PAGE_TITLE', 'SUPERDEPARTMENT.PAGE_TITLE' ,'SuperDepartment', 'S' ,'F', 'F' ,'Page Title', 'One' , null);
insert into cms.attributedefinition values ('SEO_META_DESC', 'SUPERDEPARTMENT.SEO_META_DESC' ,'SuperDepartment', 'S' ,'F', 'F' ,'SEO Meta Description', 'One', null );


--contentnode 
insert into cms.contentnode values ('WebPage:delivery_info_help', 'WebPage'); 
insert into cms.contentnode values ('WebPage:welcome', 'WebPage'); 
insert into cms.contentnode values ('WebPage:shop_list', 'WebPage'); 
insert into cms.contentnode values ('WebPage:delivery_info_avail', 'WebPage'); 
insert into cms.contentnode values ('WebPage:customer_profile', 'WebPage'); 
insert into cms.contentnode values ('WebPage:landing', 'WebPage'); 
insert into cms.contentnode values ('WebPage:delivery_info', 'WebPage'); 
insert into cms.contentnode values ('WebPage:standing_orders', 'WebPage'); 
insert into cms.contentnode values ('WebPage:delivery_lic', 'WebPage'); 
insert into cms.contentnode values ('WebPage:signin_info', 'WebPage'); 
insert into cms.contentnode values ('WebPage:payment_info', 'WebPage'); 
insert into cms.contentnode values ('WebPage:past_orders', 'WebPage'); 
insert into cms.contentnode values ('WebPage:delivery_info_cos', 'WebPage'); 
insert into cms.contentnode values ('WebPage:remainder_service', 'WebPage'); 
insert into cms.contentnode values ('WebPage:index', 'WebPage'); 
insert into cms.contentnode values ('WebPage:cos', 'WebPage'); 
insert into cms.contentnode values ('WebPage:FAQHome', 'WebPage'); 

--attribute 
insert into cms.attribute values ('WebPage:delivery_info_help',SYSTEM_SEQ.nextval,'Delivery Information','0','PAGE_TITLE','WebPage'); 
insert into cms.attribute values ('WebPage:delivery_info_help',SYSTEM_SEQ.nextval,'Find information on FreshDirect''s grocery delivery service and home delivery areas.','0','SEO_META_DESC','WebPage'); 
insert into cms.attribute values ('WebPage:delivery_info_help',SYSTEM_SEQ.nextval,'/help/delivery_info.jsp','0','URL','WebPage'); 

insert into cms.attribute values ('WebPage:welcome',SYSTEM_SEQ.nextval,'FreshDirect is the leading online grocery shopping service. We provide fast grocery delivery to your home and office. Order today for delivery tomorrow!','0','SEO_META_DESC','WebPage'); 
insert into cms.attribute values ('WebPage:welcome',SYSTEM_SEQ.nextval,'Grocery Delivery from FreshDirect | Fresh Food Delivered Faster','0','PAGE_TITLE','WebPage'); 
insert into cms.attribute values ('WebPage:welcome',SYSTEM_SEQ.nextval,'/welcome.jsp','0','URL','WebPage'); 

insert into cms.attribute values ('WebPage:shop_list',SYSTEM_SEQ.nextval,'FreshDirect - Reorder from Your Lists','0','PAGE_TITLE','WebPage'); 
insert into cms.attribute values ('WebPage:shop_list',SYSTEM_SEQ.nextval,'/quickshop/qs_shop_from_list.jsp','0','URL','WebPage'); 

insert into cms.attribute values ('WebPage:delivery_info_avail',SYSTEM_SEQ.nextval,'Available Delivery Slots','0','PAGE_TITLE','WebPage'); 
insert into cms.attribute values ('WebPage:delivery_info_avail',SYSTEM_SEQ.nextval,'/your_account/delivery_info_avail_slots.jsp','0','URL','WebPage'); 

insert into attribute values ('WebPage:customer_profile',SYSTEM_SEQ.nextval,'FreshDirect - Your Profile','0','PAGE_TITLE','WebPage'); 
insert into attribute values ('WebPage:customer_profile',SYSTEM_SEQ.nextval,'/your_account/customer_profile_summary.jsp','0','URL','WebPage'); 

insert into attribute values ('WebPage:landing',SYSTEM_SEQ.nextval,'FreshDirect - Purchase Gift Card','0','PAGE_TITLE','WebPage'); 
insert into attribute values ('WebPage:landing',SYSTEM_SEQ.nextval,'/gift_card/purchase/landing.jsp','0','URL','WebPage'); 
insert into attribute values ('WebPage:landing',SYSTEM_SEQ.nextval,'Now you can slip thousands of fresh and delicious food options into one little envelope or email. Give the gift of food, the one gift everyone loves.','0','SEO_META_DESC','WebPage'); 

insert into attribute values ('WebPage:delivery_info',SYSTEM_SEQ.nextval,'FreshDirect - Your Account - Delivery Addresses','0','PAGE_TITLE','WebPage'); 
insert into attribute values ('WebPage:delivery_info',SYSTEM_SEQ.nextval,'/your_account/delivery_information.jsp','0','URL','WebPage'); 

insert into attribute values ('WebPage:standing_orders',SYSTEM_SEQ.nextval,'FreshDirect - Standing Orders','0','PAGE_TITLE','WebPage'); 
insert into attribute values ('WebPage:standing_orders',SYSTEM_SEQ.nextval,'/quickshop/qs_standing_orders.jsp','0','URL','WebPage'); 

insert into attribute values ('WebPage:delivery_lic',SYSTEM_SEQ.nextval,'Delivery Information','0','PAGE_TITLE','WebPage'); 
insert into attribute values ('WebPage:delivery_lic',SYSTEM_SEQ.nextval,'/help/delivery_lic_pickup.jsp','0','URL','WebPage'); 

insert into attribute values ('WebPage:signin_info',SYSTEM_SEQ.nextval,'Fresh Direct - Your Account - User Name, Password and Contact Info','0','PAGE_TITLE','WebPage'); 
insert into attribute values ('WebPage:signin_info',SYSTEM_SEQ.nextval,'/your_account/signin_information.jsp','0','URL','WebPage'); 

insert into attribute values ('WebPage:payment_info',SYSTEM_SEQ.nextval,'FreshDirect - Your Account - Payment Options','0','PAGE_TITLE','WebPage'); 
insert into attribute values ('WebPage:payment_info',SYSTEM_SEQ.nextval,'/your_account/payment_information.jsp','0','URL','WebPage'); 

insert into attribute values ('WebPage:past_orders',SYSTEM_SEQ.nextval,'FreshDirect - Reorder from Past Orders','0','URL','WebPage'); 
insert into attribute values ('WebPage:past_orders',SYSTEM_SEQ.nextval,'/quickshop/qs_past_orders.jsp','0','URL','WebPage'); 

insert into attribute values ('WebPage:delivery_info_cos',SYSTEM_SEQ.nextval,'/help/delivery_info_cos.jsp','0','URL','WebPage'); 
insert into attribute values ('WebPage:delivery_info_cos',SYSTEM_SEQ.nextval,'Best Snacks, Coffee '||'&'||' Supplies | Delivered to Your Office','0','PAGE_TITLE','WebPage'); 
insert into attribute values ('WebPage:delivery_info_cos',SYSTEM_SEQ.nextval,'FreshDirect makes stocking the breakroom fast and easy. Shop for snacks, supplies, coffee and more. We have catering and meals for your next meeting!','0','SEO_META_DESC','WebPage'); 

insert into attribute values ('WebPage:remainder_service',SYSTEM_SEQ.nextval,'FreshDirect - Your Account - Delivery Addresses','0','URL','WebPage'); 
insert into attribute values ('WebPage:remainder_service',SYSTEM_SEQ.nextval,'/your_account/reminder_service.jsp','0','URL','WebPage'); 

insert into attribute values ('WebPage:index',SYSTEM_SEQ.nextval,'/index.jsp','0','URL','WebPage'); 
insert into attribute values ('WebPage:index',SYSTEM_SEQ.nextval,'Online Grocery Shopping and Express Food Delivery | FreshDirect','0','PAGE_TITLE','WebPage'); 
insert into attribute values ('WebPage:index',SYSTEM_SEQ.nextval,'FreshDirect makes online grocery shopping fast and easy. Find fresh, high quality food and meals, plus all your supermarket brand favorites.','0','SEO_META_DESC','WebPage'); 

insert into attribute values ('WebPage:cos',SYSTEM_SEQ.nextval,'Welcome to FreshDirect','0','PAGE_TITLE','WebPage'); 
insert into attribute values ('WebPage:cos',SYSTEM_SEQ.nextval,'/cos.jsp','0','URL','WebPage'); 

insert into attribute values ('WebPage:FAQHome',SYSTEM_SEQ.nextval,'Welcome to FreshDirect - Frequently Asked Questions','0','PAGE_TITLE','WebPage'); 
insert into attribute values ('WebPage:FAQHome',SYSTEM_SEQ.nextval,'/help/faq_home.jsp','0','URL','WebPage'); 

insert into contentnode values ('FDFolder:WebPage', 'FDFolder');
insert into relationship values ('Store:FreshDirect', 0,SYSTEM_SEQ.nextval,'folders','FDFolder','FDFolder:WebPage');

--relationship 
insert into relationship values ('FDFolder:WebPage','0',SYSTEM_SEQ.nextval,'children','WebPage','WebPage:index'); 
insert into relationship values ('FDFolder:WebPage','1',SYSTEM_SEQ.nextval,'children','WebPage','WebPage:welcome'); 
insert into relationship values ('FDFolder:WebPage','11',SYSTEM_SEQ.nextval,'children','WebPage','WebPage:shop_list'); 
insert into relationship values ('FDFolder:WebPage','22',SYSTEM_SEQ.nextval,'children','WebPage','WebPage:delivery_info_avail'); 
insert into relationship values ('FDFolder:WebPage','21',SYSTEM_SEQ.nextval,'children','WebPage','WebPage:customer_profile'); 
insert into relationship values ('FDFolder:WebPage','2',SYSTEM_SEQ.nextval,'children','WebPage','WebPage:landing'); 
insert into relationship values ('FDFolder:WebPage','9',SYSTEM_SEQ.nextval,'children','WebPage','WebPage:standing_orders');
insert into relationship values ('FDFolder:WebPage','24',SYSTEM_SEQ.nextval,'children','WebPage','WebPage:delivery_lic'); 
insert into relationship values ('FDFolder:WebPage','3',SYSTEM_SEQ.nextval,'children','WebPage','WebPage:delivery_info'); 
insert into relationship values ('FDFolder:WebPage','14',SYSTEM_SEQ.nextval,'children','WebPage','WebPage:payment_info'); 
insert into relationship values ('FDFolder:WebPage','29',SYSTEM_SEQ.nextval,'children','WebPage','WebPage:past_orders'); 
insert into relationship values ('FDFolder:WebPage','4',SYSTEM_SEQ.nextval,'children','WebPage','WebPage:delivery_info_cos'); 
insert into relationship values ('FDFolder:WebPage','16',SYSTEM_SEQ.nextval,'children','WebPage','WebPage:remainder_service'); 
insert into relationship values ('FDFolder:WebPage','19',SYSTEM_SEQ.nextval,'children','WebPage','WebPage:cos'); 

commit;