DROP TABLE IF EXISTS users, categories, locations, events, compilations, requests, compilation_events CASCADE;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    name varchar(250) NOT NULL,
    email varchar(254) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    name varchar(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS locations (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    lat FLOAT NOT NULL,
    lon FLOAT NOT NULL,
    location_name VARCHAR(250),
    address VARCHAR(1000),
    status VARCHAR
);

CREATE TABLE IF NOT EXISTS events (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    annotation varchar(2000) NOT NULL,
    category_id BIGINT REFERENCES categories (id) ON DELETE NO ACTION NOT NULL,
    description varchar(7000) NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    location_id BIGINT NOT NULL REFERENCES locations (id) ON DELETE CASCADE,
    paid Boolean NOT NULL DEFAULT false,
    participant_limit BIGINT NOT NULL DEFAULT 0,
    request_moderation Boolean NOT NULL DEFAULT true,
    state varchar(255) NOT NULL DEFAULT 'PENDING',
    title varchar(120) NOT NULL,
    initiator_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    created TIMESTAMP WITHOUT TIME ZONE,
    published TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS compilations (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    pinned Boolean NOT NULL
);

CREATE TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    event_id BIGINT NOT NULL REFERENCES events (id) ON DELETE CASCADE,
    requester_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    status varchar(255) NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL
    );

CREATE TABLE IF NOT EXISTS compilation_events (
    compilation_id INT REFERENCES compilations (id) ON DELETE CASCADE NOT NULL,
    event_id INT REFERENCES events (id) ON DELETE CASCADE NOT NULL,
    PRIMARY KEY(compilation_id, event_id)
);

CREATE OR REPLACE FUNCTION distance(lat1 float, lon1 float, lat2 float, lon2 float)
    RETURNS float
AS
'
declare
    dist float = 0;
    rad_lat1 float;
    rad_lat2 float;
    theta float;
    rad_theta float;
BEGIN
    IF lat1 = lat2 AND lon1 = lon2
    THEN
        RETURN dist;
    ELSE
        -- переводим градусы широты в радианы
        rad_lat1 = pi() * lat1 / 180;
        -- переводим градусы долготы в радианы
        rad_lat2 = pi() * lat2 / 180;
        -- находим разность долгот
        theta = lon1 - lon2;
        -- переводим градусы в радианы
        rad_theta = pi() * theta / 180;
        -- находим длину ортодромии
        dist = sin(rad_lat1) * sin(rad_lat2) + cos(rad_lat1) * cos(rad_lat2) * cos(rad_theta);
        IF dist > 1
            THEN dist = 1;
        END IF;
        dist = acos(dist);
        -- переводим радианы в градусы
        dist = dist * 180 / pi();
        -- переводим градусы в километры
        dist = dist * 60 * 1.8524;
        RETURN dist;
    END IF;
END;
'
LANGUAGE PLPGSQL;