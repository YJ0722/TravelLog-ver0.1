package com.example.admin.travellog;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.admin.travellog.models.Coord;
import com.example.admin.travellog.models.Memo;
import com.example.admin.travellog.models.MemoDAO;
import com.example.admin.travellog.models.TrackingHistory;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ViewerActivity extends FragmentActivity implements OnMapReadyCallback {

    private MemoDAO memoDAO;
    private ArrayList<Memo> memoArr;
    private Memo memoBeans;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = getIntent();
        TrackingHistory history = intent.getParcelableExtra("data");

        String pathJson = history.getPathJson();
        ArrayList<Coord> pathList = new Gson().fromJson(pathJson, new TypeToken<ArrayList<Coord>>() {
        }.getType());

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.parseColor("#8BC34A"));
        polylineOptions.width(5);

        List<LatLng> latLngList = new ArrayList<>();
        for (Coord coord : pathList) {
            System.out.println(coord);
            latLngList.add(new LatLng(coord.getLatitude(), coord.getLongitude()));
        }
        polylineOptions.addAll(latLngList);

        int size = latLngList.size();
        if (size > 2) {
            LatLng firstLocation = latLngList.get(0);
            LatLng lastLocation = latLngList.get(size - 1);
            mMap.addMarker(new MarkerOptions().position(firstLocation).title("시작"));
            mMap.addMarker(new MarkerOptions().position(lastLocation).title("종료"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 18));
        }

        // 메모 마커 표시
        addMarker();

        mMap.addPolyline(polylineOptions);

        //정보창 클릭 리스너
        googleMap.setOnInfoWindowClickListener(infoWindowClickListener);
    }


    //정보창 클릭 리스너
    GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
//            String markerId = marker.getId();
//            Toast.makeText(MapsActivity.this, "정보창 클릭 Marker ID : "+markerId, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ViewerActivity.this, MemoViewActivity.class);
            startActivity(intent);
        }
    };


    private void addMarker() {
        // memoDB에서 가져온 데이터를 받을 memoArrayList 설정
        memoArr = new ArrayList<>();

        memoDAO = new MemoDAO(getApplicationContext());

        // 메모 전체 목록 불러오기
        Cursor cursor = memoDAO.findAll();
        int recordCount = cursor.getCount();    // 저장된 메모 개수

        for(int i=0; i<recordCount; i++) {
            cursor.moveToNext();
            String memoTitle = cursor.getString(0);
            double latitude = cursor.getDouble(1);
            double longitude = cursor.getDouble(2);
            //String latitudeStr = cursor.getString(0);
            //String longitudeStr = cursor.getString(1);
            String memoContent = cursor.getString(3);
            //Long date = cursor.getLong(3);

            //double latitude = Double.parseDouble(latitudeStr);
            //double longitude = Double.parseDouble(longitudeStr);
            memoBeans = new Memo(memoTitle, latitude, longitude, memoContent, 0);
            Log.d("@@@@@@@", "Recode #" + i + " : " + memoBeans.getMemoTitle() + ", " + memoBeans.getLatitude() + ", " +
                    memoBeans.getLongitude() + ", " + memoBeans.getMemoContent());

            // 해당 memo 객체에 해당하는 마크 추가
            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(memoBeans.getMemoTitle()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 18));

        }
    }
}
