package com.example.administrator.ccoupons.User.UserCouponFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.ccoupons.Main.Coupon;
import com.example.administrator.ccoupons.R;
import com.example.administrator.ccoupons.User.UserCouponInfoAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/21 0021.
 */

public class OnSaleCouponFragment extends Fragment{


    private UserCouponInfoAdapter adapter;
    private ArrayList<Coupon> mCouponList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_user_onsalecoupon, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.onsale_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new UserCouponInfoAdapter(mCouponList);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void setData(ArrayList<Coupon> cList) {
        this.mCouponList = cList;
    }

}
