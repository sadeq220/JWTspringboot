package com.sadeqstore.demo.paypalConfig;

public class Order {
    private Double price;
    private String currency;
    private String method;
    private String intent;
    private String description;
    public Order(){}
    public Order(Double price,String currency
                 ,String method,String intent
                    ,String description){
                                          this.price=price;this.currency=currency;
                                          this.method=method;this.intent=intent;
                                          this.description=description;
                                        }

    @Override
    public String toString() {
        return price+currency+method+intent+description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
