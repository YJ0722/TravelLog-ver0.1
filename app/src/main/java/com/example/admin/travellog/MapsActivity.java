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
import com.example.admin.travellog.models.ExpenseHistory;
import com.example.admin.travellog.models.ExpenseHistoryDAO;
import com.example.admin.travellog.models.Memo;
import com.example.admin.travellog.models.MemoDAO;
import com.example.admin.travellog.models.TrackingHistory;
import com.example.admin.travellog.models.TrackingHistoryDAO;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
    private ExpenseHistory expenseHistory;
    private ArrayList<Memo> memoArr;
    private ArrayList<ExpenseHistory> expenseArr;
    private ExpenseHistoryDAO expenseHistoryDAO;

    private int travelNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initDialog();

        mTrackingHistoryDAO = new TrackingHistoryDAO(getApplicationContext());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        updateMap();

        mPolyLineOptions = new PolylineOptions();
        mPolyLineOptions.color(Color.parseColor("#3949AB"));
        mPolyLineOptions.width(18);

        mTimer = new Timer();
        initViews();

        Intent intent = getIntent();
        travelNo = Integer.parseInt(intent.getStringExtra("travelNo"));
        Log.d("MapsActivity에서 확인22! : " , String.valueOf(travelNo));

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

    // 메모 마커 추가
    private void addMemoMarker() {
        // memoDB에서 가져온 데이터를 받을 memoArrayList 설정
        memoArr = new ArrayList<>();

        memoDAO = new MemoDAO(getApplicationContext());

        // 메모 전체 목록 불러오기
        Cursor cursor = memoDAO.findAll();
        int recordCount = cursor.getCount();    // 저장된 메모 개수

        for (int i = 0; i < recordCount; i++) {
            cursor.moveToNext();
            int travel_no = cursor.getInt(1);
            String memoTitle = cursor.getString(2);
            double latitude = cursor.getDouble(3);
            double longitude = cursor.getDouble(4);
            //String latitudeStr = cursor.getString(0);
            //String longitudeStr = cursor.getString(1);
            //String memoContent = cursor.getString(3);
            //Long date = cursor.getLong(3);

            //double latitude = Double.parseDouble(latitudeStr);
            //double longitude = Double.parseDouble(longitudeStr);
            memoBeans = new Memo(memoTitle, latitude, longitude);
            Log.d("@@@@@@@", "Recode #" + i + " : " + memoBeans.getMemoTitle() + ", " + memoBeans.getLatitude() + ", " +
                    memoBeans.getLongitude());

            // 해당 memo 객체에 해당하는 마크 추가
            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title(memoBeans.getMemoTitle()));//.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 18));
            //memoArr.add(memoBeans);

        }
    }

        // 경비 마커 추가
        private void addExpenseMarker() {
            // memoDB에서 가져온 데이터를 받을 memoArrayList 설정
            expenseArr = new ArrayList<>();

            expenseHistoryDAO = new ExpenseHistoryDAO(getApplicationContext());

            // 메모 전체 목록 불러오기
            Cursor cursor = expenseHistoryDAO.findAll();
            int recordCount = cursor.getCount();    // 저장된 메모 개수

            for(int i=0; i<recordCount; i++) {
                cursor.moveToNext();
                String expenseTitle = cursor.getString(0);
                int expenseType = cursor.getInt(1);
                int cost = cursor.getInt(2);
                int balance = cursor.getInt(3);
                double latitude = cursor.getDouble(4);
                double longitude = cursor.getDouble(5);
                long date = cursor.getLong(6);
                //String latitudeStr = cursor.getString(0);
                //String longitudeStr = cursor.getString(1);
//                String memoContent = cursor.getString(3);

                //double latitude = Double.parseDouble(latitudeStr);
                //double longitude = Double.parseDouble(longitudeStr);
                expenseHistory = new ExpenseHistory(0, expenseTitle, expenseType, cost, balance, latitude, longitude, date);
                Log.d("@@@@@@@", "Recode #" + i + " : " + expenseHistory.getExpenseTitle() + "\n " + expenseHistory.getExpenseType() + ",\n" + expenseHistory.getCost() + "\n" + expenseHistory.getBalance() +
                                "\n" + expenseHistory.getLatitude() + "\n" + expenseHistory.getLongitude() + "\n" + expenseHistory.getDate());

                // 해당 expense 객체에 해당하는 마크 추가
                mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).title(expenseHistory.getExpenseTitle()));//.showInfoWindow();
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


        addMemoMarker();
        //addExpenseMarker();

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
            Log.d("&&&&&&&&&&&&&", "&&&&&&&&");
            mCurrentLocation = location;
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Log.d("****", "*****");
                    // todo : 나중에 해제! 불편해서 잠시 주석
                    /*
                    mCurrentSpeed = mCurrentLocation.getSpeed() * 3.6;
                    if (mCurrentSpeed > 0) {
                    */
                        Log.d("22222", "22222");
                        mHistory.add(mCurrentLocation);
                        Log.d("location : ", mCurrentLocation.getLatitude() + ", " + mCurrentLocation.getLongitude());
                        mPolyLineOptions.add(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
                        int size = mHistory.size();
                        if (size > 1) {
                            Log.d("3333", "3333");
                            Location previousLocation = mHistory.get(size - 2);
                            // 누적 이동거리 (미터)
                            mDistance += mCurrentLocation.distanceTo(previousLocation);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("dddddddd", "dddddddddddd");
                                mDistanceTextView.setText(FormatUtil.getDouble(mDistance) + " m");
                                //mSpeedTextView.setText(FormatUtil.getDouble(mCurrentSpeed) + " km/h");
                                mMap.clear();
                                mMap.addPolyline(mPolyLineOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 18));
                            }
                        });
                    //}

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

                    trackingHistory.setTravelNo(travelNo);
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
                //String memoContent = data.getStringExtra("memoContent");
                double memoLatitude = data.getDoubleExtra("memoLatitude", 0);
                double memoLongitude = data.getDoubleExtra("memoLongitude", 0);

                memo = new Memo(travelNo, memoTitle, memoLatitude, memoLongitude, System.currentTimeMillis());


                Toast.makeText(MapsActivity.this, memo.toString(), Toast.LENGTH_SHORT).show();


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

            updateMap();
            //onResume();
        } else {
            onResume();
        }

        // 경비 저장 후 돌아올 때
        // position 1 : 수입 (+), position 2 : 지출 (-)
        // TODO : ExpenseDAO
        // 1) position 1 : DAO - sum

        // 2) position 2 : DAO - sub
        if(requestCode==3){

            String expenseTitle;
            int type, cost;

//            if(resultCode==1){
            if(resultCode==RESULT_OK){

                expenseTitle = data.getStringExtra("expenseTitle");
                type = data.getIntExtra("type", 0);
                cost = data.getIntExtra("cost", 0);
                double expenseLatitude = data.getDoubleExtra("expenseLatitude", 0);
                double expenseLongitude = data.getDoubleExtra("expenseLongitude", 0);

                Toast.makeText(getBaseContext(), "expenseTitle: " + expenseTitle + "\ntype: " + type + "\ncost: " + cost, Toast.LENGTH_SHORT).show();

                //TODO : 수정 필요. 해당 트래킹 기록에 해당하는 경비만 보여줄 수 있게 수정해야한다
                expenseHistory = new ExpenseHistory(0, expenseTitle, type, cost, expenseLatitude, expenseLongitude, System.currentTimeMillis());

                // expenseHistory 객체 DB에 save
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        expenseHistoryDAO.save(expenseHistory);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mSaveDialog.dismiss();
                                Toast.makeText(MapsActivity.this, "비용 정보가 저장되었습니다!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }

            onResume();
        } else {
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
            //startActivityForResult(intent, 4);
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
                    Log.d("qqqqqqqqqq", pathJson);
                    double time = mTime / 1000.0;
                    double averageSpeed = mDistance / time;
                    trackingHistory = new TrackingHistory(travelNo, System.currentTimeMillis(), mDistance, mTime, pathJson);
                    Log.d("MMMMMMMMMMMMMMM", trackingHistory.toString());

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
            intent.putExtra("expenseLatitude", mCurrentLocation.getLatitude());
            intent.putExtra("expenseLongitude", mCurrentLocation.getLongitude());

            startActivityForResult(intent, 3);
        }
    };
}

