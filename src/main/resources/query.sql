--1
SELECT * FROM (
SELECT id, t2.person_id, content FROM tweet AS t2 JOIN (
SELECT DISTINCT person_id 
FROM followers 
WHERE person_id=1 OR follower_person_id=1) AS t1
ON t1.person_id = t2.person_id )
WHERE content LIKE '%alique%'

--2
SELECT * 
FROM followers 
WHERE person_id = :person_id OR follower_person_id = :person_id

--3
INSERT INTO followers (person_id, follower_person_id)
VALUES (:person_id, :follower_person_id)

--4
DELETE FROM followers 
WHERE person_id = :person_id AND follower_person_id = :follower_person_id

--5
SELECT * FROM followers

--6
SELECT p.id, p.person_id, p.follower_person_id, p.cnt
FROM (
SELECT t1.id, t1.person_id, t1.follower_person_id, t2.cnt
FROM followers AS t1
JOIN (
   SELECT person_id, COUNT(*) AS cnt
   FROM followers
   GROUP BY person_id 
) AS t2 ON t1.follower_person_id = t2.person_id) p INNER JOIN (
SELECT person_id, MAX(cnt) AS maxcnt
FROM (
SELECT t1.person_id, t1.follower_person_id, t2.cnt
FROM followers AS t1
JOIN (
   SELECT person_id, COUNT(*) AS cnt
   FROM followers
   GROUP BY person_id 
) AS t2 ON t1.follower_person_id = t2.person_id)
GROUP BY person_id) AS q
ON p.person_id = q.person_id AND p.cnt = q.maxcnt