package com.app.zad.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.zad.R;
import com.app.zad.helper.FlipImageView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Quotes_List_adapter extends RecyclerView.Adapter<Quotes_List_adapter.QuoteVH>
		implements Filterable{

    private ArrayList<Quote> QuotesList;
	private boolean isFavFrag = true;
	Context mContext ;

	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private Set<String> ids;

    public Quotes_List_adapter(Context context, ArrayList<Quote> quotes,
			boolean b) {

		this.QuotesList = quotes;
		this.isFavFrag = b;
		sp = context.getSharedPreferences("com.app.zad.fav_id",
                Context.MODE_PRIVATE);

	}


	@Override
	public Filter getFilter() {
		Filter QuoteFilter;
		QuoteFilter = new QuoteFilter();
		return QuoteFilter;
	}
	@Override
	public QuoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
		mContext = parent.getContext();
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.quote_list_item, parent, false);
        ids = sp.getStringSet("ids", new HashSet<String>());

		return new QuoteVH(view);
	}

	@Override
	public void onBindViewHolder(final QuoteVH holder, int position) {
		Integer favid = QuotesList.get(position).ID;
		Quote quote = QuotesList.get(position);

		holder.Quote_text.setText(quote.Quote);
		holder.Author_title_text.setText(quote.Author);

		if (isFav(favid)) {
			holder.Fav_Button.setRotationReversed(true);
			holder.Fav_Button.setChecked(true);

		} else {
			holder.Fav_Button.setRotationReversed(false);
			holder.Fav_Button.setChecked(false);
		}

		holder.Fav_Button.setOnClickListener(new OnClickListener() {
			Quote quote1 = QuotesList.get(holder.getAdapterPosition());
			Integer idfav = quote1.ID;
			String idfavstring = idfav.toString();

			@Override
			public void onClick(View v) {
				if (holder.Fav_Button.isRotationReversed()) {

					holder.Fav_Button.setRotationReversed(false);
					holder.Fav_Button.setChecked(false);

					ids.remove(idfavstring);

				} else if (!holder.Fav_Button.isRotationReversed()) {

					holder.Fav_Button.setRotationReversed(true);
					holder.Fav_Button.setChecked(true);
					ids.add(idfavstring);
				}
                editor = sp.edit();
				editor.clear();
				editor.putStringSet("ids", ids);
				editor.apply();

				if (isFavFrag) {

					QuotesList.remove(QuotesList.get(holder.getAdapterPosition()));
					notifyItemRemoved(holder.getAdapterPosition());

				}

			}
		});
	}
    private Boolean isFav(Integer idInteger) {
        return ids.contains(idInteger.toString());
    }

	@Override
	public int getItemCount() {
		return QuotesList.size();
	}
    public void addQuotes(List<Quote> quotes){
        this.QuotesList.addAll(quotes);
    }
    public void clearQuotes(){
        this.QuotesList.clear();
    }

	public static class QuoteVH extends RecyclerView.ViewHolder{
		 TextView Quote_text;
		 TextView Author_title_text;
		 FlipImageView Fav_Button;
		 RelativeLayout Fav_lay;

		 QuoteVH(View itemView) {
			super(itemView);
			Quote_text = (TextView) itemView
					.findViewById(R.id.Quote_text);
			Author_title_text = (TextView) itemView
					.findViewById(R.id.Author_title_text);
			Fav_Button = (FlipImageView) itemView
					.findViewById(R.id.Star_Button);
			Fav_lay = (RelativeLayout) itemView
					.findViewById(R.id.fav_layout);
		}
	}

	private class QuoteFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			if (constraint == null || constraint.length() == 0) {
				results.values = QuotesList.iterator();
				results.count = QuotesList.size();
			} else {
				ArrayList<Quote> nPlanetList = new ArrayList<>();
				Quote p;
				for (int i = 0; i < QuotesList.size(); i++) {
					p = QuotesList.get(i);
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
				notifyDataSetChanged();
			else {
				QuotesList = (ArrayList<Quote>) results.values;
				notifyDataSetChanged();
			}
		}
	}
}