package co.edu.udea.salasinfo.controller.v1;

import co.edu.udea.salasinfo.dto.request.RestrictionRequest;
import co.edu.udea.salasinfo.dto.response.RestrictionResponse;
import co.edu.udea.salasinfo.service.RestrictionService;
import co.edu.udea.salasinfo.utils.RestConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/restrictions")
@RequiredArgsConstructor
public class RestrictionController {

    private final RestrictionService restrictionService;

    @Operation(summary = RestConstants.SWAGGER_FIND_ALL_RESTRICTIONS_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RestConstants.CODE_OK, description = RestConstants.SWAGGER_FOUND_RESTRICTIONS,
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RestrictionResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<RestrictionResponse>> findAll() {
        return ResponseEntity.ok(restrictionService.findAllRestrictions());
    }

    @Operation(summary = RestConstants.SWAGGER_CREATE_RESTRICTION_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RestConstants.CODE_CREATED, description = RestConstants.SWAGGER_RESTRICTION_CREATED,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestrictionResponse.class)))
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<RestrictionResponse> save(@RequestBody @Valid RestrictionRequest restrictionRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restrictionService.createRestriction(restrictionRequest));
    }


    @Operation(summary = RestConstants.SWAGGER_FIND_RESTRICTION_BY_ID_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RestConstants.CODE_OK, description = RestConstants.SWAGGER_RESTRICTION_FOUND,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestrictionResponse.class))),
            @ApiResponse(responseCode = RestConstants.CODE_NOT_FOUND, description = RestConstants.SWAGGER_RESTRICTION_NOT_FOUND)
    })
    @GetMapping("/{id}")
    public ResponseEntity<RestrictionResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(restrictionService.findRestrictionById(id));
    }

    @Operation(summary = RestConstants.SWAGGER_DELETE_RESTRICTION_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RestConstants.CODE_OK, description = RestConstants.SWAGGER_RESTRICTION_DELETED,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestrictionResponse.class))),
            @ApiResponse(responseCode = RestConstants.CODE_NOT_FOUND, description = RestConstants.SWAGGER_RESTRICTION_NOT_FOUND)
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<RestrictionResponse> remove(@PathVariable Long id) {
        return ResponseEntity.ok(restrictionService.deleteRestriction(id));
    }


    @Operation(summary = RestConstants.SWAGGER_UPDATE_RESTRICTION_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RestConstants.CODE_OK, description = RestConstants.SWAGGER_RESTRICTION_UPDATED,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestrictionResponse.class))),
            @ApiResponse(responseCode = RestConstants.CODE_NOT_FOUND, description = RestConstants.SWAGGER_RESTRICTION_NOT_FOUND)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<RestrictionResponse> update(@PathVariable Long id, @RequestBody @Valid RestrictionRequest restrictionRequest) {
        return ResponseEntity.ok(restrictionService.updateRestriction(id, restrictionRequest));
    }
}

