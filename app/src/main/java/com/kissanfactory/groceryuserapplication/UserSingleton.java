package com.kissanfactory.groceryuserapplication;

public class UserSingleton {

    private String name, email;
    private Long mobile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getMobile() {
        return mobile;
    }

    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }

    private static UserSingleton user_instance = null;

    public static UserSingleton getInstance(){
        if(user_instance==null){
            user_instance = new UserSingleton();
        }
        return user_instance;
    }
}
