package com.example.aliaslight.ui.main;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.aliaslight.R;
import com.example.aliaslight.databinding.FragmentMainBinding;

public class MainFragment extends Fragment {

    private FragmentMainBinding bind;

    private AliasViewModel vm;

//    Button btnNext;
//    TextView tvHints;
//    EditText etAlias;

    public static MainFragment newInstance() {
        return new MainFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new ViewModelProvider(this).get(AliasViewModel.class);
        // TODO: Use the ViewModel
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        bind = FragmentMainBinding.inflate(
                inflater, container, false);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        btnNext = view.findViewById(R.id.btnNext);
//        tvHints = view.findViewById(R.id.tvHints);
//        etAlias = view.findViewById(R.id.etAlias);

        initListeners();
        initObservers();
    }

    private void initObservers() {
        vm.getHints().observe(getViewLifecycleOwner(), hints -> {
            String text = String.join("\n", hints);
            bind.tvHints.setText(text);
        });

        vm.getSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success != null) {
                if (success)
                    Toast.makeText(getContext(), "YESSSS...!!!", LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "No....", LENGTH_SHORT).show();
            }
        });
    }

    private void initListeners() {
        bind.btnNext.setOnClickListener(v->{
            String alias = bind.btnNext.getText().toString();
            if (alias.isBlank()) { // no answer yet
                vm.nextHint();
            } else {
                vm.check(alias);
            }
        });
    }

}