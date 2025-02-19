package co.edu.udea.salasinfo.utils;


import lombok.Getter;
import lombok.Setter;

public class TokenHolder {
    @Getter
    @Setter
    private static String token;

    private TokenHolder() {
        throw new IllegalStateException("Utility class");
    }
}
