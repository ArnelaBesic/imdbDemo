CREATE TABLE Actor
(
    actorId   IDENTITY,
    givenName VARCHAR(50) NOT NULL,
    lastName  VARCHAR(50) NOT NULL,
    birthDate DATE,
    CONSTRAINT PK_ACTOR PRIMARY KEY (actorId)
);

CREATE TABLE Image
(
    imageId    IDENTITY,
    name       VARCHAR(50) NOT NULL,
    content    IMAGE       NOT NULL,
    uploadDate TIMESTAMP   NOT NULL,
    CONSTRAINT PK_IMAGE PRIMARY KEY (imageId)
);

CREATE TABLE Movie
(
    imdbId      VARCHAR(255) NOT NULL,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    releaseYear YEAR         NOT NULL,
    length      SMALLINT,
    CONSTRAINT PK_MOVIE PRIMARY KEY (imdbId)
);

CREATE TABLE MovieActor
(
    imdbId  VARCHAR(255) NOT NULL,
    actorId BIGINT       NOT NULL,
    constraint FK_MOVIEACTOR_MOVIE foreign key (imdbId) references Movie,
    constraint FK_MOVIEACTOR_ACTOR foreign key (actorId) references Actor
);

CREATE TABLE MovieImage
(
    imdbId  VARCHAR(255) NOT NULL,
    imageId BIGINT       NOT NULL,
    constraint FK_MOVIEIMAGE_MOVIE foreign key (imdbId) references Movie,
    constraint FK_MOVIEIMAGE_IMGE foreign key (imageId) references Image
);

CREATE TABLE ActorImage
(
    actorId VARCHAR(255) NOT NULL,
    imageId BIGINT       NOT NULL,
    constraint FK_ACTORIMAGE_MOVIE foreign key (actorId) references Actor,
    constraint FK_ACTORIMAGE_IMGE foreign key (imageId) references Image
);