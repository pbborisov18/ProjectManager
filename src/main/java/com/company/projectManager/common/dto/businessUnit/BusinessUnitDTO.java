package com.company.projectManager.common.dto.businessUnit;

import com.company.projectManager.common.utils.BusinessUnitDTODeserializer;
import com.company.projectManager.common.utils.TypeName;
import com.company.projectManager.whiteboard.whiteboards.dto.WhiteboardDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = BusinessUnitDTODeserializer.class)
public interface BusinessUnitDTO {

    Long id();

    String name();

    TypeName type();

    CompanyDTO company();

    ProjectDTO project();

    WhiteboardDTO whiteboard();

}
