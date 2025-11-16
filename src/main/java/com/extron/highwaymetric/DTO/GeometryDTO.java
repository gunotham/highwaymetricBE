package com.extron.highwaymetric.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeometryDTO {
    private String type;
    private String jsonFeatures;
}
