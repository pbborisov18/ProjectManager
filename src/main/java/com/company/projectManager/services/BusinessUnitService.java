package com.company.projectManager.services;

import com.company.projectManager.DTOs.*;
import com.company.projectManager.exceptions.FailedToDeleteException;
import com.company.projectManager.exceptions.FailedToSaveException;
import com.company.projectManager.exceptions.FailedToSelectException;
import com.company.projectManager.exceptions.FailedToUpdateException;
import com.company.projectManager.mappers.BusinessUnitMapper;
import com.company.projectManager.mappers.TypeMapper;
import com.company.projectManager.models.BusinessUnit;
import com.company.projectManager.repositories.BusinessUnitRepository;
import com.company.projectManager.exceptions.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public abstract class BusinessUnitService {

    @Autowired
    BusinessUnitRepository businessUnitRepository;

    @Autowired
    BusinessUnitMapper businessUnitMapper;

    @Autowired
    TypeMapper typeMapper;


    @Transactional
    public void saveBusinessUnit(BusinessUnitDTO businessUnitDTO) throws FailedToSaveException {
        try {
            BusinessUnit businessUnit = businessUnitMapper.toBusinessUnitEntity(businessUnitDTO);

            businessUnitRepository.save(businessUnit);
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Unsuccessful save! " + e.getMessage());
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
    public List<BusinessUnitDTO> findBusinessUnitsByType(TypeDTO typeDTO) throws FailedToSelectException, EntityNotFoundException {
        try {
            List<BusinessUnit> businessUnits = businessUnitRepository.findAllByTypeName(typeDTO.name());

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
    public List<BusinessUnitDTO> findBusinessUnitsByCompanyAndType(CompanyDTO companyDTO, TypeDTO typeDTO) throws FailedToSelectException, EntityNotFoundException {
        try {
            List<BusinessUnit> businessUnits = businessUnitRepository.findAllByCompanyAndType(businessUnitMapper.toBusinessUnitEntity(companyDTO), typeMapper.toEntity(typeDTO));

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
