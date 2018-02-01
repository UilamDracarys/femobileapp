SELECT farms.farm_id as FarmID, 
farms.farm_name AS FarmName, 
(SELECT person_name FROM person
WHERE person_id = farms.farm_pltr_id) As PlanterName,
(SELECT person_name FROM person
WHERE person_id = farms.farm_ovsr_id) As OverseerName,
farms.farm_loc As Locality, 
farms.farm_city As City, 
farms.farm_cmt As Comment 
FROM farms
LEFT JOIN person
ON (farms.farm_pltr_id = person.person_id 
AND farms.farm_ovsr_id = person.person_id )