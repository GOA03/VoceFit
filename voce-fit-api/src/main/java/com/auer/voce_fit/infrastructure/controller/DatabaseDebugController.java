package com.auer.voce_fit.infrastructure.controller;

import com.auer.voce_fit.infrastructure.persistence.JpaWorkoutRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@RestController
@RequestMapping("/api/database-debug")
public class DatabaseDebugController {

    private final JpaWorkoutRepository workoutRepository;
    private final DataSource dataSource;

    public DatabaseDebugController(JpaWorkoutRepository workoutRepository, DataSource dataSource) {
        this.workoutRepository = workoutRepository;
        this.dataSource = dataSource;
    }

    @GetMapping("/full-diagnosis")
    public String getFullDiagnosis() {
        StringBuilder diagnosis = new StringBuilder();
        
        try {
            // Check repository count
            long count = workoutRepository.count();
            diagnosis.append("Repository count: ").append(count).append("\n");
            
            // Check actual database
            try (Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement()) {
                
                // Check if table exists
                try (ResultSet resultSet = statement.executeQuery(
                        "SELECT COUNT(*) FROM workouts")) {
                    if (resultSet.next()) {
                        diagnosis.append("Actual database count: ").append(resultSet.getLong(1)).append("\n");
                    }
                }
                
                // Check table structure
                try (ResultSet columns = connection.getMetaData().getColumns(null, null, "workouts", null)) {
                    diagnosis.append("Table columns:\n");
                    while (columns.next()) {
                        diagnosis.append("  - ").append(columns.getString("COLUMN_NAME"))
                                .append(" (").append(columns.getString("TYPE_NAME")).append(")\n");
                    }
                }
                
                // Show actual data
                try (ResultSet data = statement.executeQuery(
                        "SELECT id, title, created_at, updated_at FROM workouts LIMIT 10")) {
                    diagnosis.append("Sample data:\n");
                    while (data.next()) {
                        diagnosis.append("  ID: ").append(data.getString("id")).append("\n")
                                .append("  Title: ").append(data.getString("title")).append("\n")
                                .append("  Created: ").append(data.getTimestamp("created_at")).append("\n")
                                .append("  Updated: ").append(data.getTimestamp("updated_at")).append("\n---\n");
                    }
                }
            }
            
        } catch (Exception e) {
            diagnosis.append("Error: ").append(e.getMessage()).append("\n");
        }
        
        return diagnosis.toString();
    }
}
