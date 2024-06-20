CREATE TABLE site (
    id SERIAL PRIMARY KEY,
    login VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    site_name VARCHAR(255) NOT NULL,
    UNIQUE(login)
);

CREATE TABLE urls (
    id SERIAL PRIMARY KEY,
    original_url TEXT NOT NULL,
    short_code VARCHAR(10) NOT NULL,
    site_id INT,
    FOREIGN KEY (site_id) REFERENCES site(id),
    visit_count BIGINT DEFAULT 0
);