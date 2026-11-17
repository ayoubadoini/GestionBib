package com.example.gestionbib.data;

import javafx.scene.paint.Color;

public class Livre {
    private int id;
    private String titre;
    private Color couleur;
    private int nbPages;

    public Livre(int id, String titre, String couleurStr, int nbPages) {
        this.id = id;
        this.titre = titre;
        // Convertir le String "0x008000ff" en Color
        try {
            this.couleur = Color.web(couleurStr);
        } catch (Exception e) {
            this.couleur = Color.WHITE;
        }
        this.nbPages = nbPages;
    }

    public int getId() { return id; }
    public String getTitre() { return titre; }
    public Color getCouleur() { return couleur; }
    public int getNbPages() { return nbPages; }

    public void setId(int id) { this.id = id; }
    public void setTitre(String titre) { this.titre = titre; }
    public void setCouleur(Color couleur) { this.couleur = couleur; }
    public void setNbPages(int nbPages) { this.nbPages = nbPages; }
}