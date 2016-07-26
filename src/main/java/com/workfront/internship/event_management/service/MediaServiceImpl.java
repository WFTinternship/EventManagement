package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.MediaDAO;
import com.workfront.internship.event_management.dao.MediaDAOImpl;
import com.workfront.internship.event_management.dao.MediaTypeDAO;
import com.workfront.internship.event_management.dao.MediaTypeDAOImpl;
import com.workfront.internship.event_management.exception.DAOException;
import com.workfront.internship.event_management.model.Media;
import com.workfront.internship.event_management.model.MediaType;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/22/16.
 */
public class MediaServiceImpl implements MediaService {

    MediaDAO mediaDAO = new MediaDAOImpl();

    @Override
    public int addMedia(Media media) {
        return mediaDAO.addMedia(media);
    }

    @Override
    public Media getMediaById(int mediaId) {
        return mediaDAO.getMediaById(mediaId);
    }

    @Override
    public boolean updateMediaDescription(int mediaId, String description) {
        return mediaDAO.updateMediaDescription(mediaId, description);
    }

    @Override
    public boolean deleteMedia(int mediaId) throws DAOException {
        return mediaDAO.deleteMedia(mediaId);
    }

    @Override
    public List<Media> getAllMedia() {
        return mediaDAO.getAllMedia();
    }

    @Override
    public boolean deleteAllMedia() throws DAOException {
        return mediaDAO.deleteAllMedia();
    }

    @Override
    public List<Media> getMediaByEventId(int eventId) {
        return mediaDAO.getMediaByEventId(eventId);
    }

    @Override
    public List<Media> getMediaByType(int typeId) {
        return mediaDAO.getMediaByType(typeId);
    }

    @Override
    public List<Media> getMediaByUploaderId(int uploaderId) {
        return mediaDAO.getMediaByUploaderId(uploaderId);
    }


    //Media type operations
    MediaTypeDAO mediaTypeDAO = new MediaTypeDAOImpl();

    @Override
    public int addMediaType(MediaType mediaType) {
        return mediaTypeDAO.addMediaType(mediaType);
    }

    @Override
    public MediaType getMediaTypeById(int mediaTypeId) {
        return mediaTypeDAO.getMediaTypeById(mediaTypeId);
    }

    @Override
    public boolean updateMediaType(MediaType mediaType) {
        return mediaTypeDAO.updateMediaType(mediaType);
    }

    @Override
    public boolean deleteMediaType(int mediaTypeId) throws DAOException {
        return mediaTypeDAO.deleteMediaType(mediaTypeId);
    }

    @Override
    public List<MediaType> getAllMediaTypes() {
        return mediaTypeDAO.getAllMediaTypes();
    }

    @Override
    public boolean deleteAllMediaTypes() throws DAOException {
        return mediaTypeDAO.deleteAllMediaTypes();
    }
}
