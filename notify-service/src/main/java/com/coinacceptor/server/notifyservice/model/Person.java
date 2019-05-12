package com.coinacceptor.server.notifyservice.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "person")
@Data
public class Person {

    @Id
    private String coinAcceptorID;
    private String personId;
    private String personName;
    private String personPhoneNumber;


}
