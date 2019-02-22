WITH apoc.date.format(timestamp(), "ms", 'yyyyMMddHHmmss') AS ts
WITH "&ts=" + ts + "&apikey=" + $marvel_public +
     "&hash=" + apoc.util.md5([ts,$marvel_private,$marvel_public]) as suffix
RETURN suffix;

CALL apoc.periodic.iterate(
'UNWIND split("ABCDEFGHIJKLMNOPQRSTUVWXYZ","") AS letter RETURN letter',
'CALL apoc.load.json($prefix+letter+$suffix)
YIELD value
UNWIND value.data.results as results WITH results, results.comics.available AS comics
WHERE comics > 0
MERGE (char:Character {id: results.id})
ON CREATE SET char.name = results.name,
char.description = results.description,
char.thumbnail = results.thumbnail.path + "." + results.thumbnail.extension,
char.resourceURI = results.resourceURI',
{batchSize:10000, iterateList:true, parallel:true, params:{suffix: "&orderBy=name&limit=100&ts=20190125134059&apikey=33ee2485d7f3b3639ed66a4727ae1f6e&hash=7fac0f76a8346d00de626fa4271cfb8e", prefix: "http://gateway.marvel.com/v1/public/characters?nameStartsWith="}});


CALL apoc.periodic.iterate(
'MATCH (c:Character) WHERE c.resourceURI IS NOT NULL AND NOT exists((c)<-[:INCLUDES]-()) RETURN c LIMIT 100',
'CALL apoc.util.sleep(1000)
CALL apoc.load.json(c.resourceURI+"/comics?format=comic&formatType=comic&limit=100"+$suffix)
YIELD value
WITH c, value.data.results as results
     WHERE results IS NOT NULL
UNWIND results as result
MERGE (comic:ComicIssue {id: result.id})
     ON CREATE SET comic.name = result.title,
     comic.issueNumber = result.issueNumber,
     comic.pageCount = result.pageCount,
     comic.resourceURI = result.resourceURI,
     comic.thumbnail = result.thumbnail.path +
"." + result.thumbnail.extension
WITH c, comic, result
MERGE (comic)-[r:INCLUDES]->(c)
WITH c, comic, result WHERE result.series IS NOT NULL
UNWIND result.series as comicSeries
MERGE (series:Series {id: toInt(split(comicSeries.resourceURI,"/")[-1])})
ON CREATE SET series.name = comicSeries.name,
series.resourceURI = comicSeries.resourceURI
WITH c, comic, series, result
MERGE (comic)-[r2:BELONGS_TO]->(series)
WITH c, comic, result, result.creators.items as items
     WHERE items IS NOT NULL
UNWIND items as item
MERGE (creator:Creator {id: toInt(split(item.resourceURI,"/")[-1])})
ON CREATE SET creator.name = item.name,
creator.resourceURI = item.resourceURI
WITH c, comic, result, creator
MERGE (comic)-[r3:CREATED_BY]->(creator)
WITH c, comic, result, result.stories.items as items
     WHERE items IS NOT NULL
UNWIND items as item
MERGE (story:Story {id: toInt(split(item.resourceURI,"/")[-1])})
ON CREATE SET story.name = item.name,
story.resourceURI = item.resourceURI,
story.type = item.type
WITH c, comic, result, story
MERGE (comic)-[r4:MADE_OF]->(story)
WITH c, comic, result, result.events.items AS items
     WHERE items IS NOT NULL
UNWIND items as item
MERGE (event:Event {id: toInt(split(item.resourceURI,"/")[-1])})
ON CREATE SET event.name = item.name,
event.resourceURI = item.resourceURI
MERGE (comic)-[r5:PART_OF]->(event)',
{batchSize: 20, iterateList:false, retries:2, params:{suffix:"&ts=20190125134059&apikey=33ee2485d7f3b3639ed66a4727ae1f6e&hash=7fac0f76a8346d00de626fa4271cfb8e"}});


CALL apoc.load.json(c.resourceURI+"/comics?format=comic&formatType=comic&limit=100"+$suffix)
YIELD value
WITH c, value.data.results as results
     WHERE results IS NOT NULL
UNWIND results as result
MERGE (comic:ComicIssue {id: result.id})
     ON CREATE SET comic.name = result.title,
     comic.issueNumber = result.issueNumber,
     comic.pageCount = result.pageCount,
     comic.resourceURI = result.resourceURI,
     comic.thumbnail = result.thumbnail.path +
     "." + result.thumbnail.extension
WITH c, comic, result
MERGE (comic)-[r:INCLUDES]->(c)
WITH c, comic, result WHERE result.series IS NOT NULL
UNWIND result.series as comicSeries
MERGE (series:Series {id: toInt(split(comicSeries.resourceURI,"/")[-1])})
     ON CREATE SET series.name = comicSeries.name,
     series.resourceURI = comicSeries.resourceURI
WITH c, comic, series, result
MERGE (comic)-[r2:BELONGS_TO]->(series)
WITH c, comic, result, result.creators.items as items
     WHERE items IS NOT NULL
UNWIND items as item
MERGE (creator:Creator {id: toInt(split(item.resourceURI,"/")[-1])})
     ON CREATE SET creator.name = item.name,
     creator.resourceURI = item.resourceURI
WITH c, comic, result, creator
MERGE (comic)-[r3:CREATED_BY]->(creator)
WITH c, comic, result, result.stories.items as items
     WHERE items IS NOT NULL
UNWIND items as item
MERGE (story:Story {id: toInt(split(item.resourceURI,"/")[-1])})
     ON CREATE SET story.name = item.name,
     story.resourceURI = item.resourceURI,
     story.type = item.type
WITH c, comic, result, story
MERGE (comic)-[r4:MADE_OF]->(story)
WITH c, comic, result, result.events.items AS items
     WHERE items IS NOT NULL
UNWIND items as item
MERGE (event:Event {id: toInt(split(item.resourceURI,"/")[-1])})
     ON CREATE SET event.name = item.name,
     event.resourceURI = item.resourceURI
MERGE (comic)-[r5:PART_OF]->(event)


CALL apoc.load.json($prefix+letter+$suffix)
YIELD value
UNWIND value.data.results as results WITH results, results.comics.available AS comics
     WHERE comics > 0
MERGE (char:Character {id: results.id})
     ON CREATE SET char.name = results.name,
     char.description = results.description,
     char.thumbnail = results.thumbnail.path + "." + results.thumbnail.extension,
     char.resourceURI = results.resourceURI