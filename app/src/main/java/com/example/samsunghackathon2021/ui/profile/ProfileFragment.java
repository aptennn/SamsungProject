package com.example.samsunghackathon2021.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsunghackathon2021.R;
import com.example.samsunghackathon2021.State;
import com.example.samsunghackathon2021.StateAdapter;
import com.example.samsunghackathon2021.databinding.FragmentProfileBinding;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;
    ArrayList<State> states = new ArrayList<State>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setInitialData();
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.list);
        // создаем адаптер
        StateAdapter adapter = new StateAdapter(getContext(), states);
        // устанавливаем для списка адаптер
        recyclerView.setAdapter(adapter);


        return root;
    }

    private void setInitialData(){

        states.add(new State ("Бразилия", "Бразилиа", R.drawable.baseline_home_24));
        states.add(new State ("Аргентина", "Буэнос-Айрес", R.drawable.baseline_house_siding_24));
        states.add(new State ("Колумбия", "Богота", R.drawable.baseline_agriculture_24));
        states.add(new State ("Уругвай", "Монтевидео", R.drawable.baseline_window_24));
        states.add(new State ("Чили", "Сантьяго", R.drawable.ic_baseline_ac_unit_24));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}