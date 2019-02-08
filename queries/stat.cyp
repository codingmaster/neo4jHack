MATCH (c:Character) Return left(c.name, 1) as l, count(left(c.name, 1)) as count, collect(c.name) order by count desc;


