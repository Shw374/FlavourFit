package com.flavourfit.User;

import com.flavourfit.Exceptions.PaymentException;
import com.flavourfit.Exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class UserService implements IUserService {
    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    private final IUserDao userDao;

    @Autowired
    public UserService(IUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public int updateUser(UserDto user) throws SQLException {
        logger.info("Started updateUser() method");
        if (user == null) {
            logger.error("Invalid user parameter");
            throw new RuntimeException("Invalid user");
        }
        return this.userDao.updateUser(user);
    }

    @Override
    public UserDto fetchUserById(int id) throws UserNotFoundException {
        logger.info("Started fetchUserById() method");

        try {
            UserDto user = this.userDao.getUserById(id);
            logger.info("Exiting fetchUserById() method");
            return user;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new UserNotFoundException(e);
        }
    }

    public boolean resetPassword(int userID, String newPassword) throws SQLException {
        logger.info("Started resetPassword() method");
        if (newPassword == null || newPassword.isEmpty()) {
            logger.warn("Invalid password parameter");
            throw new RuntimeException("Invalid Password");
        }

        return this.userDao.resetUserPassword(userID, newPassword);
    }

    @Override
    public PremiumUserDto getUserBymembership(int user) throws SQLException {
        logger.info("Started getUserByMembership() method");
        return this.userDao.getUserBymembership(user);
    }

    @Override
    public UserDto fetchUserByEmail(String email) throws UserNotFoundException {
        logger.info("Started fetchUserByEmail() method");

        try {
            logger.info("Calling getUserByEmail() method of userDao");

            UserDto user = this.userDao.getUserByEmail(email);
            return user;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new UserNotFoundException(e);
        }
    }

    public boolean paymentForPremium(int userID, String cardNumber, String mmyy, String cvv) throws PaymentException,
                                                                                                    SQLException {
        logger.info("Started paymentForPremiumCheck() method");
        if (cardNumber.length() != 16) {
            logger.warn("Invalid card number length");
            throw new PaymentException("Invalid Payment: Card Number entered is not valid");
        }
        if (cvv.length() != 3) {
            logger.warn("Invalid cvv length");
            throw new PaymentException("Invalid Payment: CVV entered is not valid");
        }

        if (mmyy.length() != 4) {
            logger.warn("Invalid MM/YY length");
            throw new PaymentException("Invalid Payment: MM/YY entered is not valid");
        } else {
            int midIndex = mmyy.length() / 2;

            String mm = mmyy.substring(0, midIndex);
            String yy = mmyy.substring(midIndex);

            int month = Integer.parseInt(mm);
            int year = Integer.parseInt(yy);

            if (!(month >= 12 && month < 00) || !(year >= 30 && month < 10)) {
                logger.warn("Invalid MM/YY ranges");
                throw new PaymentException("Invalid Payment: MM/YY entered is not in valid ranges");
            }
        }
        return this.userDao.userUpgradedToPremium(userID);
    }
}
