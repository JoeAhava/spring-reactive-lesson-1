package com.yigbu.reactive_lesson_1;

import com.yigbu.reactive_lesson_1.domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class PersonRepoImplTest {
    PersonRepoImpl personRepo;

    @BeforeEach
    void setUp() {
        personRepo = new PersonRepoImpl();
    }

    @Test
    void getByIdBlock() {
        Mono<Person> personMono = personRepo.getById(6);

        Person person = personMono.block();

        assert person != null;
        System.out.println(person.toString());

    }
    @Test
    void getByIdSubscribe(){
        Mono<Person> personMono = personRepo.getById(6);
        StepVerifier.create(personMono).expectNextCount(0).verifyComplete();
        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });
    }
    @Test
    void getByIdMapFunction(){
        Mono<Person> personMono = personRepo.getById(9);
        personMono.map(person -> {
            System.out.println(person.toString());
            return person.getName();
        }).subscribe(name -> System.out.println("Name from map: " + name));
    }

    @Test
    void findAllBlockFirst(){
        Flux<Person> personFlux = personRepo.findAll();

        Person person = personFlux.blockLast();

        System.out.println(person);
    }

    @Test
    void findAllSubscribe(){
        Flux<Person> personFlux = personRepo.findAll();

        StepVerifier.create(personFlux).expectNextCount(2).verifyComplete();

        personFlux.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void findAllToListMono(){
        Flux<Person> personFlux = personRepo.findAll();

        Mono<List<Person>> personsMono = personFlux.collectList();

        personsMono.subscribe(people -> System.out.println(people.toString()));
    }

    @Test
    void findById(){
        Flux<Person> personFlux = personRepo.findAll();
        final Integer id = 2;
        Mono<Person> personMono = personFlux.filter(person -> person.getId() == id).next();

        personMono.subscribe(person -> {
            assert person.getId() == id;
            System.out.println(person.toString());
        });
    }

    @Test
    void findByIdNotFound(){
        Flux<Person> personFlux = personRepo.findAll();
        final Integer id = 9;

        Mono<Person> personMono = personFlux.filter(person -> person.getId() == id).single();

        personMono
                .doOnError(throwable -> {
                    if(throwable instanceof NoSuchElementException) {
                        System.out.println("Person not Found");
                    }
                    else if(throwable instanceof IndexOutOfBoundsException) {
                        System.out.println("Seems id is greater than what we have");
                    }
                    else System.out.println("Could not find id");
                })
                .onErrorComplete()
                .subscribe(person -> {
            assert person.getId() == id;
            System.out.println(person.toString());
        });
    }

    @Test
    void findByIdSubscriber(){
        final Integer id = 2;
        Mono<Person> personMono = personRepo.getById(id);

        personMono.subscribe(person -> {
            assert person.getId() == id;
            System.out.println(person.toString());
        });
    }

    @Test
    void findByIdSubscriberNotFound(){
        final Integer id = 9;
        Mono<Person> personMono = personRepo.getById(id);

        personMono.subscribe(person -> {
            assert person.getId() == id;
            System.out.println(person.toString());
        });
    }
}