package com.flavourfit.Homepage;

import com.flavourfit.Exceptions.TrackerException;
import com.flavourfit.Exceptions.UserNotFoundException;
import com.flavourfit.Homepage.DTO.FitnessStreakDTO;
import com.flavourfit.Homepage.DTO.RoutineDTO;
import com.flavourfit.Trackers.Calories.CalorieHistoryServiceImpl;
import com.flavourfit.Trackers.Calories.ICalorieHistoryService;
import com.flavourfit.Trackers.Water.IWaterHistoryService;
import com.flavourfit.User.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class
HomepageServiceImpl implements IHomepageService {

    private static Logger logger = LoggerFactory.getLogger(CalorieHistoryServiceImpl.class);
    private final IHomepageDao homepageDao;

    private final IWaterHistoryService waterHistoryService;

    private final ICalorieHistoryService calorieHistoryService;

    private final IUserService userService;

    @Autowired
    public HomepageServiceImpl(
            IHomepageDao homepageDao, IWaterHistoryService waterHistoryService,
            ICalorieHistoryService calorieHistoryService,
            IUserService userService
    ) {
        this.homepageDao = homepageDao;
        this.waterHistoryService = waterHistoryService;
        this.calorieHistoryService = calorieHistoryService;
        this.userService = userService;
    }


    @Override
    public List<HomepageEventDto> getEventList() {


        try {
            List<HomepageEventDto> eventlist = this.homepageDao.getEventList();

            return eventlist;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public HashMap<String, Object> getExerciseByUser(int userID) throws SQLException {
        logger.info("Entered getExcerciseByUser method");
        List<RoutineDTO> routines = homepageDao.getRoutinesByUser(userID);
        String quoteOfTheDay = homepageDao.getQuoteOfTheDay();
        HashMap<String, Object> result = new HashMap<>();
        result.put("quoteOfTheDay", quoteOfTheDay);
        result.put("routines", routines);
        return result;
    }

    @Override
    public Map<String, Object> fetchTrackerSummary(int userId) throws TrackerException {
        logger.info("Entered service method fetchTrackerSummary()");
        if (userId == 0) {
            logger.error("Invalid User");
            throw new UserNotFoundException("Invalid user");
        }

        Map<String, Object> trackerSummary = new HashMap<>();
        try {
            logger.info("Fetching current water intake and calories from respective services.");
            double waterIntake = this.waterHistoryService.fetchWaterIntakeByUserIdCurrent(userId).getWaterIntake();
            double calorieCount = this.calorieHistoryService.fetchCalorieByUserIdCurrent(userId).getCalorieCount();

            logger.info("Fetching currentWeight from userService");
            double currentWeight = this.userService.fetchUserCurrentWeight(userId);

            logger.info("Fetching tracker summary");
            FitnessStreakDTO fitnessStreak = this.homepageDao.getFitnessStreak(userId);

            trackerSummary.put("calorieCount", calorieCount);
            trackerSummary.put("waterIntake", waterIntake);
            trackerSummary.put("currentWeight", currentWeight);
            trackerSummary.put("fitnessStreak", fitnessStreak.getStreak());
            trackerSummary.put("averageStreakCalories", fitnessStreak.getAvgCalorie());
            trackerSummary.put("averageStreakWater", fitnessStreak.getAvgWaterIntake());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new TrackerException(e);
        }

        return trackerSummary;
    }
}
