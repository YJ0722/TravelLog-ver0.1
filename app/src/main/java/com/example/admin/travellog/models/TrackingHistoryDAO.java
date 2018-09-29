package com.example.admin.travellog.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.example.admin.travellog.db.TrackingHistoryHelper;

/**
 * Created by massivcode@gmail.com on 2017. 1. 5. 13:59
 */

public class TrackingHistoryDAO {
    private TrackingHistoryHelper mHelper;

    public TrackingHistoryDAO(Context context) {
        this.mHelper = TrackingHistoryHelper.getInstance(context);
    }

    /**
     * 메모를 테이블에 추가합니다.
     *
     * @param data : 추가할 메모
     * @return : 성공 유무
     */
    public boolean save(TrackingHistory data) {
        // db는 읽기만 가능한 것과 읽고 쓸 수 있는 것이 있습니다.
        // 삽입은 쓰는 것이므로 getWritableDatabase() 메소드를 이용해야 합니다.
        SQLiteDatabase db = mHelper.getWritableDatabase();

        // 삽입할 데이터는 ContentValues 객체에 담깁니다.
        // 맵과 동일하게 key 와 value 로 데이터를 저장합니다.
        ContentValues values = new ContentValues();

        // 삽입할 메모의 제목, 내용, 시간을 ContentValues 에 넣습니다.
        // 메모의 id 는 AUTO INCREMENT 이므로 추가하지 않습니다.
        values.put("logTitle", data.getTitle());
        values.put("elapsedTime", data.getElapsedTime());
        // TODO : 추후 삭제 필요
        values.put("averageSpeed", 0);
        values.put("distance", data.getDistance());
        values.put("pathJson", data.getPathJson());
        values.put("date", data.getDate());

        /**
         * 안드로이드에서는 기본적으로 삽입, 갱신, 삭제, 조회의 기능을 하는 메소드를 구현해놓았습니다.
         * insert 메소드의 파라메터는 다음과 같습니다.
         *
         * public long insert (String table, String nullColumnHack, ContentValues values)
         *
         * table : 삽입할 테이블, nullColumnHack : null , values : 삽입할 데이터들
         *
         * 이 메소드는 삽입된 행의 id 를 리턴합니다.
         *
         * -1 을 리턴할 경우 작업에 오류가 발생한 것 입니다.
         */
        long insertedId = db.insert(TrackingHistory.class.getSimpleName(), null, values);

        return insertedId != -1;
    }

    /**
     * 테이블에 존재하는 모든 데이터를 로드합니다.
     */
    public Cursor findAll() {

        // SELECT 작업은 읽기 작업이므로 getReadableDatabase 메소드를 이용하여 읽기 전용 database 를 얻습니다.
        SQLiteDatabase db = mHelper.getReadableDatabase();

        /**
         * public Cursor query (String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
         * table : 접근할 테이블명, columns : 가져올 데이터들의 컬럼명,
         * selection : where 절의 key 들, selectionsArgs : where 절의 value 들
         *
         * Cursor 객체는 해당 쿼리의 결과가 담기는 객체입니다.
         */
        return db.query(TrackingHistory.class.getSimpleName(), null, null, null, null, null, "date" + " DESC");
    }

    /**
     * ListView 에서 롱클릭한 메모를 삭제합니다.
     */
    public boolean delete(int id) {
        // DELETE 작업은 쓰기 권한이 필요합니다.
        SQLiteDatabase db = mHelper.getWritableDatabase();

        /**
         * public int delete (String table, String whereClause, String[] whereArgs)
         * table : 지울 데이터가 위치하는 테이블, whereClause : 조건절, whereArgs : 조건절
         *
         * update 메소드와 동일하게 delete 메소드는 반영된 행의 카운트를 리턴합니다.
         */
        int affectedRowsCount = db.delete(TrackingHistory.class.getSimpleName(), BaseColumns._ID + " = ? ", new String[]{String.valueOf(id)});
        return affectedRowsCount == 1;
    }
}
