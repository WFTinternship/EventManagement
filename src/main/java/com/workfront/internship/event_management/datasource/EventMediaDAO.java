package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventMedia;

import java.util.List;

/**
 * Created by hermine on 7/2/16.
 */
public interface EventMediaDAO {

     //insert data into db
     int insertMedia(EventMedia media);

     //read data from db
     EventMedia getMediaById(int mediaId);
     List<EventMedia> getAllMedia();
     List<EventMedia> getMediaByEventId(int eventId);
     List<EventMedia> getMediaByType(String type);
     List<EventMedia> getMediaByUploaderId(int uploaderId);

     //update data in db
     boolean updateMediaDescription(int mediaId, String description);

     //delete data from db
     boolean deleteMedia(int mediaId);
     boolean deleteAllMedia();
}
