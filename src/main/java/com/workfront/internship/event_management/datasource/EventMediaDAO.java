package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventMedia;

import java.util.List;

/**
 * Created by hermine on 7/2/16.
 */
public interface EventMediaDAO {
    public List<EventMedia> getAllMedia();
    public List<EventMedia> getMediaByEvent(int eventId);
    public List<EventMedia> getMediaByType(String type);
    public List<EventMedia> getMediaByUploader(int uploaderId);
    public boolean insertMedia(EventMedia media);
    public boolean updateMediaDescription(int mediaId, String desc);
    public boolean deleteMedia(int mediaId);
}
