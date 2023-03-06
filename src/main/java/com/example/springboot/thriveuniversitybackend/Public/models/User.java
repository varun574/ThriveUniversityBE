package com.example.springboot.thriveuniversitybackend.Public.models;

import com.example.springboot.thriveuniversitybackend.enums.UserTypes;
import com.example.springboot.thriveuniversitybackend.validators.annotations.EnumValue;
import com.example.springboot.thriveuniversitybackend.validators.annotations.NullableNonEmpty;
import com.example.springboot.thriveuniversitybackend.validators.annotations.Password;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "user")
@NoArgsConstructor
public class User {
    @Id
    private String id;
    @Pattern(regexp = "^[a-z]+\\.[a-z]+@thrive.ac.in$", message = "Must be well formed mail address.")
    private String email;
    @Password
    private String password;
    @NotEmpty(message = "Name must not be empty")
    private String name;
    @NullableNonEmpty(message = "Department must not be empty")
    @EnumValue(enumC = UserTypes.class, message = "User type must be one of STUDENT, TEACHER, ADMIN.")
    private String type;
    private boolean isRegistered;

    public User(String id, String email, String name, String type) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.type = type;
        this.isRegistered = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", isRegistered=" + isRegistered +
                '}';
    }
}
