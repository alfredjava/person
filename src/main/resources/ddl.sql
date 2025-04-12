DROP TABLE IF EXISTS users;
CREATE TABLE users(
                      id uuid DEFAULT random_uuid() PRIMARY KEY,
                      name VARCHAR(200) not null,
                      email VARCHAR(60) NOT NULL,
                      password VARCHAR(60) NOT NULL,
                      active BOOLEAN,
                      created TIMESTAMP,
                      modified TIMESTAMP,
                      last_login TIMESTAMP,
                      token VARCHAR(200) NOT NULL
);

DROP TABLE IF EXISTS phones;
CREATE TABLE phones(
                      id uuid DEFAULT random_uuid() PRIMARY KEY,
                      number VARCHAR(16) not null,
                      city_code VARCHAR(4) NOT NULL,
                      country_code VARCHAR(6) NOT NULL,
                      user_id uuid NOT NULL REFERENCES users(id) ON DELETE CASCADE
);



