package com.multiplatform.time_management_backend.user.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@DiscriminatorValue("ADMIN")
public class Admin extends User {

    public Admin(User user) {
        super(null, user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getBirthDate(), user.getCreatedAt(), user.getUpdatedAt(), null, null);
    }

    public Admin(String email, String password, String firstName, String lastName, LocalDate birthDate) {
        super(null, email, password, firstName, lastName, birthDate);
    }
}
