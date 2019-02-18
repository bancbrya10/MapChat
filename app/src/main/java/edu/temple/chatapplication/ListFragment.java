package edu.temple.chatapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class ListFragment extends Fragment implements RecyclerViewItemClicked {
    RecyclerView recyclerView;
    ListAdapter listAdapter;
    ArrayList<String> partners;


    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        Bundle args = getArguments();
        if (args != null) {
            partners = (ArrayList<String>) args.getSerializable("LIST_PARTNERS");
            listAdapter.notifyDataSetChanged();
        }
        recyclerView = v.findViewById(R.id.recycler_view);
        listAdapter = new ListAdapter(partners, this, getContext());
        recyclerView.setAdapter(listAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return v;
    }

    //TODO alter code for actual use
    @Override
    public void userItemClick(View v, int position) {
        Toast.makeText(getContext(), partners.get(position).toString(), Toast.LENGTH_SHORT).show();
    }
}