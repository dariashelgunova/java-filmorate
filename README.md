# java-filmorate
# diagram
![filmorate_diagram.png](..%2F..%2FDownloads%2Ffilmorate_diagram.png)

### Пояснение связей и зависимостей:
">" - many-to-one;
 
"<" one-to-many;
 
"-" one-to-one;
 
"<>" many-to-many; 

### Table user as U 
{user_id int *[pk, ref: < FS.friend1_id]*

email varchar

login varchar

name varchar

birthday varchar}

### Table friendship as FS 
{friend1_id int *[pk]*

friend2_id int *[ref: > U.user_id]*}

### Table friend_request as R 
{request_id int *[pk, increment]*

initializer_id int *[ref: > U.user_id]*

supposed_friend_id int *[ref: > U.user_id]*}

### Table film as F 
{film_id int *[pk]*

name varchar

description varchar

release_date varchar

duration int

rating_id int}

### Table like as L 
{user_id int *[pk, ref: > U.user_id]*

film_id int *[ref: > F.film_id]*}

### Table genre as G 
{genre_id int *[pk]*

name varchar}

### Table rating as Rt 
{rating_id int *[pk, ref: < F.rating_id]*

name varchar}

### Table film_genre as FG 
{film_id int *[pk, ref: > F.film_id]*

genre_id int *[ref: > G.genre_id]*}

### Примеры запросов:
1.
SELECT FS.friend2_id

FROM friendship as FS

WHERE friend1_id = 1

GROUP BY FS.friend2_id;

2.
SELECT L.user_id

FROM like as L

WHERE film_id = 3

GROUP BY L.user_id

ORDER BY L.user_id DESC;
