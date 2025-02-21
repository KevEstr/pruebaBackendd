package co.edu.udea.salasinfo.configuration.security.jwt.exceptions;

import co.edu.udea.salasinfo.utils.Constants;
import co.edu.udea.salasinfo.utils.Generated;

@Generated
public class InvalidTokenTypeException extends RuntimeException {
    public InvalidTokenTypeException(){
        super(Constants.INVALID_TOKEN_TYPE_MESSAGE);
    }
}
