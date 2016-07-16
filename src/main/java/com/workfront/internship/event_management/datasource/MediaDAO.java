package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.Media;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/2/16.
 */
public interface MediaDAO {

     //insert media into db
     int addMedia(Media media);

     //read data from db
     Media getMediaById(int mediaId);

    List<Media> getMediaByEventId(int eventId);

    List<Media> getMediaByType(String type);

    List<Media> getMediaByUploaderId(int uploaderId);

    List<Media> getAllMedia();

     //update data in db
     boolean updateMediaDescription(int mediaId, String description);

     //delete data from db
     boolean deleteMedia(int mediaId);
     boolean deleteAllMedia();
}
