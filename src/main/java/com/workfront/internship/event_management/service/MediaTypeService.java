package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.exception.DAOException;
import com.workfront.internship.event_management.model.MediaType;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/22/16.
 */
public interface MediaTypeService {

    //CRUD operations with media type
    int addMediaType(MediaType mediaType);

    MediaType getMediaTypeById(int mediaTypeId);

    boolean updateMediaType(MediaType mediaType);

    boolean deleteMediaType(int mediaTypeId) throws DAOException;

    //operations with all media types
    List<MediaType> getAllMediaTypes();

    boolean deleteAllMediaTypes() throws DAOException;

}
