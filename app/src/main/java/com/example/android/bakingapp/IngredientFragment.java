package com.example.android.bakingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.adapters.IngListAdapter;
import com.example.android.bakingapp.model.Cake;
import com.example.android.bakingapp.model.Ingredient;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientFragment extends Fragment {

    @BindView(R.id.rv_ing)
    RecyclerView rv_ing;
    IngListAdapter ingListAdapter;

    ArrayList <Ingredient> ingredients = new ArrayList<>();

    public IngredientFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ingredient_fragment, container, false);
        ButterKnife.bind(this, view);
        Cake cake = getActivity().getIntent().getExtras().getParcelable("Cakes");
        ingredients = cake.getIngredients();

        ingListAdapter = new IngListAdapter(ingredients, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rv_ing.setLayoutManager(layoutManager);
        rv_ing.setAdapter(ingListAdapter);
        return view;
    }
}
