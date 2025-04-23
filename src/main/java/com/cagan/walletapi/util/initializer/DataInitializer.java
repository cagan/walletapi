package com.cagan.walletapi.util.initializer;

import com.cagan.walletapi.data.entity.AppUser;
import com.cagan.walletapi.data.entity.Customer;
import com.cagan.walletapi.data.repository.AppUserRepository;
import com.cagan.walletapi.data.repository.CustomerRepository;
import com.cagan.walletapi.util.enums.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final CustomerRepository customerRepository;

    public DataInitializer(AppUserRepository appUserRepository, CustomerRepository customerRepository) {
        this.appUserRepository = appUserRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) {
        if (appUserRepository.count() == 0) {
            Customer customer = new Customer();
            customer.setName("Çağan");
            customer.setSurname("Şit");
            customer.setTckn("12345678901");
            customerRepository.save(customer);

            AppUser user = new AppUser();
            user.setUsername("test");
            user.setPassword(passwordEncoder().encode("123456"));
            user.setRole(Role.CUSTOMER);
            user.setCustomer(customer);
            appUserRepository.save(user);

            System.out.println("Default user and customer created.");
        }
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}