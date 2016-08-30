package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.MediaType;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/30/16.
 */
public interface MediaTypeService {

    //Create
    MediaType addMediaType(MediaType mediaType);

    //Read
    MediaType getMediaTypeById(int mediaTypeId);

    List<MediaType> getAllMediaTypes();

    //Update
    boolean editMediaType(MediaType mediaType);

    //Delete
    boolean deleteMediaType(int mediaTypeId);

    void deleteAllMediaTypes();
}
