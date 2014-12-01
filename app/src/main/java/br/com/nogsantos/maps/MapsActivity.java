package br.com.nogsantos.maps;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Documentação oficial:
 *
 * https://developers.google.com/maps/documentation/android/map?hl=pt#configuring_initial_state
 * https://developers.google.com/maps/documentation/android/
 */
public class MapsActivity extends FragmentActivity {
    /*
     * Might be null if Google Play services APK is not available.
     */
    private GoogleMap mMap;
    /*
     * Monitorar a localização do disposotivo
     */
    private PolylineOptions polylineOptions;
    private Polyline polyline;
    /**
     * Create
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        /*
         * Click
         */
        mMap.setOnMapClickListener(mapClick);
        /*
         * info window
         */
        mMap.setInfoWindowAdapter(infoWindow);
        /*
         * monitorando a localização
         */
        mMap.setOnMyLocationChangeListener(myLocationChange);
        polylineOptions = new PolylineOptions();
    }
    /**
     * Resume
     */
    @Override
    protected void onResume() {
        super.onResume();
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            GooglePlayServicesUtil.getErrorDialog(resultCode, this, 1).show();
        }
        setUpMapIfNeeded();
    }
    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only
     * ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }
    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location arg0) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("It's Me!"));
            }
        });
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }
    /**
     * Maker, marcar pontos no mapa
     */
    private GoogleMap.OnMapClickListener mapClick = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng point) {
            if(mMap != null){
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(point);
                markerOptions.title("Click em " + point);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));
                mMap.addMarker(markerOptions);

            }
        }
    };
    /**
     * Customizar a janela de informação do maker
     */
    private GoogleMap.InfoWindowAdapter infoWindow = new GoogleMap.InfoWindowAdapter() {
        @Override
        public View getInfoWindow(Marker arg0) {
            return null;
        }
        @Override
        public View getInfoContents(Marker marker) {
            View view       = getLayoutInflater().inflate(R.layout.info_window_custom, null);
            LatLng latLng   = marker.getPosition();
            ImageView image = (ImageView) view.findViewById(R.id.imageViewIcone);
            image.setImageResource(R.drawable.ic_launcher);
            TextView tvTitulo = (TextView) view.findViewById(R.id.textViewInfoWindowTitulo);
            tvTitulo.setText("Posição");
            TextView tvTexto = (TextView) view.findViewById(R.id.textViewInfoWindowTexto);
            tvTexto.setText("Latitude:" + latLng.latitude + "\nLongitude: " + latLng.longitude);
            return view;
        }
    };
    /**
     * Utilizado para criar a linha que indicará o caminho que o dispositivo percorre
     */
    private GoogleMap.OnMyLocationChangeListener myLocationChange = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            if(polylineOptions != null){
                if(polyline != null){
                    polyline.remove();
                    polyline = null;
                }
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                polylineOptions.add(latLng);
                polylineOptions.color(Color.RED);
                polyline = mMap.addPolyline(polylineOptions);
            }
        }
    };
}
