-- Delete composite phenomena and relationships.
DELETE FROM com_phen_off;
DELETE FROM composite_phenomenon;

-- Delete domain features and relationships.
DELETE FROM df_off;
DELETE FROM foi_df;
DELETE FROM obs_df;
DELETE FROM proc_df;
DELETE FROM domain_feature;

-- Delete requests and relationships.
DELETE FROM request_composite_phenomenon;
DELETE FROM request_phenomenon;
DELETE FROM request;

-- Delete measurements and relationships.
DELETE FROM foi_off;
DELETE FROM obs_unc;
DELETE FROM phen_off;
DELETE FROM proc_foi;
DELETE FROM proc_off;
DELETE FROM proc_phen;
DELETE FROM quality;
DELETE FROM observation_template;
DELETE FROM observation;
DELETE FROM feature_of_interest;
DELETE FROM offering;
DELETE FROM phenomenon;
DELETE FROM procedure_history;
DELETE FROM procedure;

-- Delete uncertainties.
DELETE FROM u_mean;
DELETE FROM u_normal;
DELETE FROM u_realisation;
DELETE FROM u_uncertainty;
DELETE FROM u_value_unit;

