package com.maureen.schedule.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.maureen.schedule.R;
import com.maureen.schedule.data.CourseInfoBean;
import com.maureen.schedule.utils.DisplayUtil;

import java.util.List;


/**
 * Function:
 *
 * @author lianml
 * Create 2020-11-28
 */
public class CourseInfoAdapter extends Adapter<CourseInfoAdapter.ViewHolder> {
    private Context mContext;
    private List<CourseInfoBean> mDataList;
    private int mItemWidth;
    private int mItemHeight;

    public CourseInfoAdapter(Context context, List<CourseInfoBean> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
        mItemWidth = (DisplayUtil.getScreenWidth(mContext) - DisplayUtil.dp2px(mContext, 32)) / 7;
        mItemHeight = DisplayUtil.dp2px(mContext, 64);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_item_course_info, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourseInfoBean infoBean = mDataList.get(position);
        holder.courseInfoTv.setWidth(mItemWidth);
        holder.courseInfoTv.setHeight(infoBean.getLength() * mItemHeight);
        holder.courseInfoTv.setText(infoBean.getName());
    }

    @Override
    public int getItemCount() {
        return null == mDataList ? 0 : mDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseInfoTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseInfoTv = itemView.findViewById(R.id.tv_course_info);
        }
    }
}