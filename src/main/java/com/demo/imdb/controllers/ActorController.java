package com.demo.imdb.controllers;

import com.demo.imdb.json.ActorResponse;
import com.demo.imdb.model.Actor;
import com.demo.imdb.services.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/imdb/actors")
public class ActorController {
    @Autowired
    private ActorService actorService;
    //TODO: log important info
    private static final Logger logger = Logger.getLogger(ActorController.class.getName());


    //e.g. http://localhost:8080/imdb/actors/list/
    @GetMapping(path = "/list/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ActorResponse> getAllActors() {
        //TODO: what if empty?
        List<Actor> actors = actorService.getAllActors();
        return actors.stream().map(ActorResponse::new).collect(Collectors.toList());
    }

    //e.g. http://localhost:8080/imdb/actors/paginatedList/
    @GetMapping(path = "/paginatedList/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ActorResponse> getAllActorsWithPagination() {
        List<Actor> actors = actorService.getAllActors();
        return actors.stream().map(ActorResponse::new).collect(Collectors.toList());
    }

    //e.g. http://localhost:8080/imdb/actors/actor/1
    @GetMapping(path = "/actor/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ActorResponse findActorById(@PathVariable("id") Long id) {
        Optional<Actor> actor = actorService.findActorById(id);
        if (actor.isPresent()) {
            return new ActorResponse(actor.get());
        }
        return null;
    }

    //e.g. http://localhost:8080/imdb/actors/
    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addActor(@RequestBody ActorResponse actor) {
        //TODO: request body entity? , validation, response
//Actor savedActor = actorService.createOrUpdateActor(actor);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand("1")
                .toUri();

        return ResponseEntity.created(location).build();
    }

    //e.g. http://localhost:8080/imdb/actors/
    @PutMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateActor(@RequestBody Actor actor) {
        //TODO: request body entity? , validation, response
        Actor savedActor = actorService.createOrUpdateActor(actor);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedActor.getActorID())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    //e.g. http://localhost:8080/imdb/actors/actor/1
    @DeleteMapping(path = "/actor/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteActorByID(@PathVariable("id") Long id) {
//TODO: handle exception - movie, validation, response
        actorService.deleteActorByID(id);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    //e.g. http://localhost:8080/imdb/actors/
    @DeleteMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteActor(@RequestBody Actor actor) {
//TODO: handle exception - movie, validation, response, request body entity
        actorService.deleteActor(actor);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(actor.getActorID())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}