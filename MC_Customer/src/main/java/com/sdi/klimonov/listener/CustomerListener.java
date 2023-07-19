package com.sdi.klimonov.listener;

import com.sdi.klimonov.domain.Customer;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertCallback;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CustomerListener implements BeforeConvertCallback<Customer> {

    @Override
    public Customer onBeforeConvert(Customer entity, String collection) {
        System.out.println("Customer Name = " + entity.getLastName());
        if (Objects.isNull(entity.getBillingAddress().getId())) {
            ObjectId id = new ObjectId();
            String x = id.toString();
            entity.getBillingAddress().setId(id.toString());
        }
        return entity;
    }
}