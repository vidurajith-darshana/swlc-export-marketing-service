package com.swlc.swlcexportmarketingservice.constant;

public class ApplicationConstant {
    //Error Messages
    public static final String APPLICATION_ERROR_OCCURRED_MESSAGE = "Something went wrong. Please, try again.";
    public static final String INVALID_LOGIN_CREDENTIALS = "Invalid login credentials.";
    public static final String NOT_FOUND_CATEGORY = "Category not found!";
    public static final String NOT_FOUND_PRODUCT = "Product not found!";
    public static final String NOT_FOUND_THUMBNAIL = "Thumbnail not found!";
    public static final String NOT_FOUND_PROMOTION = "Promotion not found!";
    public static final String NOT_FOUND_TESTIMONIAL = "Testimonial not found!";
    public static final String NOT_FOUND_VIDEO = "Video not found!";

    //Success Messages
    public static final String REQUEST_SUCCESS_MESSAGE = "Your request was success.";

    //Email Subjects
    public static final String FORGOT_PASSWORD_EMAIL_SUBJECT = "CHILLI - Reset Password Request!";

    public enum USER_ROLES {
        ROLE_CUSTOMER,ROLE_OPERATOR,ROLE_ADMIN
    }

    public enum ORDER_STATUS {
        REVIEWING,IN_PROGRESS,DELIVERED
    }

    public enum PRODUCT_STATUS {
        ACTIVE,INACTIVE
    }
}
