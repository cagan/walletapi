package com.cagan.walletapi.service.customer;

import com.cagan.walletapi.data.entity.Customer;
import com.cagan.walletapi.data.repository.CustomerRepository;
import com.cagan.walletapi.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new BusinessException("Customer not found for given ID: " + id));
    }

}
