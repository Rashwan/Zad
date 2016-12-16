package com.app.zad.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.zad.R;
import com.app.zad.helper.FlipImageView;

public class Quotes_List_adapter extends ArrayAdapter<Quote> implements
		Filterable {

	private static String idfavstring;
	private final Context contextx;
	ArrayList<Quote> itemsArrayList;
	boolean isFavFrag = true;
	Context mContext = getContext();

	SharedPreferences sp = mContext.getSharedPreferences("com.app.zad.fav_id",
			Context.MODE_PRIVATE);
	SharedPreferences.Editor editor = sp.edit();
	Set<String> ids;
	ArrayList<String> idlist;
	boolean fav_not;
	@SuppressWarnings("unused")
	private boolean favcheck;
	private View x = null;

	public Quotes_List_adapter(Context context, ArrayList<Quote> arrayList,
			boolean b) {
		super(context, R.layout.quote_list_item, arrayList);

		this.contextx = context;
		this.itemsArrayList = arrayList;
		this.isFavFrag = b;
	}

	public void Quotes_List_adapterUpdate(ArrayList<Quote> arrayList2) {

		this.itemsArrayList = arrayList2;
	}

	@Override
	public Filter getFilter() {
		Filter QuoteFilter = null;
		if (QuoteFilter == null)
			QuoteFilter = new QuoteFilter();
		return QuoteFilter;
	}

	// //////////////////
	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) contextx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate(R.layout.quote_list_item, null);

			holder = new ViewHolder();
			holder.Quote_text = (TextView) convertView
					.findViewById(R.id.Quote_text);
			holder.Author_title_text = (TextView) convertView
					.findViewById(R.id.Author_title_text);
			holder.Fav_Button = (FlipImageView) convertView
					.findViewById(R.id.Star_Button);
			holder.Fav_lay = (RelativeLayout) convertView
					.findViewById(R.id.fav_layout);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Integer favid = itemsArrayList.get(position).ID;
		holder.Quote_text.setText(itemsArrayList.get(position).Quote);
		holder.Author_title_text.setText(itemsArrayList.get(position).Author);
		// works but, with mistaken Quote
		// x = holder.Fav_lay;

		if (isFav(favid)) {
			holder.Fav_Button.setRotationReversed(true);
			holder.Fav_Button.setChecked(true);
			favcheck = true;

		} else {
			holder.Fav_Button.setRotationReversed(false);
			holder.Fav_Button.setChecked(false);
			favcheck = false;
		}

		holder.Fav_Button.setOnClickListener(new OnClickListener() {
			Quote quote1 = itemsArrayList.get(position);
			Integer idfav = quote1.ID;
			String idfavstring = idfav.toString();

			@Override
			public void onClick(View v) {
				if (holder.Fav_Button.isRotationReversed() == true) {

					holder.Fav_Button.setRotationReversed(false);
					holder.Fav_Button.setChecked(false);

					ids.remove(idfavstring);
					favcheck = false;

				} else if (holder.Fav_Button.isRotationReversed() == false) {

					holder.Fav_Button.setRotationReversed(true);
					holder.Fav_Button.setChecked(true);
					ids.add(idfavstring);
					favcheck = true;
				}

				editor.clear();
				editor.putStringSet("ids", ids);
				editor.commit();

				if (isFavFrag) {

					Quote item = Quotes_List_adapter.this.getItem(position);
					Quotes_List_adapter.this.remove(item);

					Quotes_List_adapter.this.notifyDataSetChanged();

				}

			}
		});
		return convertView;

	}

	private static class ViewHolder {
		public TextView Quote_text;
		public TextView Author_title_text;
		public FlipImageView Fav_Button;
		public RelativeLayout Fav_lay;

	}

	/*
	 * //
	 * //////////////////////xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
	 * // @Override public View getViewXX(final int position, View convertView,
	 * ViewGroup parent) {
	 * 
	 * LayoutInflater inflater = (LayoutInflater) context
	 * .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 * 
	 * View rowView = inflater .inflate(R.layout.quote_list_item, parent,
	 * false);
	 * 
	 * TextView Quote_text = (TextView) rowView.findViewById(R.id.Quote_text);
	 * TextView Name_Author = (TextView) rowView
	 * .findViewById(R.id.Author_title_text); final FlipImageView Star_Button =
	 * (FlipImageView) rowView .findViewById(R.id.Star_Button);
	 * 
	 * Integer favid = itemsArrayList.get(position).ID;
	 * Quote_text.setText(itemsArrayList.get(position).Quote);
	 * Name_Author.setText(itemsArrayList.get(position).Author);
	 * 
	 * if (isFav(favid)) { Star_Button.setRotationReversed(true);
	 * Star_Button.setChecked(true); favcheck = true; } else {
	 * Star_Button.setRotationReversed(false); Star_Button.setChecked(false);
	 * favcheck = false; }
	 * 
	 * Star_Button.setOnClickListener(new OnClickListener() { Quote quote1 =
	 * itemsArrayList.get(position); Integer idfav = quote1.ID; String
	 * idfavstring = idfav.toString();
	 * 
	 * @Override public void onClick(View v) { //
	 * XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX KAZAKY if
	 * (Star_Button.isRotationReversed() == true) {
	 * 
	 * Star_Button.setRotationReversed(false); Star_Button.setChecked(false);
	 * ids.remove(idfavstring); favcheck = false;
	 * 
	 * } else if (Star_Button.isRotationReversed() == false) {
	 * 
	 * Star_Button.setRotationReversed(true); Star_Button.setChecked(true);
	 * ids.add(idfavstring); favcheck = true; }
	 * 
	 * editor.clear(); editor.putStringSet("ids", ids); editor.commit();
	 * 
	 * if (isFavFrag) { // itemsArrayList.remove(position); Quote item =
	 * Quotes_List_adapter.this.getItem(position);
	 * Quotes_List_adapter.this.remove(item);
	 * Quotes_List_adapter.this.notifyDataSetChanged(); }
	 * 
	 * } });
	 * 
	 * // makes padding in Case of it's not Fvourite list // deprecated from Zad
	 * /* if (isFavFrag) {
	 * 
	 * float scale = Quote_text.getResources().getDisplayMetrics().density; int
	 * dpAsPixels = (int) (16 * scale + 0.5f);
	 * Star_Button.setVisibility(View.GONE); Quote_text
	 * .setPaddingRelative(dpAsPixels, dpAsPixels, dpAsPixels, 0); }
	 */
	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX KAZAKY
	/*
	 * return rowView; }
	 */

	public Boolean isFav(Integer idInteger) {
		idfavstring = idInteger.toString();
		ids = sp.getStringSet("ids", new HashSet<String>());
		idlist = new ArrayList<String>(ids);
		fav_not = idlist.contains(idfavstring);
		return fav_not;
	}

	private class QuoteFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			if (constraint == null || constraint.length() == 0) {
				results.values = itemsArrayList.iterator();
				results.count = itemsArrayList.size();
			} else {
				ArrayList<Quote> nPlanetList = new ArrayList<Quote>();
				Quote p;
				for (int i = 0; i < itemsArrayList.size(); i++) {
					p = itemsArrayList.get(i);
					if (p.Quote.startsWith(constraint.toString()))
						nPlanetList.add(p);
				}

				results.values = nPlanetList.iterator();
				results.count = nPlanetList.size();

			}
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			if (results.count == 0)
				notifyDataSetInvalidated();
			else {
				itemsArrayList = (ArrayList<Quote>) results.values;
				notifyDataSetChanged();
			}
		}
	}
}