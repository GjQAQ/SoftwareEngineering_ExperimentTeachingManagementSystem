package team.segroup.etms.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ControllerUtils {
    public static <T> ResponseEntity<T> defaultBadRequest(
        boolean ok, T bodyWhenOk
    ) {
        return defaultResponse(ok, bodyWhenOk, ResponseEntity.badRequest());
    }

    public static <T> ResponseEntity<T> defaultNotFound(
        boolean ok, T bodyWhenOk
    ) {
        return defaultResponse(ok, bodyWhenOk, ResponseEntity.status(HttpStatus.NOT_FOUND));
    }

    public static <T> ResponseEntity<T> defaultForbidden(
        boolean ok, T bodyWhenOk
    ) {
        return defaultResponse(ok, bodyWhenOk, ResponseEntity.status(HttpStatus.FORBIDDEN));
    }

    public static <T> ResponseEntity<T> defaultResponse(
        boolean ok, T bodyWhenOk, ResponseEntity.BodyBuilder builder
    ) {
        if (ok) {
            return ResponseEntity.ok(bodyWhenOk);
        } else {
            return builder.build();
        }
    }
}
