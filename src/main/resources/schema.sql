CREATE TABLE IF NOT EXISTS users (
        id INTEGER AUTO_INCREMENT PRIMARY KEY,
        email VARCHAR NOT NULL,
        login VARCHAR NOT NULL,
        name VARCHAR,
        birthday DATE
);

CREATE TABLE IF NOT EXISTS friendship (
        friend1_id INTEGER REFERENCES users (id),
        friend2_id INTEGER REFERENCES users (id),
        PRIMARY KEY (friend1_id, friend2_id)
);

CREATE TABLE IF NOT EXISTS mpa (
        id INTEGER PRIMARY KEY,
        name VARCHAR,
        description VARCHAR
);

CREATE TABLE IF NOT EXISTS film (
        id INTEGER AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR NOT NULL,
        description VARCHAR(200) NOT NULL,
        release_date DATE,
        duration INTEGER,
        rate INTEGER,
        mpa_id INTEGER REFERENCES mpa (id)
);

CREATE TABLE IF NOT EXISTS likes (
        id INTEGER AUTO_INCREMENT PRIMARY KEY,
        user_id INTEGER REFERENCES users (id),
        film_id INTEGER REFERENCES film (id)
);

CREATE TABLE IF NOT EXISTS genre (
        id INTEGER PRIMARY KEY,
        name VARCHAR
);

CREATE TABLE IF NOT EXISTS film_genre (
        film_id INTEGER REFERENCES film(id),
        genre_id INTEGER REFERENCES genre (id),
        PRIMARY KEY (film_id, genre_id)
);
