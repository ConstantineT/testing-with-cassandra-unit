CREATE TABLE apartments (
    city text,
    street text,
    house_number int,
    apartment_number int,
    owner text,
    features set<text>,
    PRIMARY KEY((city, street), house_number, apartment_number)
);

CREATE INDEX apartments_owner_idx ON apartments (owner);

INSERT INTO apartments (city, street, house_number, apartment_number, owner, features)
            values ('Salem', 'Pine Lane', 10, 100, 'John Smith', {'Balcony', 'Luxury bathroom'});
INSERT INTO apartments (city, street, house_number, apartment_number, owner, features)
            values ('Salem', 'Pine Lane', 10, 101, 'Mary Johnson', {'Close to public transportation', 'Garden'});
INSERT INTO apartments (city, street, house_number, apartment_number, owner, features)
            values ('Salem', 'Lake Avenue', 2, 20, 'John Smith', {'Attic','Balcony','Swimming pool'});