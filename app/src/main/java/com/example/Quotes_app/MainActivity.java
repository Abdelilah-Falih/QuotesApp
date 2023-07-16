package com.example.Quotes_app;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.Quotes_app.Database.FavoutiteQuotesDatabase.FavouriteQuotesDB;
import com.example.Quotes_app.Models.Quote;
import com.example.Quotes_app.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    View root;
    SharedPreferences sharedPreferences;
    FavouriteQuotesDB database;

    boolean isLiked;
    ArrayList<String> colors;
    AlertDialog.Builder alert;
    Window window;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        root = binding.getRoot();
        setContentView(root);
        sharedPreferences = getSharedPreferences("favorite-quotes", MODE_PRIVATE);
        database = new FavouriteQuotesDB(this);

        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);



        int quote_id = sharedPreferences.getInt("_id", -1);
        if (quote_id == -1) {
            loadQuote();
        } else {
            String quote = sharedPreferences.getString("quote", null);
            String author = sharedPreferences.getString("author", null);
            binding.tvIdQuote.setText("#" + quote_id);
            binding.tvQuoteMain.setText(quote);
            binding.tvAuthorMain.setText(author);
            binding.tbPinUnpinMain.setChecked(true);
            isLiked = !database.isFavourite(quote_id);
            changeImage();
            binding.ivPin.setVisibility(View.VISIBLE);
        }

        if (sharedPreferences.getBoolean("colors", false)) {
            setupSppiner();
        } else {
            loadColors();
        }


        binding.btnShowFavouriteQuotes.setOnClickListener(v->{
            startActivity(new Intent(this, AllFavouritesQuotes.class));
        });


        binding.tbPinUnpinMain.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            int id = Integer.parseInt(binding.tvIdQuote.getText().toString().substring(1));
            String quote = binding.tvQuoteMain.getText().toString();
            String author = binding.tvAuthorMain.getText().toString();
            if (isChecked) {
                editor.putInt("_id", id);
                editor.putString("quote", quote);
                editor.putString("author", author);
                database.addQuote(new Quote(id, quote, author));
                isLiked = true;
                binding.ibLikeDeslike.setImageResource(R.drawable.ic_like);
                binding.ivPin.setVisibility(View.VISIBLE);
            } else {
                editor.clear();
                binding.ivPin.setVisibility(View.INVISIBLE);
            }
            editor.apply();
        });


        binding.ibLikeDeslike.setOnClickListener(v -> {
            int id = Integer.parseInt(binding.tvIdQuote.getText().toString().substring(1));
            if (isLiked) {
                database.deleteQuote(id);
                if (binding.tbPinUnpinMain.isChecked()) {
                    binding.tbPinUnpinMain.setChecked(false);
                }
            } else {
                String quote_text = binding.tvQuoteMain.getText().toString();
                String quote_author = binding.tvAuthorMain.getText().toString();
                database.addQuote(new Quote(id, quote_text, quote_author));
            }
            changeImage();
        });

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String color ;
                if (position == 0){
                    color = "#FFFFFFFF";
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("Favourite_color", position).apply();
                }else {
                    color = colors.get(position);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("Favourite_color", position).apply();
                }
                getWindow().getDecorView().setBackgroundColor(Color.parseColor(color));
                binding.tbPinUnpinMain.setTextColor(Color.parseColor(color));
                binding.btnShowFavouriteQuotes.setTextColor(Color.parseColor(color));
                changeStatuBarColor(color);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spinner.setSelection(sharedPreferences.getInt("Favourite_color", 0));



        binding.contentContainer.setOnTouchListener(new View.OnTouchListener() {
            long last_click = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    long clickTime = System.currentTimeMillis();
                    if ((clickTime - last_click) <= 600) {
                        myThread thread = new myThread();
                        thread.start();

                    } else {
                        last_click = clickTime;
                    }
                }
                return true;
            }
        });


    }
    void changeStatuBarColor(String color){
        if(color.equals("#FFFFFFFF"))
            window.setStatusBarColor(Color.parseColor("#FF000000"));
        else
            window.setStatusBarColor(Color.parseColor(color));
    }

    private void loadColors() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Default", "Default");
        editor.putString("LightSalmon", "#FFA07A");
        editor.putString("Plum", "#DDA0DD");
        editor.putString("PaleGreen", "#98FB98");
        editor.putString("CornflowerBlue", "#6495ED");
        editor.apply();
        setupSppiner();
    }

    private void setupSppiner() {
        colors = new ArrayList<>();
        colors.add(sharedPreferences.getString("Default", null));
        colors.add(sharedPreferences.getString("LightSalmon", null));
        colors.add(sharedPreferences.getString("Plum", null));
        colors.add(sharedPreferences.getString("PaleGreen", null));
        colors.add(sharedPreferences.getString("CornflowerBlue", null));

        ColorsSpinnerAdapter adapter = new ColorsSpinnerAdapter(this, colors);
        binding.spinner.setAdapter(adapter);
    }


    void changeImage() {
        if (isLiked) binding.ibLikeDeslike.setImageResource(R.drawable.ic_unlike);
        else binding.ibLikeDeslike.setImageResource(R.drawable.ic_like);
        isLiked = !isLiked;
    }


    private void loadQuote() {
        FrDialog frDialog = new FrDialog(false, true);
        frDialog.show(getFragmentManager(), "tag01");
        frDialog.setCancelable(false);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://dummyjson.com/quotes/random";

        JsonObjectRequest jsonObject = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int id = response.getInt("id");
                    binding.tvIdQuote.setText("#" + id);
                    binding.tvQuoteMain.setText(response.getString("quote"));
                    binding.tvAuthorMain.setText(response.getString("author"));
                    isLiked = !database.isFavourite(id);
                    changeImage();
                    frDialog.dismiss();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                frDialog.dismiss();
                error.printStackTrace();
                 alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Internet Error");
                alert.setMessage("Pleas check your internet connexion and try again");
                alert.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alert.setCancelable(false);
                alert.show();
            }
        });

        queue.add(jsonObject);
    }

    private  class myThread extends Thread{
        FrDialog frDialog;

        @Override
        public void run() {
            frDialog = new FrDialog(isLiked, false);
            frDialog.show(getFragmentManager(), "tag");
            frDialog.setCancelable(false);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            frDialog.dismiss();
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    binding.ibLikeDeslike.performClick();
                }
            });
        }
    }
}