package com.arifaradisa.githubusers.ui.base;

import com.arifaradisa.githubusers.api.RestService;

import io.reactivex.disposables.CompositeDisposable;

public class BasePresenter<T extends MvpView> implements Presenter<T> {

    private T mMvpView;
    private final CompositeDisposable mCompositeDisposable;
    private final RestService mRestService;

    public BasePresenter(CompositeDisposable mCompositeDisposable, RestService mRestService) {
        this.mCompositeDisposable = mCompositeDisposable;
        this.mRestService = mRestService;
    }

    @Override
    public void attachView(T mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mCompositeDisposable.dispose();
        mMvpView = null;
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public T getMvpView() {
        return mMvpView;
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    public RestService getRestService() {
        return mRestService;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }

}
