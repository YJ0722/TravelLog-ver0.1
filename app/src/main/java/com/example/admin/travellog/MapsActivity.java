package com.example.admin.travellog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.travellog.models.Coord;
import com.example.admin.travellog.models.Memo;
import com.example.admin.travellog.models.MemoDAO;
import com.example.admin.travellog.models.TrackingHistory;
import com.example.admin.travellog.models.TrackingHistoryDAO;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener {

    private TrackingHistoryDAO mTrackingHistoryDAO;
    private boolean mIsInitialized = false;
    private boolean mIsTracking = false;
    private TextView mDistanceTextView;
    // , mSpeedTextView;
    private TextView mTimeTextView;
    private GoogleMap mMap;
    private ProgressDialog mProgressDialog, mSaveDialog;
    private FloatingActionButton mToggleButton, mStopButton;
    private LinkedList<Location> mHistory = new LinkedList<>();
    private double mCurrentSpeed;
    private long mTime;
    private float mDistance;
    private Location mCurrentLocation;
    private Timer mTimer;
    private PolylineOptions mPolyLineOptions;

    private int recordedPathSize;

    private FloatingActionButton mMemoButton, mExpenseButton;
    private MemoDAO memoDAO;

    private TrackingHistory trackingHistory;
    private Memo memoBeans;
    private ArrayList<Memo> memoArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initDialog();

        mTrackingHistoryDAO = new TrackingHistoryDAO(getApplicationContext());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        updateMap();

        mPolyLineOptions = new PolylineOptions();
        mPolyLineOptions.color(Color.parseColor("#8BC34A"));
        mPolyLineOptions.width(5);

        mTimer = new Timer();
        initViews();

    }

    public void  updateMap() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initDialog() {
        mProgressDialog = new ProgressDialog(MapsActivity.this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("위치 로딩");
        mProgressDialog.setMessage("현재 위치를 불러오고 있습니다.");
        mProgressDialog.show();

        mSaveDialog = new ProgressDialog(MapsActivity.this);
        mSaveDialog.setCancelable(false);
        mSaveDialog.setTitle("트래킹 기록 저장");
        mSaveDialog.setMessage("경로 데이터를 저장하고 있습니다.");
    }

    private void initViews() {
        mDistanceTextView = (TextView) findViewById(R.id.distance_tv);
        //mSpeedTextView = (TextView) findViewById(R.id.speed_tv);
        mTimeTextView = (TextView) findViewById(R.id.time_tv);

        mToggleButton = (FloatingActionButton) findViewById(R.id.toggle_fab);
        mToggleButton.setOnClickListener(mOnToggleButtonClickListener);
        mStopButton = (FloatingActionButton) findViewById(R.id.stop_fab);
        mStopButton.setOnClickListener(mOnStopButtonClickListener);
        mMemoButton = (FloatingActionButton) findViewById(R.id.memo_fab);
        mMemoButton.setOnClickListener(mOnMemoButtonClickListener);
        mExpenseButton = (FloatingActionButton) findViewById(R.id.expense_fab);
        mExpenseButton.setOnClickListener(mOnExpenseButtonOnClickListener);
    }

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
            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(memoBeans.getMemoTitle()));//.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 18));
            //memoArr.add(memoBeans);

        }
        //mMap.addMarker(new MarkerOptions().position(new LatLng(123, 123 )).title("메모이름")).showInfoWindow();
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
        System.out.println("onMapReady");
        mMap = googleMap;
        mMap.setOnMyLocationChangeListener(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        /////////
        addMarker();
        ////////

        //정보창 클릭 리스너
        googleMap.setOnInfoWindowClickListener(infoWindowClickListener);


        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    @Override
    public void onMyLocationChange(Location location) {
        if (!mIsInitialized) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18));
            mIsInitialized = true;

            mProgressDialog.dismiss();
        }

        if (mIsTracking) {
            mCurrentLocation = location;
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mCurrentSpeed = mCurrentLocation.getSpeed() * 3.6;
                    if (mCurrentSpeed > 0) {
                        mHistory.add(mCurrentLocation);
                        mPolyLineOptions.add(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
                        int size = mHistory.size();
                        if (size > 1) {
                            Location previousLocation = mHistory.get(size - 2);
                            // 누적 이동거리 (미터)
                            mDistance += mCurrentLocation.distanceTo(previousLocation);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDistanceTextView.setText(FormatUtil.getDouble(mDistance) + " m");
                                //mSpeedTextView.setText(FormatUtil.getDouble(mCurrentSpeed) + " km/h");
                                mMap.clear();
                                mMap.addPolyline(mPolyLineOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 18));
                            }
                        });
                    }

                    mTime += 500;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTimeTextView.setText(FormatUtil.getTime(mTime));
                        }
                    });
                }
            }, 500);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        final Memo memo;

        if(requestCode==1){
//            if(resultCode==1){
            if(resultCode==RESULT_OK){

                if(recordedPathSize > 1) {
                    //데이터 받기
                    String logTitle = data.getStringExtra("title");

                    Toast.makeText(MapsActivity.this, logTitle, Toast.LENGTH_SHORT).show();

                    trackingHistory.setTitle(logTitle);

                    Toast.makeText(MapsActivity.this, trackingHistory.toString(), Toast.LENGTH_SHORT).show();

                    ////
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mTrackingHistoryDAO.save(trackingHistory);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mSaveDialog.dismiss();
                                    Toast.makeText(MapsActivity.this, "트래킹 기록이 저장되었습니다!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }
                    }).start();
                    ////
                } else {
                    Toast.makeText(MapsActivity.this, "움직임이 적어 저장하지 않습니다!", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        }

        // 메모 저장 후 돌아올 때
        if(requestCode==2){
//            if(resultCode==1){
            if(resultCode==RESULT_OK){

                String memoTitle = data.getStringExtra("memoTitle");
                String memoContent = data.getStringExtra("memoContent");
                double memoLatitude = data.getDoubleExtra("memoLatitude", 0);
                double memoLongitude = data.getDoubleExtra("memoLongitude", 0);

                Toast.makeText(MapsActivity.this, "t: " + memoTitle + "\nc:" + memoContent, Toast.LENGTH_SHORT).show();

                memo = new Memo(memoTitle, memoLatitude, memoLongitude, memoContent, System.currentTimeMillis());

                // TODO : memo 객체 DB에 save
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        memoDAO.save(memo);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mSaveDialog.dismiss();
                                Toast.makeText(MapsActivity.this, "메모가 저장되었습니다!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }

            onResume();
        }

        // 경비 저장 후 돌아올 때
        if(requestCode==3){
//            if(resultCode==1){
            if(resultCode==RESULT_OK){

            }

            onResume();
        }
    }

    //정보창 클릭 리스너
    GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
//            String markerId = marker.getId();
//            Toast.makeText(MapsActivity.this, "정보창 클릭 Marker ID : "+markerId, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MapsActivity.this, MemoViewActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener mOnToggleButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mIsTracking) {
                mIsTracking = false;
                mToggleButton.setImageResource(R.drawable.ic_start);
            } else {
                mIsTracking = true;
                mToggleButton.setImageResource(R.drawable.ic_pause);

            }
        }
    };

    private View.OnClickListener mOnStopButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mIsTracking) {
                mOnToggleButtonClickListener.onClick(mToggleButton);
                List<LatLng> recordedPath = mPolyLineOptions.getPoints();
                recordedPathSize = recordedPath.size();

                    //mSaveDialog.show();
                    List<Coord> coords = new ArrayList<>();
                    for (LatLng each : recordedPath) {
                        coords.add(new Coord(each.latitude, each.longitude));
                    }

                    String pathJson = new Gson().toJson(coords);
                    double time = mTime / 1000.0;
                    double averageSpeed = mDistance / time;
                    trackingHistory = new TrackingHistory(mTime, averageSpeed, mDistance, pathJson, System.currentTimeMillis());

                    //데이터 담아서 팝업(액티비티) 호출
                    Intent intent = new Intent(MapsActivity.this, InputTitlePopupActivtiy.class);
                    intent.putExtra("trackingHistory", trackingHistory);
                    startActivityForResult(intent, 1);


                    //// 잠깐 닫아놓기!
                    /*
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mTrackingHistoryDAO.save(trackingHistory);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mSaveDialog.dismiss();
                                    Toast.makeText(MapsActivity.this, "트래킹 기록이 저장되었습니다!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }
                    }).start();
*/

            } else {
                finish();
            }
        }
    };

    // 메모 버튼 클릭리스너
    private View.OnClickListener mOnMemoButtonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // 현재 위치 위도, 경도
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lon = String.valueOf(mCurrentLocation.getLongitude());
            Log.d(lat, lon);
            Toast.makeText(MapsActivity.this, "위도 : " + lat + ", 경도 : " + lon, Toast.LENGTH_SHORT).show();
            LatLng location = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            //mMap.addMarker(new MarkerOptions().position(location));

            // TODO : 팝업 연결 후
//            // 현재 위치는 intent에 담아 MemoActivity로 이동
//            Intent intent = new Intent(MapsActivity.this, MemoActivtiy.class);
//            intent.putExtra("location", location);
//            startActivity(intent);

            //데이터 담아서 팝업(액티비티) 호출
            Intent intent = new Intent(MapsActivity.this, MemoPopupActivity.class);
            intent.putExtra("memoLatitude", mCurrentLocation.getLatitude());
            intent.putExtra("memoLongitude", mCurrentLocation.getLongitude());
            startActivityForResult(intent, 2);
        }
    };

    // 경비 버튼 클릭 리스너
    private View.OnClickListener mOnExpenseButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //데이터 담아서 팝업(액티비티) 호출
            Intent intent = new Intent(MapsActivity.this, ExpensePopupActivity.class);
//            intent.putExtra("memoLatitude", mCurrentLocation.getLatitude());
//            intent.putExtra("memoLongitude", mCurrentLocation.getLongitude());
            startActivityForResult(intent, 3);
        }
    };
}
