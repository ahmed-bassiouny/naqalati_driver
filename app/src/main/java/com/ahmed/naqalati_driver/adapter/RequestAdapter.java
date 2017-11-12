package com.ahmed.naqalati_driver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahmed.naqalati_driver.R;
import com.ahmed.naqalati_driver.helper.Utils;
import com.ahmed.naqalati_driver.model.RequestInfo;
import com.ahmed.naqalati_driver.model.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bassiouny on 13/11/17.
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {

    private List<RequestInfo> requestInfoList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvPhone;
        public CircleImageView profileImage;

        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_name);
            tvPhone = view.findViewById(R.id.tv_phone);
            profileImage=view.findViewById(R.id.profile_image);
        }
    }


    public RequestAdapter(List<RequestInfo> requestInfoList,Context context) {
        this.requestInfoList = requestInfoList;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_request, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RequestInfo requestInfo = requestInfoList.get(position);
        holder.tvName.setText(requestInfo.getUserName());
        holder.tvPhone.setText(requestInfo.getUserPhone());
        if(!requestInfo.getUserImage().isEmpty())
            Utils.showImage(context,requestInfo.getUserImage(),holder.profileImage);
    }

    @Override
    public int getItemCount() {
        return requestInfoList.size();
    }
}
