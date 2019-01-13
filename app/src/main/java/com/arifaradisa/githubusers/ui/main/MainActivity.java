package com.arifaradisa.githubusers.ui.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.Toast;

import com.arifaradisa.githubusers.R;
import com.arifaradisa.githubusers.api.RestService;
import com.arifaradisa.githubusers.api.RetrofitClient;
import com.arifaradisa.githubusers.api.response.Item;
import com.arifaradisa.githubusers.helper.EndlessRecyclerViewScrollListener;
import com.arifaradisa.githubusers.helper.SimpleDividerItemDecoration;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_state)
    MultiStateView stateView;
    @BindView(R.id.main_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.main_edittext)
    EditText et_search;

    private MainPresenter mainPresenter;
    private CompositeDisposable compositeDisposable;
    private List<Item> itemList;
    private MainAdapter mainAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        itemList = new ArrayList<>();
        mainAdapter = new MainAdapter(this, itemList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mainAdapter);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mainPresenter.doSearchQuery(et_search.getText().toString().trim(), page);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        RestService restService = RetrofitClient.getClient().create(RestService.class);
        compositeDisposable = new CompositeDisposable();
        mainPresenter = new MainPresenter(compositeDisposable, restService);
        mainPresenter.attachView(this);
        mainPresenter.observeField(et_search);

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideList() {
        itemList.clear();
        mainAdapter.notifyDataSetChanged();
        stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
    }

    @Override
    public void showQueryResult(List<Item> items) {
        if (items.size()>0) {
            itemList.addAll(items);
            mainAdapter.notifyDataSetChanged();
            stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        }else{
            if (itemList.size()>0){
                //end of list
                stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            }else{
                scrollListener.resetState();
                itemList.clear();
                stateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            }
        }
    }

    @Override
    public void showLoadingState() {
        scrollListener.resetState();
        itemList.clear();
        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
    }
}
