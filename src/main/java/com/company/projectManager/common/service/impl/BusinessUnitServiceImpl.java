package com.company.projectManager.common.service.impl;

import com.company.projectManager.common.dto.BusinessUnitDTO;
import com.company.projectManager.common.dto.CompanyDTO;
import com.company.projectManager.common.dto.ProjectDTO;
import com.company.projectManager.common.entity.BusinessUnit;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.common.mapper.BusinessUnitMapper;
import com.company.projectManager.common.repository.BusinessUnitRepository;
import com.company.projectManager.common.service.BusinessUnitService;
import com.company.projectManager.common.utils.TypeName;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BusinessUnitServiceImpl implements BusinessUnitService {

    private final BusinessUnitRepository businessUnitRepository;

    private final BusinessUnitMapper businessUnitMapper;

    public BusinessUnitServiceImpl(BusinessUnitRepository businessUnitRepository, BusinessUnitMapper businessUnitMapper) {
        this.businessUnitRepository = businessUnitRepository;
        this.businessUnitMapper = businessUnitMapper;
    }

    @Transactional
    public void saveBusinessUnit(BusinessUnitDTO businessUnitDTO) {
        try {
            BusinessUnit businessUnit = businessUnitMapper.toBusinessUnitEntity(businessUnitDTO);

            businessUnitRepository.save(businessUnit);
        } catch (ConstraintViolationException | DataAccessException e) {
//            throw new FailedToSaveException("Unsuccessful save! " + e.getMessage());
        }
    }


    @Transactional
    public void updateBusinessUnit(BusinessUnitDTO businessUnitDTO) throws FailedToUpdateException, EntityNotFoundException {
        try {
            Optional<BusinessUnit> existingBusinessUnit = businessUnitRepository.findById(businessUnitDTO.id());

            if(existingBusinessUnit.isEmpty()) {
                throw new EntityNotFoundException("Business unit not found!");
            }

            BusinessUnit businessUnit = businessUnitMapper.toBusinessUnitEntity(businessUnitDTO);

            businessUnitRepository.save(businessUnit);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToUpdateException("Unsuccessful update!" + e.getMessage());
        }
    }

    @Transactional
    public void deleteBusinessUnit(BusinessUnitDTO businessUnitDTO) throws FailedToDeleteException, EntityNotFoundException {
        try {
            Optional<BusinessUnit> existingBusinessUnit = businessUnitRepository.findById(businessUnitDTO.id());

            if(existingBusinessUnit.isEmpty()) {
                throw new EntityNotFoundException("Business unit not found!");
            }

            businessUnitRepository.delete(existingBusinessUnit.get());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToDeleteException("Unsuccessful delete! " + e.getMessage());
        }
    }

    @Transactional
    public BusinessUnitDTO findBusinessUnitById(Long id) throws FailedToSelectException, EntityNotFoundException {
        try {
            Optional<BusinessUnit> businessUnit = businessUnitRepository.findById(id);

            if(businessUnit.isEmpty()) {
                throw new EntityNotFoundException("Business unit doesn't exist!");
            }

            return businessUnitMapper.toBusinessUnitDTO(businessUnit.get());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select! " + e.getMessage());
        }
    }

    @Transactional
    public List<BusinessUnitDTO> findBusinessUnitsByType(TypeName typeName) throws FailedToSelectException, EntityNotFoundException {
        try {
            List<BusinessUnit> businessUnits = businessUnitRepository.findAllByType(typeName);

            if(businessUnits.size() == 0) {
                throw new EntityNotFoundException("No business units found!");
            }

            return businessUnitMapper.toBusinessUnitDTO(businessUnits);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select! " + e.getMessage());
        }
    }

    @Transactional
    public List<BusinessUnitDTO> findBusinessUnitsByCompany(CompanyDTO companyDTO) throws FailedToSelectException, EntityNotFoundException {
        try {
            List<BusinessUnit> businessUnits = businessUnitRepository.findAllByCompany(businessUnitMapper.toBusinessUnitEntity(companyDTO));

            if(businessUnits.size() == 0) {
                throw new EntityNotFoundException("No business units found!");
            }

            return businessUnitMapper.toBusinessUnitDTO(businessUnits);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select! " + e.getMessage());
        }
    }

    @Transactional
    public List<BusinessUnitDTO> findBusinessUnitsByCompanyAndType(CompanyDTO companyDTO, TypeName typeName) throws FailedToSelectException, EntityNotFoundException {
        try {
            List<BusinessUnit> businessUnits = businessUnitRepository.findAllByCompanyAndType(businessUnitMapper.toBusinessUnitEntity(companyDTO), typeName);

            if(businessUnits.size() == 0) {
                throw new EntityNotFoundException("No business units found!");
            }

            return businessUnitMapper.toBusinessUnitDTO(businessUnits);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select!" + e.getMessage());
        }
    }

    @Transactional
    public List<BusinessUnitDTO> findBusinessUnitsByProject(ProjectDTO projectDTO) throws FailedToSelectException, EntityNotFoundException {
        try {
            List<BusinessUnit> businessUnits = businessUnitRepository.findAllByProject(businessUnitMapper.toEntityFromProjectDTO(projectDTO));

            if(businessUnits.size() == 0) {
                throw new EntityNotFoundException("No business units found!");
            }

            return businessUnitMapper.toBusinessUnitDTO(businessUnits);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select!" + e.getMessage());
        }
    }

}
