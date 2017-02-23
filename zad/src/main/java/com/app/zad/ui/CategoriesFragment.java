package com.app.zad.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.zad.R;
import com.app.zad.helper.ItemClickSupport;

public class CategoriesFragment extends Fragment {

	public Context mContext;
	RecyclerView categoriesRv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.categories_fragment, container,
				false);
		categoriesRv = (RecyclerView) view.findViewById(R.id.categories_rv);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(),1);
        categoriesRv.setHasFixedSize(true);
        categoriesRv.setLayoutManager(linearLayoutManager);
        final CategoriesAdapter adapter = new CategoriesAdapter(getActivity());
        categoriesRv.setAdapter(adapter);
        ItemClickSupport.addTo(categoriesRv).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent i1 = new Intent(getActivity(),
                        CategoriesListQuotesNotBoring.class);
                TextView nameTv = (TextView) v.findViewById(R.id.category_name);
                i1.putExtra("categoriesString", nameTv.getText());
                if (position == 0) {
                    i1.putExtra("categoriesNum", position + 1);
                } else {
                    i1.putExtra("categoriesNum", position + 2);
                }
                startActivity(i1);
            }
        });

		return view;
	}

}