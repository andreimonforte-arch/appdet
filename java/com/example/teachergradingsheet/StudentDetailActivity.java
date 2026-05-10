package com.example.teachergradingsheet;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StudentDetailActivity extends AppCompatActivity {

    public static final String EXTRA_STUDENT_ID = "detail_student_id";

    private DatabaseHelper db;

    // Header info
    private TextView tvDetailName;
    private TextView tvDetailStudentId;
    private TextView tvDetailTerm;

    // Five component score views
    private TextView tvDetailWritten;     // 30%
    private TextView tvDetailSeatwork;    // 15%
    private TextView tvDetailMidterm;     // 20%
    private TextView tvDetailProject;     // 25%
    private TextView tvDetailLaboratory;  // 10%

    // Final result views
    private TextView tvDetailTotal;
    private TextView tvDetailGrade;
    private TextView tvDetailStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);

        db = new DatabaseHelper(this);

        bindViews();
        loadStudent();
    }

    private void bindViews() {
        tvDetailName       = findViewById(R.id.tvDetailName);
        tvDetailStudentId  = findViewById(R.id.tvDetailStudentId);
        tvDetailTerm       = findViewById(R.id.tvDetailTerm);

        tvDetailWritten    = findViewById(R.id.tvDetailWritten);
        tvDetailSeatwork   = findViewById(R.id.tvDetailSeatwork);
        tvDetailMidterm    = findViewById(R.id.tvDetailMidterm);
        tvDetailProject    = findViewById(R.id.tvDetailProject);
        tvDetailLaboratory = findViewById(R.id.tvDetailLaboratory);

        tvDetailTotal      = findViewById(R.id.tvDetailTotal);
        tvDetailGrade      = findViewById(R.id.tvDetailGrade);
        tvDetailStatus     = findViewById(R.id.tvDetailStatus);
    }

    private void loadStudent() {
        int studentId = getIntent().getIntExtra(EXTRA_STUDENT_ID, -1);

        if (studentId == -1) {
            finish();
            return;
        }

        Student s = db.getStudentById(studentId);

        if (s == null) {
            finish();
            return;
        }

        setTitle(s.getName() + " — Details");
        fillHeader(s);
        fillComponentScores(s);
        fillFinalResult(s);
    }

    private void fillHeader(Student s) {
        tvDetailName.setText(s.getName());
        tvDetailStudentId.setText("ID: " + s.getStudentId());
        tvDetailTerm.setText("Term: " + s.getTerm());
    }

    private void fillComponentScores(Student s) {
        tvDetailWritten.setText(String.format("%.2f", s.getWrittenExam()));
        tvDetailSeatwork.setText(String.format("%.2f", s.getSeatwork()));
        tvDetailMidterm.setText(String.format("%.2f", s.getMidtermExam()));
        tvDetailProject.setText(String.format("%.2f", s.getProject()));
        tvDetailLaboratory.setText(String.format("%.2f", s.getLaboratory()));
    }

    private void fillFinalResult(Student s) {
        double  total  = s.getTotalScore();
        boolean passed = s.isPassed();

        tvDetailTotal.setText(String.format("%.2f", total));
        tvDetailGrade.setText(s.getGrade());

        if (passed) {
            tvDetailStatus.setText("✅  PASSED");
            tvDetailStatus.setBackgroundColor(Color.parseColor("#2E7D32"));
        } else {
            tvDetailStatus.setText("❌  FAILED");
            tvDetailStatus.setBackgroundColor(Color.parseColor("#C62828"));
        }
    }
}