package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.Media;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/22/16.
 */
public interface MediaService {

    //Create
    int addMedia(Media media);

    //Read
    Media getMediaById(int mediaId);

    List<Media> getMediaByEventId(int eventId);

    List<Media> getMediaByType(int typeId);

    List<Media> getMediaByUploaderId(int uploaderId);

    List<Media> getAllMedia();

    //Update
    void updateMediaDescription(int mediaId, String description);

    void updateMediaList(List<Media> media);

    //Delete
    void deleteMedia(int mediaId);

    void deleteAllMedia();
}
