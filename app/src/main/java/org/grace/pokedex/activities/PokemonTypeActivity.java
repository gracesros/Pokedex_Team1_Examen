package org.grace.pokedex.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.grace.pokedex.R;
import org.grace.pokedex.adapters.DamageRelationAdapter;
import org.grace.pokedex.adapters.PokemonAdapter;
import org.grace.pokedex.models.Pokemon;
import org.grace.pokedex.models.PokemonType;
import org.grace.pokedex.interfaces.AsyncTaskHandler;
import org.grace.pokedex.network.PokemonTypeAsyncTask;

public class PokemonTypeActivity extends AppCompatActivity implements AsyncTaskHandler, PokemonAdapter.ItemClickListener {

    TextView name;
    RecyclerView damageRelations;
    RecyclerView pokemons;
    PokemonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_type);

        name = findViewById(R.id.type_name);
        damageRelations = findViewById(R.id.type_damage_relations);
        pokemons = findViewById(R.id.type_pokemons);

        String type = getIntent().getStringExtra("TYPE");
        String url = "https://pokeapi.co/api/v2/type/" + type;

        PokemonTypeAsyncTask pokemonTypeAsyncTask = new PokemonTypeAsyncTask();
        pokemonTypeAsyncTask.handler = this;
        pokemonTypeAsyncTask.execute(url);
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
    public void onTaskEnd(Object result) {
        PokemonType pokemonType = (PokemonType) result;

        name.setText(pokemonType.getName());

        damageRelations.setLayoutManager(new LinearLayoutManager(this));
        damageRelations.setAdapter(new DamageRelationAdapter(this, pokemonType));

        pokemons.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new PokemonAdapter(this, pokemonType.getPokemons());
        adapter.setClickListener(this);
        pokemons.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Pokemon pokemon = adapter.getPokemon(position);

        Intent intent = new Intent(this, PokemonDetailsActivity.class);
        intent.putExtra("URL", pokemon.getUrl());
        startActivity(intent);
    }
}
