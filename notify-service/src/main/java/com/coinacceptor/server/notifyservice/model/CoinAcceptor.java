package com.coinacceptor.server.notifyservice.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "coinacceptor")
@Data
public class CoinAcceptor {
    @Id
    @Column(
            name = "coinacceptorid"
    )
    private String coinAcceptorId;

}
