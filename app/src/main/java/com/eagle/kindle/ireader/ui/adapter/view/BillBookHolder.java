package com.eagle.kindle.ireader.ui.adapter.view;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eagle.kindle.ireader.App;
import com.eagle.kindle.ireader.R;
import com.eagle.kindle.ireader.model.bean.BillBookBean;
import com.eagle.kindle.ireader.ui.base.adapter.ViewHolderImpl;
import com.eagle.kindle.ireader.utils.Constant;

/**
 * Created by newbiechen on 17-5-3.
 */

public class BillBookHolder extends ViewHolderImpl<BillBookBean> {

    private ImageView mIvPortrait;
    private TextView mTvTitle;
    private TextView mTvAuthor;
    private TextView mTvBrief;
    private TextView mTvMsg;

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_book_brief;
    }

    @Override
    public void initView() {
        mIvPortrait = findById(R.id.book_brief_iv_portrait);
        mTvTitle = findById(R.id.book_brief_tv_title);
        mTvAuthor = findById(R.id.book_brief_tv_author);
        mTvBrief = findById(R.id.book_brief_tv_brief);
        mTvMsg = findById(R.id.book_brief_tv_msg);
    }

    @Override
    public void onBind(BillBookBean value, int pos) {

        //头像
        Glide.with(App.getContext())
                .load(Constant.IMG_BASE_URL+value.getCover())
                .placeholder(R.drawable.ic_default_portrait)
                .error(R.drawable.ic_load_error)
                .fitCenter()
                .into(mIvPortrait);
        //书单名
        mTvTitle.setText(value.getTitle());
        //作者
        mTvAuthor.setText(value.getAuthor());
        //简介
        mTvBrief.setText(value.getShortIntro());
        //信息
        mTvMsg.setText(App.getContext().getString(R.string.nb_book_message,
                value.getLatelyFollower(),value.getRetentionRatio()));
    }
}