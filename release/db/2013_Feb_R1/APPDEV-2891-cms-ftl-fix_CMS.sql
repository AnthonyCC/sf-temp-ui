-- APPDEV-2891 - FTL handling is broken in CMS
-- fix invalid DB content: change all Template types to Html in CMS media tables

update cms.media set type = 'Html' where type = 'Template';

