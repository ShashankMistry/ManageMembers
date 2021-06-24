package com.shashank.managemembers.FragmentDialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.shashank.managemembers.R;

import java.util.Objects;

public class OfferDialog extends DialogFragment {


    private static final String TAG = "OfferDialog";

    public interface OnInputListener {
        void sendInput(String send);
    }
    OnInputListener mOnInputListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sms_dialog, container, false);
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        EditText sms = rootView.findViewById(R.id.offer);
        Button smsSend = rootView.findViewById(R.id.offerSend);
        smsSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sms.getText().toString().equals("")){
                    sms.setError("type something");
                } else {
                    mOnInputListener.sendInput(sms.getText().toString());
                    Objects.requireNonNull(getDialog()).dismiss();
                }
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        try {
            mOnInputListener = (OfferDialog.OnInputListener) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage());
        }
    }
}
