package com.infybuzz.service;


import com.infybuzz.response.AddressResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonService {

    Logger logger = LoggerFactory.getLogger(CommonService.class);

    long count = 1;
    @Autowired
    AddressFeignClient AddressFeignClient;

    @CircuitBreaker(name = "addressService", fallbackMethod = "fallbackGetAddressById")
    public AddressResponse getAddressById (long addressId) {
        logger.info("count = " + count);
        count++;

		/*Mono<AddressResponse> addressResponse =
				webClient.get().uri("/getById/" + addressId).retrieve().bodyToMono(AddressResponse.class);*/

        AddressResponse addressResponse =AddressFeignClient.getById(addressId);



        return addressResponse;
    }

    public AddressResponse fallbackGetAddressById (long addressId, Throwable th) {
        logger.error("Error = " + th);

		/*Mono<AddressResponse> addressResponse =
				webClient.get().uri("/getById/" + addressId).retrieve().bodyToMono(AddressResponse.class);*/

        AddressResponse addressResponse =new AddressResponse();
        addressResponse.setAddressId(addressId);
        addressResponse.setCity("Cant fetch the address currently");
        addressResponse.setStreet("Cant fetch the address currently");
        return addressResponse;
    }
}
