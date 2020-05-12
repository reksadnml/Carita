package com.example.carita.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carita.R;
import com.example.carita.adapter.ListNewsAdapter;
import com.example.carita.viewmodel.NewsViewModel;

import java.util.Locale;

public class SearchFragment extends Fragment implements View.OnClickListener {

    private NewsViewModel newsViewModel;
    private RecyclerView rvNews;
    private ListNewsAdapter listNewsAdapter;
    private View root;
    private ImageView searchButton;
    private EditText searchField;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newsViewModel =
                ViewModelProviders.of(this).get(NewsViewModel.class);
        root = inflater.inflate(R.layout.fragment_search, container, false);

        searchField = root.findViewById(R.id.search_field);
        searchButton = root.findViewById(R.id.search_button);
        searchButton.setOnClickListener(this);

        setAdapter();
        setRecyclerView();

        return root;
    }

    private void setAdapter() {
        listNewsAdapter = new ListNewsAdapter();
        listNewsAdapter.notifyDataSetChanged();
    }

    private void setRecyclerView() {
        rvNews = root.findViewById(R.id.rv_search_news);
        rvNews.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNews.setAdapter(listNewsAdapter);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
    }

    private void searchNews (FragmentActivity activity, String category) {
        newsViewModel.setSearchNews(activity, category);
        newsViewModel.getNews().observe(activity, newsList -> {
            if (newsList != null) {
                listNewsAdapter.setData(newsList);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_button:
                String query = searchField.getText().toString();
                searchNews(getActivity(), query);
        }
    }
}
