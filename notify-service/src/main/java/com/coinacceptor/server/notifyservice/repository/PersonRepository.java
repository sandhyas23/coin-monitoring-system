package com.coinacceptor.server.notifyservice.repository;

import com.coinacceptor.server.notifyservice.model.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, String> {
}
