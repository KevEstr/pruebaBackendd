package co.edu.udea.salasinfo.controller.v1;

import co.edu.udea.salasinfo.dto.request.RoomRequest;
import co.edu.udea.salasinfo.dto.request.filter.RoomFilter;
import co.edu.udea.salasinfo.configuration.advisor.responses.ExceptionResponse;
import co.edu.udea.salasinfo.dto.response.room.FreeScheduleResponse;
import co.edu.udea.salasinfo.dto.response.room.RoomResponse;
import co.edu.udea.salasinfo.dto.response.room.RoomScheduleResponse;
import co.edu.udea.salasinfo.dto.response.room.SpecificRoomResponse;
import co.edu.udea.salasinfo.service.RoomService;
import co.edu.udea.salasinfo.utils.RestConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @Operation(summary = RestConstants.SWAGGER_FIND_ALL_ROOMS_SUMMARY,
            security = @SecurityRequirement(name = RestConstants.SWAGGER_SECURITY_TYPE))
    @ApiResponses(value = {
            @ApiResponse(responseCode = RestConstants.CODE_OK, description = RestConstants.SWAGGER_FOUND_ROOMS,
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RoomResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<RoomResponse>> findAll(@Nullable @Valid RoomFilter filter) {
        return ResponseEntity.ok(roomService.findAll(filter));
    }

    @Operation(summary = RestConstants.SWAGGER_CREATE_ROOM_SUMMARY,
            security = @SecurityRequirement(name = RestConstants.SWAGGER_SECURITY_TYPE))
    @ApiResponses(value = {
            @ApiResponse(responseCode = RestConstants.CODE_CREATED, description = "Room created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoomResponse.class)))
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<RoomResponse> save(@RequestBody @Valid RoomRequest room) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.createRoom(room));
    }

    @Operation(summary = RestConstants.SWAGGER_FOUND_ROOM_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RestConstants.CODE_OK, description = RestConstants.SWAGGER_FOUND_ROOM,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SpecificRoomResponse.class))),
            @ApiResponse(responseCode = RestConstants.CODE_NOT_FOUND, description = RestConstants.SWAGGER_ROOM_NOT_FOUND,
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    @GetMapping("/{id}")
    public ResponseEntity<SpecificRoomResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.findById(id));
    }

    @Operation(summary = RestConstants.SWAGGER_DELETE_ROOM_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RestConstants.CODE_OK, description = "Room deleted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoomResponse.class))),
            @ApiResponse(responseCode = RestConstants.CODE_NOT_FOUND, description = RestConstants.SWAGGER_ROOM_NOT_FOUND,
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<RoomResponse> remove(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.deleteRoom(id));
    }

    @Operation(summary = RestConstants.SWAGGER_UPDATE_ROOM_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RestConstants.CODE_OK, description = "Room updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoomResponse.class))),
            @ApiResponse(responseCode = RestConstants.CODE_NOT_FOUND, description = RestConstants.SWAGGER_ROOM_NOT_FOUND,
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<RoomResponse> update(@PathVariable Long id, @RequestBody @Valid RoomRequest room) {
        return ResponseEntity.ok(roomService.updateRoom(id, room));
    }

    @Operation(summary = RestConstants.SWAGGER_FIND_FREE_ROOM_AT_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RestConstants.CODE_OK, description = RestConstants.SWAGGER_FOUND_FREE_ROOMS,
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RoomResponse.class))))
    })
    @GetMapping("/free")
    public ResponseEntity<List<RoomResponse>> findFree(@Nullable LocalDateTime date) {
        if (date == null) date = LocalDateTime.now();
        return ResponseEntity.ok(roomService.findFreeAt(date));
    }

    @Operation(summary = RestConstants.SWAGGER_FIND_SCHEDULE_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RestConstants.CODE_OK, description = RestConstants.SWAGGER_FOUND_SCHEDULE_LIST,
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RoomScheduleResponse.class)))),
            @ApiResponse(responseCode = RestConstants.CODE_NOT_FOUND, description = RestConstants.SWAGGER_ROOM_NOT_FOUND,
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    @GetMapping("/{id}/schedule")
    public ResponseEntity<List<RoomScheduleResponse>> findSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.findRoomSchedule(id));
    }

    @GetMapping("/{id}/freeSchedule")
    public ResponseEntity<List<FreeScheduleResponse>> findFreeRoomSchedule(
            @PathVariable Long id,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDate) {
        System.out.println("Fecha seleccionada: "+selectedDate);
        List<FreeScheduleResponse> freeSchedule = roomService.findFreeRoomSchedule(id, selectedDate);

        return ResponseEntity.ok(freeSchedule);
    }

}
