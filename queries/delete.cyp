MATCH (n:Book) DETACH DELETE n;
MATCH (n:Originator) DETACH DELETE n;
MATCH (n:Keyword) DETACH DELETE n;
MATCH (n:PMC) DETACH DELETE n;



MATCH (n:PMC)
OPTIONAL MATCH (n)-[r]-()
DELETE n,r