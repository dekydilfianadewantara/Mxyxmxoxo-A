package com.mhanyamoto.mhanyamoto;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by stark on 4/19/2015.
 */
public class PaymentFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        ListView listView = ViewHolder.get(view, R.id.paymentList);
        PaymentListAdapter adapter = new PaymentListAdapter(getActivity());
        listView.setAdapter(adapter);
        return view;
    }
}
