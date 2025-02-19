package co.edu.udea.salasinfo.controller.v1;

import co.edu.udea.salasinfo.dto.response.NotificationResponse;
import co.edu.udea.salasinfo.service.NotificationService;
import co.edu.udea.salasinfo.utils.RestConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(summary = RestConstants.SWAGGER_FIND_ALL_NOTIFICATIONS_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RestConstants.CODE_OK,
                    description = RestConstants.SWAGGER_FIND_ALL_NOTIFICATIONS_DESCRIPTION,
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = NotificationResponse.class)))),
    })
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> findAll(){
        return ResponseEntity.ok(notificationService.findAll());
    }
}
