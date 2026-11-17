package com.example.gestionbib;

import com.example.gestionbib.data.BibUtil;
import com.example.gestionbib.data.Livre;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GestionBibController implements Initializable {

    @FXML
    private TableView<Livre> tabLivres;

    @FXML
    private TextField titreR;

    @FXML
    private TextField tfTitre;

    @FXML
    private Button btnRechercher;

    @FXML
    private Button btnSupprimer;

    @FXML
    private Button btnSupprimerTout;

    @FXML
    private Button btnQuitter;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnAnnulerM;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnAnnuler;

    @FXML
    private ColorPicker cpCouleur;

    @FXML
    private ColorPicker cpCouleurM;

    @FXML
    private Label lbNbPages;

    @FXML
    private Label lbNbPagesM;

    @FXML
    private Slider slNbPages;

    @FXML
    private Slider slNbPagesM;

    @FXML
    private TextField tfTitreM;

    @FXML
    private ComboBox<Integer> cbId;

    @FXML
    private TabPane tpMenu;

    ObservableList<Integer> listId;

    boolean refrechCombo = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        cpCouleur.setValue(Color.GREEN);

        slNbPages.setValue(100);

        actualiserNbPages();

        actualiserNbPagesM();

        ajouterColonnes();

        remplir();

        ecouteurs();

        cbId.setItems(listId);
    }

    private void ajouterColonnes() {

        tabLivres.getColumns().clear();

        TableColumn<Livre, Integer> idCol = new TableColumn<>("Id");
        TableColumn<Livre, String> titreCol = new TableColumn<>("Titre");
        TableColumn<Livre, Color> couleurCol = new TableColumn<>("Couleur");
        TableColumn<Livre, Integer> nbPagesCol = new TableColumn<>("Nb Pages");

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titreCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
        couleurCol.setCellValueFactory(new PropertyValueFactory<>("couleur"));
        nbPagesCol.setCellValueFactory(new PropertyValueFactory<>("nbPages"));

        tabLivres.getColumns().addAll(idCol, titreCol, couleurCol, nbPagesCol);
    }

    private void ecouteurs() {

        btnQuitter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                quitter();
            }
        });

        btnAjouter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ajouter();
            }
        });

        btnAnnuler.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                annuler();
            }
        });

        btnAnnulerM.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                annuler();
            }
        });

        btnModifier.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                modifier();
            }
        });

        btnRechercher.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                rechercher();
            }
        });

        btnSupprimer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                supprimer();
            }
        });

        btnSupprimerTout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                supprimerTout();
            }
        });

        slNbPages.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue,
                                Number oldValue,
                                Number newValue) {

                slNbPages.setValue(Math.round(newValue.doubleValue()));

                actualiserNbPages();
            }
        });

        slNbPagesM.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue,
                                Number oldValue,
                                Number newValue) {

                slNbPagesM.setValue(Math.round(newValue.doubleValue()));

                actualiserNbPagesM();
            }
        });

        tabLivres.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (event.getClickCount() == 2)
                    afficherModifier();
            }
        });

        cbId.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (refrechCombo &&
                        actionEvent.getEventType() == ActionEvent.ACTION)

                    actualiserLivre();
            }
        });
    }

    private void quitter() {

        Platform.exit();
    }

    private void ajouter() {

        if (BibUtil.ajouterLivre(
                tfTitre.getText(),
                cpCouleur.getValue().toString(),
                (int) slNbPages.getValue())) {

            remplir();

            tfTitre.clear();

            cpCouleur.setValue(Color.GREEN);

            slNbPages.setValue(100);

            tpMenu.getSelectionModel().select(0);

        } else {

            afficherErreur(
                    BibUtil.getDernierTitreErreur(),
                    BibUtil.getDernierMessageErreur());
        }
    }

    private void modifier() {

        if (cbId.getValue() == null) {

            afficherErreur(
                    "Erreur",
                    "Veuillez sélectionner un livre.");

            return;
        }

        int id = cbId.getValue();

        if (BibUtil.modifierLivre(
                id,
                tfTitreM.getText(),
                cpCouleurM.getValue().toString(),
                (int) slNbPagesM.getValue())) {

            remplir();

            viderChampsModification();

            tpMenu.getSelectionModel().select(0);

        } else {

            afficherErreur(
                    BibUtil.getDernierTitreErreur(),
                    BibUtil.getDernierMessageErreur());
        }
    }

    private void supprimer() {

        if (tabLivres.getSelectionModel().getSelectedItem() == null) {

            afficherErreur(
                    "Erreur",
                    "Veuillez sélectionner un livre.");

            return;
        }

        Livre livreSelectionne =
                tabLivres.getSelectionModel().getSelectedItem();

        int id = livreSelectionne.getId();

        if (BibUtil.supprimerLivre(id)) {

            remplir();

        } else {

            afficherErreur(
                    BibUtil.getDernierTitreErreur(),
                    BibUtil.getDernierMessageErreur());
        }
    }

    private void supprimerTout() {

        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);

        dialog.setTitle("Supprimer Tout ?");

        dialog.setHeaderText(
                "Etes vous sûr de supprimer tous les livres ?");

        dialog.showAndWait();

        if (dialog.getResult() == ButtonType.OK) {

            if (BibUtil.supprimerTout()) {

                remplir();

                viderChampsModification();

            } else {

                afficherErreur(
                        BibUtil.getDernierTitreErreur(),
                        BibUtil.getDernierMessageErreur());
            }
        }
    }

    private void rechercher() {

        ObservableList<Livre> list =
                BibUtil.rechercherLivres(titreR.getText());

        tabLivres.setItems(list);
    }

    private void afficherModifier() {

        Livre livreSelectionne =
                tabLivres.getSelectionModel().getSelectedItem();

        if (livreSelectionne != null) {

            cbId.getSelectionModel()
                    .select(Integer.valueOf(livreSelectionne.getId()));

            tfTitreM.setText(livreSelectionne.getTitre());

            cpCouleurM.setValue(livreSelectionne.getCouleur());

            slNbPagesM.setValue(livreSelectionne.getNbPages());

            tpMenu.getSelectionModel().select(2);
        }
    }

    private void actualiserLivre() {

        Integer id = cbId.getValue();

        if (id != null) {

            Livre livre = BibUtil.getLivreById(id);

            if (livre != null) {

                tfTitreM.setText(livre.getTitre());

                cpCouleurM.setValue(livre.getCouleur());

                slNbPagesM.setValue(livre.getNbPages());

                tpMenu.getSelectionModel().select(2);
            }
        }
    }

    private void remplir() {

        ObservableList<Livre> listLivres = BibUtil.getLivres();

        tabLivres.setItems(listLivres);

        listId = listLivres.stream()
                .map(Livre::getId)
                .collect(Collectors.toCollection(
                        FXCollections::observableArrayList));

        refrechCombo = false;

        cbId.setItems(listId);

        refrechCombo = true;
    }

    private void actualiserNbPages() {

        lbNbPages.setText(
                String.format("%.0f", slNbPages.getValue()));
    }

    private void actualiserNbPagesM() {

        lbNbPagesM.setText(
                String.format("%.0f", slNbPagesM.getValue()));
    }

    private void annuler() {

        viderChampsModification();

        tpMenu.getSelectionModel().select(0);
    }

    private void viderChampsModification() {

        cbId.getSelectionModel().clearSelection();

        tfTitreM.clear();

        cpCouleurM.setValue(Color.WHITE);

        slNbPagesM.setValue(0);

        lbNbPagesM.setText("0");

        tabLivres.getSelectionModel().clearSelection();
    }

    public static void afficherErreur(String titre, String message) {

        Alert dialog = new Alert(Alert.AlertType.ERROR);

        dialog.setTitle(titre);

        dialog.setHeaderText(message);

        dialog.showAndWait();
    }
}