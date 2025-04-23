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

            Customer customer2 = new Customer();
            customer2.setName("AdminUser");
            customer2.setSurname("AdminUser");
            customer2.setTckn("12345678902");
            customerRepository.save(customer2);

            AppUser user2 = new AppUser();
            user2.setUsername("admin");
            user2.setPassword(passwordEncoder().encode("123456"));
            user2.setRole(Role.EMPLOYEE);
            user2.setCustomer(customer2);
            appUserRepository.save(user2);

            System.out.println("Default employee and customer created.");
        }
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}