package com.eagle.kindle.ireader.model.bean.packages;

import com.eagle.kindle.ireader.model.bean.BaseBean;
import com.eagle.kindle.ireader.model.bean.HotCommentBean;

import java.util.List;

/**
 * Created by newbiechen on 17-5-4.
 */

public class HotCommentPackage extends BaseBean {

    private List<HotCommentBean> reviews;

    public List<HotCommentBean> getReviews() {
        return reviews;
    }

    public void setReviews(List<HotCommentBean> reviews) {
        this.reviews = reviews;
    }
}
