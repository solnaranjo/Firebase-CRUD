package com.example.super_firebase_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.super_firebase_app.model.Artist;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/* Firebase Real-Time es una base de datos que guarda datos en la nube
* en un formato JSON, sincronizados en tiempo real.
* Recibe actualizaciones automaticamente.
*  */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DatabaseReference databaseReference;
    private static final String ARTIST_NODE = "Artists";

    private EditText edtId;
    private EditText edtGenre;
    private EditText edtName;

    private ListView lstArtist;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> artistNames;
    private List<Artist> artists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtId = findViewById(R.id.edtID);
        edtGenre = findViewById(R.id.edtValue);
        edtName = findViewById(R.id.edtValueChange);

        lstArtist = findViewById(R.id.lstArtist);
        artists = new ArrayList<>();
        artistNames = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, artistNames);
        lstArtist.setAdapter(arrayAdapter);


        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference(); // ref
        databaseReference.child(ARTIST_NODE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                artistNames.clear();
                artists.clear();
                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot:dataSnapshot.getChildren()) {

                        Artist artist = snapshot.getValue(Artist.class);
                        //assert artist != null;
                        Log.w(TAG, "Artist Name: " + artist.getName());
                        artistNames.add(artist.getName());
                        artists.add(artist);

                    }

                }

                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void deleteArtisdt(View view) {

        //Toast.makeText(MainActivity.this, "Tamaño de Artists: " + artists.size(), Toast.LENGTH_LONG).show();
        //try {

            //deleteArtist();

        /*} catch (Exception exception) {

            Toast.makeText(MainActivity.this, "Borrar elemento fallo: " + exception.toString(), Toast.LENGTH_LONG).show();

        }*/


    }

    public void deleteArtist(View view) {

        int position;
        String artistName = edtName.getText().toString();

        Log.w(TAG, "Nombre del artista: " + artistName);

        for (int i = 0; i < artistNames.size(); i++) {

            String artist1 = artistNames.get(i);
            position = i;
            Log.w(TAG, artist1);
            Log.w(TAG, "" + position);

            if (artist1.equals(artistName)) {

                Log.w(TAG, "Exito");
                String idArtist = artists.get(position).getId();
                //artists.remove(position);
                artistNames.remove(position);

                databaseReference.child(ARTIST_NODE).child(idArtist).removeValue();
                break;

            }
        }




    }

    public void createArtist(View view) {

        Artist artist;

        if (edtId.getText().toString().equals("") && edtGenre.getText().toString().equals("") && edtName.getText().toString().equals("")) {

            artist = new Artist(databaseReference.push().getKey(), "Garbage", "Rock");
            databaseReference.child(ARTIST_NODE).child(artist.getId()).setValue(artist);
            Log.w(TAG, "Creacion Exitosa");

        } else if (edtId.getText().toString().equals("")) {

            artist = new Artist(databaseReference.push().getKey(), edtName.getText().toString(), edtGenre.getText().toString());
            databaseReference.child(ARTIST_NODE).child(artist.getId()).setValue(artist);
            Log.w(TAG, "Creacion Exitosa");

        }

    }



    public void updateArtist(View view) {

        String artistId = edtId.getText().toString();
        String artistGenre = edtGenre.getText().toString();
        String artistName = edtName.getText().toString();
        String path = ARTIST_NODE + "/" + artistId + "/genre";

        Log.w(TAG, "Tamaño de Artistas: " + artists.size());

        for (int i = 0; i < artists.size(); i++) {

            Artist artist2 = artists.get(i);

            if (artistId.equals(artist2.getId())) {

                Log.w(TAG, "Busqueda Exitosa");

                if (!artistGenre.equals("") && !artistGenre.equals(artist2.getGenre())) {

                    artist2.setGenre(artistGenre, artist2.getGenre(), artistId, databaseReference, ARTIST_NODE + "/" + artistId);
                    Log.w(TAG, "Cambio exitoso");


                } else if (!artistName.equals("") && !artistGenre.equals(artist2.getGenre())) {

                    artist2.setName(artistName, artist2.getName(), artistId, databaseReference, ARTIST_NODE + "/" + artistId);
                    Log.w(TAG, "Cambio exitoso");

                }

                break;

            }

        }



    }

    public void getArtist(View view) {

        String artistName = edtName.getText().toString();
        String artistGenre = edtGenre.getText().toString();

        for (int i = 0; i < artists.size(); i++) {

            if (artists.get(i).getName().equals(artistName) && artists.get(i).getGenre().equals(artistGenre)) {

                edtId.setText(artists.get(i).getId());
                edtGenre.setText(artists.get(i).getGenre());
                edtName.setText(artists.get(i).getName());

            }

        }

    }

}



/*lstArtist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Log.w(TAG, "Posicion: " + position);
                Log.w(TAG, "Size: " + artists.size());

                if (artists.get(position).getId() != null && artists.get(position).getName() != null && artists.get(position).getGenre() != null) {

                    edtId.setText(artists.get(position).getId());
                    edtGenre.setText(artists.get(position).getGenre());
                    edtName.setText(artists.get(position).getGenre());
                    Log.w(TAG, "Verdadero" + position);

                } else {

                    Log.w(TAG, "Falso: " + position);

                }

            }
        });*/

        /*lstArtist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String idArtist = artists.get(position).getId();
                artists.remove(position);
                artistNames.remove(position);

                databaseReference.child(ARTIST_NODE).child(idArtist).removeValue();

                return false;

            }
        });*/