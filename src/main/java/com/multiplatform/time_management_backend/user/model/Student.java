package com.multiplatform.time_management_backend.user.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
public class Student extends User {

    //Please make sure that the password is encrypted before it is passed in here
    public Student(String email, String password, String firstName, String lastName, LocalDate birthDate) {
        super(null,email, password,firstName, lastName, birthDate, Role.STUDENT);
    }
}
