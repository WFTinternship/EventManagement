CREATE TABLE event (
  id              INTEGER UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  title           VARCHAR(255)     NOT NULL,
  short_desc      VARCHAR(300),
  full_desc       VARCHAR(1500),
  location        VARCHAR(200),
  lat             DOUBLE,
  lng             DOUBLE,
  file_path       VARCHAR(255),
  image_path      VARCHAR(255),
  category_id     INTEGER          NOT NULL,
  public_accessed INTEGER          NOT NULL,
  guests_allowed  INTEGER          NOT NULL,
  start           TIMESTAMP        NOT NULL,
  end             TIMESTAMP        NOT NULL,
  creation_date   TIMESTAMP        NOT NULL,
  last_modified   TIMESTAMP
);


CREATE TABLE event_category (
  id            INTEGER UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  title         VARCHAR(100)     NOT NULL,
  description   VARCHAR(150),
  creation_date TIMESTAMP
);

CREATE TABLE event_invitation (
  id               INTEGER UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  event_id         INTEGER          NOT NULL,
  user_id          INTEGER          NOT NULL,
  user_role        VARCHAR(20)      NOT NULL,
  user_response_id INTEGER          NOT NULL,
  attendees_count  INTEGER,
  participated     INTEGER,
  last_modified    TIMESTAMP,
  creation_date    TIMESTAMP
);

CREATE TABLE event_media (
  id            INTEGER UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  event_id      INTEGER          NOT NULL,
  path          VARCHAR(300)     NOT NULL,
  media_type_id INTEGER          NOT NULL,
  description   VARCHAR(300),
  uploader_id   INTEGER          NOT NULL,
  upload_date   TIMESTAMP        NOT NULL
);

CREATE TABLE event_recurrence (
  id                   INTEGER UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  event_id             INTEGER          NOT NULL,
  recurrence_type_id   INTEGER          NOT NULL,
  repeat_interval      INTEGER          NOT NULL,
  recurrence_option_id INTEGER,
  repeat_end           TIMESTAMP
);

CREATE TABLE media_type (
  id    INTEGER UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(45)      NOT NULL
);

CREATE TABLE recurrence_option (
  id                 INTEGER UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  recurrence_type_id INTEGER          NOT NULL,
  title              VARCHAR(200)     NOT NULL,
  abbreviation       VARCHAR(20)
);

CREATE TABLE recurrence_type (
  id            INTEGER UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  title         VARCHAR(200)     NOT NULL,
  interval_unit VARCHAR(45)      NOT NULL
);

CREATE TABLE user (
  id                INTEGER UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  first_name        VARCHAR(300)     NOT NULL,
  last_name         VARCHAR(300)     NOT NULL,
  password          VARCHAR(150)     NOT NULL,
  email             VARCHAR(100)     NOT NULL,
  phone_number      VARCHAR(30),
  avatar_path       VARCHAR(300),
  verified          INTEGER,
  registration_date TIMESTAMP
);

CREATE TABLE user_response (
  id    INTEGER UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(45)      NOT NULL
);

ALTER TABLE event
  ADD FOREIGN KEY (category_id) REFERENCES event_category (id)
  ON DELETE SET NULL
  ON UPDATE NO ACTION;

ALTER TABLE event_invitation
  ADD FOREIGN KEY (event_id) REFERENCES event (id)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;
ALTER TABLE event_invitation
  ADD FOREIGN KEY (user_id) REFERENCES user (id)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;
ALTER TABLE event_invitation
  ADD FOREIGN KEY (user_response_id) REFERENCES user_response (id)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE event_media
  ADD FOREIGN KEY (event_id) REFERENCES event (id);
ALTER TABLE event_media
  ADD FOREIGN KEY (media_type_id) REFERENCES media_type (id)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;
ALTER TABLE event_media
  ADD FOREIGN KEY (uploader_id) REFERENCES user (id)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE event_recurrence
  ADD FOREIGN KEY (event_id) REFERENCES event (id)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;
ALTER TABLE event_recurrence
  ADD FOREIGN KEY (recurrence_option_id) REFERENCES recurrence_option (id)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;
ALTER TABLE event_recurrence
  ADD FOREIGN KEY (recurrence_type_id) REFERENCES recurrence_type (id)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE recurrence_option
  ADD FOREIGN KEY (recurrence_type_id) REFERENCES recurrence_type (id)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

