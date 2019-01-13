package com.arifaradisa.githubusers.ui.main;

import com.arifaradisa.githubusers.ui.base.BasePresenter;

import io.reactivex.disposables.CompositeDisposable;

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {

    public MainPresenter(CompositeDisposable mCompositeDisposable) {
        super(mCompositeDisposable);
    }

    @Override
    public void attachView(MainContract.View mvpView) {
        super.attachView(mvpView);
        getMvpView().showError("Test error");
    }

    @Override
    public void detachView() {
        super.detachView();
    }
}
