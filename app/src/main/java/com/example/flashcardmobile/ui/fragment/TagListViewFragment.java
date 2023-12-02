package com.example.flashcardmobile.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.ui.view.TagListViewAdapter;
import com.example.flashcardmobile.viewmodel.TagViewModel;
import org.jetbrains.annotations.NotNull;



public class TagListViewFragment extends Fragment implements TagListViewAdapter.onTagOperationListener {
    private TagViewModel tagViewModel;
    private TagListViewAdapter tagListViewAdapter;
    private TextView firstColumn;
    private TextView secondColumn;
    private TextView thirdColumn;
    private Button backToTagsBtn;
    
   

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tag_list_view, container, false);

        tagViewModel = new ViewModelProvider(requireActivity()).get(TagViewModel.class);

        firstColumn = view.findViewById(R.id.firstCol);
        secondColumn = view.findViewById(R.id.secondCol);
        thirdColumn = view.findViewById(R.id.thirdCol);
        backToTagsBtn = view.findViewById(R.id.back_to_tags_button);
        
        backToTagsBtn.setOnClickListener(v -> {
            thirdColumn.setVisibility(View.VISIBLE);
            backToTagsBtn.setVisibility(View.GONE);
            setParams(1.2F);
            setColumnNames("Name", "Color");
        });

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        tagListViewAdapter = new TagListViewAdapter(this);
        recyclerView.setAdapter(tagListViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), recyclerView.VERTICAL));
        

        tagViewModel.getAllTags().observe(getViewLifecycleOwner(), newTags -> {
            tagListViewAdapter.setTags(newTags);
            tagListViewAdapter.notifyDataSetChanged();
        });
        
        
        return view;
    }

    @Override
    public void onViewCards(long tagId) {
        thirdColumn.setVisibility(View.GONE);
        backToTagsBtn.setVisibility(View.VISIBLE);

        setParams(1.8F);
        setColumnNames("Front side", "Back side");
        
        tagViewModel.getTagWithCards(tagId).observe(getViewLifecycleOwner(), tagWithCards -> {
            tagListViewAdapter.setCards(tagWithCards.cards);
            tagListViewAdapter.notifyDataSetChanged();

        });
        
    }
    
    private void setParams(Float weight) {
        LinearLayout.LayoutParams firstColParams = (LinearLayout.LayoutParams) firstColumn.getLayoutParams();
        firstColParams.weight = weight;

        LinearLayout.LayoutParams secondColParams = (LinearLayout.LayoutParams) secondColumn.getLayoutParams();
        secondColParams.weight = weight;

        firstColumn.setLayoutParams(firstColParams);
        secondColumn.setLayoutParams(secondColParams);
    }
    private void setColumnNames(String columnOne, String columnTwo) {
        firstColumn.setText(columnOne);
        secondColumn.setText(columnTwo);
    }
}
