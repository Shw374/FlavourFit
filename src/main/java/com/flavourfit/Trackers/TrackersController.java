package com.flavourfit.Trackers;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.Helpers.DateHelpers;
import com.flavourfit.ResponsesDTO.PutResponse;
import com.flavourfit.Trackers.Calories.*;
import com.flavourfit.Trackers.Water.IWaterHistoryService;
import com.flavourfit.Trackers.Water.WaterHistoryDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/trackers")
public class TrackersController {
    private static Logger logger = LoggerFactory.getLogger(TrackersController.class);

    private ICalorieHistoryService calorieHistoryService;
    private IWaterHistoryService waterHistoryService;

    @Autowired
    public TrackersController(ICalorieHistoryService calorieHistoryService, IWaterHistoryService waterHistoryService) {
        this.calorieHistoryService = calorieHistoryService;
        this.waterHistoryService = waterHistoryService;
    }

    @Autowired
    public void setCalorieHistoryService(ICalorieHistoryService calorieHistoryService) {
        this.calorieHistoryService = calorieHistoryService;
    }

    @Autowired
    public void setWaterHistoryService(IWaterHistoryService waterHistoryService) {
        this.waterHistoryService = waterHistoryService;
    }


    @PutMapping("/record-calories")
    public ResponseEntity<Object> recordCalories(@RequestBody Map<String, Object> request) {
        logger.info("Entered controlled method recordCalories()");
        double calorieCount = (Double) request.get("calorieCount");
        try {
            logger.info("Updating calorie count through calorieHistoryService.");
            this.calorieHistoryService.recordCalorieUpdate(calorieCount, 1);
            Map<String, Object> data = new HashMap<>();

            logger.info("Fetching the total calorie count for current date.");
            CalorieHistoryDto todaysCalorieCount = this.calorieHistoryService.fetchCalorieByUserIdDate(DateHelpers.getCurrentDateString(), 1);
            data.put("todaysCalorieCount", todaysCalorieCount.getCalorieCount());

            logger.info("Updated record count. Returning response through api");
            return ResponseEntity.ok().body(new PutResponse(true, "Successfully recorded calorie count", data));
        } catch (SQLException e) {
            logger.error("Bad api request during recordCalorieCount()");
            return ResponseEntity.badRequest().body(new PutResponse(false, "Failed to record calorieCount"));
        }
    }

    @PutMapping("/record-waterIntake")
    public ResponseEntity<Object> recordWaterIntake(@RequestBody Map<String, Object> request) {
        logger.info("Entered controller method recordWaterIntake()");
        double waterIntake = (Double) request.get("waterIntake");
        try {
            logger.info("Updating water intake through waterHistoryService.");
            this.waterHistoryService.recordWaterIntake(waterIntake, 1);
            Map<String, Object> data = new HashMap<>();

            logger.info("Fetching the total water intake for current date.");
            WaterHistoryDto todaysWaterIntake = this.waterHistoryService.fetchWaterIntakeByUserIdDate(DateHelpers.getCurrentDateString(), 1);
            data.put("todaysWaterIntake", todaysWaterIntake.getWaterIntake());

            logger.info("Updated record count. Returning response through api");
            return ResponseEntity.ok().body(new PutResponse(true, "Successfully recorded Water intake", data));
        } catch (SQLException e) {
            logger.error("Bad api request during recordWaterIntake()");
            return ResponseEntity.badRequest().body(new PutResponse(false, "Failed to record Water intake"));
        }
    }
}