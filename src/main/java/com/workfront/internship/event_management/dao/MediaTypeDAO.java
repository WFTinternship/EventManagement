package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.model.MediaType;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/21/16.
 */

public interface MediaTypeDAO {

    //insert data into db
    int addMediaType(MediaType mediaType);

    //read data from db
    List<MediaType> getAllMediaTypes();

    MediaType getMediaTypeById(int mediaTypeId);

    //update data in db
    boolean updateMediaType(MediaType mediaType);

    //delete data from db
    boolean deleteMediaType(int mediaTypeId);

    boolean deleteAllMediaTypes();
}
