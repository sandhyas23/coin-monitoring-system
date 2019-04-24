package com.coinacceptor.server.notifyservice.controller;

import com.coinacceptor.server.notifyservice.model.CoinAcceptor;
import com.coinacceptor.server.notifyservice.model.Person;
import com.coinacceptor.server.notifyservice.model.Response;
import com.coinacceptor.server.notifyservice.repository.CoinRepository;
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
    @Autowired
    CoinRepository coinRepository;



    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public Response handleRequest(String coinAcceptorId, String personName, String phoneNumber,String personId ){


        //CoinAcceptor coinAcceptor = new CoinAcceptor();
        if (coinRepository.existsById(coinAcceptorId)){
            //System.out.println(c.getCoinAcceptorId());
            if(personRepository.existsById(coinAcceptorId)){
                Person person = personRepository.findById(coinAcceptorId).get();
                person.setPersonName(personName);
                person.setPersonPhoneNumber(phoneNumber);
                person.setPersonId(personId);
                personRepository.save(person);
            }
            else {
                Person person = new Person();
                person.setCoinAcceptorID(coinAcceptorId);
                person.setPersonPhoneNumber(phoneNumber);
                person.setPersonName(personName);
                person.setPersonId(personId);
                personRepository.save(person);
            }

        }

        return new Response(200, "");
        //return machineId;
    }

    @RequestMapping(path = "/event", method = RequestMethod.GET)
    public Response handleCoinEvent(String coinAcceptorId, String coinType ){

        logger.info("Handled request for /event with request parameters id={}, coinType={}", coinAcceptorId, coinType);
        CoinAcceptor coindetails = coinRepository.findById(coinAcceptorId).get();
        System.out.println(coindetails.getCoinAcceptorId());
        if(coinType.equals("quarter")){
            System.out.println(coinType);
            coindetails.setQuarterCount(coindetails.getQuarterCount()+1);
            System.out.println(coindetails.getQuarterCount());
        }
        else if(coinType.equals("five-cents")){
            coindetails.setFivecentsCount(coindetails.getFivecentsCount()+1);
        }
        coindetails.setTotalCount(coindetails.getFivecentsCount()+coindetails.getQuarterCount());
        coinRepository.save(coindetails);

        //Threshold check
        CoinAcceptor thresholdCheck = coinRepository.findById(coinAcceptorId).get();
        if(thresholdCheck.getTotalCount() > 15 ){
            logger.info("Send text message to user");
        }

        return new Response(200, "");
        //return machineId;
    }
}
