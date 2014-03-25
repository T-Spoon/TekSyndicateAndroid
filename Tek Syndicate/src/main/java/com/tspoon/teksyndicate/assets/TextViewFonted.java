package com.tspoon.teksyndicate.assets;


import com.tspoon.teksyndicate.R;

import android.app.Notification.Style;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewFonted extends TextView {
	
	private static final int COLOR_WHITE = Color.parseColor("#FFFFFF");
	
	public TextViewFonted(Context context) {
        super(context);
        init(context);
    }

    public TextViewFonted(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextViewFonted(Context context, AttributeSet attrs, int defStyle) {
         super(context, attrs, defStyle);
         init(context);
    }

    private void init(Context context) {
    	Typeface font = Typeface.createFromAsset(context.getAssets(), "DIN1451StdMittelschrift.otf");
    	/*
    	if (this.getTypeface() != null) {
    		font = Typeface.createFromAsset(context.getAssets(), "Purista Bold.otf");
    	} else {
    		font = Typeface.createFromAsset(context.getAssets(), "DIN1451StdMittelschrift.otf");
    	}
    	*/
    	this.setTypeface(font);
    	
    	int textColor = this.getCurrentTextColor();
    	if (textColor != COLOR_WHITE)
    		this.setTextColor(getContext().getResources().getColor(R.color.text_standard));
    	//System.out.println(Integer.toHexString(this.getCurrentTextColor()));
    	//this.setTextColor(getContext().getResources().getColor(R.color.text_default));
    }
}
