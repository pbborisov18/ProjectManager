package com.company.projectManager.common.utils;


import com.company.projectManager.common.dto.businessUnit.BusinessUnitDTO;
import com.company.projectManager.common.dto.businessUnit.CompanyDTO;
import com.company.projectManager.common.dto.businessUnit.ProjectDTO;
import com.company.projectManager.common.dto.businessUnit.TeamDTO;
import com.company.projectManager.whiteboard.whiteboards.dto.WhiteboardDTO;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class BusinessUnitDTODeserializer extends JsonDeserializer<BusinessUnitDTO> {

    @Override
    public BusinessUnitDTO deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode tree = codec.readTree(jsonParser);

        Long id = tree.get("id").asLong();
        String name = tree.get("name").asText();
        TypeName type = codec.treeToValue(tree.get("type"), TypeName.class);
        WhiteboardDTO whiteboardDTO = codec.treeToValue(tree.get("whiteboard"), WhiteboardDTO.class);

        BusinessUnitDTO businessUnitDTO;

        switch (type) {
            case COMPANY -> businessUnitDTO = new CompanyDTO(id, name, type, whiteboardDTO);
            case PROJECT -> {
                CompanyDTO companyDTO = codec.treeToValue(tree.get("company"), CompanyDTO.class);
                businessUnitDTO = new ProjectDTO(id, name, type, companyDTO, whiteboardDTO);
            }
            case TEAM -> {
                CompanyDTO companyDTO1 = codec.treeToValue(tree.get("company"), CompanyDTO.class);
                ProjectDTO projectDTO = codec.treeToValue(tree.get("project"), ProjectDTO.class);
                businessUnitDTO = new TeamDTO(id, name, type, companyDTO1, projectDTO, whiteboardDTO);
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);

        }

        return businessUnitDTO;
    }


}
