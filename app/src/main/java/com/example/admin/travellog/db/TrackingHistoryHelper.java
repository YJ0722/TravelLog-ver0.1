package com.example.admin.travellog.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by massivcode@gmail.com on 2017. 1. 5. 13:01
 */
public class TrackingHistoryHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TrackingHistory.db";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TRACKING_HISTORY_TABLE_FORMAT = "CREATE TABLE IF NOT EXISTS TrackingHistory " +
            "(" +
            " _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "logTitle TEXT NOT NULL, " +
            "averageSpeed INTEGER NOT NULL, " +
            "date REAL NOT NULL, " +
            "distance REAL NOT NULL, " +
            "elapsedTime TEXT NOT NULL, " +
            "pathJson INTEGER NOT NULL " +
            ");";
    private static final String SQL_CREATE_MEMO_TABLE_FORMAT ="CREATE TABLE IF NOT EXISTS Memo " +
            "(" +
            "memoTitle REAL NOT NULL, " +
            "latitude REAL NOT NULL, " +
            "longitude REAL NOT NULL, " +
            "memoContent TEXT NOT NULL, " +
            "date TEXT NOT NULL" +
            ");";
//    private static final String SQL_CREATE_TABLE_FORMAT = "CREATE TABLE IF NOT EXISTS {0} " +
//            "( " +
//            "{1} INTEGER PRIMARY KEY AUTOINCREMENT," +
//            " {2} INTEGER NOT NULL," +
//            " {3} REAL NOT NULL," +
//            " {4} REAL NOT NULL," +
//            " {5} TEXT NOT NULL," +
//            " {6} INTEGER NOT NULL" +
//            " );";
    /**
     * {0} : TrackingHistory
     * {1} : _id
     * {2} : averageSpeed
     * {3} : date
     * {4} : distance
     * {5} : elapsedTime
     * {6} : pathJson
     **/

    private static TrackingHistoryHelper sSingleton = null;


    // 싱글톤 패턴을 구현할 때, 주로 메소드를 getInstance 로 명명합니다.
    // 여러 곳에서 동시에 db 를 열면 동기화 문제가 생길 수 있기 때문에 synchronized 키워드를 이용합니다.
    public static synchronized TrackingHistoryHelper getInstance(Context context) {
        // 객체가 없을 경우에만 객체를 생성합니다.
        if (sSingleton == null) {
            sSingleton = new TrackingHistoryHelper(context);
        }

        // 객체가 이미 존재할 경우, 기존 객체를 리턴합니다.
        return sSingleton;
    }


    // 싱글톤 패턴을 구현할 때, 해당 클래스의 생성자는 private 로 선언하여 외부에서의 직접 접근을 막아야 합니다.
    private TrackingHistoryHelper(Context context) {
        // 파라메터는 다음과 같습니다.
        // Context context : db를 만들거나 열 때 사용함
        // String name : 접근할 데이터베이스의 이름입니다.
        // CursorFactory factory : Cursor 객체를 만들어야 할 때 사용하는데, 기본값은 null 입니다.
        // int version : 데이터베이스의 버전입니다. 이 값을 이용하여 업그레이드나 다운그레이드의 여부를 판단합니다.
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_TRACKING_HISTORY_TABLE_FORMAT);
        Log.d("Tracking_history", "테이블 생성");
        db.execSQL(SQL_CREATE_MEMO_TABLE_FORMAT);
        Log.d("Memo", "테이블 생성");
    }

/*
    public String check() {

        Field[] fields = TrackingHistory.class.getDeclaredFields();
        ArrayList<String> fieldNames = new ArrayList<>();
        fieldNames.add(TrackingHistory.class.getSimpleName());
        fieldNames.add(BaseColumns._ID);
        for (Field field : fields) {
            fieldNames.add(field.getName());
        }

        setStr(fieldNames.toString());

        Object[] args = fieldNames.toArray();
        String sql = MessageFormat.format(SQL_CREATE_TABLE_FORMAT, args);
        setStr(sql);
        return sql;
        //db.execSQL(sql);
    }
*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
