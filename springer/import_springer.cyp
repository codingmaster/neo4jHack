// 1. get book
CALL apoc.load.json('http://sprcom-product-source-qa.dev.cf.private.springer.com/api/v2/book/978-3-319-13416-1')
YIELD value
MERGE (book:Book {id: value.id})
     ON CREATE SET book.title = value.title,
     book.subTitle = value.subTitle

// 2. get books

WITH '978-3-540-64764-5,978-3-540-40220-6,978-0-387-95218-5,978-3-540-61619-1,978-3-540-60752-6,978-0-387-98349-3,978-3-540-67815-1,978-3-540-50625-6,978-3-540-44197-7,978-1-4020-8572-7,978-3-642-01984-5,978-3-642-01484-0,978-3-540-00984-9,978-3-540-43807-6,978-3-540-43625-6,978-3-540-43663-8,978-3-540-14944-6,978-3-540-00854-5,978-3-540-44105-2,978-3-540-00853-8,978-3-540-00233-8,978-3-540-43693-5,978-3-540-44133-5,978-3-211-83771-9,978-3-540-14949-1,978-3-540-00205-5,978-0-387-24568-3,978-3-540-20849-5,978-0-7923-3067-7,978-0-89838-028-6,978-90-207-0865-3,978-1-59059-034-8,978-1-55953-544-1,978-1-893115-73-6,978-3-7643-2167-3,978-3-540-00815-6,978-3-540-40073-8,978-3-540-52895-1,978-3-8274-3001-4,978-1-62703-136-3,978-3-540-11658-5,978-3-540-17093-8,978-3-540-13612-5,978-3-540-61443-2,978-3-8349-4032-2,978-3-8349-4270-8,978-94-007-4993-1,978-3-642-31521-3,978-3-8348-1606-1,978-3-663-00522-3,978-3-8349-9331-1,978-3-540-15279-8,978-3-540-57426-2,978-3-8349-8982-6,978-3-540-15295-8,978-3-319-01853-9,978-3-319-01854-6,978-90-247-1659-3,978-3-211-81147-4,978-3-211-83583-8,978-3-540-42901-2,978-3-540-52018-4,978-3-540-52171-6,978-0-8176-4970-8,978-3-658-03874-8,978-3-540-42342-3,978-3-540-65318-9,978-3-540-41892-4,978-3-540-65620-3,978-1-85233-224-2,978-3-540-41086-7,978-1-85233-328-7,978-3-540-41082-9,978-1-85233-237-2,978-3-540-42384-3,978-3-540-41667-8,978-3-540-00036-5,978-3-540-00276-5,978-3-540-43615-7,978-3-540-65744-6,978-3-663-00521-6,978-3-540-13719-1,978-3-540-41240-3,978-3-540-43542-6,978-3-211-88569-7,978-3-319-02086-0,978-3-319-02087-7,978-981-4560-28-3,978-981-4560-29-0,978-1-4614-9101-9,978-1-4614-9102-6,978-4-431-54546-0,978-4-431-54547-7,978-3-319-02114-0,978-94-007-7746-0,978-94-007-7748-4,978-3-540-88836-9,978-3-540-79312-0,978-0-387-89642-7' AS isbns
CALL apoc.load.json('http://sprcom-product-source-qa.dev.cf.private.springer.com/api/v2/books/' + isbns)
YIELD value
UNWIND value.success AS result WITH result
MERGE (book:Book {id: result.id})
     ON CREATE SET
     book.title = result.title,
     book.subTitle = result.subTitle
WITH book, result
UNWIND result.keywords AS keywords
MERGE (keyword:Keyword {
     value: keywords.value
})
MERGE (book)-[r:HAS_KEYWORD]->(keyword)
WITH book, result
UNWIND result.productMarketCodes AS pmcs
MERGE (pmc: PMC {
     id: pmcs.value,
     value: pmcs.term
})
MERGE (book)-[r:HAS_PMC]->(pmc)
WITH book, result
UNWIND result.originators AS originators
FOREACH (x IN CASE WHEN originators.firstName IS NULL OR originators.lastName IS NULL THEN [] ELSE [1] END |
     MERGE (originator: Originator{
       id: originators.firstName + ' ' + originators.lastName,
       firstName: originators.firstName,
       lastName: originators.lastName,
       type: originators.originatorType
     })
     MERGE (book)-[r:HAS_ORIGINATOR]->(originator)
)
WITH book, result
UNWIND result.renditions AS renditions
FOREACH (x IN CASE WHEN renditions.isbn IS NULL OR renditions.doi IS NULL OR renditions.medium IS NULL THEN []
  ELSE [1]
  END |

     MERGE (rendition:Rendition {
       isbn:     renditions.isbn,
       doi:      renditions.doi,
       medium:   renditions.medium,
       coverImg: 'https://images.springer.com/sgw/books/medium/' + renditions.isbn + '.jpg'
     })
     MERGE (book)-[r:HAS_RENDITION]->(rendition)
);

