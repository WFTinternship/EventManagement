package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;

/**
 * Created by Hermine Turshujyan 7/29/16.
 */
public class UserResponseDAOImpl extends GenericDAO implements UserResponseDAO {

    UserResponseDAOImpl(DataSourceManager dataSourceManager) throws Exception {
        super(dataSourceManager);
    }

    UserResponseDAOImpl() throws DAOException {
    }
}
