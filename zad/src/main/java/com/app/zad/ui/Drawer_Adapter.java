package com.app.zad.ui;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import com.app.zad.R;

public class Drawer_Adapter extends ArrayAdapter<DrawerItem> {

	Context context;
	List<DrawerItem> drawerItemList;
	int layoutResID;
	@SuppressWarnings("unused")
	private int selectedItem;

	public Drawer_Adapter(Context context, int layoutResourceID,
			List<DrawerItem> listItems) {
		super(context, layoutResourceID, listItems);
		this.context = context;
		this.drawerItemList = listItems;
		this.layoutResID = layoutResourceID;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DrawerItemHolder drawerHolder;
		View view = convertView;

		if (view == null) {
			drawerHolder = new DrawerItemHolder();

			LayoutInflater inflater = ((Activity) context).getLayoutInflater();

			view = inflater.inflate(layoutResID, parent, false);
			drawerHolder.ItemName = (CheckedTextView) view
					.findViewById(R.id.title);
			drawerHolder.icon = (ImageView) view.findViewById(R.id.icon);

			view.setTag(drawerHolder);

		} else {
			drawerHolder = (DrawerItemHolder) view.getTag();
		}

		DrawerItem dItem = (DrawerItem) this.drawerItemList.get(position);

		drawerHolder.icon.setImageDrawable(view.getResources().getDrawable(
				dItem.getImgResID()));
		drawerHolder.ItemName.setText(dItem.getItemName());

		// Bug Fuckin Shette Got Damn
		if (drawerHolder.ItemName.isChecked() || parent.isSelected()
				|| view.isSelected()) {
			drawerHolder.ItemName.setTypeface(null, Typeface.BOLD);
			drawerHolder.ItemName.setTextColor(getContext().getResources()
					.getColor(R.color.Purple_Deep));
			drawerHolder.icon.setColorFilter(getContext().getResources()
					.getColor(R.color.Purple_Deep),
					android.graphics.PorterDuff.Mode.SRC_IN);
		}

		else if (!drawerHolder.ItemName.isChecked()) {
			drawerHolder.ItemName.setTypeface(null, Typeface.NORMAL);
			drawerHolder.ItemName.setTextColor(getContext().getResources()
					.getColor(R.color.text_secondary));

			drawerHolder.icon.setColorFilter(getContext().getResources()
					.getColor(R.color.grey_clicked),
					android.graphics.PorterDuff.Mode.SRC_IN);
		}

		else if (position == 0 && !drawerHolder.ItemName.isChecked()) {
			drawerHolder.ItemName.setTypeface(null, Typeface.BOLD);
			drawerHolder.ItemName.setTextColor(getContext().getResources()
					.getColor(R.color.Purple_Deep));
			drawerHolder.icon.setColorFilter(getContext().getResources()
					.getColor(R.color.Purple_Deep),
					android.graphics.PorterDuff.Mode.SRC_IN);
		}

		/*
		 * drawerHolder.ItemName = (CheckedTextView) parent.getChildAt(0);
		 * 
		 * if (!drawerHolder.ItemName.isChecked()) {
		 * drawerHolder.ItemName.setTypeface(null, Typeface.BOLD);
		 * drawerHolder.ItemName.setTextColor(getContext().getResources()
		 * .getColor(R.color.Purple_Deep));
		 * drawerHolder.icon.setColorFilter(getContext().getResources()
		 * .getColor(R.color.Purple_Deep),
		 * android.graphics.PorterDuff.Mode.SRC_IN);
		 * 
		 * }
		 */
		return view;
	}

	public void selectItem(int selectedItem) {
		this.selectedItem = selectedItem;
		notifyDataSetChanged();

	}

	private static class DrawerItemHolder {
		CheckedTextView ItemName;
		ImageView icon;
	}

}