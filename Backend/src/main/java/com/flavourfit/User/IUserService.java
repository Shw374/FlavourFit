package com.flavourfit.User;

import com.flavourfit.Exceptions.UserNotFoundException;

import java.sql.SQLException;

public interface IUserService {
    public String fetchAllUsers() throws SQLException;

    public boolean resetPassword(int userId, String newPassword) throws SQLException;

    public int updateUser(UserDto user) throws SQLException;

    UserDto fetchUserById(int id) throws UserNotFoundException;

    UserDto fetchUserByEmail(String email) throws UserNotFoundException;
    public void registerUser(UserDto user) throws SQLException;

    public UserDto getUserbyID(int user) throws SQLException;

    public PremiumUserDto getUserBymembership(int user) throws SQLException;
}
