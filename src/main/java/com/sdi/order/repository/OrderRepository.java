package com.sdi.order.repository;
import com.sdi.order.domain.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;



@SuppressWarnings("unused")
@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

}
