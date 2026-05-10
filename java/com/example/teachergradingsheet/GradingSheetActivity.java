package com.example.teachergradingsheet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class GradingSheetActivity extends AppCompatActivity
        implements StudentAdapter.OnStudentClickListener {

        public static final String EXTRA_TERM = "term";

        private DatabaseHelper db;
        private StudentAdapter adapter;
        private String term;

        private TextView tvPassed;
        private TextView tvFailed;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_grading_sheet);

                term = getIntent().getStringExtra(EXTRA_TERM);
                setTitle(term + " Grading Sheet");

                db = new DatabaseHelper(this);
                bindViews();
                setupRecyclerView();
                setupFab();
        }

        @Override
        protected void onResume() {
                super.onResume();
                refreshList();
        }

        private void bindViews() {
                tvPassed = findViewById(R.id.tvPassed);
                tvFailed = findViewById(R.id.tvFailed);
        }

        private void setupRecyclerView() {
                RecyclerView rv = findViewById(R.id.recyclerView);
                rv.setLayoutManager(new LinearLayoutManager(this));

                List<Student> students = db.getStudentsByTerm(term);
                adapter = new StudentAdapter(students, this);
                rv.setAdapter(adapter);

                updateStats(students);
        }

        private void setupFab() {
                FloatingActionButton fab = findViewById(R.id.fab);
                fab.setOnClickListener(v -> openAddEdit(null));
        }

        private void refreshList() {
                List<Student> students = db.getStudentsByTerm(term);
                adapter.updateList(students);
                updateStats(students);
        }

        private void updateStats(List<Student> students) {
                int passed = 0;
                for (Student s : students) {
                        if (s.isPassed()) passed++;
                }

                tvPassed.setText(String.valueOf(passed));
                tvFailed.setText(String.valueOf(students.size() - passed));
        }
        @Override
        public void onEditClick(Student student) {
                openAddEdit(student);
        }

        @Override
        public void onDeleteClick(Student student) {
                new AlertDialog.Builder(this)
                        .setTitle("Remove Student")
                        .setMessage(
                                "Are you sure you want to remove "
                                        + student.getName()
                                        + " from the " + term + " sheet? "
                                        + "This cannot be undone."
                        )
                        .setPositiveButton("Yes, Delete", (dialog, which) -> {
                                db.deleteStudent(student.getId());
                                refreshList();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
        }


        private void openAddEdit(Student student) {
                Intent intent = new Intent(this, AddEditStudentActivity.class);
                intent.putExtra(EXTRA_TERM, term);

                if (student != null) {
                        intent.putExtra(AddEditStudentActivity.EXTRA_STUDENT_ID, student.getId());
                }

                startActivity(intent);
        }
}