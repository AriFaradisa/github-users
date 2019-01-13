package com.arifaradisa.githubusers.ui.main;

import android.text.TextUtils;
import android.widget.EditText;

import com.arifaradisa.githubusers.api.RestService;
import com.arifaradisa.githubusers.ui.base.BasePresenter;
import com.jakewharton.rxbinding3.widget.RxTextView;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import timber.log.Timber;

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {

    public MainPresenter(CompositeDisposable mCompositeDisposable, RestService mRestService) {
        super(mCompositeDisposable, mRestService);
    }

    @Override
    public void attachView(MainContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void observeField(EditText et_search) {
        Observable<Boolean> searchStream = RxTextView.textChanges(et_search)
                .map(charSequence -> !TextUtils.isEmpty(charSequence)
                        && charSequence.toString().trim().length() >2);

        Observable<Boolean> emptyFieldStream = RxTextView.textChanges(et_search)
                .map(TextUtils::isEmpty);

        Observer<Boolean> searchObserver = new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean value) {
                if (value) {
                    getMvpView().showLoadingState();
                    doSearchQuery(et_search.getText().toString().trim(),1);
                }
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e);
            }

            @Override
            public void onComplete() {

            }
        };

        Observer<Boolean> emptyObserver = new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean value) {
                if (value) {
                    getMvpView().hideList();
                }

            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e);
            }

            @Override
            public void onComplete() {

            }
        };

        searchStream.subscribe(searchObserver);
        emptyFieldStream.subscribe(emptyObserver);

    }

    @Override
    public void doSearchQuery(String query, int page) {
        getCompositeDisposable().add(
                getRestService().searchUsers(query, page, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchResponse -> {
                    getMvpView().showQueryResult(searchResponse.getItems());
                },throwable -> {
                    if (throwable instanceof HttpException) {
                        HttpException exception = (HttpException) throwable;
                        switch (exception.code()) {
                            case 403:
                                getMvpView().showError("API rate limit exceeded");
                                break;
                            default:
                                break;
                        }
                    }else{
                        getMvpView().showError("No internet connection");
                    }
                })
        );
    }
}
