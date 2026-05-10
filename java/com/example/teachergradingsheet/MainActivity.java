package com.example.teachergradingsheet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnMidterm = findViewById(R.id.btnMidterm);
        Button btnFinals  = findViewById(R.id.btnFinals);

        btnMidterm.setOnClickListener(v -> openSheet("Midterm"));
        btnFinals.setOnClickListener(v  -> openSheet("Finals"));
    }

    private void openSheet(String term) {
        Intent intent = new Intent(this, GradingSheetActivity.class);
        intent.putExtra(GradingSheetActivity.EXTRA_TERM, term);
        startActivity(intent);
    }
}