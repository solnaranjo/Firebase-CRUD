package com.example.super_firebase_app.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Artist {

    private String id;
    private String name;
    private String genre;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public Artist() {


    }

    public Artist(String id, String name, String genre) {

        this.id = id;
        this.name = name;
        this.genre = genre;

    }

    public String getId() {

        return id;

    }

    public void setId(String id) {

        this.id = id;

    }

    public String getName() {

        return name;

    }

    public void setName(String name, String original_name, String id, DatabaseReference databaseReference, String path) {

        //databaseReference.child("Artists").child(id).child(original_name).setValue(name);
        databaseReference.child(path + "/name").setValue(name);
        this.name = name;

    }

    public String getGenre() {

        return genre;

    }

    public void setGenre(String genre, String original_genre, String id, DatabaseReference databaseReference, String path) {

        //databaseReference.child("Artists").child(id).child(original_genre).setValue(genre);
        databaseReference.child(path + "/genre").setValue(genre);
        this.genre = genre;

    }

}
