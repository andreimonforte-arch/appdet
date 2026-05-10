package com.example.teachergradingsheet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME    = "grading.db";
    private static final int    DB_VERSION = 3; // bumped — Finals columns added

    static final String TABLE           = "students";
    static final String COL_ID          = "id";
    static final String COL_NAME        = "name";
    static final String COL_STUDENT_ID  = "student_id";
    static final String COL_TERM        = "term";

    // Midterm columns
    static final String COL_WRITTEN     = "written_exam";
    static final String COL_SEATWORK    = "seatwork";
    static final String COL_MIDTERM     = "midterm_exam";
    static final String COL_PROJECT     = "project";
    static final String COL_LAB         = "laboratory";

    // Finals columns
    static final String COL_FINAL_PROJECT = "final_project";
    static final String COL_FINAL_EXAM    = "final_exam";
    static final String COL_LAB_ACTIVITY  = "lab_activity";
    static final String COL_FINAL_WRITTEN = "final_written";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE + " ("
                    + COL_ID            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_NAME          + " TEXT NOT NULL, "
                    + COL_STUDENT_ID    + " TEXT NOT NULL, "
                    + COL_TERM          + " TEXT NOT NULL, "
                    // Midterm
                    + COL_WRITTEN       + " REAL DEFAULT 0, "
                    + COL_SEATWORK      + " REAL DEFAULT 0, "
                    + COL_MIDTERM       + " REAL DEFAULT 0, "
                    + COL_PROJECT       + " REAL DEFAULT 0, "
                    + COL_LAB           + " REAL DEFAULT 0, "
                    // Finals
                    + COL_FINAL_PROJECT + " REAL DEFAULT 0, "
                    + COL_FINAL_EXAM    + " REAL DEFAULT 0, "
                    + COL_LAB_ACTIVITY  + " REAL DEFAULT 0, "
                    + COL_FINAL_WRITTEN + " REAL DEFAULT 0)";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }


    public long addStudent(Student s) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(TABLE, null, toValues(s));
        db.close();
        return result;
    }

    public int updateStudent(Student s) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.update(TABLE, toValues(s),
                COL_ID + "=?", new String[]{String.valueOf(s.getId())});
        db.close();
        return rows;
    }

    public boolean deleteStudent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE,
                COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rows > 0;
    }

    public List<Student> getStudentsByTerm(String term) {
        List<Student> list = new ArrayList<>();
        SQLiteDatabase db  = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE, null,
                COL_TERM + "=?", new String[]{term},
                null, null, COL_NAME + " ASC");
        if (cursor.moveToFirst()) {
            do { list.add(fromCursor(cursor)); }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public Student getStudentById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE, null,
                COL_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);
        Student s = null;
        if (cursor.moveToFirst()) s = fromCursor(cursor);
        cursor.close();
        db.close();
        return s;
    }


    private ContentValues toValues(Student s) {
        ContentValues v = new ContentValues();
        v.put(COL_NAME,          s.getName());
        v.put(COL_STUDENT_ID,    s.getStudentId());
        v.put(COL_TERM,          s.getTerm());
        // Midterm
        v.put(COL_WRITTEN,       s.getWrittenExam());
        v.put(COL_SEATWORK,      s.getSeatwork());
        v.put(COL_MIDTERM,       s.getMidtermExam());
        v.put(COL_PROJECT,       s.getProject());
        v.put(COL_LAB,           s.getLaboratory());
        // Finals
        v.put(COL_FINAL_PROJECT, s.getFinalProject());
        v.put(COL_FINAL_EXAM,    s.getFinalExam());
        v.put(COL_LAB_ACTIVITY,  s.getLabActivity());
        v.put(COL_FINAL_WRITTEN, s.getFinalWritten());
        return v;
    }

    private Student fromCursor(Cursor c) {
        Student s = new Student();
        s.setId(c.getInt(c.getColumnIndexOrThrow(COL_ID)));
        s.setName(c.getString(c.getColumnIndexOrThrow(COL_NAME)));
        s.setStudentId(c.getString(c.getColumnIndexOrThrow(COL_STUDENT_ID)));
        s.setTerm(c.getString(c.getColumnIndexOrThrow(COL_TERM)));

        s.setWrittenExam(c.getDouble(c.getColumnIndexOrThrow(COL_WRITTEN)));
        s.setSeatwork(c.getDouble(c.getColumnIndexOrThrow(COL_SEATWORK)));
        s.setMidtermExam(c.getDouble(c.getColumnIndexOrThrow(COL_MIDTERM)));
        s.setProject(c.getDouble(c.getColumnIndexOrThrow(COL_PROJECT)));
        s.setLaboratory(c.getDouble(c.getColumnIndexOrThrow(COL_LAB)));

        s.setFinalProject(c.getDouble(c.getColumnIndexOrThrow(COL_FINAL_PROJECT)));
        s.setFinalExam(c.getDouble(c.getColumnIndexOrThrow(COL_FINAL_EXAM)));
        s.setLabActivity(c.getDouble(c.getColumnIndexOrThrow(COL_LAB_ACTIVITY)));
        s.setFinalWritten(c.getDouble(c.getColumnIndexOrThrow(COL_FINAL_WRITTEN)));
        return s;
    }
}