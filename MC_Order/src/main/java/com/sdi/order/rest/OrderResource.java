package com.sdi.order.rest;

import com.sdi.order.domain.Order;
import com.sdi.order.exceptions.BadRequestAlertException;
import com.sdi.order.repository.OrderRepository;
import com.sdi.order.service.OrderService;
import com.sdi.order.util.HeaderUtil;
import com.sdi.order.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class OrderResource {

    private final Logger log = LoggerFactory.getLogger(OrderResource.class);

    private static final String ENTITY_NAME = "order";

    @Value("${spring.application.name}")
    private String applicationName;

    private final OrderRepository orderRepository;

    private final OrderService orderService;

    public OrderResource(OrderRepository orderRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    /**
     * {@code POST  /orders} : Create a new order.
     *
     * @param order the order to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new order, or with status {@code 400 (Bad Request)} if the order has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/orders")
    @Transactional
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order) throws URISyntaxException {
        log.debug("REST request to save Order : {}", order);
        if (order.getId() != null) {
            throw new BadRequestAlertException("A new order cannot already have an ID", ENTITY_NAME, "idexists");
        }
        final var result = orderRepository.save(order);
        orderService.createOrder(result);
        return ResponseEntity.created(new URI("/api/orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    @PutMapping("/orders")
    @Transactional
    public ResponseEntity<Order> updateOrder(@Valid @RequestBody Order order) throws URISyntaxException {
        log.debug("REST request to update Order : {}", order);
        if (order.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        final var result = orderRepository.save(order);
        orderService.updateOrder(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, order.getId().toString()))
            .body(result);
    }


    @GetMapping("/orders")
    @Transactional
    public List<Order> getAllOrders() {
        log.debug("REST request to get all Orders");
        return orderRepository.findAll();
    }


    @GetMapping("/orders/{id}")
    @Transactional
    public ResponseEntity<Order> getOrder(@PathVariable String id) {
        log.debug("REST request to get Order : {}", id);
        final var order = orderRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(order);
    }


    @DeleteMapping("/orders/{id}")
    @Transactional
    public ResponseEntity<Void> deleteOrder(@PathVariable String id) {
        log.debug("REST request to delete Order : {}", id);
        final var orderOptional = orderRepository.findById(id);
        orderRepository.deleteById(id);
        if (orderOptional.isPresent()) {
          orderService.deleteOrder(orderOptional.get());
        };
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
