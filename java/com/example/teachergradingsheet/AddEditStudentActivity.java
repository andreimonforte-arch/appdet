package com.example.teachergradingsheet;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditStudentActivity extends AppCompatActivity {

    public static final String EXTRA_STUDENT_ID = "student_id";

    private DatabaseHelper db;
    private Student existingStudent;
    private String term;

    // Info fields
    private EditText etName;
    private EditText etStudentId;

    // Midterm score fields
    private EditText etWritten;
    private EditText etSeatwork;
    private EditText etMidtermExam;
    private EditText etProject;
    private EditText etLaboratory;

    // Finals score fields
    private EditText etFinalProject;
    private EditText etFinalExam;
    private EditText etLabActivity;
    private EditText etFinalWritten;

    // Which card is visible depends on the term
    private LinearLayout layoutMidtermScores;
    private LinearLayout layoutFinalsScores;

    // Live preview card
    private LinearLayout layoutPreview;
    private TextView     tvPreviewTotal;
    private TextView     tvPreviewGrade;
    private TextView     tvPreviewStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_student);

        db   = new DatabaseHelper(this);
        term = getIntent().getStringExtra(GradingSheetActivity.EXTRA_TERM);

        bindViews();
        showFieldsForTerm();
        checkIfEditing();
        attachScoreWatchers();

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> saveStudent());
    }


    private void bindViews() {
        etName      = findViewById(R.id.etName);
        etStudentId = findViewById(R.id.etStudentId);

        // Midterm
        etWritten    = findViewById(R.id.etWritten);
        etSeatwork   = findViewById(R.id.etSeatwork);
        etMidtermExam = findViewById(R.id.etMidtermExam);
        etProject    = findViewById(R.id.etProject);
        etLaboratory = findViewById(R.id.etLaboratory);

        // Finals
        etFinalProject = findViewById(R.id.etFinalProject);
        etFinalExam    = findViewById(R.id.etFinalExam);
        etLabActivity  = findViewById(R.id.etLabActivity);
        etFinalWritten = findViewById(R.id.etFinalWritten);

        layoutMidtermScores = findViewById(R.id.layoutMidtermScores);
        layoutFinalsScores  = findViewById(R.id.layoutFinalsScores);

        layoutPreview   = findViewById(R.id.layoutPreview);
        tvPreviewTotal  = findViewById(R.id.tvPreviewTotal);
        tvPreviewGrade  = findViewById(R.id.tvPreviewGrade);
        tvPreviewStatus = findViewById(R.id.tvPreviewStatus);
    }

    private void showFieldsForTerm() {
        if ("Finals".equals(term)) {
            layoutFinalsScores.setVisibility(View.VISIBLE);
            layoutMidtermScores.setVisibility(View.GONE);
            setTitle("Add Student — Finals");
        } else {
            layoutMidtermScores.setVisibility(View.VISIBLE);
            layoutFinalsScores.setVisibility(View.GONE);
            setTitle("Add Student — Midterm");
        }
    }

    private void checkIfEditing() {
        int studentDbId = getIntent().getIntExtra(EXTRA_STUDENT_ID, -1);

        if (studentDbId == -1) return;

        setTitle("Edit Student");
        existingStudent = db.getStudentById(studentDbId);

        if (existingStudent == null) return;

        etName.setText(existingStudent.getName());
        etStudentId.setText(existingStudent.getStudentId());

        if ("Finals".equals(term)) {
            etFinalProject.setText(String.valueOf(existingStudent.getFinalProject()));
            etFinalExam.setText(String.valueOf(existingStudent.getFinalExam()));
            etLabActivity.setText(String.valueOf(existingStudent.getLabActivity()));
            etFinalWritten.setText(String.valueOf(existingStudent.getFinalWritten()));
        } else {
            etWritten.setText(String.valueOf(existingStudent.getWrittenExam()));
            etSeatwork.setText(String.valueOf(existingStudent.getSeatwork()));
            etMidtermExam.setText(String.valueOf(existingStudent.getMidtermExam()));
            etProject.setText(String.valueOf(existingStudent.getProject()));
            etLaboratory.setText(String.valueOf(existingStudent.getLaboratory()));
        }
    }

    private void attachScoreWatchers() {
        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) { updatePreview(); }
        };

        // Midterm fields
        etWritten.addTextChangedListener(watcher);
        etSeatwork.addTextChangedListener(watcher);
        etMidtermExam.addTextChangedListener(watcher);
        etProject.addTextChangedListener(watcher);
        etLaboratory.addTextChangedListener(watcher);

        // Finals fields
        etFinalProject.addTextChangedListener(watcher);
        etFinalExam.addTextChangedListener(watcher);
        etLabActivity.addTextChangedListener(watcher);
        etFinalWritten.addTextChangedListener(watcher);
    }

    private void updatePreview() {
        double total;

        if ("Finals".equals(term)) {
            double fp = parseRaw(etFinalProject.getText().toString());
            double fe = parseRaw(etFinalExam.getText().toString());
            double la = parseRaw(etLabActivity.getText().toString());
            double fw = parseRaw(etFinalWritten.getText().toString());

            if (fp < 0 || fe < 0 || la < 0 || fw < 0) {
                layoutPreview.setVisibility(View.GONE);
                return;
            }
            total = (fp * 0.60) + (fe * 0.15) + (la * 0.15) + (fw * 0.10);

        } else {
            double we = parseRaw(etWritten.getText().toString());
            double sw = parseRaw(etSeatwork.getText().toString());
            double me = parseRaw(etMidtermExam.getText().toString());
            double pr = parseRaw(etProject.getText().toString());
            double lb = parseRaw(etLaboratory.getText().toString());

            if (we < 0 || sw < 0 || me < 0 || pr < 0 || lb < 0) {
                layoutPreview.setVisibility(View.GONE);
                return;
            }
            total = (we * 0.30) + (sw * 0.15) + (me * 0.20) + (pr * 0.25) + (lb * 0.10);
        }

        boolean passed = total >= 75.0;

        layoutPreview.setVisibility(View.VISIBLE);
        tvPreviewTotal.setText(String.format("%.2f", total));
        tvPreviewGrade.setText(computeGrade(total));

        if (passed) {
            tvPreviewStatus.setText("PASSED");
            tvPreviewStatus.setBackgroundColor(Color.parseColor("#2E7D32"));
        } else {
            tvPreviewStatus.setText("FAILED");
            tvPreviewStatus.setBackgroundColor(Color.parseColor("#C62828"));
        }
    }

    private double parseRaw(String raw) {
        if (TextUtils.isEmpty(raw.trim())) return -1;
        try {
            double v = Double.parseDouble(raw.trim());
            return (v >= 0 && v <= 100) ? v : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private String computeGrade(double total) {
        if (total >= 90) return "1.0";
        if (total >= 85) return "1.5";
        if (total >= 80) return "2.0";
        if (total >= 75) return "2.5";
        if (total >= 70) return "3.0";
        return "5.0";
    }


    private void saveStudent() {
        String name = etName.getText().toString().trim();
        String sid  = etStudentId.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            etName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(sid)) {
            etStudentId.setError("Student ID is required");
            etStudentId.requestFocus();
            return;
        }

        Student student = existingStudent != null ? existingStudent : new Student();
        student.setName(name);
        student.setStudentId(sid);
        student.setTerm(term);

        double total;

        if ("Finals".equals(term)) {
            double fp, fe, la, fw;
            try {
                fp = parseScore(etFinalProject, "Final Project");
                fe = parseScore(etFinalExam,    "Final Exam");
                la = parseScore(etLabActivity,  "Lab Activity");
                fw = parseScore(etFinalWritten, "Written Exam");
            } catch (IllegalArgumentException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            student.setFinalProject(fp);
            student.setFinalExam(fe);
            student.setLabActivity(la);
            student.setFinalWritten(fw);
            total = (fp * 0.60) + (fe * 0.15) + (la * 0.15) + (fw * 0.10);

        } else {
            double we, sw, me, pr, lb;
            try {
                we = parseScore(etWritten,    "Written Exam");
                sw = parseScore(etSeatwork,   "Seatwork");
                me = parseScore(etMidtermExam,"Midterm Exam");
                pr = parseScore(etProject,    "Project");
                lb = parseScore(etLaboratory, "Laboratory");
            } catch (IllegalArgumentException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            student.setWrittenExam(we);
            student.setSeatwork(sw);
            student.setMidtermExam(me);
            student.setProject(pr);
            student.setLaboratory(lb);
            total = (we * 0.30) + (sw * 0.15) + (me * 0.20) + (pr * 0.25) + (lb * 0.10);
        }

        boolean passed = total >= 75.0;

        boolean saveOk;
        if (existingStudent != null) {
            saveOk = db.updateStudent(student) > 0;
        } else {
            saveOk = db.addStudent(student) != -1;
        }

        if (!saveOk) {
            Toast.makeText(this, "Could not save. Try again.", Toast.LENGTH_SHORT).show();
            return;
        }
        showResultToast(name, total, passed);
        finish();
    }

    private void showResultToast(String name, double total, boolean passed) {
        String icon    = passed ? "✅" : "❌";
        String status  = passed ? "PASSED" : "FAILED";
        String grade   = computeGrade(total);
        String message = name + " | " + String.format("%.2f", total) + " | Grade " + grade + " | " + icon + " " + status;
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private double parseScore(EditText field, String label) {
        String raw = field.getText().toString().trim();

        if (TextUtils.isEmpty(raw)) {
            field.setError(label + " cannot be empty");
            field.requestFocus();
            throw new IllegalArgumentException(label + " cannot be empty.");
        }
        double value;
        try {
            value = Double.parseDouble(raw);
        } catch (NumberFormatException e) {
            field.setError("Enter a valid number");
            field.requestFocus();
            throw new IllegalArgumentException(label + " must be a number.");
        }
        if (value < 0 || value > 100) {
            field.setError("Must be 0 to 100");
            field.requestFocus();
            throw new IllegalArgumentException(label + " must be between 0 and 100.");
        }
        return value;
    }
}