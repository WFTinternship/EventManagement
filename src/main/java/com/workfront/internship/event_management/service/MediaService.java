package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.Media;
import com.workfront.internship.event_management.model.MediaType;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/22/16.
 */
public interface MediaService {

    //CRUD operations with media
    int addMedia(Media media);
    Media getMediaById(int mediaId);
    boolean updateMediaDescription(int mediaId, String description);

    boolean deleteMedia(int mediaId);
    List<Media> getAllMedia();

    boolean deleteAllMedia();
    List<Media> getMediaByEventId(int eventId);
    List<Media> getMediaByType(int typeId);
    List<Media> getMediaByUploaderId(int uploaderId);

    //CRUD operations with media type
    int addMediaType(MediaType mediaType);
    MediaType getMediaTypeById(int mediaTypeId);
    boolean updateMediaType(MediaType mediaType);

    boolean deleteMediaType(int mediaTypeId);
    List<MediaType> getAllMediaTypes();

    boolean deleteAllMediaTypes();
}
