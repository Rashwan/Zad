package com.app.zad.ui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.zad.R;
import com.app.zad.work_in_background.HttpUtility;

public class AddQuote_Fragment extends Fragment {

	EditText quoteText;
	EditText authorText;
	EditText sourceText;
	EditText otherCategoryText;
	CheckBox politicsCheck;
	CheckBox religionsCheck;
	CheckBox humandevCheck;
	CheckBox wisdomCheck;
	Button submit;
	protected String quote;
	protected String author;
	protected String category = "";
	protected final String DASH = "-";
	protected static final String NAME_FIELD = "entry.1361055940";
	protected final String QUOTE_FIELD = "entry.1865861650";
	protected final String AUTHOR_FIELD = "entry.232540";
	protected final String SOURCE_FIELD = "entry.1865650096";
	protected final String CATEGORY_FIELD = "entry.188899649";
	protected final String REQUESTURL = "https://docs.google.com/forms/d/1bGTUD2eROLnto8lxyQx8Z5NKMHGZ1LwKVLXeiEp-CSw/formResponse";

	protected String source;
	protected Context context;
	private CheckedTextView agree_sending;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.add_quote, container, false);

		quoteText = (EditText) view.findViewById(R.id.quote_text);
		authorText = (EditText) view.findViewById(R.id.author_text);
		sourceText = (EditText) view.findViewById(R.id.quote_source_text);
		otherCategoryText = (EditText) view.findViewById(R.id.otherCategory);
		agree_sending = (CheckedTextView) view.findViewById(R.id.agree_sending);
		final Spinner spinner = (Spinner) view.findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity().getApplicationContext(), R.array.categoriesnew,
				R.layout.zad_text_item);
		final String[] items = getResources()
				.getStringArray(R.array.categories);

		adapter.setDropDownViewResource(R.layout.zad_radio_item);
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				/*
				 * spinner after popup color ((TextView)
				 * spinner.getSelectedView())
				 * .setTextColor(getResources().getColor( R.color.Purple_Deep));
				 */

				if (position == 9) {
					otherCategoryText.setEnabled(true);
					otherCategoryText.requestFocus();
					InputMethodManager imm = (InputMethodManager) getActivity()
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
				} else {
					otherCategoryText.setEnabled(false);
					category = items[position];
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				category = "";

			}
		});
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		submit = (Button) view.findViewById(R.id.submit);

		agree_sending.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (agree_sending.isChecked()) {
					agree_sending.setChecked(false);
				} else {
					agree_sending.setChecked(true);
				}
			}
		});

		submit.setOnClickListener(new View.OnClickListener() {
			private String first_name;
			private String last_name;

			public void onClick(View view) {
				quote = quoteText.getText().toString();
				author = authorText.getText().toString();
				if (quote.isEmpty() || author.isEmpty()) {
					Toast.makeText(getActivity(),
							getString(R.string.not_completed_fields_toast),
							Toast.LENGTH_LONG).show();
		//			Snackbar.make(getActivity().findViewById(android.R.id.content), "WHOOO!", Snackbar.LENGTH_SHORT).show();
				} else {
					source = sourceText.getText().toString();
					final SharedPreferences sharedPreferences = getActivity()
							.getSharedPreferences("firstlastname",
									Context.MODE_PRIVATE);

					if (agree_sending.isChecked()) {
						first_name = sharedPreferences.getString("firstname",
								"");
						last_name = sharedPreferences.getString("secondname",
								"");
					}

					Map<String, String> params = new HashMap<String, String>();
					{
						params.put(QUOTE_FIELD, quote);
						params.put(AUTHOR_FIELD, author);
						params.put(SOURCE_FIELD, source);
						params.put(CATEGORY_FIELD, category);
						params.put(NAME_FIELD, first_name + " " + last_name);
						try {
							HttpUtility.sendPostRequest(REQUESTURL, params);
							HttpUtility.readMultipleLinesRespone();
							Toast.makeText(getActivity(),
									getString(R.string.success_toast),
									Toast.LENGTH_LONG).show();
							category = "";
						} catch (IOException ex) {
							// NEW - MELEGY
							SharedPreferences pref = getActivity()
									.getSharedPreferences("MyPref", 0);
							pref.edit().putBoolean("unsent_new", true).commit();
							Toast.makeText(
									getActivity(),
									R.string.send_when_connected,
									Toast.LENGTH_LONG).show();
							Editor editor = pref.edit();
							for (String s : params.keySet()) {
								editor.putString(s, params.get(s));
							}
							editor.commit();
						}
						// --------------------------------------------------------
						HttpUtility.disconnect();
					}
				}
			}
		});
		return view;
	}
}
