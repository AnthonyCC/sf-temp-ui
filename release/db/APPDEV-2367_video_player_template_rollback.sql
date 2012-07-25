DELETE FROM cms.contenttype WHERE ID='YoutubeVideo';
DELETE FROM cms.attributedefinition WHERE ID='YoutubeVideo.YOUTUBE_VIDEO_ID';
DELETE FROM cms.attributedefinition WHERE ID='YoutubeVideo.TITLE';
DELETE FROM cms.relationshipdefinition WHERE ID='YoutubeVideo.CONTENT';
DELETE FROM cms.relationshipdestination WHERE ID='FDFolder.children.YoutubeVideo';
DELETE FROM cms.relationshipdestination WHERE ID='YoutubeVideo.CONTENT.Html';
