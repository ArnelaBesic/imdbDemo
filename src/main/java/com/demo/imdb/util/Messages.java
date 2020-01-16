package com.demo.imdb.util;

public class Messages {
    public static final String NO_DATA = "There is no records of this type";
    public static final String USE_PUT_FOR_RECORD_UPDATE = "Found ID. PUT should be used for update actions";
    public static final String IMAGE_NOT_FOUND = "Image with %d ID was not found";
    public static final String IMAGE_CONVERSION_ERROR = "Image %s couldn't be converted to Blob type. Cause: %s";
    public static final String USE_MOVIE_API_TO_CREATE_NEW_MOVIE = "New movie (%s) should be created through movie api.";
    public static final String MOVIE_NOT_FOUND = "Movie with ID %s was not found";
    public static final String USE_POST_FOR_RECORD_CREATION = "ID not found. POST should be used for record creation";
    public static final String SUCCESSFUL_DELETION = "Record id[%s]was deleted.";
    public static final String ACTOR_NOT_FOUND = "Actor with ID %d was not found";
    public static final String CONSTRAINT_VIOLATION_EXCEPTION = "Record has connections to other objects.";
    public static final String DELETE_EXCEPTION = "Exception occurred while attempting to delete %s %s. Cause: %s";
    public static final String USE_ACTOR_API_TO_CREATE_NEW_ACTOR = "New actor (%s) should be created through actor api.";
}
