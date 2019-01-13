package com.arifaradisa.githubusers.ui.main;

import android.widget.EditText;

import com.arifaradisa.githubusers.api.response.Item;
import com.arifaradisa.githubusers.ui.base.MvpView;

import java.util.List;

public interface MainContract {
    interface View extends MvpView {

        void hideList();

        void showQueryResult(List<Item> items);

        void showLoadingState();

    }

    interface Presenter {

        void observeField(EditText et_search);

        void doSearchQuery(String query, int page);

    }
}
