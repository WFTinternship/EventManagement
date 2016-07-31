package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.Media;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/22/16.
 */
public interface MediaService {

    //Create
    Media addMedia(Media media);

    //Read
    Media getMedia(int mediaId);

    List<Media> getMediaByEvent(int eventId);

    List<Media> getMediaByType(int typeId);

    List<Media> getMediaByUploader(int uploaderId);

    List<Media> getAllMedia();

    //Update
    void updateMediaDescription(int mediaId, String description);

    //Delete
    void deleteMedia(int mediaId);

    void deleteAllMedia();
}
