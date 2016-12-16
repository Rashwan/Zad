package com.app.zad.ui;

public class Quotes_List_Item {

	private String Quote_Text;
	private String Author_Name;


	public Quotes_List_Item(String Quote_Text, String Author_Name) {
		super();
		this.Quote_Text = Quote_Text;
		this.Author_Name = Author_Name;
	}

	public String getQuote_Text() {
		return Quote_Text;
	}

	public void setQuote_Text(String quote_Text) {
		Quote_Text = quote_Text;
	}

	public String getAuthor_Name() {
		return Author_Name;
	}

	public void setAuthor_Name(String author_Name) {
		Author_Name = author_Name;
	}

}