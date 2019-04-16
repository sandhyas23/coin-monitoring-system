package com.coinacceptor.server.notifyservice.controller;

import com.coinacceptor.server.notifyservice.model.Person;
import com.coinacceptor.server.notifyservice.model.Response;
import com.coinacceptor.server.notifyservice.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotifyController {

    @Autowired
    PersonRepository personRepository;

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public Response handleRequest(Integer coinAcceptorId, String personName, String phoneNumber ){

        Person person = new Person();
        person.setPersonName(personName);
        person.setPersonPhoneNumber(phoneNumber);

        personRepository.save(person);

        return new Response(200, "");
        //return machineId;
    }
}
