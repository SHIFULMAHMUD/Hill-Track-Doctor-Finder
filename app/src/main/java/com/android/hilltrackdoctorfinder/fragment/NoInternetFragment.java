package com.android.hilltrackdoctorfinder.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.utils.CheckStatus;
import com.android.hilltrackdoctorfinder.utils.Tools;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

public class NoInternetFragment extends DialogFragment {


    AppCompatButton tab_to_retry;


    public NoInternetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_dialog_no_internet, container, false);


        Tools.setSystemBarColor(getActivity(),R.color.colorWhite);
        Tools.setSystemBarLight(getActivity());

        tab_to_retry=view.findViewById(R.id.id_no_internet_tab_to_retry_btn);

        tab_to_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (CheckStatus.network(getActivity())){
                    Tools.setSuccessToast(getActivity(),"Internet Connected");
                    dismiss();
                }else {
                    Tools.setErrorToast(getActivity(),"Check Your Internet Connection");
                }
            }
        });
        return view;
    }
}
