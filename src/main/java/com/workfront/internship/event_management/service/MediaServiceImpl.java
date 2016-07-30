package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.Media;

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
    public void updateMediaDescription(int mediaId, String description) {

    }

    @Override
    public void updateMediaList(List<Media> media) {

    }

    @Override
    public void deleteMedia(int mediaId) {

    }

    @Override
    public void deleteAllMedia() {

    }

    @Override
    public List<Media> getAllMedia() {
        return null;
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


}
