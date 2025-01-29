package com.multiplatform.time_management_backend.user.model;

import com.multiplatform.time_management_backend.group.model.Group;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
public class Student extends User {

    @ManyToOne
    private Group group;

    //Please make sure that the password is encrypted before it is passed in here
    public Student(String email, String password, String firstName, String lastName, LocalDate birthDate) {
        super(null, email, password, firstName, lastName, birthDate, Role.STUDENT);
    }
}
