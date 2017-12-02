package com.bassiouny.naqalati_driver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bassiouny.naqalati_driver.R;
import com.bassiouny.naqalati_driver.helper.Utils;
import com.bassiouny.naqalati_driver.model.RequestListener;
import com.bassiouny.naqalati_driver.model.RequestInfo;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bassiouny on 13/11/17.
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {

    private List<RequestInfo> requestInfoList;
    private List<String> requestInfoKeyList;
    private Context context;
    private RequestListener requestListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvPhone;
        public Button btnShowDetails;
        public CircleImageView profileImage;

        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_name);
            tvPhone = view.findViewById(R.id.tv_phone);
            btnShowDetails =view.findViewById(R.id.btn_show_details);
            profileImage=view.findViewById(R.id.profile_image);
        }
    }


    public RequestAdapter(List<RequestInfo> requestInfoList,List<String>requestInfoKeyList,Context context) {
        this.requestInfoList = requestInfoList;
        this.requestInfoKeyList=requestInfoKeyList;
        this.context=context;
        this.requestListener = (RequestListener) context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_request, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final RequestInfo requestInfo = requestInfoList.get(position);
        final String key = requestInfoKeyList.get(position);
        holder.tvName.setText(requestInfo.getUserName());
        holder.tvPhone.setText(requestInfo.getUserPhone());
        if(!requestInfo.getUserImage().isEmpty())
            Utils.showImage(context,requestInfo.getUserImage(),holder.profileImage);
        holder.btnShowDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestListener.showMore(requestInfo,key);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestInfoList.size();
    }
}
