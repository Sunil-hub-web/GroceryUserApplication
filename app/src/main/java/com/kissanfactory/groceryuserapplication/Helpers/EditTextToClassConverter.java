package com.kissanfactory.groceryuserapplication.Helpers;

import android.widget.EditText;

import com.kissanfactory.groceryuserapplication.Models.Customer;

public class EditTextToClassConverter {

    private String textToString(EditText editText){
        return editText.getText().toString().trim();
    }

    private long textToLong(EditText editText){
        return Long.parseLong(editText.getText().toString().trim());
    }

    public Customer toCustomer(EditText name, EditText email, EditText password, int countryCode, EditText mobile){
        return new Customer(textToString(name), textToString(email), textToString(password), countryCode, textToLong(mobile));
    }

    public Customer toCustomer(EditText mobile,EditText password,String fcmid){
        Customer customer = new Customer();
        customer.setMobile(Long.parseLong(mobile.getText().toString()));
        customer.setPassword(password.getText().toString().trim());
        customer.setFcm_id(fcmid);
        return customer;
    }
}
