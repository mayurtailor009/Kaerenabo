package com.kaerenabo.models;


public enum LoginEnum {
    FB,
    NUMBER_VERIFY,
    LOCATION_VERIFY,
    HOME;

    public static LoginEnum toMyEnum (String myEnumString) {
        try {
            return valueOf(myEnumString);
        } catch (Exception ex) {
            // For error cases
            return FB;
        }
    }
}
