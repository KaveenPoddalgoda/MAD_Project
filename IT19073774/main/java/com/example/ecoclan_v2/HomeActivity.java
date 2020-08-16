package com.example.ecoclan_v2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private GoogleMap mMap;
    GoogleApiClient client;
    LocationRequest request;
    LatLng startLatLng;
    Double Lat;
    Double Lng;
    FirebaseAuth auth;
    FirebaseFirestore db;
    int cameraSet = 0;
    HashMap<String, Marker> hashMapMarker = new HashMap<>();
    TextView uName, uType;
    String current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        uName = findViewById(R.id.UserName);
        uType = findViewById(R.id.UserType);

        String current_user = auth.getCurrentUser().getEmail();
        CollectionReference users = db.collection("Users");
        Query query = users.whereEqualTo("Email", current_user);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (e !=null) {}
                for (DocumentChange documentChange : documentSnapshots.getDocumentChanges())
                {
                    String   firstName =  documentChange.getDocument().getData().get("FirstName").toString();
                    String   lastName =  documentChange.getDocument().getData().get("LastName").toString();
                    uName.setText(firstName + " " + lastName);
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        client = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        client.connect();
        cameraSet = 0;

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // inflate the layout of the popup window
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_window, null);

                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken

                popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, -360);

                final String markerTitle = marker.getTitle();
                Button but=popupView.findViewById(R.id.detail);
                but.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent i = new Intent(HomeActivity.this, ResourceDetailActivity.class);
                        i.putExtra("resID", markerTitle);
                        startActivity(i);
                    }
                });

                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });

                return false;
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null){
            Toast.makeText(getApplicationContext(), "Location Could Not Be Found!", Toast.LENGTH_SHORT).show();
        } else {
            Marker marker;
            if (cameraSet == 0) {
                startLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                Lat = location.getLatitude();
                Lng = location.getLongitude();
                //mMap.clear();
                final MarkerOptions options = new MarkerOptions();
                options.position(startLatLng);
                options.title("Current Position");
                options.snippet("Description");
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                marker = mMap.addMarker(options);
                hashMapMarker.put("myLocation",marker);

                /*options.position(new LatLng(6.96662319943162, 79.86848164433901));
                options.title("Trash");
                options.snippet("Description");
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                mMap.addMarker(options);
                options.position(new LatLng(6.9666912545700015, 79.86778264677211));
                options.title("Trash");
                options.snippet("Description");
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                mMap.addMarker(options);
                options.position(new LatLng(6.964932443304884, 79.8695376538585));
                options.title("Trash");
                options.snippet("Description");
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                mMap.addMarker(options);*/

                db.collection("Resources").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                        if (e !=null) {}
                        for (DocumentChange documentChange : documentSnapshots.getDocumentChanges())
                        {
                            Double lati =  Double.parseDouble(documentChange.getDocument().getData().get("Latitude").toString());
                            Double lngi =  Double.parseDouble(documentChange.getDocument().getData().get("Longitude").toString());
                            String Material = documentChange.getDocument().getData().get("Material").toString();
                            String resourceID = documentChange.getDocument().getData().get("resourceID").toString();

                            MarkerOptions options2 = new MarkerOptions();
                            options2.position(new LatLng(lati, lngi));
                            options2.title(resourceID + " :: " + Material);
                            options2.snippet(Double.toString(lati) + ", " + Double.toString(lngi));

                            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.res_marker);
                            Bitmap b=bitmapdraw.getBitmap();
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 125, 120, false);
                            options2.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                            //options2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));


                            mMap.addMarker(options2);
                        }
                    }
                });

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng,17));
                cameraSet = 1;
            } else {
                startLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                Lat = location.getLatitude();
                Lng = location.getLongitude();
                //mMap.clear();
                MarkerOptions options = new MarkerOptions();
                options.position(startLatLng);
                options.title("Current Position");
                options.snippet("Description");
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                /////
                marker = hashMapMarker.get("myLocation");
                marker.remove();
                hashMapMarker.remove("myLocation");
                ////
                marker = mMap.addMarker(options);
                hashMapMarker.put("myLocation",marker);
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        request = new LocationRequest().create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void centerCameraView(View v) {
        startLatLng = new LatLng(Lat, Lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng,17));
    }

    public void sigOutUser (View v) {
        auth.signOut();
        Intent i = new Intent (HomeActivity.this, MainActivity.class);
        startActivity(i);
        finish();
        Toast.makeText(getApplicationContext(), "User Logged Out!", Toast.LENGTH_SHORT).show();
    }

    public void viewProfile (View v) {
        Intent i = new Intent (HomeActivity.this, UserProfileActivity.class);
        startActivity(i);
    }

    public void ItemLog (View v) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_collected_items, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        Button but=popupView.findViewById(R.id.sellBtn);
        but.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, SellResourceActivity.class);
                startActivity(i);
            }
        });

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public void infoLog (View v) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.info, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

}