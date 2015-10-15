package com.mhanyamoto.mhanyamoto;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by stark on 4/18/2015.
 */
public class MenuFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        ImageButton bidBtn = ViewHolder.get(view, R.id.biddingBtn);
        ImageButton orderBtn = ViewHolder.get(view, R.id.ordersBtn);
        ImageButton payBtn = ViewHolder.get(view, R.id.paymentBtn);
        ImageButton notifBtn = ViewHolder.get(view, R.id.notifBtn);
        ImageButton navBtn = ViewHolder.get(view, R.id.navigationBtn);

        bidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BidFragment fragment = new BidFragment();
                ((MenuActivity) getActivity()).changeLayout(fragment, MenuActivity.PARENT_MENU);
                ((MenuActivity) getActivity()).setActionTitle("BIDDING");
                ((MenuActivity) getActivity()).showHome(false);
            }
        });

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderFragment fragment = new OrderFragment();
                ((MenuActivity) getActivity()).changeLayout(fragment, MenuActivity.PARENT_MENU);
                ((MenuActivity) getActivity()).setActionTitle("ORDER");
                ((MenuActivity) getActivity()).showHome(false);
            }
        });

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentFragment fragment = new PaymentFragment();
                ((MenuActivity) getActivity()).changeLayout(fragment, MenuActivity.PARENT_MENU);
                ((MenuActivity) getActivity()).setActionTitle("PAYMENT");
                ((MenuActivity) getActivity()).showHome(false);
            }
        });
        return view;
    }

}
