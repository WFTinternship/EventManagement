package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventMedia;

import java.util.List;

/**
 * Created by hermine on 7/2/16.
 */
public interface EventMediaDAO {
     boolean insertMedia(EventMedia media);
     boolean insertMediaList(List<EventMedia> mediaList);
     List<EventMedia> getAllMedia();
     List<EventMedia> getMediaByEvent(int eventId);
     List<EventMedia> getMediaByType(String type);
     List<EventMedia> getMediaByUploader(int uploaderId);
     List<EventMedia> getMediaByEventId(int eventId);
     boolean updateMediaDescription(int mediaId, String desc);
     boolean deleteMedia(int mediaId);
}
