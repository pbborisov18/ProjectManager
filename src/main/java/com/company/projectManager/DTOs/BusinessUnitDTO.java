package com.company.projectManager.DTOs;

import com.company.projectManager.utils.BusinessUnitDTODeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = BusinessUnitDTODeserializer.class)
public interface BusinessUnitDTO {

    Long id();

    String name();

    TypeDTO type();

    CompanyDTO company();

    ProjectDTO project();

    WhiteboardDTO whiteboard();

}
