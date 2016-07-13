package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventMedia;

import java.util.List;

/**
 * Created by hermine on 7/2/16.
 */
public interface EventMediaDAO {

     //Create
     int insertMedia(EventMedia media);

     //Read
     EventMedia getMediaById(int mediaId);
     List<EventMedia> getAllMedia();
     List<EventMedia> getMediaByEventId(int eventId);
     List<EventMedia> getMediaByType(String type);
     List<EventMedia> getMediaByUploaderId(int uploaderId);

     //Update
     boolean updateMediaDescription(int mediaId, String description);

     //Delete
     boolean deleteMedia(int mediaId);
}
