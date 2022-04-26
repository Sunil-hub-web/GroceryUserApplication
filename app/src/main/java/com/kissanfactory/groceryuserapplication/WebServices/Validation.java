package com.kissanfactory.groceryuserapplication.WebServices;

import java.util.regex.Pattern;

/**
 * Created by My PC on 4/2/2016.
 */
public class Validation
{



    // THIS IS VALIDATION FOR CHECK PATTERN OF EMAIL ADDRESS
    private  final static String EMAIL_PATTERN =  "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private final static String Mobile_PATTERN="[0-9]{10}";
    private final static String Password_PATTERN="[0-9]{8}";

    private  final static String Text_PATTERN = "[a-zA-Z0-9]+";

    private  final static Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(EMAIL_PATTERN);
    private final static Pattern MOBILE_NUMBER_PATTERN= Pattern.compile(Mobile_PATTERN);
    private final static Pattern TEXT_NUMBER_PATTERN= Pattern.compile(Text_PATTERN);

    private final static Pattern USER_NAME_PATTERN = Pattern.compile("-?\\\\d+(.\\\\d+)?");




    public static boolean checkEmail(String s)
    {

//        return EMAIL_ADDRESS_PATTERN.matcher(s).matches();
        return EMAIL_ADDRESS_PATTERN.matcher(s).matches();

    }
    public static boolean Text_PATTERN(String s)
    {

//        return EMAIL_ADDRESS_PATTERN.matcher(s).matches();
        return TEXT_NUMBER_PATTERN.matcher(s).matches();

    }
    public static boolean checkUserName(String username)
    {

        return USER_NAME_PATTERN.matcher(username).matches();
    }
        public static boolean checkMobile(String mob)
    {
 return  MOBILE_NUMBER_PATTERN.matcher(mob).matches();

    }
}
