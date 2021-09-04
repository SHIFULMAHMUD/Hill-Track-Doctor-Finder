package com.android.hilltrackdoctorfinder.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.model.ScreenItem;

import java.util.List;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class AdapterPagerIntro extends PagerAdapter {
    Context context;
    List<ScreenItem> screenItemList;

    public AdapterPagerIntro(Context context, List<ScreenItem> screenItemList) {
        this.context = context;
        this.screenItemList = screenItemList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_intro, null);
        TextView textViewIntro = view.findViewById(R.id.textView_intro);
        ImageView imageViewIntro;
        imageViewIntro = view.findViewById(R.id.imageView_intro);
        imageViewIntro.setImageResource(screenItemList.get(position).getImg());
        if (position == 0) {
            textViewIntro.setText(context.getResources().getString(R.string.first_text));
        } else if (position == 1) {
            textViewIntro.setText(textStyleChanging(context.getResources().getString(R.string.second_text), 4, 8));
        } else if (position == 2) {
            textViewIntro.setText(textStyleChanging(context.getResources().getString(R.string.third_text), 0, 5));
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return screenItemList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    Spannable textStyleChanging(String details, int start, int end) {
        Spannable wordGreen = new SpannableString(details);
        wordGreen.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return wordGreen;
    }
}
