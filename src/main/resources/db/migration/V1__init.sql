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

CREATE TABLE ActorImage
(
    actorID VARCHAR(255) NOT NULL,
    imageID BIGINT       NOT NULL,
    constraint FK_ACTORIMAGE_MOVIE foreign key (actorID) references Actor,
    constraint FK_ACTORIMAGE_IMGE foreign key (imageID) references Image
);