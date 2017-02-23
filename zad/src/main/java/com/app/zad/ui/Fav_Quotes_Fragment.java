package com.app.zad.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArraySet;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.zad.R;
import com.app.zad.helper.ItemClickSupport;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Fav_Quotes_Fragment extends Fragment {
	String author_retrived;
	ArrayList<Quote> allQuotesObjects;
	ArrayList<Quote> object_retrieved;
	SharedPreferences sp;
	Set<String> id;
	List<String> idlist;
	Integer idinteger;
	private RecyclerView favoritesRv;
    Quotes_List_adapter adapter;
    private View emptyFavoritesLayout;


    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_favorite_quotes, container, false);
        favoritesRv = (RecyclerView) view.findViewById(R.id.favorites_rv);
        emptyFavoritesLayout = view.findViewById(R.id.empty_favorites_layout);

        setupRecyclerView();
		return view;

	}
    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        favoritesRv.setHasFixedSize(true);
        favoritesRv.setLayoutManager(linearLayoutManager);
        adapter = new Quotes_List_adapter(getActivity(), new ArrayList<Quote>(), true);
        favoritesRv.setAdapter(adapter);

        ItemClickSupport.addTo(favoritesRv).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent i1 = new Intent(getActivity(), Quote_view_pager_activity.class);
                i1.putExtra("oneQuote", false);
                i1.putExtra("authorRetrived", author_retrived);
                i1.putExtra("favo", true);
                i1.putExtra("pos", position + 1);
                startActivity(i1);
            }
        });

    }


        private ArrayList<Quote> generateData() throws SQLException {
		try {
			allQuotesObjects = new ArrayList<>();
			sp = getActivity().getSharedPreferences("com.app.zad.fav_id", Context.MODE_PRIVATE);
			id = sp.getStringSet("ids", new ArraySet<String>());
			idlist = new ArrayList<>(id);
			for (int i = 0; i < id.size(); i++) {
				idinteger = Integer.parseInt(idlist.get(i));
				Quote quoteInstance = new Quote();
				object_retrieved = quoteInstance.getAnObjects(getActivity(), "ID",
						idinteger);
				try {
					allQuotesObjects.add(object_retrieved.get(0));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return allQuotesObjects;
	}

    @Override
    public void onResume() {
        super.onResume();
        try {
            adapter.clearQuotes();
            adapter.addQuotes(generateData());
            adapter.notifyDataSetChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (adapter.getItemCount() == 0){
            emptyFavoritesLayout.setVisibility(View.VISIBLE);
        }
    }
}