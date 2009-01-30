--
-- 'Override Variants' profile
--
INSERT INTO CUST.PROFILE_ATTR_NAME (NAME, DESCRIPTION, CATEGORY, IS_EDITABLE) VALUES ('OverrideVariants', 'Override Variants', 'SmartStore', 'X');

-- upgrade customer overrides
--
-- INSERT INTO cust.profile
-- SELECT customer_id, 'S', 'OverrideVariants', 'dyf-freqbought1', '-1'
-- FROM cust.profile
-- where PROFILE_NAME='DYF.VariantID' and PROFILE_VALUE='freqbought'

-- delete old profile entries and the definition
--
-- DELETE FROM cust.profile WHERE PROFILE_NAME='DYF.VariantID';
-- DELETE FROM cust.profile_attr_name WHERE name='DYF.VariantID';

