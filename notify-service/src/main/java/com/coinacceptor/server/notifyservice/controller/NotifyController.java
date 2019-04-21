package com.coinacceptor.server.notifyservice.controller;

import com.coinacceptor.server.notifyservice.model.Person;
import com.coinacceptor.server.notifyservice.model.Response;
import com.coinacceptor.server.notifyservice.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotifyController {

    private static final Logger logger = LoggerFactory.getLogger(NotifyController.class);

    @Autowired
    PersonRepository personRepository;

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public Response handleRequest(String coinAcceptorId, String personName, String phoneNumber ){

        Person person = new Person();
        person.setPersonName(personName);
        person.setPersonPhoneNumber(phoneNumber);
        person.setCoinAcceptorID(coinAcceptorId);

        personRepository.save(person);

        return new Response(200, "");
        //return machineId;
    }

    @RequestMapping(path = "/event", method = RequestMethod.GET)
    public Response handleCoinEvent(String coinAcceptorId, String coinType ){

        logger.info("Handled request for /event with request parameters id={}, coinType={}", coinAcceptorId, coinType);
        return new Response(200, "");
        //return machineId;
    }
}
