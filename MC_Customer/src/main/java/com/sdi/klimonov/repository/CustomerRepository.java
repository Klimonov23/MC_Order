package com.sdi.klimonov.repository;

import com.sdi.klimonov.domain.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")

public interface CustomerRepository extends MongoRepository<Customer, String> {

}