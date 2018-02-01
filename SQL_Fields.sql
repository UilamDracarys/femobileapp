SELECT b.fld_id as FieldID, b.fld_farm_id as FarmID, b.fld_name as FieldName, s.fld_suit as Suitability, 
b.fld_area as Area, s.fld_limits as Limits, o.fld_canals as Canals, s.fld_rdcond as RoadCond,
o.fld_mechmeth as MechMeth, o.fld_tractacc as TractorAcc, o.fld_rowwidth as RowWidth,
o.fld_rowdir as RowDir, b.fld_soilTyp as SoilType, b.fld_var as Variety, 
o.fld_harvmeth as HarvMeth, o.fld_cropcls as CropClass, o.fld_cmt as Comment
FROM fldsBasic b JOIN fldsSuit s JOIN fldsOthers o ON (b.fld_id = o.fld_id) AND (b.fld_id = s.fld_id)