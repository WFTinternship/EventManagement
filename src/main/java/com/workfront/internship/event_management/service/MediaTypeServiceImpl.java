package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.MediaTypeDAO;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.MediaType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.workfront.internship.event_management.service.util.Validator.isValidMediaType;

/**
 * Created by Hermine Turshujyan 7/30/16.
 */

@Component
public class MediaTypeServiceImpl implements MediaTypeService {

    private static final Logger LOGGER = Logger.getLogger(MediaTypeServiceImpl.class);

    @Autowired
    private MediaTypeDAO mediaTypeDAO;

    @Override
    public MediaType addMediaType(MediaType mediaType) {
        if (!isValidMediaType(mediaType)) {
            throw new OperationFailedException("Invalid media type");
        }

        try {
            //insert media type into db, get generated id
            int mediaTypeId = mediaTypeDAO.addMediaType(mediaType);

            //set generated it to media type
            mediaType.setId(mediaTypeId);
        } catch (DuplicateEntryException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Media type with title " + mediaType.getTitle() + " already exists!", e);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
        return mediaType;
    }

    @Override
    public MediaType getMediaTypeById(int mediaTypeId) {
        if (mediaTypeId < 0) {
            throw new OperationFailedException("Invalid media type id");
        }

        try {
            return mediaTypeDAO.getMediaTypeById(mediaTypeId);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Media type not found");
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public List<MediaType> getAllMediaTypes() {
        try {
            return mediaTypeDAO.getAllMediaTypes();
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void editMediaType(MediaType mediaType) {
        if (!isValidMediaType(mediaType)) {
            throw new OperationFailedException("Invalid recurrence option");
        }

        try {
            //update recurrence type in db
            mediaTypeDAO.updateMediaType(mediaType);
        } catch (DuplicateEntryException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Media type with title " + mediaType.getTitle() + " already exists!", e);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Media type not found", e);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteMediaType(int mediaTypeId) {
        if (mediaTypeId < 1) {
            throw new OperationFailedException("Invalid media type id");
        }
        try {
            mediaTypeDAO.deleteMediaType(mediaTypeId);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Recurrence options not found", e);
        }
    }

    @Override
    public void deleteAllMediaTypes() {
        try {
            mediaTypeDAO.deleteAllMediaTypes();
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }
}
