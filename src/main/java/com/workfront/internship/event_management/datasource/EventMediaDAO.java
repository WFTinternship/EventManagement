package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventMedia;

import java.util.List;

/**
 * Created by hermine on 7/2/16.
 */
public interface EventMediaDAO {
     boolean insertMedia(EventMedia media);
     boolean insertMediaList(List<EventMedia> mediaList);
     EventMedia getMediaById(int mediaId);
     List<EventMedia> getAllMedia();
     List<EventMedia> getMediaByEventId(int eventId);
     List<EventMedia> getMediaByType(String type);
     List<EventMedia> getMediaByUploaderId(int uploaderId);
     boolean updateMediaDescription(int mediaId, String desc);
     boolean deleteMedia(int mediaId);
}
