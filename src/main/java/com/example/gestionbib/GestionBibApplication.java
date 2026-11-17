package com.example.gestionbib;

import com.example.gestionbib.data.BibUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GestionBibApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        BibUtil.initialiserDB(); // Crée la table si elle n'existe pas
        FXMLLoader fxmlLoader = new FXMLLoader(
                GestionBibApplication.class.getResource("gestion-bib-view.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Gestion Bibliothèque");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}