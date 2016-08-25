package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.ObjectNotFoundException;
import com.workfront.internship.event_management.model.Media;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/2/16.
 */
public interface MediaDAO {

    //insert media into db
    int addMedia(Media media) throws DuplicateEntryException, DAOException;

    void addMedia(List<Media> mediaList) throws DAOException;


    //read data from db
    Media getMediaById(int mediaId) throws ObjectNotFoundException, DAOException;

    List<Media> getMediaByEventId(int eventId) throws DAOException;

    List<Media> getMediaByUploaderId(int uploaderId) throws DAOException;

    List<Media> getAllMedia() throws DAOException;

    //update data in db
    void updateMediaDescription(int mediaId, String description) throws DAOException, ObjectNotFoundException;

    //delete data from db
    void deleteMedia(int mediaId) throws ObjectNotFoundException, DAOException;

    void deleteAllMedia() throws DAOException;
}
