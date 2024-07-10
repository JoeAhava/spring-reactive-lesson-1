package com.yigbu.reactive_lesson_1;

import com.yigbu.reactive_lesson_1.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonRepo {

    Mono<Person> getById(Integer id);
    Flux<Person> findAll();
}
