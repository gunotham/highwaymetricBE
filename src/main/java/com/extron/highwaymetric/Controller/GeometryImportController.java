package com.extron.highwaymetric.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.extron.highwaymetric.DTO.GeometryImportDTO;
import com.extron.highwaymetric.DTO.ImportResultDTO;
import com.extron.highwaymetric.Service.GeometryImportService;

@RestController
@RequestMapping("/api/import")
public class GeometryImportController {

    private final GeometryImportService geometryImportService;

    
    public GeometryImportController(GeometryImportService geometryImportService) {
        this.geometryImportService = geometryImportService;
    }

    @PostMapping("/geometry")
    public ResponseEntity<ImportResultDTO> importHighwayGeometry(@RequestBody GeometryImportDTO importDTO) {
        ImportResultDTO result = geometryImportService.importGeometry(importDTO);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            // You might want to return a different HTTP status for different errors
            // For now, using 400 Bad Request for any failure.
            return ResponseEntity.badRequest().body(result);
        }
    }
}
