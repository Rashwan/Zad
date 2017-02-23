package com.app.zad.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.zad.R;
import com.app.zad.helper.ItemClickSupport;

import java.sql.SQLException;
import java.util.ArrayList;

public class Quotes_Fragment extends Fragment {
	String author_retrived;
	String quote_retrived;
	Integer id;
	String wiki;
	ArrayList<Quote> allQuotesObjects;
	RecyclerView quotesRv;
    private Quotes_List_adapter adapter;


    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.list_quotes, container, false);
        quotesRv = (RecyclerView) view.findViewById(R.id.quotes_rv);
        setupRecyclerView();
		return view;

	}
    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        quotesRv.setHasFixedSize(true);
        quotesRv.setLayoutManager(linearLayoutManager);
        adapter = new Quotes_List_adapter(getActivity(), new ArrayList<Quote>(), false);
        quotesRv.setAdapter(adapter);

        ItemClickSupport.addTo(quotesRv).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                quote_retrived = allQuotesObjects.get(position).Quote;
                author_retrived = allQuotesObjects.get(position).Author;
                Quote thequote = allQuotesObjects.get(position);
                wiki = thequote.getwiki(getActivity(), thequote);
                Intent i1 = new Intent(getActivity(), Quote_view_pager_activity.class);
                i1.putExtra("oneQuote", true);
                i1.putExtra("quoteRetrived", quote_retrived);
                i1.putExtra("authorRetrived", author_retrived);
                i1.putExtra("wiki", wiki);
                startActivity(i1);
            }
        });
    }

        private ArrayList<Quote> generateData() throws SQLException {
		Quote quoteInstance = new Quote();
		allQuotesObjects = quoteInstance.getAllObjects(getActivity());
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
	}


}