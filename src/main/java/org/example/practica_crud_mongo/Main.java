package org.example.practica_crud_mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage stage) throws IOException {
        CochesController controller = new CochesController();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(R.getUI("coches.fxml"));
        fxmlLoader.setController(controller);
        VBox vbox = fxmlLoader.load();

        controller.cargarDatos();

        Scene scene = new Scene(vbox, 600, 581);
        stage.setTitle("CRUD Gestión de coches");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}