package com.dylanfastdev.mvvm;

/**
 * @author YDL
 * @version 1.0
 * @date 2020/06/12/15:31
 */
public class User {
    private final String firstName;
    private final String lastName;
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public String getFirstName() {
        return this.firstName;
    }
    public String getLastName() {
        return this.lastName;
    }
}

