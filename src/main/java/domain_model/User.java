package domain_model;

import utilities.UserType;

public abstract class User {
    //Region Fields
    private String id;
    private String name;
    private String surname;
    private String email;
    private String telephone;
    private String password;
    private UserType type;
    //end Region

    public User(String id, String name, String surname, String email, String telephone, String password, UserType type) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.telephone = telephone;
        this.password = password;
        this.type = type;
    }

    //Region Getters and Setters
    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }
    //end Region


}
