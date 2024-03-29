package com.myproject.webclient.client;

import com.myproject.webclient.model.CustomerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerServiceImplTest {

    @Autowired
    CustomerService customerService;
    AtomicBoolean atomicBoolean;


    @BeforeEach
    void setUp() {
        atomicBoolean = new AtomicBoolean(false);
    }

    @Test
    @Order(1)
    void listCustomers() {

        customerService.listCustomers().subscribe(response -> {
            System.out.println(response);
            atomicBoolean.set(true);
        });

        await().untilTrue(atomicBoolean);
    }

    @Test
    @Order(2)
    void testListCustomersGetMap() {

        customerService.listCustomersMap().subscribe(response -> {
            System.out.println(response);
            atomicBoolean.set(true);
        });

        await().untilTrue(atomicBoolean);
    }

    @Test
    @Order(3)
    void testListCustomersJsonNode() {

        customerService.listCustomersJsonNode().subscribe(jsonNode -> {

            System.out.println(jsonNode.toPrettyString());
            atomicBoolean.set(true);
        });

        await().untilTrue(atomicBoolean);
    }

    @Test
    @Order(4)
    void testListCustomerDtos() {

        customerService.listCustomerDtos().subscribe(dto -> {
            System.out.println(dto.getCustomerName());
            atomicBoolean.set(true);
        });

        await().untilTrue(atomicBoolean);
    }

    @Test
    @Order(5)
    void testGetCustomerById() {

        customerService.listCustomerDtos()
                .flatMap(dto -> customerService.getCustomerById(dto.getId()))
                .subscribe(byIdDto -> {
                    System.out.println(byIdDto.getCustomerName());
                    atomicBoolean.set(true);
                });

        await().untilTrue(atomicBoolean);
    }

    @Test
    @Order(6)
    void testGetCustomerByName() {

        customerService.getCustomerByName("Mike")
                .subscribe(dto -> {
                    System.out.println(dto.toString());
                    atomicBoolean.set(true);
                });

        await().untilTrue(atomicBoolean);
    }

    @Test
    @Order(7)
    void testCreateCustomer() {

        CustomerDTO customerDTO = CustomerDTO.builder().customerName("Mike").build();

        customerService.createCustomer(customerDTO)
                .subscribe(dto -> {
                    System.out.println(dto.toString());
                    atomicBoolean.set(true);
                });

        await().untilTrue(atomicBoolean);
    }

    @Test
    @Order(8)
    void testUpdateCustomer() {

        final String NAME = "Papieżak Hehe";

        customerService.listCustomerDtos()
                .next()
                .doOnNext(customerDTO -> customerDTO.setCustomerName(NAME))
                .flatMap(dto -> customerService.updateCustomer(dto))
                .subscribe(updatedDto -> {
                    System.out.println(updatedDto.toString());
                    atomicBoolean.set(true);
                });

        await().untilTrue(atomicBoolean);
    }
}