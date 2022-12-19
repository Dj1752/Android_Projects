package com.team606.newsapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//6da7d4dbfbb34350b23ec0ee46f6be75
public class MainActivity extends AppCompatActivity  implements CategoryRVAdapter.CategoryClickInterface{
     private RecyclerView newsRV,categoryRV;
     private ProgressBar loadingPB;
     private ArrayList<Articles> articlesArrayList;
     private ArrayList<CategoryRVModel> categoryRVModelArrayList;
     private CategoryRVAdapter categoryRVAdapter;
     private NewsRVAdapter newsRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsRV = findViewById(R.id.idRVNews);
        categoryRV = findViewById(R.id.idRVCategories);
        loadingPB = findViewById(R.id.idProgressBar);
        articlesArrayList = new ArrayList<>();
        categoryRVModelArrayList = new ArrayList<>();
        newsRVAdapter = new NewsRVAdapter(articlesArrayList, this);
        categoryRVAdapter = new CategoryRVAdapter(categoryRVModelArrayList,this,this::onCategoryClick);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdapter);
        categoryRV.setAdapter(categoryRVAdapter);
        getCategories();
        getNews("All");
        newsRVAdapter.notifyDataSetChanged();
    }

    private void getCategories(){
        categoryRVModelArrayList.add(new CategoryRVModel("All","https://images.unsplash.com/photo-1451187580459-43490279c0fa?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=872&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Technology","https://images.unsplash.com/photo-1537498425277-c283d32ef9db?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=878&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Science","https://plus.unsplash.com/premium_photo-1663047033711-bc451bc3c2b1?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8c2NpZW5jZSUyMGFuZCUyMHRlY2hub2xvZ3l8ZW58MHx8MHx8&auto=format&fit=crop&w=500&q=60"));
        categoryRVModelArrayList.add(new CategoryRVModel("Sports","https://images.unsplash.com/photo-1569517282132-25d22f4573e6?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MjF8fHNwb3J0JTIwYmFja2dyb3VuZHxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60"));
        categoryRVModelArrayList.add(new CategoryRVModel("General","https://images.unsplash.com/photo-1521587760476-6c12a4b040da?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Business","https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Entertainment","https://images.unsplash.com/photo-1470229722913-7c0e2dbbafd3?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Health","https://images.unsplash.com/photo-1471864190281-a93a3070b6de?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80"));
        categoryRVAdapter.notifyDataSetChanged();

    }
 private void getNews(String category){
        loadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        String categoryURL = "https://newsapi.org/v2/top-headlines?country=in&category="+category+"&apiKey=6da7d4dbfbb34350b23ec0ee46f6be75";
        String url= "https://newsapi.org/v2/top-headlines?country=in&excludeDomains=stackoverflow.com&sortby=publishedAt&language=en&apiKey=6da7d4dbfbb34350b23ec0ee46f6be75";
        String BASE_URL = "https://newsapi.org/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
     Call<NewsModal> call;
     if(category.equals("All")){
         call = retrofitAPI.getAllNews(url);

     }else{
         call = retrofitAPI.getNewsByCategory(categoryURL);
     }
     call.enqueue(new Callback<NewsModal>() {
         @Override
         public void onResponse(Call<NewsModal> call, Response<NewsModal> response) {
              NewsModal newsModal = response.body();
              loadingPB.setVisibility(View.GONE);
              ArrayList<Articles> articles = newsModal.getArticles();
              for (int i=0;i<articles.size();i++){
                  articlesArrayList.add(new Articles(articles.get(i).getTitle(),articles.get(i).getDescription(),articles.get(i).getUrlToImage(),articles.get(i).getUrl(),articles.get(i).getContent()));

              }
              newsRVAdapter.notifyDataSetChanged();
         }

         @Override
         public void onFailure(Call<NewsModal> call, Throwable t) {
             Toast.makeText(MainActivity.this, "Fail to get news", Toast.LENGTH_SHORT).show();
         }
     });
    }
    @Override
    public void onCategoryClick(int position){
      String category = categoryRVModelArrayList.get(position).getCategory();

        getNews(category);
    }
}