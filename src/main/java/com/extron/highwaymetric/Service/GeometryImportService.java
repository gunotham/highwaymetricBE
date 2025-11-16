package com.extron.highwaymetric.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.PrecisionModel;

import com.extron.highwaymetric.DTO.GeometryImportDTO;
import com.extron.highwaymetric.DTO.HighwayFeatureDTO;
import com.extron.highwaymetric.DTO.ImportResultDTO;
import com.extron.highwaymetric.Model.Highway;
import com.extron.highwaymetric.Repository.HighwayRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class GeometryImportService {

    private final HighwayRepository highwayRepository;

    public GeometryImportService(HighwayRepository highwayRepository){
        this.highwayRepository = highwayRepository;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    

    public ImportResultDTO importGeometry(@NonNull GeometryImportDTO importDTO){
        try {
            Highway highway = highwayRepository.findById(importDTO.getHighwayId()).orElse(null);

            if(highway == null){
                return new ImportResultDTO(false, "Highway not found", importDTO.getHighwayId());
            }

            File jsonFile = new File(importDTO.getFilePath());
            if (!jsonFile.exists()) {
                return new ImportResultDTO(false, "JSON file does not exist: " + importDTO.getFilePath(), importDTO.getHighwayId());
            }

            List<HighwayFeatureDTO> features = objectMapper.readValue(jsonFile, new TypeReference<List<HighwayFeatureDTO>>() {});

            MultiLineString geometry = convertToMultiLineString(features);

            if (geometry == null){
                return new ImportResultDTO(false, "Failed to convert coordinates to geometry from file: " + importDTO.getFilePath(), importDTO.getHighwayId());
            }

            highway.setGeom(geometry);
            highwayRepository.save(highway);

            return new ImportResultDTO(true, "Geometry imported successfully", importDTO.getHighwayId());
            
        } catch (IOException e) {
            return new ImportResultDTO(false, "Error reading JSON file: " + e.getMessage(), importDTO.getHighwayId());
        } catch (Exception e){
            return new ImportResultDTO(false, "Error processing geometry: " + e.getMessage(), importDTO.getHighwayId());
        }
    }

    @SuppressWarnings("UseSpecificCatch")
    private MultiLineString convertToMultiLineString(List<HighwayFeatureDTO> features){
        try {
            if (features == null || features.isEmpty()) {
                return null;
            }
    
            List<LineString> lineStringList = new ArrayList<>();
    
            for (HighwayFeatureDTO feature : features) {
                HighwayFeatureDTO.GeometryDTO geometryDTO = feature.getGeometry();
    
                if (geometryDTO != null && "LineString".equals(geometryDTO.getType())) {
                    List<List<Double>> lineCoordinates = geometryDTO.getCoordinates();
                    if (lineCoordinates != null && !lineCoordinates.isEmpty()) {
                        Coordinate[] coordinates = new Coordinate[lineCoordinates.size()];
                        for (int i = 0; i < lineCoordinates.size(); i++) {
                            List<Double> point = lineCoordinates.get(i);
                            if (point != null && point.size() >= 2) {
                                coordinates[i] = new Coordinate(point.get(0), point.get(1));
                            } else {
                                throw new IllegalArgumentException("Invalid coordinate point (must have at least 2 values) in feature ID: " + feature.getId());
                            }
                        }
                        lineStringList.add(geometryFactory.createLineString(coordinates));
                    }
                }
            }
    
            if (lineStringList.isEmpty()) {
                return null;
            }
    
            return geometryFactory.createMultiLineString(lineStringList.toArray(LineString[]::new));
    
        } catch (Exception e) {
            return null;
        }
    }
}
