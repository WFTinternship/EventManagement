package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.MediaDAO;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.Media;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.workfront.internship.event_management.service.util.Validator.isEmptyString;
import static com.workfront.internship.event_management.service.util.Validator.isValidMedia;

/**
 * Created by Hermine Turshujyan 7/22/16.
 */

@Component
public class MediaServiceImpl implements MediaService {

    private static final Logger LOGGER = Logger.getLogger(MediaServiceImpl.class);

    @Autowired
    private MediaDAO mediaDAO;

    @Override
    public Media addMedia(Media media) {
        if (!isValidMedia(media)) {
            throw new OperationFailedException("Invalid media type");
        }

        try {
            //insert media type into db, get generated id
            int mediaId = mediaDAO.addMedia(media);

            //set generated it to media type
            media.setId(mediaId);
        } catch (DuplicateEntryException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Media with path " + media.getPath() + " already exists!", e);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
        return media;
    }

    @Override
    public Media getMediaById(int mediaId) {
        if (mediaId < 1) {
            throw new OperationFailedException("Invalid media id");
        }

        try {
            return mediaDAO.getMediaById(mediaId);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Media not found");
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void editMediaDescription(int mediaId, String description) {
        if (mediaId < 1) {
            throw new OperationFailedException("Invalid media id");
        }

        if (isEmptyString(description)) {
            throw new OperationFailedException("Invalid media description");
        }

        try {
            mediaDAO.updateMediaDescription(mediaId, description);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Media not found");
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteMedia(int mediaId) {
        if (mediaId < 1) {
            throw new OperationFailedException("Invalid media id");
        }

        try {
            mediaDAO.deleteMedia(mediaId);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Media not found");
        }
    }

    @Override
    public void deleteAllMedia() {
        try {
            mediaDAO.deleteAllMedia();
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public List<Media> getAllMedia() {
        try {
            return mediaDAO.getAllMedia();
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public List<Media> getMediaByEvent(int eventId) {
        if (eventId < 1) {
            throw new OperationFailedException("Invalid event id");
        }

        try {
            return mediaDAO.getMediaByEventId(eventId);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public List<Media> getMediaByUploader(int uploaderId) {
        if (uploaderId < 1) {
            throw new OperationFailedException("Invalid uploader id");
        }

        try {
            return mediaDAO.getMediaByUploaderId(uploaderId);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }


}
