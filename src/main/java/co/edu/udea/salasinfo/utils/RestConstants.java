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

    // Restriction Messages
    public static final String SWAGGER_FIND_ALL_RESTRICTIONS_SUMMARY = "Find all restrictions";
    public static final String SWAGGER_FOUND_RESTRICTIONS = "List with all restrictions";
    public static final String SWAGGER_CREATE_RESTRICTION_SUMMARY = "Create a new restriction";
    public static final String SWAGGER_RESTRICTION_CREATED = "Restriction created successfully";
    public static final String SWAGGER_RESTRICTION_FOUND = "Restriction has been found";
    public static final String SWAGGER_RESTRICTION_NOT_FOUND = "Restriction not found";
    public static final String SWAGGER_UPDATE_RESTRICTION_SUMMARY = "Update a restriction";
    public static final String SWAGGER_RESTRICTION_UPDATED = "Restriction updated successfully";
    public static final String SWAGGER_DELETE_RESTRICTION_SUMMARY = "Delete a restriction";
    public static final String SWAGGER_RESTRICTION_DELETED = "Restriction deleted successfully";
    public static final String SWAGGER_FIND_RESTRICTION_BY_ID_SUMMARY = "Get a restriction by ID";

    //Implement messages
    public static final String SWAGGER_FIND_ALL_IMPLEMENTS_SUMMARY = "Retrieve all implements";
    public static final String SWAGGER_FIND_ALL_IMPLEMENTS_DESCRIPTION = "Fetch the list of all implement records in the system.";
    public static final String SWAGGER_SAVE_IMPLEMENT_SUMMARY = "Create a new implement";
    public static final String SWAGGER_SAVE_IMPLEMENT_DESCRIPTION = "Create a new implement record with the provided details.";
    public static final String SWAGGER_FIND_IMPLEMENT_BY_ID_SUMMARY = "Retrieve implement by ID";
    public static final String SWAGGER_FIND_IMPLEMENT_BY_ID_DESCRIPTION = "Fetch the implement record by its unique ID.";
    public static final String SWAGGER_REMOVE_IMPLEMENT_SUMMARY = "Delete implement by ID";
    public static final String SWAGGER_REMOVE_IMPLEMENT_DESCRIPTION = "Delete the implement record identified by the given ID.";
    public static final String SWAGGER_UPDATE_IMPLEMENT_SUMMARY = "Update implement by ID";
    public static final String SWAGGER_UPDATE_IMPLEMENT_DESCRIPTION = "Update the implement record identified by the given ID with the provided details.";

    //Application messages
    public static final String SWAGGER_FIND_ALL_APPLICATIONS_SUMMARY = "Retrieve all applications";
    public static final String SWAGGER_FIND_ALL_APPLICATIONS_DESCRIPTION = "Fetch the list of all application records in the system.";
    public static final String SWAGGER_SAVE_APPLICATION_SUMMARY = "Create a new application";
    public static final String SWAGGER_SAVE_APPLICATION_DESCRIPTION = "Create a new application record with the provided details.";
    public static final String SWAGGER_FIND_APPLICATION_BY_ID_SUMMARY = "Retrieve application by ID";
    public static final String SWAGGER_FIND_APPLICATION_BY_ID_DESCRIPTION = "Fetch the application record by its unique ID.";
    public static final String SWAGGER_REMOVE_APPLICATION_SUMMARY = "Delete application by ID";
    public static final String SWAGGER_REMOVE_APPLICATION_DESCRIPTION = "Delete the application record identified by the given ID.";
    public static final String SWAGGER_UPDATE_APPLICATION_SUMMARY = "Update application by ID";
    public static final String SWAGGER_UPDATE_APPLICATION_DESCRIPTION = "Update the application record identified by the given ID with the provided details.";

}
