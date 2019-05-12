package com.coinacceptor.server.notifyservice.controller;

import com.coinacceptor.server.notifyservice.model.CoinAcceptor;
import com.coinacceptor.server.notifyservice.model.Person;
import com.coinacceptor.server.notifyservice.model.WebPageData;
import com.coinacceptor.server.notifyservice.repository.CoinRepository;
import com.coinacceptor.server.notifyservice.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
public class WebPageController {
    private static final Logger logger = LoggerFactory.getLogger(WebPageController.class);
    private static DecimalFormat df2 = new DecimalFormat("0.00");

    @Autowired
    PersonRepository personRepository;
    @Autowired
    CoinRepository coinRepository;

    @GetMapping("/")
    public String displayValues(Model model) {
        final Iterable<CoinAcceptor> allCoinIds = coinRepository.findAll();
        List<WebPageData> displayAll = new ArrayList<>();
        for(CoinAcceptor coinId: allCoinIds){
            int quarters = coinId.getQuarterCount();
            int fives = coinId.getFivecentsCount();
            double quarterAmount = quarters * 0.25;
            double fivesaAmount = fives *0.05;
            double totalAmount = quarterAmount + fivesaAmount;

            WebPageData data = new WebPageData(coinId.getCoinAcceptorId(),coinId.getTotalCount(),coinId.getQuarterCount(),coinId.getFivecentsCount(),
                    coinId.getThreshold(),coinId.getTimeStamp(),df2.format(totalAmount));
            displayAll.add(data);
        }
        model.addAttribute("all",displayAll);
        logger.info("fetched the results");
        return "display";

    }

    @GetMapping("/signup")
    public String showSignUpForm(Person personFromWeb,Model model) {
        model.addAttribute("personFromWeb",new Person());
        return "addperson";
    }

    @RequestMapping(value="/register", method= RequestMethod.POST)
    public String handleRequest( Person personFromWeb, BindingResult result,Model model){
        logger.info("result={}",result);
        if (result.hasErrors()) {
            return "addperson";
        }
        //CoinAcceptor coinAcceptor = new CoinAcceptor();
        String coinAcceptorId = personFromWeb.getCoinAcceptorID();
        if (coinRepository.existsById(coinAcceptorId)){
            //System.out.println(c.getCoinAcceptorId());
            if(personRepository.existsById(coinAcceptorId)){
                Person person = personRepository.findById(coinAcceptorId).get();
                person.setPersonName(personFromWeb.getPersonName());
                person.setPersonPhoneNumber(personFromWeb.getPersonPhoneNumber());
                person.setPersonId(personFromWeb.getPersonId());
                personRepository.save(person);
            }
            else {
                Person person = new Person();
                person.setCoinAcceptorID(coinAcceptorId);
                person.setPersonPhoneNumber(personFromWeb.getPersonPhoneNumber());
                person.setPersonName(personFromWeb.getPersonName());
                person.setPersonId(personFromWeb.getPersonId());
                personRepository.save(person);
            }

        }
        model.addAttribute("personFromWeb",new Person());
        return "addperson";

        //return machineId;
    }






}
