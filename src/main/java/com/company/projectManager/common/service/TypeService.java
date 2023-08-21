package com.company.projectManager.common.service;

import com.company.projectManager.common.dto.TypeDTO;
import com.company.projectManager.common.exception.FailedToDeleteException;
import com.company.projectManager.common.exception.FailedToSaveException;
import com.company.projectManager.common.exception.FailedToSelectException;
import com.company.projectManager.common.exception.FailedToUpdateException;
import com.company.projectManager.common.mapper.TypeMapper;
import com.company.projectManager.common.entity.Type;
import com.company.projectManager.common.repository.TypeRepository;
import com.company.projectManager.common.utils.TypeName;
import com.company.projectManager.common.exception.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//100% Useless for now but open to extension in the future
@Service
public abstract class TypeService {

    @Autowired
    TypeRepository typeRepository;

    @Autowired
    TypeMapper typeMapper;


    public void saveType(TypeDTO typeDTO) throws FailedToSaveException {
        try{
            Type type = typeMapper.toEntity(typeDTO);

            typeRepository.save(type);
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Unsuccessful save!" + e.getMessage());
        }
    }

    public void updateType(TypeDTO typeDTO) throws FailedToUpdateException, EntityNotFoundException {
        try{
            Optional<Type> existingType = typeRepository.findById(typeDTO.id());

            if(existingType.isEmpty()) {
                throw new EntityNotFoundException("Type doesn't exist!");
            }

            Type type = typeMapper.toEntity(typeDTO);

            typeRepository.save(type);


        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToUpdateException("Unsuccessful update!" + e.getMessage());
        }
    }

    public void deleteType(TypeDTO typeDTO) throws FailedToDeleteException, EntityNotFoundException {
        try {
            Optional<Type> type = typeRepository.findById(typeDTO.id());

            if(type.isEmpty()) {
                throw new EntityNotFoundException("Type was not found!");
            }

            typeRepository.delete(type.get());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToDeleteException("Unsuccessful delete!" + e.getMessage());
        }
    }

    public TypeDTO findTypeById(Long id) throws FailedToSelectException, EntityNotFoundException {
        try {
            Optional<Type> type = typeRepository.findById(id);

            if(type.isEmpty()) {
                throw new EntityNotFoundException("Type was not found!");
            }

            return typeMapper.toDTO(type.get());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select!" + e.getMessage());
        }
    }

    public TypeDTO findTypeByName(TypeName name) throws FailedToSelectException, EntityNotFoundException {
        try {
            Optional<Type> type = typeRepository.findByName(name);

            if(type.isEmpty()) {
                throw new EntityNotFoundException("Type was not found!");
            }

            return typeMapper.toDTO(type.get());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select!" + e.getMessage());
        }
    }

    public List<TypeDTO> findAllTypes() throws FailedToSelectException, EntityNotFoundException {
        try {
            List<Type> types = (List<Type>) typeRepository.findAll();

            if(types.size() == 0) {
                throw new EntityNotFoundException("No types were found!");
            }

            return typeMapper.toDTO(types);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select!" + e.getMessage());
        }
    }

}
