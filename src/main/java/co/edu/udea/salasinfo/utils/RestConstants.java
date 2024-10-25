package co.edu.udea.salasinfo.utils;

public class RestConstants {
    private RestConstants() {
        // Not used
        throw new IllegalStateException("Utility class");
    }
    // HTTP Status Codes
    public static final String CODE_OK = "200";
    public static final String CODE_CREATED = "201";
    public static final String CODE_ACCEPTED = "202";
    public static final String CODE_CONFLICT = "409";
    public static final String CODE_BAD_REQUEST = "400";
    public static final String CODE_NOT_FOUND = "404";
    // Validation Messages
    public static final String SWAGGER_VALIDATIONS_DONT_PASS = "Some of the fields don't pass validations";
    public static final String SWAGGER_SECURITY_TYPE = "bearerAuth";


    // Authentication Messages
    public static final String SWAGGER_LOGIN_SUMMARY = "Authenticate a user with their email and password";
    public static final String SWAGGER_LOGIN_SUCCESSFUL = "User has been authenticated";

    public static final String SWAGGER_REGISTER_SUMMARY = "Create a new user in the system with USER role by default";
    public static final String SWAGGER_REGISTER_SUCCESSFUL = "User has been registered";
    public static final String SWAGGER_USER_ALREADY_REGISTERED = "A user with that email is already registered";

    // Reservation Messages
    public static final String SWAGGER_CREATE_SINGLE_TIME_RESERVATION_SUMMARY = "Creates a new single time reservation ";
    public static final String SWAGGER_CREATE_RESERVATION_SUCCESS = "Reservation has been saved";
    public static final String SWAGGER_CREATE_BUNCH_RESERVATIONS_SUMMARY = "Creates a bunch of reservations for the whole semester";
    public static final String SWAGGER_BUNCH_RESERVATIONS_SUCCESS = "Reservations have been saved";
    public static final String SWAGGER_RESERVATION_CONFLICT = "A reservation at that time already exists";
    public static final String SWAGGER_FIND_ALL_RESERVATIONS_SUMMARY = "Find all reservations";
    public static final String SWAGGER_FIND_REJECTED_RESERVATIONS_SUMMARY = "Find rejected reservations";
    public static final String SWAGGER_FIND_ACCEPTED_RESERVATIONS_SUMMARY = "Find accepted reservations";
    public static final String SWAGGER_FIND_PENDING_RESERVATIONS_SUMMARY = "Find pending reservations";
    public static final String SWAGGER_ACCEPT_RESERVATION_SUCCESS = "Reservation has been accepted";
    public static final String SWAGGER_REJECT_RESERVATION_SUCCESS = "Reservation has been rejected";
    public static final String SWAGGER_RESERVATION_NOT_FOUND = "Reservation not found";

    // Room Messages
    public static final String SWAGGER_FIND_ALL_ROOMS_SUMMARY = "Find all rooms and, if you pass a filter parameter, filters it";
    public static final String SWAGGER_FOUND_ROOMS = "Found rooms";
    public static final String SWAGGER_FOUND_ROOM_SUMMARY = "Find a specific room";
    public static final String SWAGGER_FOUND_ROOM = "Found room";
    public static final String SWAGGER_ROOM_NOT_FOUND = "Room not found";
    public static final String SWAGGER_CREATE_ROOM_SUMMARY = "Create a new room";
    public static final String SWAGGER_UPDATE_ROOM_SUMMARY = "Update an existing room";
    public static final String SWAGGER_DELETE_ROOM_SUMMARY = "Remove a room";
    public static final String SWAGGER_FIND_FREE_ROOM_NOW_SUMMARY = "Find free room right now";
    public static final String SWAGGER_FOUND_FREE_ROOMS = "Found free rooms";
    public static final String SWAGGER_FIND_FREE_ROOM_AT_SUMMARY = "Find schedule free room at a specific time";
    public static final String SWAGGER_FIND_SCHEDULE_SUMMARY = "Find schedule of a specific room";
    public static final String SWAGGER_FOUND_SCHEDULE_LIST = "Found schedule list";

    // User Messages
    public static final String SWAGGER_FIND_ALL_USERS_SUMMARY = "Find all users";
    public static final String SWAGGER_FOUND_USERS = "List with all users";
    public static final String SWAGGER_FIND_USER_BY_ID_SUMMARY = "Find a user by their id";
    public static final String SWAGGER_USER_FOUND = "User has been found";
    public static final String SWAGGER_USER_NOT_FOUND = "User not found";
    public static final String SWAGGER_UPDATE_USER_ROLE_SUMMARY = "Update the role of a User";
    public static final String SWAGGER_USER_ROLE_CHANGED = "User role has changed";
}
