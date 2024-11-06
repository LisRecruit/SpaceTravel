CREATE TABLE Clients (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL CHECK (LENGTH(name) BETWEEN 3 AND 200)
);

CREATE TABLE Planets (
    id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(500) NOT NULL CHECK (LENGTH(name) BETWEEN 1 AND 500),
    CONSTRAINT chk_planet_id CHECK (id ~ '^[A-Z0-9]+$')
);

CREATE TABLE Tickets (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    client_id BIGINT NOT NULL,
    from_planet_id VARCHAR(10) NOT NULL,
    to_planet_id VARCHAR(10) NOT NULL,
    FOREIGN KEY (client_id) REFERENCES Clients(id) ON DELETE CASCADE,
    FOREIGN KEY (from_planet_id) REFERENCES Planets(id),
    FOREIGN KEY (to_planet_id) REFERENCES Planets(id)
);