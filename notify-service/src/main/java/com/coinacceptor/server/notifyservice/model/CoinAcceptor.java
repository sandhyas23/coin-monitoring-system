package com.coinacceptor.server.notifyservice.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity(name = "coinacceptor")
@Data
public class CoinAcceptor {
    @Id
    private String coinAcceptorId;
    private int totalCount;
    private int quarterCount;
    private int fivecentsCount;
    private int threshold;
    private Date timeStamp;

}
