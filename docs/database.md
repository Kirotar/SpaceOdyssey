CREATE TABLE pricelist (
id UUID PRIMARY KEY,
valid_until  VARCHAR(100) NOT NULL
);
CREATE TABLE legs (
id UUID PRIMARY KEY,
pricelist_id UUID NOT NULL,
FOREIGN KEY (pricelist_id) REFERENCES pricelist(id) ON DELETE CASCADE
);
CREATE TABLE routes(
id UUID PRIMARY KEY,
from_id UUID NOT NULL,
from_name VARCHAR(100) NOT NULL,
to_id UUID NOT NULL,
to_name VARCHAR(100) NOT NULL,
distance BIGINT NOT NULL,
leg_id UUID NOT NULL,
FOREIGN KEY (leg_id) REFERENCES legs(id) ON DELETE CASCADE
);
CREATE TABLE providers (
id UUID PRIMARY KEY,
leg_id UUID NOT NULL,
company_id UUID NOT NULL,
company_name VARCHAR(100) NOT NULL,
price DECIMAL(12,2) NOT NULL,
flight_start VARCHAR(100) NOT NULL,
flight_end VARCHAR(100) NOT NULL,
FOREIGN KEY (leg_id) REFERENCES legs(id) ON DELETE CASCADE
);
CREATE TABLE reservations (
id SERIAL PRIMARY KEY,
first_name VARCHAR(100) NOT NULL,
last_name VARCHAR(100) NOT NULL,
provider_id UUID NOT NULL,
route_id UUID NOT NULL,
FOREIGN KEY (provider_id) REFERENCES providers(id) ON DELETE CASCADE,
FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE
);
