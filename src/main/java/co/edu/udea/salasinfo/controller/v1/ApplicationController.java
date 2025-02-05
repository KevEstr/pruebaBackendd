package co.edu.udea.salasinfo.controller.v1;

import co.edu.udea.salasinfo.configuration.advisor.responses.ExceptionResponse;
import co.edu.udea.salasinfo.dto.request.ApplicationRequest;
import co.edu.udea.salasinfo.dto.response.ApplicationResponse;
import co.edu.udea.salasinfo.service.ApplicationService;
import co.edu.udea.salasinfo.utils.RestConstants;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/v1/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = RestConstants.CODE_OK, description = "Applications retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationResponse.class)))
    })
    @Operation(summary = RestConstants.SWAGGER_FIND_ALL_APPLICATIONS_SUMMARY, description = RestConstants.SWAGGER_FIND_ALL_APPLICATIONS_DESCRIPTION)
    @GetMapping
    public ResponseEntity<List<ApplicationResponse>> findAll() {
        return ResponseEntity.ok(applicationService.findAll());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = RestConstants.CODE_CREATED, description = "Application created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationResponse.class)))
    })
    @Operation(summary = RestConstants.SWAGGER_SAVE_APPLICATION_SUMMARY, description = RestConstants.SWAGGER_SAVE_APPLICATION_DESCRIPTION)
    @PostMapping
    @PreAuthorize("hasAnyRole('Admin')")
    public ResponseEntity<ApplicationResponse> save(@RequestBody @Valid ApplicationRequest applicationRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationService.createApplication(applicationRequest));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = RestConstants.CODE_OK, description = "Application found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationResponse.class))),
            @ApiResponse(responseCode = RestConstants.CODE_NOT_FOUND, description = "Application not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @Operation(summary = RestConstants.SWAGGER_FIND_APPLICATION_BY_ID_SUMMARY, description = RestConstants.SWAGGER_FIND_APPLICATION_BY_ID_DESCRIPTION)
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.findById(id));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = RestConstants.CODE_OK, description = "Application deleted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationResponse.class))),
            @ApiResponse(responseCode = RestConstants.CODE_NOT_FOUND, description = "Application not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @Operation(summary = RestConstants.SWAGGER_REMOVE_APPLICATION_SUMMARY, description = RestConstants.SWAGGER_REMOVE_APPLICATION_DESCRIPTION)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('Admin')")
    public ResponseEntity<ApplicationResponse> remove(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.deleteApplication(id));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = RestConstants.CODE_OK, description = "Application updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationResponse.class))),
            @ApiResponse(responseCode = RestConstants.CODE_NOT_FOUND, description = "Application not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @Operation(summary = RestConstants.SWAGGER_UPDATE_APPLICATION_SUMMARY, description = RestConstants.SWAGGER_UPDATE_APPLICATION_DESCRIPTION)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('Admin')")
    public ResponseEntity<ApplicationResponse> update(@PathVariable Long id, @RequestBody @Valid ApplicationRequest applicationRequest) {
        return ResponseEntity.ok(applicationService.updateApplication(id, applicationRequest));
    }
}
