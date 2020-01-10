-- private List<Image> images;
CREATE TABLE Actor
(
    actorID   IDENTITY,
    givenName VARCHAR(50) NOT NULL,
    lastName  VARCHAR(50) NOT NULL,
    birthDate DATE,
    CONSTRAINT PK_ACTOR PRIMARY KEY (actorID)
);

CREATE TABLE Image
(
    imageID    IDENTITY,
    name       VARCHAR(50) NOT NULL,
    content    IMAGE       NOT NULL,
    uploadDate TIMESTAMP   NOT NULL,
    CONSTRAINT PK_IMAGE PRIMARY KEY (imageID)
);

-- private List<Image> images;
-- private List<Actor> cast;
CREATE TABLE Movie
(
    imdbID      VARCHAR(255) NOT NULL,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    releaseYear YEAR         NOT NULL,
    length      SMALLINT,
    CONSTRAINT PK_MOVIE PRIMARY KEY (imdbID)
);

CREATE TABLE MovieActor
(
    imdbID  VARCHAR(255) NOT NULL,
    actorID BIGINT       NOT NULL,
    constraint FK_MOVIEACTOR_MOVIE foreign key (imdbID) references Movie,
    constraint FK_MOVIEACTOR_ACTOR foreign key (actorID) references Actor
);

CREATE TABLE MovieImage
(
    imdbID  VARCHAR(255) NOT NULL,
    imageID BIGINT       NOT NULL,
    constraint FK_MOVIEIMAGE_MOVIE foreign key (imdbID) references Movie,
    constraint FK_MOVIEIMAGE_IMGE foreign key (imageID) references Image
);
/*https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/  - zašto vmesna? zato što imam razlicite tabele vezane na images,
  images pa je lako više od 1 na svakom enetity . Imena tabela možda nisu ok, recimo ActorImage_join
 */
CREATE TABLE ActorImage
(
    actorID VARCHAR(255) NOT NULL,
    imageID BIGINT       NOT NULL,
    constraint FK_ACTORIMAGE_MOVIE foreign key (actorID) references Actor,
    constraint FK_ACTORIMAGE_IMGE foreign key (imageID) references Image
);

-- create table gu_Job_Contact
-- (
--     gu_job_id     numeric(19, 0) not null,
--     gu_contact_id numeric(19, 0) not null,
--     constraint FK_JOBCONTACT_JOB foreign key (gu_job_id) references gu_Job,
--     constraint FK_JOBCONTACT_CONTACT foreign key (gu_contact_id) references gu_Contact
-- );
--
-- @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
--     @Sort(type = SortType.NATURAL)
--     @JoinTable(name = "ContourPlotRP_SecondSP")
--     public List<EntityPath> getSecondSelectedPaths() {
--         return secondSelectedPaths;
-- }

-- CREATE TABLE gu_TractionDataStoreMin
-- (
--     gu_traction_id          NUMERIC(19, 0) IDENTITY NOT NULL,
--     gu_dataDatetime         SMALLDATETIME           NOT NULL,
--     gu_valueCreationDate    DATETIME2(2)            NOT NULL,
--     gu_dataSource_id        NUMERIC(19, 0)          NOT NULL,
--     gu_consumed             REAL,
--     gu_returned             REAL,
--     gu_latitude             FLOAT,
--     gu_longitude            FLOAT,
--     gu_speed                REAL,
--     gu_timeQuality          SMALLINT                NOT NULL,
--     gu_energyQuality        SMALLINT                NOT NULL,
--     gu_locationQuality      SMALLINT                NOT NULL,
--     gu_erexStatus           VARCHAR(255),
--     gu_erexExportTime       DATETIME2(2),
--     gu_dataDownloadDatetime DATETIME2(2),
--     gu_key                  NUMERIC(19, 0),
--     CONSTRAINT PK_TractionDataStoreMin PRIMARY KEY (gu_traction_id),
--     CONSTRAINT CHK_TDS_energyQuality CHECK (gu_energyQuality in (46, 56, 61, 127)),
--     CONSTRAINT CHK_TDS_locationQuality CHECK (gu_locationQuality in (46, 56, 61, 127)),
--     CONSTRAINT FK_TDS_datasource FOREIGN KEY (gu_dataSource_id) REFERENCES gu_DataSource
-- );
--
-- create table gu_DHSDevice
-- (
--     gu_id                     numeric(19, 0) identity primary key not null,
--     gu_lastModificationDate   datetime2,
--     gu_version                int,
--     gu_externalIdent          varchar(255),
--     gu_locomotive_id          numeric(19, 0),
--     gu_longitude              float(53),
--     gu_latitude               float(53),
--     gu_consumedEnergy         float(53),
--     gu_lastState              datetime2,
--     gu_returnedEnergy         float(53),
--     gu_softwareVersion        varchar(255),
--     gu_systemParameterVersion varchar(255)
-- );
-- create table gu_DHSFile
-- (
--     gu_id                   numeric(19, 0) identity primary key not null,
--     gu_lastModificationDate datetime2,
--     gu_version              int,
--     gu_content              varbinary(max),
--     gu_fileName             varchar(255),
--     gu_stateTime            datetime2,
--     gu_type                 varchar(255),
--     gu_dhsDevice_id         numeric(19, 0)
-- );
-- create table gu_DHSStatus
-- (
--     gu_id                   numeric(19, 0) identity primary key not null,
--     gu_lastModificationDate datetime2,
--     gu_version              int,
--     gu_latitude             float(53),
--     gu_longitude            float(53),
--     gu_stateTime            datetime2,
--     gu_