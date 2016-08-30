package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.MediaTypeDAO;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
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

    private static final Logger logger = Logger.getLogger(MediaTypeServiceImpl.class);

    @Autowired
    private MediaTypeDAO mediaTypeDAO;

    @Override
    public MediaType addMediaType(MediaType mediaType) {
        if (!isValidMediaType(mediaType)) {
            throw new InvalidObjectException("Invalid media type");
        }

        try {
            //insert media type into db, get generated id
            int mediaTypeId = mediaTypeDAO.addMediaType(mediaType);

            //set generated it to media type
            mediaType.setId(mediaTypeId);
        } catch (DuplicateEntryException e) {
            logger.error(e.getMessage(), e);
            throw new OperationFailedException("Media type with title " + mediaType.getTitle() + " already exists!", e);
        }

        return mediaType;
    }

    @Override
    public MediaType getMediaTypeById(int mediaTypeId) {
        if (mediaTypeId < 0) {
            throw new InvalidObjectException("Invalid media type id");
        }

        MediaType mediaType = mediaTypeDAO.getMediaTypeById(mediaTypeId);
        if (mediaType == null) {
            throw new ObjectNotFoundException("Media type not found");
        }

        return mediaType;
    }

    @Override
    public List<MediaType> getAllMediaTypes() {
        return mediaTypeDAO.getAllMediaTypes();
    }

    @Override
    public boolean editMediaType(MediaType mediaType) {
        if (!isValidMediaType(mediaType)) {
            throw new InvalidObjectException("Invalid media type");
        }

        boolean success = false;
        try {
            //update recurrence type in db
            success = mediaTypeDAO.updateMediaType(mediaType);
        } catch (DuplicateEntryException e) {
            logger.error(e.getMessage(), e);
            throw new OperationFailedException("Media type with title " + mediaType.getTitle() + " already exists!", e);
        }
        if (!success) {
            throw new ObjectNotFoundException("Media type not found!");
        }

        return success;
    }

    @Override
    public boolean deleteMediaType(int mediaTypeId) {
        if (mediaTypeId < 1) {
            throw new InvalidObjectException("Invalid media type id");
        }

        return mediaTypeDAO.deleteMediaType(mediaTypeId);
    }

    @Override
    public void deleteAllMediaTypes() {
        mediaTypeDAO.deleteAllMediaTypes();
    }
}
