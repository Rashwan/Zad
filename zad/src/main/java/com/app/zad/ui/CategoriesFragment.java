package com.app.zad.ui;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.zad.R;
import com.app.zad.helper.ItemClickSupport;

public class CategoriesFragment extends Fragment {

	public Context mContext;
	RecyclerView categoriesRv;
    public static final String CATEGORY_SHARED_ELEMENT_NAME = "com.app.zad.ui.CategoriesFragment";
    TextView nameTv;
    View shade;


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
                nameTv = (TextView) v.findViewById(R.id.category_name);
                shade = v.findViewById(R.id.category_shade);
                ImageView categoryIv = (ImageView) v.findViewById(R.id.category_image);
                i1.putExtra("categoriesString", nameTv.getText());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    i1.putExtra(CATEGORY_SHARED_ELEMENT_NAME,categoryIv.getTransitionName());
                }


                if (position == 0) {
                    i1.putExtra("categoriesNum", position + 1);
                } else {
                    i1.putExtra("categoriesNum", position + 2);
                }
                nameTv.animate().alpha(0.0f).start();
                shade.animate().alpha(0.0f).start();

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions activityOptions = ActivityOptions.
                            makeSceneTransitionAnimation(getActivity(), categoryIv, categoryIv.getTransitionName());
                    startActivity(i1,activityOptions.toBundle());

                }
                else {
                    startActivity(i1);
                }
            }
        });

		return view;
	}

    @Override
    public void onResume() {
        super.onResume();
        if (nameTv != null) {
            nameTv.animate().alpha(1.0f).start();
            shade.animate().alpha(1.0f).start();
        }
    }
}