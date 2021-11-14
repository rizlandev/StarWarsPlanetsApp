DROP TABLE IF EXISTS tbl_planet_data;

CREATE TABLE tbl_planet_data(
     planet_ref_id INTEGER PRIMARY KEY,
     p_name TEXT,
     p_climate TEXT,
     p_orbital_period TEXT,
     p_gravity TEXT,
     p_img_path TEXT
);
