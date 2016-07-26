package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.DAOException;
import com.workfront.internship.event_management.model.Media;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/2/16.
 */
public interface MediaDAO {

    //insert media into db
    int addMedia(Media media);

    boolean addMedia(List<Media> mediaList);


    //read data from db
    Media getMediaById(int mediaId);

    List<Media> getMediaByEventId(int eventId);

    List<Media> getMediaByType(int typeId);

    List<Media> getMediaByUploaderId(int uploaderId);

    List<Media> getAllMedia();

    //update data in db
    boolean updateMediaDescription(int mediaId, String description);

    //delete data from db
    boolean deleteMedia(int mediaId) throws DAOException;

    boolean deleteAllMedia() throws DAOException;
}
