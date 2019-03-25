package edu.temple.chatapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;

public class ListFragment extends Fragment implements RecyclerViewItemClicked {
    RecyclerView recyclerView;
    ListAdapter listAdapter;
    ArrayList<String> partners;
    public ListSelectListener lsListener;

    public ListFragment() {
        // Required empty public constructor
    }

    public interface ListSelectListener {
        void selectedPartner(String partnerName);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        lsListener = (ListSelectListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        lsListener = null;
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

    //when user clicks item in list, pass selected user up to parent Activity if their key is saved.
    @Override
    public void userItemClick(View v, int position) {
        String partnerName = partners.get(position).toString();
        File file = new File(getContext().getFilesDir(), partnerName);
        if (file.exists()) {
            lsListener.selectedPartner(partnerName);
        } else {
            Toast.makeText(getContext(), R.string.no_key_error, Toast.LENGTH_LONG).show();
        }
    }
}