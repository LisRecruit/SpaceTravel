INSERT INTO Clients (name) VALUES
('Alice Johnson'),
('Bob Smith'),
('Carol White'),
('David Brown'),
('Emma Davis'),
('Frank Miller'),
('Grace Wilson'),
('Henry Moore'),
('Isabel Taylor'),
('Jack Anderson');

INSERT INTO Planets (id, name) VALUES
('EARTH', 'Earth'),
('MARS', 'Mars'),
('VENUS', 'Venus'),
('JUPITER', 'Jupiter'),
('SATURN', 'Saturn');

INSERT INTO Tickets (created_at, client_id, from_planet_id, to_planet_id) VALUES
(CURRENT_TIMESTAMP, 1, 'EARTH', 'MARS'),
(CURRENT_TIMESTAMP, 2, 'MARS', 'VENUS'),
(CURRENT_TIMESTAMP, 3, 'VENUS', 'EARTH'),
(CURRENT_TIMESTAMP, 4, 'JUPITER', 'SATURN'),
(CURRENT_TIMESTAMP, 5, 'SATURN', 'JUPITER'),
(CURRENT_TIMESTAMP, 6, 'EARTH', 'JUPITER'),
(CURRENT_TIMESTAMP, 7, 'MARS', 'EARTH'),
(CURRENT_TIMESTAMP, 8, 'VENUS', 'MARS'),
(CURRENT_TIMESTAMP, 9, 'SATURN', 'EARTH'),
(CURRENT_TIMESTAMP, 10, 'JUPITER', 'VENUS');