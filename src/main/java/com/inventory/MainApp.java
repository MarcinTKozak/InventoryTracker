package com.inventory;

import com.inventory.database.DatabaseConnection;
import com.inventory.database.DatabaseInitializer;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.xml.crypto.Data;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Inventory Tracker");
        primaryStage.show();
    }

    @Override
    public void stop() {
        com.inventory.database.DatabaseConnection.closeConnection();

    }

    public static void main(String[] args) {
        DatabaseInitializer.initialize();
        launch(args);
    }
}
