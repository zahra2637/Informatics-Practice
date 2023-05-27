package com.sample.app.entity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.io.Serial;
import java.io.Serializable;

public class PersonDto implements Serializable {
    @Serial
    private  static final long SerialVersionUID=1L;

    @JsonProperty
   @NotBlank(message ="First Name must not be Empty")
    private String firstName;
    @JsonProperty
    @NotNull(message ="First Name must not be Empty")
    private String lastName;
    @JsonProperty
    @NotNull(message ="First Name must not be Empty")
    @Pattern(regexp = "^[0-9]*$", message = "age pattern incorrect")
    private int age;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
