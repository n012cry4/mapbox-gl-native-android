package com.mapbox.mapboxsdk.testapp.activity.style;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.FillExtrusionLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.testapp.R;
import com.mapbox.geojson.Polygon;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionHeight;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionOpacity;

/**
 * Test activity showcasing fill extrusions
 */
public class FillExtrusionActivity extends AppCompatActivity {

  private MapView mapView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fill_extrusion_layer);

    mapView = findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.attachLifeCycle(this);
    mapView.getMapAsync(mapboxMap -> {
      mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {
        List<List<Point>> lngLats = Collections.singletonList(
          Arrays.asList(
            Point.fromLngLat(5.12112557888031, 52.09071040847704),
            Point.fromLngLat(5.121227502822875, 52.09053901776669),
            Point.fromLngLat(5.121484994888306, 52.090601641371805),
            Point.fromLngLat(5.1213884353637695, 52.090766439912635),
            Point.fromLngLat(5.12112557888031, 52.09071040847704)
          )
        );

        Polygon domTower = Polygon.fromLngLats(lngLats);
        GeoJsonSource source = new GeoJsonSource("extrusion-source", domTower);
        style.addSource(source);

        style.addLayer(
          new FillExtrusionLayer("extrusion-layer", source.getId())
            .withProperties(
              fillExtrusionHeight(40f),
              fillExtrusionOpacity(0.5f),
              fillExtrusionColor(Color.RED)
            )
        );

        mapboxMap.animateCamera(
          CameraUpdateFactory.newCameraPosition(
            new CameraPosition.Builder()
              .target(new LatLng(52.09071040847704, 5.12112557888031))
              .tilt(45.0)
              .zoom(18)
              .build()
          ),
          10000
        );
      });
    });
  }


  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

}
