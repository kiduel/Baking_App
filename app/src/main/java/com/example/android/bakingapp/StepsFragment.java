package com.example.android.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.adapters.StepListAdapter;
import com.example.android.bakingapp.model.Cake;
import com.example.android.bakingapp.model.Step;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class StepsFragment extends Fragment {
    @BindView(R.id.rv_step)
    RecyclerView rv_step;
    @BindView(R.id.cake_name)
    TextView cake_title;

    StepListAdapter stepListAdapter;
    ArrayList <Step> steps = new ArrayList<>();

    OnClickStepListener onClickStepListener;
    public StepsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.steps_fragment, container, false);
        ButterKnife.bind(this, view);
        Cake cake = getActivity().getIntent().getExtras().getParcelable("Cakes");
        steps = cake.getSteps();

        cake_title.setText(cake.getName());


        final StepListAdapter.MyClickListener listener = new StepListAdapter.MyClickListener() {
            @Override
            public void onStepSelected(ArrayList<Step> steps, Step single_step, String videoURL, String description) {
                onClickStepListener.onStepSelected(steps, single_step, videoURL, description);
            }
        };

        stepListAdapter = new StepListAdapter(steps, getContext(), listener);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_step.setLayoutManager(layoutManager);
        rv_step.setAdapter(stepListAdapter);
        RecipeWidgetService.startActionRecipeUpdate(getActivity(), cake);
        return view;
    }

    /*
    Interface to be implemented
     */
    public interface OnClickStepListener{
         void onStepSelected(ArrayList <Step> steps, Step single_step, String videoURL, String description);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onClickStepListener = (OnClickStepListener) context;

         try {
             onClickStepListener = (OnClickStepListener) context;
         } catch (ClassCastException e) {
             throw new ClassCastException(context.toString() + "needs to implement OnClickStepListener");
         }
    }
}
