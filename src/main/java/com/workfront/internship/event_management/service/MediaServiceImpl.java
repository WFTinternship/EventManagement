package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.Media;
import com.workfront.internship.event_management.model.MediaType;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/22/16.
 */
public class MediaServiceImpl implements MediaService {


    @Override
    public int addMedia(Media media) {
        return 0;
    }

    @Override
    public Media getMediaById(int mediaId) {
        return null;
    }

    @Override
    public boolean updateMediaDescription(int mediaId, String description) {
        return false;
    }

    @Override
    public boolean deleteMedia(int mediaId) {
        return false;
    }

    @Override
    public List<Media> getAllMedia() {
        return null;
    }

    @Override
    public boolean deleteAllMedia() {
        return false;
    }

    @Override
    public List<Media> getMediaByEventId(int eventId) {
        return null;
    }

    @Override
    public List<Media> getMediaByType(int typeId) {
        return null;
    }

    @Override
    public List<Media> getMediaByUploaderId(int uploaderId) {
        return null;
    }

    @Override
    public int addMediaType(MediaType mediaType) {
        return 0;
    }

    @Override
    public MediaType getMediaTypeById(int mediaTypeId) {
        return null;
    }

    @Override
    public boolean updateMediaType(MediaType mediaType) {
        return false;
    }

    @Override
    public boolean deleteMediaType(int mediaTypeId) {
        return false;
    }

    @Override
    public List<MediaType> getAllMediaTypes() {
        return null;
    }

    @Override
    public boolean deleteAllMediaTypes() {
        return false;
    }
}
