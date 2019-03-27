
--
-- Structure for table shorturl
--

DROP TABLE IF EXISTS shorturl;
CREATE TABLE shorturl (		
id_shorturl int default '0' NOT NULL,
shorturl_url varchar(256) default '' NOT NULL,
abbreviation varchar(50) default '' NOT NULL,
creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
hits int default '0' NOT NULL,
    PRIMARY KEY (id_shorturl)
);
