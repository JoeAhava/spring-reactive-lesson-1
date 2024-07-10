package com.yigbu.reactive_lesson_1;

import com.yigbu.reactive_lesson_1.domain.Person;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PersonRepoImpl implements PersonRepo {
    @Override
    public Mono<Person> getById(Integer id) {
        return findAll().filter(person -> person.getId() == id).next();
    }

    @Override
    public Flux<Person> findAll() {
        return Flux.just(new Person(1, "John Doe"), new Person(2, "Jane Doe"));
    }
}
