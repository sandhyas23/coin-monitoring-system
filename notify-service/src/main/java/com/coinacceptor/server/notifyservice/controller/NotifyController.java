package com.coinacceptor.server.notifyservice.controller;

import com.coinacceptor.server.notifyservice.model.CoinAcceptor;
import com.coinacceptor.server.notifyservice.model.Person;
import com.coinacceptor.server.notifyservice.model.Response;
import com.coinacceptor.server.notifyservice.repository.CoinRepository;
import com.coinacceptor.server.notifyservice.repository.PersonRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotifyController {

    private static final Logger logger = LoggerFactory.getLogger(NotifyController.class);
    public static final String ACCOUNT_SID =
            "xxx";
    public static final String AUTH_TOKEN =
            "xxx";

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
            return new Response(200, "");
        }

        return new Response(400, "Coin acceptor doesn't exist");
        //return machineId;
    }

    @RequestMapping(path = "/event", method = RequestMethod.GET)
    public Response handleCoinEvent(String coinAcceptorId, String coinType ) {
        int totalCount = 0;

        logger.info("Handled request for /event with request parameters id={}, coinType={}", coinAcceptorId, coinType);
        if (coinRepository.existsById(coinAcceptorId)) {
            CoinAcceptor coindetails = coinRepository.findById(coinAcceptorId).get();
            System.out.println(coindetails.getCoinAcceptorId());
            if (coinType.equals("quarter")) {
                System.out.println(coinType);
                coindetails.setQuarterCount(coindetails.getQuarterCount() + 1);
                System.out.println(coindetails.getQuarterCount());
            } else if (coinType.equals("five-cents")) {
                coindetails.setFivecentsCount(coindetails.getFivecentsCount() + 1);
            }
            else {
                logger.info("Coin type does not exist");
            }
            totalCount = coindetails.getFivecentsCount() + coindetails.getQuarterCount();
            coindetails.setTotalCount(totalCount);

            //Check for threshold
            if (totalCount > coindetails.getThreshold()) {
                Person person = personRepository.findById(coindetails.getCoinAcceptorId()).get();

                logger.info("collect money tray from machine={}, phnumbersent={} ",coindetails.getCoinAcceptorId(), person.getPersonPhoneNumber());
                String notosend ="+1"+person.getPersonPhoneNumber();
                String bodyMessage = "please collect money tray from machine" +coindetails.getCoinAcceptorId();
                System.out.println(notosend);
                System.out.println(bodyMessage);
                Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

                Message message = Message
                        .creator(new PhoneNumber(notosend), // to
                                new PhoneNumber("+16292069754"), // from
                                bodyMessage)
                        .create();

                System.out.println(message.getSid());

            }

            coinRepository.save(coindetails);

            return new Response(200, "");
        }
            return new Response(400, "Coin accpetor doesnt exist");
            //return machineId;
        }

}
