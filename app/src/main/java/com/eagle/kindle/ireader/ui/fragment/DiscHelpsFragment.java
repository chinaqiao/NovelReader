package com.eagle.kindle.ireader.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.eagle.kindle.ireader.R;
import com.eagle.kindle.ireader.RxBus;
import com.eagle.kindle.ireader.event.SelectorEvent;
import com.eagle.kindle.ireader.model.bean.BookHelpsBean;
import com.eagle.kindle.ireader.model.flag.BookDistillate;
import com.eagle.kindle.ireader.model.flag.BookSort;
import com.eagle.kindle.ireader.model.flag.CommunityType;
import com.eagle.kindle.ireader.presenter.DiscHelpsPresenter;
import com.eagle.kindle.ireader.presenter.contract.DiscHelpsContract;
import com.eagle.kindle.ireader.ui.activity.DiscDetailActivity;
import com.eagle.kindle.ireader.ui.adapter.DiscHelpsAdapter;
import com.eagle.kindle.ireader.ui.base.BaseMVPFragment;
import com.eagle.kindle.ireader.utils.Constant;
import com.eagle.kindle.ireader.widget.itemdecoration.DividerItemDecoration;
import com.eagle.kindle.ireader.widget.refresh.ScrollRefreshRecyclerView;
import com.eagle.kindle.ireader.widget.adapter.WholeAdapter;

import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by newbiechen on 17-4-21.
 */

public class DiscHelpsFragment extends BaseMVPFragment<DiscHelpsContract.Presenter> implements DiscHelpsContract.View{
    private static final String BUNDLE_SORT = "bundle_sort";
    private static final String BUNDLE_DISTILLATE = "bundle_distillate";
    /*****************View********************/
    @BindView(R.id.scroll_refresh_rv_content)
    ScrollRefreshRecyclerView mRvContent;
    /******************Object******************/
    private DiscHelpsAdapter mDiscHelpsAdapter;
    /******************Params*******************/
    private BookSort mBookSort = BookSort.DEFAULT;
    private BookDistillate mDistillate = BookDistillate.ALL;
    private int mStart = 0;
    private int mLimited = 20;

    /************************init method*********************************/
    @Override
    protected int getContentId() {
        return R.layout.fragment_scroll_refresh_list;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (savedInstanceState != null){
            mBookSort = (BookSort) savedInstanceState.getSerializable(BUNDLE_SORT);
            mDistillate = (BookDistillate) savedInstanceState.getSerializable(BUNDLE_DISTILLATE);
        }
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        setUpAdapter();
    }

    private void setUpAdapter(){
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.addItemDecoration(new DividerItemDecoration(getContext()));
        mDiscHelpsAdapter = new DiscHelpsAdapter(getContext(),new WholeAdapter.Options());
        mRvContent.setAdapter(mDiscHelpsAdapter);
    }

    /******************************click method******************************/
    @Override
    protected void initClick() {
        mRvContent.setOnRefreshListener(
                () -> startRefresh()
        );
        mDiscHelpsAdapter.setOnLoadMoreListener(
                () -> mPresenter.loadingBookHelps(mBookSort,mStart, mLimited,mDistillate)
        );

        mDiscHelpsAdapter.setOnItemClickListener(
                (view,pos) -> {
                    BookHelpsBean bean = mDiscHelpsAdapter.getItem(pos);
                    DiscDetailActivity.startActivity(getContext(), CommunityType.HELP,bean.get_id());
                }
        );

        Disposable eventDispo = RxBus.getInstance()
                .toObservable(Constant.MSG_SELECTOR, SelectorEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (event) ->{
                            mBookSort = event.sort;
                            mDistillate = event.distillate;
                            startRefresh();
                        }
                );
        addDisposable(eventDispo);
    }

    @Override
    protected DiscHelpsContract.Presenter bindPresenter() {
        return new DiscHelpsPresenter();
    }

    /*****************************logic method*****************************/
    @Override
    protected void processLogic() {
        super.processLogic();

        mRvContent.startRefresh();
        mPresenter.firstLoading(mBookSort,mStart,mLimited,mDistillate);
    }

    private void startRefresh(){
        mStart = 0;
        mPresenter.refreshBookHelps(mBookSort,mStart,mLimited,mDistillate);
    }

    /**************************rewrite method****************************************/
    @Override
    public void finishRefresh(List<BookHelpsBean> beans) {
        mDiscHelpsAdapter.refreshItems(beans);
        mStart = beans.size();
        mRvContent.setRefreshing(false);
    }

    @Override
    public void finishLoading(List<BookHelpsBean> beans) {
        mDiscHelpsAdapter.addItems(beans);
        mStart += beans.size();
    }

    @Override
    public void showErrorTip() {
        mRvContent.showTip();
    }

    @Override
    public void showError() {
        mDiscHelpsAdapter.showLoadError();
    }

    @Override
    public void complete() {
        mRvContent.finishRefresh();
    }
    /****************************************************************************/
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BUNDLE_SORT, mBookSort);
        outState.putSerializable(BUNDLE_DISTILLATE,mDistillate);
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.saveBookHelps(mDiscHelpsAdapter.getItems());
    }
}
