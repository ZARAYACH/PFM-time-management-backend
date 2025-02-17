package com.multiplatform.time_management_backend.user.model;

import com.multiplatform.time_management_backend.academicclass.modal.AcademicClass;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@DiscriminatorValue("TEACHER")
public class Teacher extends User {

    @OneToMany(mappedBy = "teacher")
    private List<AcademicClass> academicClasses = new ArrayList<>();

    public Teacher(User user) {
        super(null, user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getBirthDate(), user.getCreatedAt(), user.getUpdatedAt(), null, null);
    }

    //Please make sure that the password is encrypted before it is passed in here
    public Teacher(String email, String password, String firstName, String lastName, LocalDate birthDate) {
        super(null, email, password, firstName, lastName, birthDate);
    }


}
