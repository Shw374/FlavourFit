package com.flavourfit.User;

import java.sql.SQLException;

public interface IUserService {
    public String fetchAllUsers() throws SQLException;

    public boolean resetPassword(int userId, String newPassword) throws SQLException;

    public int updateUser(UserDto user) throws SQLException;

    public void registerUser(UserDto user) throws SQLException;

    public UserDto getUserbyID(int user) throws SQLException;

    public PremiumUserDto getUserBymembership(int user) throws SQLException;
}
