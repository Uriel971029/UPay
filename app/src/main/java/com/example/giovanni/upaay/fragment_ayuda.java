package com.example.giovanni.upaay;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class fragment_ayuda extends Fragment {

    private View RootView;
    private ViewPager viewPager;
    private SlideAdapter myAdapter;
    private LinearLayout mDotLayout;
    private TextView[] mDots;
    private Button btnP, btnN;
    private int currentPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RootView=inflater.inflate(R.layout.ventana_ayuda, container, false);
        mDotLayout = (LinearLayout) RootView.findViewById(R.id.dotsLayout);
        viewPager = (ViewPager)RootView.findViewById(R.id.viewpager);
        myAdapter = new SlideAdapter(this.getContext());
        btnP = (Button)RootView.findViewById(R.id.btnPrev);
        btnN = (Button)RootView.findViewById(R.id.btnNext);
        viewPager.setAdapter(myAdapter);

        addDotsIndicators(0);
        viewPager.addOnPageChangeListener(viewListener);

        btnN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewPager.setCurrentItem(currentPage + 1);
            }
        });

        btnP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 viewPager.setCurrentItem(currentPage - 1);
            }
        });

        return RootView;
    }



    public void addDotsIndicators(int position){

        mDots = new TextView[3];
        //PARA EVITAR LA DUPLICIDAD DE VISTAS
        mDotLayout.removeAllViews();

        for (int i = 0; i < mDots.length; i++){

            mDots[i] = new TextView(getContext());
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
            mDotLayout.addView(mDots[i]);
        }

        if (mDots.length > 0){

            mDots[position].setTextColor(getResources().getColor(R.color.background_material_light));
        }


    }

    ViewPager.OnPageChangeListener viewListener =  new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDotsIndicators(position);
            currentPage = position;

            if (position == 0){

                btnN.setEnabled(true);
                btnP.setEnabled(false);
                btnP.setVisibility(View.INVISIBLE);
                btnP.setText("Siguiente");
                btnP.setText("");

            }else if(position == mDots.length - 1){

                btnN.setEnabled(true);
                btnP.setEnabled(true);
                btnP.setVisibility(View.VISIBLE);

                btnP.setText("Regresar");
                btnN.setText("Finish");

            } else {

                btnN.setEnabled(true);
                btnP.setEnabled(true);
                btnP.setVisibility(View.VISIBLE);

                btnP.setText("Regresar");
                btnN.setText("Siguiente");

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
