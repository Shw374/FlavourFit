package com.flavourfit.User;

import java.sql.SQLException;

public interface IUserService {
    public String fetchAllUsers() throws SQLException;
}