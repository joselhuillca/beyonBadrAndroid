package com.badr.nwes.beyondbadr.adapters;

import android.view.View;
import android.widget.TextView;

import com.badr.nwes.beyondbadr.R;
import com.telerik.widget.list.ListViewHolder;

/**
 * Created by Citec on 26/11/2016.
 */
class GroupItemViewHolder extends ListViewHolder {

    public TextView txtGroupTitle;

    public GroupItemViewHolder(View itemView) {
        super(itemView);
        this.txtGroupTitle = (TextView) itemView.findViewById(R.id.txtGroupName);
    }
}