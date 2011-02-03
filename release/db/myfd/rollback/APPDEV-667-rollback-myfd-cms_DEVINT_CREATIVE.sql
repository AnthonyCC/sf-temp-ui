-- rollback

-- MyFD

DELETE FROM cms.relationshipdestination WHERE ID = 'MyFD.EDITORIAL_SIDE.Image';
DELETE FROM cms.relationshipdestination WHERE ID = 'MyFD.EDITORIAL_SIDE.Html';
DELETE FROM cms.relationshipdestination WHERE ID = 'MyFD.EDITORIAL_MAIN.Image';
DELETE FROM cms.relationshipdestination WHERE ID = 'MyFD.EDITORIAL_MAIN.Html';
DELETE FROM cms.relationshipdestination WHERE ID = 'MyFD.HOLIDAY_GREETINGS.HolidayGreeting';
DELETE FROM cms.relationshipdestination WHERE ID = 'MyFD.HEADER.Image';
DELETE FROM cms.relationshipdefinition WHERE ID = 'MyFD.EDITORIAL_SIDE';
DELETE FROM cms.relationshipdefinition WHERE ID = 'MyFD.EDITORIAL_MAIN';
DELETE FROM cms.relationshipdefinition WHERE ID = 'MyFD.HOLIDAY_GREETINGS';
DELETE FROM cms.relationshipdefinition WHERE ID = 'MyFD.HEADER';

DELETE FROM cms.relationshipdestination WHERE ID = 'Store.myFD.MyFD';
DELETE FROM cms.relationshipdefinition WHERE ID = 'Store.myFD';

DELETE FROM cms.attributedefinition WHERE ID = 'MyFD.POLL_DADDY_API_KEY';
DELETE FROM cms.attributedefinition WHERE ID = 'MyFD.BLOG_ENTRY_COUNT';
DELETE FROM cms.attributedefinition WHERE ID = 'MyFD.BLOG_URL';

DELETE FROM cms.contenttype WHERE ID = 'MyFD';

-- HolidayGreeting

DELETE FROM cms.attributedefinition WHERE ID = 'HolidayGreeting.GREETING_TEXT';
DELETE FROM cms.attributedefinition WHERE ID = 'HolidayGreeting.endDate';
DELETE FROM cms.attributedefinition WHERE ID = 'HolidayGreeting.startDate';
DELETE FROM cms.attributedefinition WHERE ID = 'HolidayGreeting.FULL_NAME';
DELETE FROM cms.attributedefinition WHERE ID = 'HolidayGreeting.CODE';

DELETE FROM cms.contenttype WHERE ID = 'HolidayGreeting';


