/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prodigy_sd_03.entity;

/**
 *
 * @author kabiru
 */
public class UserEntity {

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the firstName
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @param firstname the firstName to set
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * @return the lastName
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * @param lastname the lastName to set
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * @return the phoneNo
     */
    public Integer getPhoneNo() {
        return phoneNo;
    }

    /**
     * @param phoneNo the phoneNo to set
     */
    public void setPhoneNo(Integer phoneNo) {
        this.phoneNo = phoneNo;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    private Integer id;
    private String firstname;
    private String lastname;
    private Integer phoneNo;
    private String email;   

    
//    while using the table 
    public UserEntity(Integer id, String firstname, String lastname, Integer phoneNo, String email) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNo = phoneNo;
        this.email = email;
    }
    
//    we use this constructor to add data
        public UserEntity(String firstname, String lastname, Integer phoneNo, String email) {
         this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNo = phoneNo;
        this.email = email;
    }
    
}
