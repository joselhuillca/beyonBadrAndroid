package com.badr.nwes.beyondbadr.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.badr.nwes.beyondbadr.R;
import com.badr.nwes.beyondbadr.SmartPhone.Glide.model.Image;
import com.telerik.widget.list.ListViewAdapter;
import com.telerik.widget.list.ListViewDataSourceAdapter;
import com.telerik.widget.list.ListViewHolder;

import java.util.List;

/**
 * Created by Citec on 26/11/2016.
 */
public class GeneralImageItemDataAdapter  extends ListViewDataSourceAdapter {

    private Context context;
    private int itemLayoutId;
    public GeneralImageItemDataAdapter(List items, Context context, int itemLayoutId) {

        super(items);
        this.context = context;
        this.itemLayoutId = itemLayoutId;
    }

    @Override
    public ListViewHolder onCreateItemViewHolder(ViewGroup viewGroup, int viewType) {
        return new GeneralImageItemDataHolder(LayoutInflater.from(this.context).inflate(itemLayoutId, viewGroup, false));
    }

    @Override
    public void onBindItemViewHolder(ListViewHolder holder, Object entity) {
        GeneralImageItemDataHolder typedViewHolder = (GeneralImageItemDataHolder) holder;
        typedViewHolder.bind((Image) entity);
    }

    @Override
    public ListViewHolder onCreateGroupViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View rootView = inflater.inflate(R.layout.list_view_list_mode_group_item_layout, viewGroup, false);
        GroupItemViewHolder holder = new GroupItemViewHolder(rootView);
        return holder;
    }

    @Override
    public void onBindGroupViewHolder(ListViewHolder holder, Object groupKey) {
        GroupItemViewHolder typedViewHolder = (GroupItemViewHolder) holder;
        typedViewHolder.txtGroupTitle.setText(groupKey.toString().toUpperCase());
    }

}
