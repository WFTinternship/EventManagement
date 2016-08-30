package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.model.MediaType;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/21/16.
 */

public interface MediaTypeDAO {

    //insert data into db
    int addMediaType(MediaType mediaType) throws DAOException, DuplicateEntryException;

    //read data from db
    List<MediaType> getAllMediaTypes() throws DAOException;

    MediaType getMediaTypeById(int mediaTypeId) throws ObjectNotFoundException, DAOException;

    //update data in db
    boolean updateMediaType(MediaType mediaType) throws DuplicateEntryException, DAOException, ObjectNotFoundException;

    //delete data from db
    boolean deleteMediaType(int mediaTypeId) throws ObjectNotFoundException, DAOException;

    void deleteAllMediaTypes() throws DAOException;
}
