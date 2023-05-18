docker exec -it my-postgis psql -U postgres

DROP TABLE geo_entities;

#
# For more details check: https://stackoverflow.com/questions/8150721/which-data-type-for-latitude-and-longitude
#
DROP TYPE entity_type;
CREATE TYPE entity_type AS ENUM ('supermarket', 'gas-station', 'hospital');

CREATE TABLE geo_entities (
    id SERIAL PRIMARY KEY,
    name VARCHAR(64),
    type entity_type NOT NULL,
    location geography NOT NULL
);

CREATE INDEX geo_entities_location_idx ON geo_entities USING GIST(location);

BEGIN;
INSERT INTO geo_entities (name, type, location) VALUES ('Liddle-1', 'supermarket', 'SRID=4326;POINT(1 1)');
INSERT INTO geo_entities (name, type, location) VALUES ('Liddle-2', 'supermarket', 'SRID=4326;POINT(2 2)');
INSERT INTO geo_entities (name, type, location) VALUES ('Liddle-10', 'supermarket', 'SRID=4326;POINT(10 10)');
INSERT INTO geo_entities (name, type, location) VALUES ('RomPatrol-1', 'gas-station', 'SRID=4326;POINT(180 90)');
END;

SELECT * FROM geo_entities WHERE type = 'gas-station' ORDER BY location <-> 'SRID=4326;POINT(-120 90)' LIMIT 10;




