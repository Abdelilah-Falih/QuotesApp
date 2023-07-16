package com.example.Quotes_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.Quotes_app.Adapters.RvAdapter;
import com.example.Quotes_app.Database.FavoutiteQuotesDatabase.FavouriteQuotesDB;
import com.example.Quotes_app.Models.Quote;
import com.example.Quotes_app.databinding.ActivityAllFavouritesQuotesBinding;

import java.util.ArrayList;

public class AllFavouritesQuotes extends AppCompatActivity {

    ActivityAllFavouritesQuotesBinding binding;
    View root;
    FavouriteQuotesDB database;
    RvAdapter adapter;
    ArrayList<Quote> quotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_favourites_quotes);

        binding = ActivityAllFavouritesQuotesBinding.inflate(getLayoutInflater());
        root = binding.getRoot();
        setContentView(root);



        database = new FavouriteQuotesDB(this);
        quotes = database.getAllQuotes();

        if (quotes.size() == 0){
            binding.ivEmpty.setVisibility(View.VISIBLE);
        }
        else {
            binding.ivEmpty.setVisibility(View.GONE);
        }

        adapter = new RvAdapter(this, quotes);

        binding.rvFavourteQuotes.setAdapter(adapter);
        binding.rvFavourteQuotes.setLayoutManager(new LinearLayoutManager(this));

    }
}