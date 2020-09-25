package com.sdet.auto.springboot2api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    private Long order_id;

    private String order_description;

    // multiple orders can be associated with one user
    @ManyToOne(fetch = FetchType.LAZY) // unless a call order.getUser this user will not be fetched when a request comes
    @JsonIgnore // added so when an order is created it will not expected user data to be sent
    private User user;

    public Order() {
        super();
    }

    public Long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
    }

    public String getOrder_description() {
        return order_description;
    }

    public void setOrder_description(String order_description) {
        this.order_description = order_description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
