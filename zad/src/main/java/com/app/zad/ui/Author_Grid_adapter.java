package com.app.zad.ui;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.zad.R;
import com.app.zad.helper.Drawable_into_Bitmap;
import com.app.zad.helper.GetCroppedBitmap;

public class Author_Grid_adapter extends ArrayAdapter<Author_Grid_Item> {

	private final Context context;
	ArrayList<Author_Grid_Item> itemsArrayList;

	public Author_Grid_adapter(Context context,
			ArrayList<Author_Grid_Item> arrayList) {
		super(context, R.layout.author_grid_item, arrayList);

		this.context = context;
		this.itemsArrayList = arrayList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView;

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		rowView = new View(context);

		int numColumns = rowView.getResources().getInteger(
				R.integer.num_columns);

		if (numColumns == 3) {
			// rowView = inflater.inflate(R.layout.author_list_item, null);
			rowView = inflater.inflate(R.layout.author_grid_item, null);

			TextView textView = (TextView) rowView
					.findViewById(R.id.Author_title);
			ImageView imageView = (ImageView) rowView
					.findViewById(R.id.imageViewAuthor);
			textView.setText(itemsArrayList.get(position).getAuthor_Title());
			Drawable d = itemsArrayList.get(position).getAuthor_Pic();

			Bitmap bm = Drawable_into_Bitmap.drawableToBitmap(d);
			Bitmap cropped = GetCroppedBitmap.getCroppedBitmap(bm);

			imageView.setImageBitmap(cropped);
		} else {

			rowView = inflater.inflate(R.layout.author_grid_item, null);

			TextView textView = (TextView) rowView
					.findViewById(R.id.Author_title);
			ImageView imageView = (ImageView) rowView
					.findViewById(R.id.imageViewAuthor);
			textView.setText(itemsArrayList.get(position).getAuthor_Title());
			imageView.setImageDrawable(itemsArrayList.get(position)
					.getAuthor_Pic());
		}
		return rowView;

	}
}