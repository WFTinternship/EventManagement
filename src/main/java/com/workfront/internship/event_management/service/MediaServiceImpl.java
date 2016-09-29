package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.MediaDAO;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.workfront.internship.event_management.service.util.Validator.*;

/**
 * Created by Hermine Turshujyan 7/22/16.
 */

@Component
public class MediaServiceImpl implements MediaService {

    private static final Logger logger = Logger.getLogger(MediaServiceImpl.class);

    @Autowired
    private MediaDAO mediaDAO;

    @Override
    public Media addMedia(Media media) {
        if (!isValidMedia(media)) {
            throw new InvalidObjectException("Invalid media type");
        }

        try {
            //insert media type into db, get generated id
            int mediaId = mediaDAO.addMedia(media);

            //set generated it to media type
            media.setId(mediaId);
        } catch (DuplicateEntryException e) {
            logger.error(e.getMessage(), e);
            throw new OperationFailedException("Media with path " + media.getName() + " already exists!", e);
        }
        return media;
    }

    @Override
    public void addMediaList(List<Media> mediaList) {

        if(isEmptyCollection(mediaList)){
            throw new InvalidObjectException("Empty media list");
        }

        for(Media media: mediaList) {
            if (!isValidMedia(media)) {
                throw new InvalidObjectException("Invalid media");
            }
        }

        //insert media list into d
        try {
            mediaDAO.addMediaList(mediaList);
        } catch (DuplicateEntryException e) {
            logger.error(e.getMessage(), e);
            throw new OperationFailedException("Media already exists!", e);
        }
    }

    @Override
    public Media getMediaById(int mediaId) {
        if (mediaId < 1) {
            throw new InvalidObjectException("Invalid media id");
        }
        Media media = mediaDAO.getMediaById(mediaId);
        if (media == null) {
            throw new ObjectNotFoundException("Media not found");
        }
        return media;
    }

    @Override
    public boolean editMediaDescription(int mediaId, String description) {
        if (mediaId < 1) {
            throw new InvalidObjectException("Invalid media id");
        }

        if (isEmptyString(description)) {
            throw new InvalidObjectException("Invalid media description");
        }
        boolean success = mediaDAO.updateMediaDescription(mediaId, description);

        if (!success) {
            throw new ObjectNotFoundException("Media not found!");
        }
        return success;
    }

    @Override
    public boolean deleteMedia(int mediaId) {
        if (mediaId < 1) {
            throw new InvalidObjectException("Invalid media id");
        }

        return mediaDAO.deleteMedia(mediaId);
    }

    @Override
    public void deleteAllMedia() {
        mediaDAO.deleteAllMedia();
    }

    @Override
    public List<Media> getAllMedia() {
        return mediaDAO.getAllMedia();
    }

    @Override
    public List<Media> getMediaByEvent(int eventId) {
        if (eventId < 1) {
            throw new InvalidObjectException("Invalid event id");
        }

        return mediaDAO.getMediaByEventId(eventId);
    }

    @Override
    public List<Media> getMediaByUploader(int uploaderId) {
        if (uploaderId < 1) {
            throw new InvalidObjectException("Invalid uploader id");
        }

        return mediaDAO.getMediaByUploaderId(uploaderId);
    }


}
