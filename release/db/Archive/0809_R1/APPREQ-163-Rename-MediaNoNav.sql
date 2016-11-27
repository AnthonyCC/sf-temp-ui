--
-- [APPREQ-163] rename 'Media No Nav' to 'Media Include'
--
update cms.lookup set label='Media Include', description='Media Include' where code='97' and label='Media No Nav';

