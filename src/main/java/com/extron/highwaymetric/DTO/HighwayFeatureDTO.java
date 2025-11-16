package com.extron.highwaymetric.DTO;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HighwayFeatureDTO {
    private String id;
    private String name;
    private String ref;

    private GeometryDTO geometry;

    @JsonProperty("properties")
    private Map<String,Object> properties;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GeometryDTO{
        private String type;

        @JsonProperty("coordinates")
        private List<List<Double>> coordinates;
    }
}
