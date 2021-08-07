package com.met.auth.seed;

import com.met.auth.entity.Role;
import com.met.auth.entity.User;
import com.met.auth.repository.RoleRepository;
import com.met.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

@Component
public class DatabaseSeeder {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // FOR TEST PURPOSES
    @EventListener
    public void seed(ContextRefreshedEvent contextRefreshedEvent) {
        seedRoles();
        seedUsers();
    }

    private void seedRoles() {
        if (roleRepository.findAll().size() == 0) {
            logger.info("Seeding roles");
            Role admin = new Role();
            admin.setRoleName("Role admin");
            admin.setRole("ROLE_ADMIN");
            Role operator = new Role();
            operator.setRoleName("Role user");
            operator.setRole("ROLE_USER");

            roleRepository.saveAll(Arrays.asList(admin, operator));
        }
    }

    private void seedUsers() {
        if (userRepository.findAll().size() == 0) {
            logger.info("Seeding users");
            Role adminRole = roleRepository.findByRoleIgnoreCase("ROLE_ADMIN").get();
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@united.cloud");
            admin.setFullName("Administrator");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles(Collections.singleton(adminRole));
            userRepository.save(admin);
        }
    }

}
