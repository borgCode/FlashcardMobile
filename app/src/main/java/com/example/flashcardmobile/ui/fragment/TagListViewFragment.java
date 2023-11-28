package com.example.flashcardmobile.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.Tag;
import com.example.flashcardmobile.ui.view.TagListViewAdapter;
import com.example.flashcardmobile.viewmodel.TagViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TagListViewFragment extends Fragment {
    private TagViewModel tagViewModel;
    private TagListViewAdapter tagListViewAdapter;
    private long selectedTagId;

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tag_list_view, container, false);

        tagViewModel = new ViewModelProvider(requireActivity()).get(TagViewModel.class);
        
        

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        tagListViewAdapter = new TagListViewAdapter();
        recyclerView.setAdapter(tagListViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), recyclerView.VERTICAL));

        tagViewModel.getAllTags().observe(getViewLifecycleOwner(), newTags -> {
            tagListViewAdapter.setTags(newTags);
            tagListViewAdapter.notifyDataSetChanged();
        });
        
        

//        tagSelection.setOnItemClickListener(((parent, view1, position, id) -> {
//            String selectedTagName = (String) parent.getItemAtPosition(position);
//            selectedTagId = tagMap.get(selectedTagName);
//            tagViewModel.getTagWithCards(selectedTagId).observe(getViewLifecycleOwner(), tagWithCards -> {
//                
//
//            });
//        }));
        
        
        
        
        
        //TODO
        return view;
    }
}
