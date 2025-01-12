package com.multiplatform.time_management_backend.user.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@DiscriminatorValue(User.Role.Fields.ADMIN)
public class Admin extends User {

    //Please make sure that the password is encrypted before it is passed in here
    public Admin(String email, String password, String firstName, String lastName, LocalDate birthDate) {
        super(email, password,firstName, lastName, birthDate, Role.ADMIN);
    }
}
