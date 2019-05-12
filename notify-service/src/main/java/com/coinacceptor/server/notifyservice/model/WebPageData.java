package com.coinacceptor.server.notifyservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class WebPageData {
    private String coinAcceptorId;
    private int totalCount;
    private int quarterCount;
    private int fivecentsCount;
    private int threshold;
    private Date timeStamp;
    private String totalAmount;
}
