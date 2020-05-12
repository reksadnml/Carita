package com.example.carita.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carita.R;
import com.example.carita.adapter.ListNewsAdapter;

import java.util.Locale;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private NewsViewModel newsViewModel;
    private RecyclerView rvNews;
    private ListNewsAdapter listNewsAdapter;
    private View root;
    private TextView businessTab, sportsTab, healthTab, techTab, scienceTab;
    private String lang = Locale.getDefault().toLanguageTag();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newsViewModel =
                ViewModelProviders.of(this).get(NewsViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);

        businessTab = root.findViewById(R.id.business_tab);
        sportsTab = root.findViewById(R.id.sports_tab);
        healthTab = root.findViewById(R.id.health_tab);
        techTab = root.findViewById(R.id.tech_tab);
        scienceTab = root.findViewById(R.id.science_tab);

        businessTab.setOnClickListener(this);
        sportsTab.setOnClickListener(this);
        healthTab.setOnClickListener(this);
        techTab.setOnClickListener(this);
        scienceTab.setOnClickListener(this);

        setAdapter();
        setRecyclerView();

        return root;
    }

    private void setAdapter() {
        listNewsAdapter = new ListNewsAdapter();
        listNewsAdapter.notifyDataSetChanged();
    }

    private void setRecyclerView() {
        rvNews = root.findViewById(R.id.rv_news);
        rvNews.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNews.setAdapter(listNewsAdapter);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (lang == "in-ID") lang = "id";
        else lang = "us";

        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        showNews(getActivity(), lang, "");
    }

    private void showNews(FragmentActivity activity, String lang, String category) {
        newsViewModel.setNews(getActivity(), lang, category);
        newsViewModel.getNews().observe(activity, newsList -> {
            if (newsList != null) {
                listNewsAdapter.setData(newsList);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.business_tab:
                Toast.makeText(getActivity(), getResources().getString(R.string.choose_category)
                        + getResources().getString(R.string.business_tab), Toast.LENGTH_SHORT).show();
                showNews(getActivity(), lang, "business");
                break;
            case R.id.sports_tab:
                Toast.makeText(getActivity(), getResources().getString(R.string.choose_category)
                        + getResources().getString(R.string.sports_tab), Toast.LENGTH_SHORT).show();
                showNews(getActivity(), lang, "sports");
                break;
            case R.id.health_tab:
                Toast.makeText(getActivity(), getResources().getString(R.string.choose_category)
                        + getResources().getString(R.string.health_tab), Toast.LENGTH_SHORT).show();
                showNews(getActivity(), lang, "health");
                break;
            case R.id.tech_tab:
                Toast.makeText(getActivity(), getResources().getString(R.string.choose_category)
                        + getResources().getString(R.string.tech_tab), Toast.LENGTH_SHORT).show();
                showNews(getActivity(), lang, "technology");
                break;
            case R.id.science_tab:
                Toast.makeText(getActivity(), getResources().getString(R.string.choose_category)
                        + getResources().getString(R.string.science_tab), Toast.LENGTH_SHORT).show();
                showNews(getActivity(), lang, "science");
                break;
        }
    }
}
