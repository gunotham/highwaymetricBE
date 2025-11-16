package com.extron.highwaymetric.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GeometryImportDTO {
    private String highwayId;
    private String filePath;

}
