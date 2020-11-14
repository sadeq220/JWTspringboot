package com.sadeqstore.demo.paypalConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;

@Component
public class PayPalServiceProxy {

    @Autowired
    public PayPalServiceProxy(PayPalService service){this.service=service;}
        PayPalService service;

    final private String SUCCESS_URL = "order/pay/success";
    final private String CANCEL_URL = "order/pay/cancel";
    @Value(value = "${server.port:8080}")
    private String port;
    @Value(value = "${host.hostname:localhost}")
    private String hostname;
    private String serverSuccessUrl;
    private String serverFailureUrl;

    @PostConstruct
    private void init(){
        this.serverSuccessUrl=String.format("http://%s:%s/%s",hostname,port,SUCCESS_URL);
        this.serverFailureUrl=String.format("http://%s:%s/%s",hostname,port,CANCEL_URL);
    }


    public String payment(Double price,String description) {
        try {
            Payment payment = service.createPayment(price,"USD", "PAYPAL",
                    "SALE", description, serverFailureUrl,
                    serverSuccessUrl);
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return link.getHref();
                }
            }

        } catch (PayPalRESTException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"PayPal exception occurred",e);
        }
        return "error";
    }

    public String successPay(String paymentId,String payerID) {
        try {
            Payment payment = service.executePayment(paymentId,payerID);
            /**
             * restTemplate.postForEntity('mongoServerUrl',payment.toJSON(),String.class);
             */
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                return "success";
            }
        } catch (PayPalRESTException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"paypal payment not successfully performed",e);
        }
        return "failure";
    }
}