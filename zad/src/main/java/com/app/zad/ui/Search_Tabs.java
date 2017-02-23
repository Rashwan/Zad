package com.app.zad.ui;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.zad.R;
import com.app.zad.helper.ItemClickSupport;
import com.app.zad.helper.SpacesItemDecoration;

import java.sql.SQLException;
import java.util.ArrayList;

public class Search_Tabs extends AppCompatActivity {
	private FragmentTabHost mTabHost;
	android.support.v7.widget.SearchView mSearchView = null;
	String mQuery = "";
    static RecyclerView quotesRv;
    static RecyclerView authorsRv;
	static ArrayList<String> all;
	private static ArrayList<Quote> allQuotesObjects;
	static ArrayList<Author_Grid_Item> Authors_items;
	static ArrayList<Quote> tempArrayList = new ArrayList<>();
	static ArrayList<Author_Grid_Item> tempAuthorArrayList = new ArrayList<>();
    Quotes_List_adapter myadapter;
	MenuItem searchItem;
	public  Context mContext;

	static String quote_retrived1;
	static String author_retrieved1;
	static private String author_retrived2;
	int numColumns = 1;
    AuthorsAdapter authorsAdapter;
	SearchManager searchManager;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_tasty);
        mContext = this;
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
		Window window;

		if (android.os.Build.VERSION.SDK_INT >= 21) {
			window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(ContextCompat.getColor(this,R.color.Purple_Deep_Black));
		}

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setHomeButtonEnabled(true);

		String query = getIntent().getStringExtra(SearchManager.QUERY);
		query = query == null ? "" : query;
		mQuery = query;

		View tabview1 = createTabView(mTabHost.getContext(), getString(R.string.quotes));
		View tabview2 = createTabView(mTabHost.getContext(), getString(R.string.authors));
		mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.quotes)).setIndicator(tabview1),
				Search_Tabs.Quotes_Fragment_Own.class, null);
		mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.authors)).setIndicator(tabview2),
				Search_Tabs.Authors_Fragment_Own.class, null);


		if (mSearchView != null) {
			mSearchView.setQuery(query, false);
		}

		overridePendingTransition(0, 0);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		onNewIntent(getIntent());

	}

	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context)
				.inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);

		tv.setText(text);
		return view;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        searchItem = menu.findItem(R.id.menu_search);
        if (searchItem != null) {
            searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            final android.support.v7.widget.SearchView view = (android.support.v7.widget.SearchView) searchItem.getActionView();
            mSearchView = view;

            if (view != null) {
                view.setSearchableInfo(searchManager
                        .getSearchableInfo(getComponentName()));
                view.setIconified(false);
                view.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        view.clearFocus();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        int textlength = newText.length();
                        tempArrayList = new ArrayList<>();
                        tempAuthorArrayList = new ArrayList<>();

                        if (mTabHost.getCurrentTab() == 0) {

                            for (Quote c : allQuotesObjects) {
                                if (textlength <= c.Quote.length()) {
                                    if (c.Quote.contains(newText)) {
                                        tempArrayList.add(c);
                                    }
                                }
                            }
                            myadapter = new Quotes_List_adapter(mContext
                                    , tempArrayList,
                                    true);
                            quotesRv.setAdapter(myadapter);
                        } else {
                            for (Author_Grid_Item c2 : Authors_items) {
                                if (textlength <= c2.getAuthor_Title().length()) {
                                    if (c2.getAuthor_Title().contains(
                                            newText)) {
                                        tempAuthorArrayList.add(c2);
                                    }
                                }
                            }
                            authorsAdapter = new AuthorsAdapter(mContext, tempAuthorArrayList);
                            authorsRv.setAdapter(authorsAdapter);
                        }
                        return true;
                    }
                });


                mSearchView.setOnCloseListener(new android.support.v7.widget.SearchView.OnCloseListener() {
                    @Override
                    public boolean onClose() {
                        finish();
                        return false;
                    }
                });

                if (!TextUtils.isEmpty(mQuery)) {
                    view.setQuery(mQuery, false);
                }
            }
            int searchPlateId = mSearchView.getContext().getResources()
                    .getIdentifier("android:id/search_plate", null, null);
            View searchPlate = mSearchView.findViewById(searchPlateId);
            if (searchPlate != null) {
                searchPlate.setBackground(ContextCompat.getDrawable(this,
                        R.drawable.transparent));

                int searchTextId = searchPlate.getContext().getResources()
                        .getIdentifier("android:id/search_src_text", null, null);

                TextView searchText = (TextView) searchPlate
                        .findViewById(searchTextId);
			/*
			 * RelativeLayout.LayoutParams lps = new
			 * RelativeLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT,
			 * ViewGroup.LayoutParams.WRAP_CONTENT);
			 * lps.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			 * lps.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			 */
                // lps.setMargins(0, 0, 0, 0);

                if (searchText != null) {

                    searchText.setTextColor(ContextCompat.getColor(this,
                            R.color.text__inverse));
                    searchText.setHintTextColor(ContextCompat.getColor(this,
                            R.color.text_hint));
                }

                int closeButtonId = getResources().getIdentifier(
                        "android:id/search_close_btn", null, null);
                ImageView closeButtonImage = (ImageView) mSearchView
                        .findViewById(closeButtonId);
                closeButtonImage.setImageResource(R.drawable.tour_icon_close);

            }
            return true;
        }
        return false;
    }


	@Override
	protected void onPause() {
		super.onPause();
		if (isFinishing()) {
			overridePendingTransition(0, 0);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_search) {
			return true;
		}

		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public static class Quotes_Fragment_Own extends Fragment {

		private String wiki;
        private Quotes_List_adapter adapter;
        @Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.list_quotes_search,
					container, false);
			quotesRv = (RecyclerView) view.findViewById(R.id.quotes_search_rv);
            setupRecyclerView();
			return view;
		}
        private void setupRecyclerView() {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            quotesRv.setHasFixedSize(true);
            quotesRv.setLayoutManager(linearLayoutManager);
            try {
                adapter = new Quotes_List_adapter(getActivity(), generateData(), true);
                quotesRv.setAdapter(adapter);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            ItemClickSupport.addTo(quotesRv).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    if (!tempArrayList.isEmpty()) {
                        quote_retrived1 = tempArrayList.get(position).Quote;
                        author_retrieved1 = tempArrayList.get(position).Author;
                        wiki = tempArrayList.get(position).getwiki(getActivity(),
                                tempArrayList.get(position));
                    }else {
                        quote_retrived1 = adapter.getItem(position).Quote;
                        author_retrieved1 = adapter.getItem(position).Author;
                        wiki = adapter.getItem(position).getwiki(getActivity(),
                                adapter.getItem(position));
                    }

                    Intent i1 = new Intent(getActivity(),
                            Quote_view_pager_activity.class);
                    i1.putExtra("oneQuote", true);
                    i1.putExtra("quoteRetrived", quote_retrived1);
                    i1.putExtra("authorRetrived", author_retrieved1);
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
	}

	public static class Authors_Fragment_Own extends Fragment {

        private AuthorsAdapter adapter;

        @Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.grid_view_search, container,
					false);

			adapter = new AuthorsAdapter(getActivity(), generateData());
			authorsRv = (RecyclerView) view.findViewById(R.id.authors_search_rv);
            setupRecyclerView();
			return view;
		}
        private void setupRecyclerView() {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);

            authorsRv.setHasFixedSize(true);
            authorsRv.setLayoutManager(gridLayoutManager);
            authorsRv.addItemDecoration(new SpacesItemDecoration(20));
            adapter = new AuthorsAdapter(getActivity(), generateData());
            authorsRv.setAdapter(adapter);
            ItemClickSupport.addTo(authorsRv).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    if (!tempAuthorArrayList.isEmpty()) {
                        author_retrived2 = tempAuthorArrayList.get(position)
                                .getAuthor_Title();
                    }else {
                        author_retrived2 = adapter.getItem(position).getAuthor_Title();
                    }
                    Intent i1 = new Intent(getActivity(),
                            Authors_list_quotes_notBoring.class);
                    i1.putExtra("authorRetrieved", author_retrived2);
                    startActivity(i1);
                }
            });
        }
		private ArrayList<Author_Grid_Item> generateData() {
			Authors_items = new ArrayList<>();

			all = new ArrayList<>(Magic_Activity.allunknowncleaned);
			all.addAll(Magic_Activity.allknowncleaned);
			for (int i = 0; i < Magic_Activity.allunknowncleaned.size(); i++) {
				Authors_items
						.add(new Author_Grid_Item(
								Magic_Activity.allunknowncleaned.get(i),
								Magic_Activity.PicsArray[Magic_Activity.PicsArray.length - 1]));
			}
			for (int i = 0; i < Magic_Activity.allknowncleaned.size(); i++) {
				Authors_items.add(new Author_Grid_Item(
						Magic_Activity.allknowncleaned.get(i),
						Magic_Activity.PicsArray[i]));
			}

			return Authors_items;
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("sv1", mSearchView.getQuery().toString());
	}


}


