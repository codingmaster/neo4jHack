LOAD CSV WITH HEADERS FROM 'file:/pmc_codes.csv' AS row
FIELDTERMINATOR ','
MERGE (pmc: PMC {
  id: "SC" + row.CAT_SUBJECTCODE,
  value: row.CAT_TITLE
})
MERGE (superpmc: PMC {
  id: "SC" + row.SUPERCAT_SUBJECTCODE,
  value: row.SUPERCAT_TITLE
})
MERGE (pmc)-[r:HAS_PMC_PARENT]->(superpmc)