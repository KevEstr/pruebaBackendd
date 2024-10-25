package co.edu.udea.salasinfo.controller.v1;

import co.edu.udea.salasinfo.dto.request.ClassReservationRequest;
import co.edu.udea.salasinfo.dto.request.ReservationRequest;
import co.edu.udea.salasinfo.configuration.advisor.responses.ExceptionResponse;
import co.edu.udea.salasinfo.dto.response.reservation.ReservationResponse;
import co.edu.udea.salasinfo.configuration.advisor.responses.ValidationExceptionResponse;
import co.edu.udea.salasinfo.service.ReservationService;
import co.edu.udea.salasinfo.utils.RestConstants;
import co.edu.udea.salasinfo.utils.enums.RStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Builder
@RestController
@RequestMapping("/v1/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @Operation(summary = RestConstants.SWAGGER_CREATE_SINGLE_TIME_RESERVATION_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RestConstants.CODE_CREATED,
                    description = RestConstants.SWAGGER_CREATE_RESERVATION_SUCCESS,
                    content = @Content(schema = @Schema(implementation = ReservationResponse.class))),
            @ApiResponse(responseCode = RestConstants.CODE_BAD_REQUEST,
                    description = RestConstants.SWAGGER_VALIDATIONS_DONT_PASS,
                    content = @Content(schema = @Schema(implementation = ValidationExceptionResponse.class))),
            @ApiResponse(responseCode = RestConstants.CODE_CONFLICT,
                    description = RestConstants.SWAGGER_RESERVATION_CONFLICT,
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    @PostMapping("/once")
    public ResponseEntity<ReservationResponse> saveSingleTimeReservation(@RequestBody @Valid ReservationRequest reservation) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.saveSingleTimeReservation(reservation));
    }

    @Operation(summary = RestConstants.SWAGGER_CREATE_BUNCH_RESERVATIONS_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RestConstants.CODE_CREATED,
                    description = RestConstants.SWAGGER_BUNCH_RESERVATIONS_SUCCESS,
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReservationResponse.class)))),
            @ApiResponse(responseCode = RestConstants.CODE_BAD_REQUEST,
                    description = RestConstants.SWAGGER_VALIDATIONS_DONT_PASS,
                    content = @Content(schema = @Schema(implementation = ValidationExceptionResponse.class))),
            @ApiResponse(responseCode = RestConstants.CODE_CONFLICT,
                    description = RestConstants.SWAGGER_RESERVATION_CONFLICT,
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    @PostMapping("/class")
    @PreAuthorize("hasAnyRole('ADMIN', 'MONITOR')")
    public ResponseEntity<List<ReservationResponse>> createClass(@RequestBody @Valid ClassReservationRequest reservation) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.saveClass(reservation));
    }

    @Operation(summary = RestConstants.SWAGGER_FIND_ALL_RESERVATIONS_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RestConstants.CODE_OK,
                    description = "List with all reservations",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReservationResponse.class)))),
    })
    @GetMapping()
    public ResponseEntity<List<ReservationResponse>> findAll() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    @Operation(summary = RestConstants.SWAGGER_FIND_REJECTED_RESERVATIONS_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RestConstants.CODE_OK,
                    description = "List with just refused reservations",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReservationResponse.class)))),
    })
    @GetMapping("/rejected")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<ReservationResponse>> findRefused() {
        return ResponseEntity.ok(reservationService.findStated(RStatus.REJECTED));
    }

    @Operation(summary = RestConstants.SWAGGER_FIND_ACCEPTED_RESERVATIONS_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RestConstants.CODE_OK,
                    description = "List with just accepted reservations",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReservationResponse.class)))),
    })
    @GetMapping("/accepted")
    public ResponseEntity<List<ReservationResponse>> findAccept() {
        return ResponseEntity.ok(reservationService.findStated(RStatus.ACCEPTED));
    }

    @Operation(summary = RestConstants.SWAGGER_FIND_PENDING_RESERVATIONS_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RestConstants.CODE_OK,
                    description = "List with just pending reservations",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReservationResponse.class)))),
    })
    @GetMapping("/pending")
    public ResponseEntity<List<ReservationResponse>> findRevision() {
        return ResponseEntity.ok(reservationService.findStated(RStatus.PENDING));
    }

    @Operation(summary = "Changes the state of a reservation to accepted")
    @ApiResponses(value = {
            @ApiResponse(responseCode = RestConstants.CODE_OK,
                    description = RestConstants.SWAGGER_ACCEPT_RESERVATION_SUCCESS,
                    content = @Content(schema = @Schema(implementation = ReservationResponse.class))),
            @ApiResponse(responseCode = RestConstants.CODE_NOT_FOUND,
                    description = RestConstants.SWAGGER_RESERVATION_NOT_FOUND,
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PatchMapping("/{id}/accept")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ReservationResponse> acceptReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.updateState(id, RStatus.ACCEPTED));
    }

    @Operation(summary = "Changes the state of a reservation to rejected")
    @ApiResponses(value = {
            @ApiResponse(responseCode = RestConstants.CODE_OK,
                    description = RestConstants.SWAGGER_REJECT_RESERVATION_SUCCESS,
                    content = @Content(schema = @Schema(implementation = ReservationResponse.class))),
            @ApiResponse(responseCode = RestConstants.CODE_NOT_FOUND,
                    description = RestConstants.SWAGGER_RESERVATION_NOT_FOUND,
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ReservationResponse> rejectReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.updateState(id, RStatus.REJECTED));
    }
}