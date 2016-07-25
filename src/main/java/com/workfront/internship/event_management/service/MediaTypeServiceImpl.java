package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.MediaTypeDAO;
import com.workfront.internship.event_management.dao.MediaTypeDAOImpl;
import com.workfront.internship.event_management.exception.DAOException;
import com.workfront.internship.event_management.model.MediaType;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/22/16.
 */
public class MediaTypeServiceImpl implements MediaTypeService {
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
