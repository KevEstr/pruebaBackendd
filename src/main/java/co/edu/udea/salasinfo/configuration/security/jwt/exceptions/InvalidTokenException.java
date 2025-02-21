package co.edu.udea.salasinfo.configuration.security.jwt.exceptions;

import co.edu.udea.salasinfo.utils.Constants;
import co.edu.udea.salasinfo.utils.Generated;
import org.springframework.security.core.AuthenticationException;

@Generated
public class InvalidTokenException extends AuthenticationException {
    public InvalidTokenException() {
        super(Constants.INVALID_TOKEN_MESSAGE);
    }
}
