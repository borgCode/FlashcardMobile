package com.example.flashcardmobile.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.example.flashcardmobile.R;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        DatabaseHelper db = DatabaseHelper.getInstance(this);
//        SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", MODE_PRIVATE);
//        if (!prefs.getBoolean("db_init", false)) {
//            db.createDatabase();
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putBoolean("db_init", true);
//            editor.apply();
//        }

        setContentView(R.layout.activity_main);


        Button practiceButton = findViewById(R.id.practiceBtn);
        Button listButton = findViewById(R.id.listBtn);

        practiceButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PracticeActivity.class);
            startActivity(intent);
        });
        
        listButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            startActivity(intent);
        });
        
        

    }
}