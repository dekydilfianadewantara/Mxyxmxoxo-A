package com.mhanyamoto.mhanyamoto;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mhanyamoto.mhanyamoto.entity.Bid;
import com.mhanyamoto.mhanyamoto.service.Service;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by stark on 4/19/2015.
 */
public class OrderFragment extends Fragment {

    Service mService;
    View view;
    LinearLayout error_layout;
    ListView bidList;
    Button b_refresh;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bidding, container, false);
        bidList = ViewHolder.get(view, R.id.bidList);
        error_layout = ViewHolder.get(view, R.id.error_layout);
        b_refresh = ViewHolder.get(view, R.id.b_refresh);

        recall();

        b_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recall();
            }
        });


        return view;
    }

    private void initAPI() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(getResources().getString(R.string.host))
                .build();

        mService = restAdapter.create(Service.class);
    }

    private void hawkInit() {
        Hawk.init(getActivity())
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSqliteStorage(getActivity()))
                .setLogLevel(LogLevel.FULL)
                .build();
    }
    private void recall(){

        //Progress Bar
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        hawkInit();
        String idUser = Hawk.get("idUser", "0");

        initAPI();
        mService.getOrder(idUser ,new Callback<List<Bid>>() {
            @Override
            public void success(List<Bid> bid, Response response) {
                System.out.println("SUKSES");


                OrderListAdapter adapter = new OrderListAdapter(getActivity(), bid);
                bidList.setAdapter(adapter);

                mProgressDialog.dismiss();
                error_layout.setVisibility(View.GONE);
                bidList.setVisibility(View.VISIBLE);
            }

            @Override
            public void failure(RetrofitError error) {
                error_layout.setVisibility(View.VISIBLE);
                bidList.setVisibility(View.GONE);
                Runnable progressRunnable = new Runnable() {

                    @Override
                    public void run() {
                        mProgressDialog.cancel();
                        error_layout.setVisibility(View.VISIBLE);
                    }
                };

                Handler pdCanceller = new Handler();
                pdCanceller.postDelayed(progressRunnable, 3000);
            }
        });
    }
}
