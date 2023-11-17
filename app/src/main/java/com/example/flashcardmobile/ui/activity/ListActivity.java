package com.example.flashcardmobile.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.DeckCard;
import com.example.flashcardmobile.ui.fragment.DeckSelectionFragment;
import com.example.flashcardmobile.ui.fragment.ListViewFragment;
import com.example.flashcardmobile.ui.view.ListViewAdapter;
import com.example.flashcardmobile.viewmodel.CardViewModel;
import com.example.flashcardmobile.viewmodel.DeckCardViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else {
                getOnBackPressedDispatcher().onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ListViewFragment())
                    .commit();
        }
    }
}
