package co.edu.udea.salasinfo.utils;

public class Constants {
    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String INVALID_TOKEN_MESSAGE = "The given token is invalid";
    public static final String INVALID_TOKEN_TYPE_MESSAGE = "The given token has a wrong typ2";
    public static final String ROOM_OCCUPIED_AT_MESSAGE = "Room '%s' is occupied at %s";
    public static final String EMAIL_REGEX_RFC5322 = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*"
            + "@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    public static final String DOCUMENT_REGEX = "^\\d{6,12}";

    // Null messages
    public static final String ACTIVITY_NAME_FIELD_NOT_NULL_MESSAGE = "'activityName' cannot be null";
    public static final String ACTIVITY_DESCRIPTION_FIELD_NOT_NULL_MESSAGE = "'activityDescription' cannot be null";
    public static final String DATE_FIELD_NOT_NULL_MESSAGE = "'date' cannot be null";
    public static final String STARTS_AT_FIELD_NOT_NULL_MESSAGE = "'startsAt' cannot be null";
    public static final String ENDS_AT_FIELD_NOT_NULL_MESSAGE = "'endsAt' cannot be null";
    public static final String TYPE_FIELD_NOT_NULL_MESSAGE = "'type' cannot be null";
    public static final String USER_ID_FIELD_NOT_NULL_MESSAGE = "'userId' cannot be null";
    public static final String ROOM_ID_FIELD_NOT_NULL_MESSAGE = "'roomId' cannot be null";
    public static final String SESSIONS_FIELD_NOT_NULL_MESSAGE = "'sessions' cannot be null";
    public static final String SEMESTER_STARTS_AT_FIELD_NOT_NULL_MESSAGE = "'semesterStartsAt' cannot be null";
    public static final String SEMESTER_ENDS_AT_FIELD_NOT_NULL_MESSAGE = "'semesterEndsAt' cannot be null";
    public static final String DAY_FIELD_NOT_NULL_MESSAGE = "'day' cannot be null";

    public static final String FIRST_NAME_FIELD_NOT_NULL_MESSAGE = "'firstname' cannot be null";
    public static final String LAST_NAME_FIELD_NOT_NULL_MESSAGE = "'lastname' cannot be null";
    public static final String EMAIL_FIELD_NOT_NULL_MESSAGE = "'email' cannot be null";
    public static final String PASSWORD_FIELD_NOT_NULL_MESSAGE = "'password' cannot be null";

    public static final String COMPUTER_AMOUNT_FIELD_NOT_NULL_MESSAGE = "'computerAmount' cannot be null";
    public static final String BUILDING_FIELD_NOT_NULL_MESSAGE = "'building' cannot be null";
    public static final String ROOM_NUM_FIELD_NOT_NULL_MESSAGE = "'roomNum' cannot be null";
    public static final String ROOM_NAME_FIELD_NOT_NULL_MESSAGE = "'roomName' cannot be null";
    public static final String SUB_ROOM_FIELD_NOT_NULL_MESSAGE = "'subRoom' cannot be null";

    // Past dates messages
    public static final String DATE_FIELD_NOT_PAST_MESSAGE = "'date' cannot be a past date";
    public static final String STARTS_AT_FIELD_NOT_PAST_MESSAGE = "'startsAt' cannot be a past date";
    public static final String ENDS_AT_FIELD_NOT_PAST_MESSAGE = "'endsAt' cannot be a past date";

    // Out of bounds
    public static final String SESSION_FIELD_OUT_OF_BOUNDS_MESSAGE = "'sessions' must be between 1 and 3";

    public static final String EMAIL_ALREADY_REGISTERED_MESSAGE = "A user with '%s' already exists";

    public static final String ENTITY_NOT_FOUND_MESSAGE = "Entity of '%s' type searched with '%s' not found";
    public static final String RESERVATION_NOT_FOUND_MESSAGE = "Reservation with id '%s' not found";

    public static final String USER_NOT_FOUND_MESSAGE = "User with id '%s' not found";
    public static final String EMAIL_NOT_FOUND_MESSAGE = "User with email '%s' not found";

    public static final String USER_REGISTERED_MESSAGE = "%s %s with '%s' has been registered";

    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String HOUR_FORMAT = "HH:mm";


    // Positive
    public static final String  COMPUTER_AMOUNT_POSITIVE_MESSAGE = "'computerAmount' must be positive'";

}
