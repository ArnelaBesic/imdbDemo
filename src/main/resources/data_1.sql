insert into Actor (givenName, lastName, birthDate) values('Elon', 'Musk', '1971-06-28');
insert into Actor (givenName, lastName, birthDate) values('Werner', 'Herzog', '1942-09-05');
insert into Actor (givenName, lastName) values('Leonard', 'Kleinrock');

insert into Movie values('tt5275828', 'Lo and Behold: Reveries of the Connected World', 'Werner Herzogs exploration of the Internet and the connected world.', 2016, 108);

-- For mock purpose only, otherwise we should first check if any of IDs exists
insert into movieActor values('tt5275828', (select actorID from Actor where givenName='Elon' and lastName='Musk'));
insert into movieActor values('tt5275828', (select actorID from Actor where givenName='Werner' and lastName='Herzog'));
insert into movieActor values('tt5275828', (select actorID from Actor where givenName='Leonard' and lastName='Kleinrock'));

insert into Image(name, content, uploadDate) values ('musk.jpg', FILE_READ( 'src/main/resources/images/musk.jpg'), CURRENT_TIMESTAMP());
insert into Image(name, content, uploadDate) values ('lo_and_behold_movie.jpg', FILE_READ( 'src/main/resources/images/lo_and_behold_movie.jpg'), CURRENT_TIMESTAMP());
insert into Image(name, content, uploadDate) values ('werner_herzog.jpg', FILE_READ( 'src/main/resources/images/werner_herzog.jpg'), CURRENT_TIMESTAMP());
insert into Image(name, content, uploadDate) values ('leonard_kleinrock.jpg', FILE_READ( 'src/main/resources/images/leonard_kleinrock.jpg'), CURRENT_TIMESTAMP());

insert into movieImage values('tt5275828', (select imageID from Image where name='lo_and_behold_movie.jpg'));
insert into actorImage values((select actorID from Actor where givenName='Elon' and lastName='Musk'), (select imageID from Image where name='musk.jpg'));
insert into actorImage values((select actorID from Actor where givenName='Werner' and lastName='Herzog'), (select imageID from Image where name='werner_herzog.jpg'));
insert into actorImage values((select actorID from Actor where givenName='Leonard' and lastName='Kleinrock'), (select imageID from Image where name='leonard_kleinrock.jpg'));
