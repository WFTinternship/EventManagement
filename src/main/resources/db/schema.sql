CREATE TABLE event (
  id              INTEGER      NOT NULL,
  title           VARCHAR(255) NOT NULL,
  short_desc      VARCHAR(300),
  full_desc       VARCHAR(1500),
  location        VARCHAR(200),
  lat             DOUBLE,
  lng             DOUBLE,
  file_path       VARCHAR(255),
  image_path      VARCHAR(255),
  category_id     INTEGER      NOT NULL,
  public_accessed INTEGER      NOT NULL,
  guests_allowed  INTEGER      NOT NULL,
  start           TIMESTAMP    NOT NULL,
  end             TIMESTAMP    NOT NULL,
  creation_date   TIMESTAMP    NOT NULL,
  last_modified   TIMESTAMP
);

CREATE TABLE event_category (
  id            INTEGER      NOT NULL,
  title         VARCHAR(100) NOT NULL,
  description   VARCHAR(150),
  creation_date TIMESTAMP
);

CREATE TABLE event_invitation (
  id              INTEGER     NOT NULL,
  event_id        INTEGER     NOT NULL,
  user_id         INTEGER     NOT NULL,
  user_role       VARCHAR(20) NOT NULL,
  user_response   INTEGER     NOT NULL,
  attendees_count INTEGER,
  participated    INTEGER,
  last_modified   TIMESTAMP,
  creation_date   TIMESTAMP
);

CREATE TABLE event_media (
  id            INTEGER      NOT NULL,
  event_id      INTEGER      NOT NULL,
  path          VARCHAR(300) NOT NULL,
  media_type_id INTEGER      NOT NULL,
  description   VARCHAR(300),
  uploader_id   INTEGER      NOT NULL,
  upload_date   TIMESTAMP    NOT NULL
);

CREATE TABLE event_recurrence (
  id                   INTEGER NOT NULL,
  event_id             INTEGER NOT NULL,
  recurrence_type_id   INTEGER NOT NULL,
  repeat_interval      INTEGER NOT NULL,
  recurrence_option_id INTEGER,
  repeat_end           TIMESTAMP
);

CREATE TABLE media_type (
  id    INTEGER     NOT NULL,
  title VARCHAR(45) NOT NULL
);

CREATE TABLE recurrence_option (
  id                 INTEGER      NOT NULL,
  recurrence_type_id INTEGER      NOT NULL,
  title              VARCHAR(200) NOT NULL,
  abbreviation       VARCHAR(20)
);
CREATE TABLE recurrence_type (
  id            INTEGER      NOT NULL,
  title         VARCHAR(200) NOT NULL,
  interval_unit VARCHAR(45)  NOT NULL
);
CREATE TABLE user (
  id                INTEGER      NOT NULL,
  first_name        VARCHAR(300) NOT NULL,
  last_name         VARCHAR(300) NOT NULL,
  password          VARCHAR(150) NOT NULL,
  email             VARCHAR(100) NOT NULL,
  phone_number      VARCHAR(30),
  avatar_path       VARCHAR(300),
  verified          INTEGER,
  registration_date TIMESTAMP
);
CREATE TABLE user_response (
  id    INTEGER     NOT NULL,
  title VARCHAR(45) NOT NULL
);
