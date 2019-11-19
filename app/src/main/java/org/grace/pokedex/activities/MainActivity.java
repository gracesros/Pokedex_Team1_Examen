package org.grace.pokedex.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.grace.pokedex.R;
import org.grace.pokedex.adapters.PokemonAdapter;
import org.grace.pokedex.models.Pokemon;
import org.grace.pokedex.interfaces.AsyncTaskHandler;
import org.grace.pokedex.network.PokemonAsyncTask;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AsyncTaskHandler, PokemonAdapter.ItemClickListener {

    PokemonAdapter adapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv_pokemon);
        progressBar = findViewById(R.id.progress_bar);

        progressBar.setVisibility(View.VISIBLE);
        PokemonAsyncTask pokemonAsyncTask = new PokemonAsyncTask();
        pokemonAsyncTask.handler = this;
        pokemonAsyncTask.execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_top, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_favorites:
                Intent intent = new Intent(this, FavoritesActivity.class);
                startActivity(intent);
                return true;

            case R.id.inicio:
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
                return true;

            case R.id.favorites:
                Toast.makeText(this, "Favorites selected", Toast.LENGTH_SHORT);
                Intent intent4 = new Intent(this, FavoritesActivity.class);
                startActivity(intent4);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onTaskEnd(Object pokemons) {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new PokemonAdapter(this, (List<Pokemon>) pokemons);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(View view, int position) {
        Pokemon pokemon = adapter.getPokemon(position);

        Intent intent = new Intent(this, PokemonDetailsActivity.class);
        intent.putExtra("URL", pokemon.getUrl());
        startActivity(intent);
    }
}
