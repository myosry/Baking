package com.yosri.mustafa.eng.bakingapp.Ui;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yosri.mustafa.eng.bakingapp.Adapter.BakingRecyclerAdapter;
import com.yosri.mustafa.eng.bakingapp.Api.BakingAPI;
import com.yosri.mustafa.eng.bakingapp.Api.BakingClient;
import com.yosri.mustafa.eng.bakingapp.Model.Recipe;
import com.yosri.mustafa.eng.baking.R;
import com.yosri.mustafa.eng.bakingapp.Utils.NetworkCheck;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private CollapsingToolbarLayout toolbarLayout;
    private BakingAPI apiService;
    private BakingClient client;
//    private RecyclerView mrecyclerView;
    private BakingRecyclerAdapter madapter;
    private List<Recipe> recipesList;
    private Parcelable mListState;
    private NetworkCheck networkCheck;
    private  Toast mToast;
    private StaggeredGridLayoutManager layoutManager;
    private Bundle getSavedInstance;
    private static final String LIST_STATE_KEY = "state";
    private ArrayList<Recipe> recipes;
    private String LOG_TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.progressBar) ProgressBar pb ;
    @BindView(R.id.main_content) SwipeRefreshLayout mswipeRefreshLayout ;
    @BindView(R.id.coordinator_layout)  CoordinatorLayout coordinatorLayout ;
    @BindView(R.id.rv)  RecyclerView mrecyclerView; ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        getSavedInstance = savedInstanceState;
        recipesList = new ArrayList<Recipe>();
        Boolean m =isTablet(this);
        Log.e("isTablet","TAblet Is  "+m);
        networkCheck = new NetworkCheck();

        if (networkCheck.isNetworkAvailable(this) == true) {
            initView();
            swipeRefresh();
            pb.setVisibility(View.GONE);


        } else {
            onFailureConnect();
        }




    }
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//
//        mListState = mrecyclerView.getLayoutManager().onSaveInstanceState();
//        outState.putParcelable(LIST_STATE_KEY, mListState);
//        Log.e("saveList", outState.toString());
//        super.onSaveInstanceState(outState);
//
//    }
//
//    @Override
//    protected void onPause() {
//        mListState = mrecyclerView.getLayoutManager().onSaveInstanceState();
//        super.onPause();
//    }
//    @Override
//    protected void onRestoreInstanceState(Bundle state) {
//        super.onRestoreInstanceState(state);
//        if (state != null) {
//            mListState = state.getParcelable(LIST_STATE_KEY);
//        }
//    }
public static boolean isTablet(Context context) {
    return (context.getResources().getConfiguration().screenLayout
            & Configuration.SCREENLAYOUT_SIZE_MASK)
            >= Configuration.SCREENLAYOUT_SIZE_LARGE;
}

    public Activity getActivity() {
        Context context = this;
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();

        }
        return null;

    }
    private void swipeRefresh() {
//        mswipeRefreshLayout = findViewById(R.id.main_content);
        mswipeRefreshLayout.setColorSchemeResources(R.color.primary);
        mswipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initView();

                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(MainActivity.this, "Movies Refreshed", Toast.LENGTH_LONG);
                mToast.show();
                pb.setVisibility(View.GONE);

            }
        });
    }
    private void initView() {

        pb.setVisibility(View.VISIBLE);
        client = new BakingClient();
        apiService = client.getClient().create(BakingAPI.class);

        setupRecyclerView();

        if (networkCheck.isNetworkAvailable(this) == true) {
            fetchJson();
            pb.setVisibility(View.GONE);


        } else {
            onFailureConnect();
        }
    }
    private void onFailureConnect() {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.check_connection, Snackbar.LENGTH_INDEFINITE);
        pb.setVisibility(View.GONE);
        snackbar.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initView();

            }
        });
        snackbar.show();
    }

//    private void setupToolbar() {
//        coordinatorLayout = findViewById(R.id.coordinator_layout);
//        toolbar = findViewById(R.id.toolbar);
//        setUpActionBar();
//        toolbarLayout = findViewById(R.id.collapsingToolbarLayout);
//        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//        toolbarLayout.setTitle("Pop Movies");
//    }
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private void setUpActionBar() {
//        if (toolbar != null) {
//
//            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
//            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
//
//            ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(7);
//
//
//        }
//    }
    private void setupRecyclerView() {
//        mrecyclerView = findViewById(R.id.rv);
        mrecyclerView.setHasFixedSize(true);

        madapter = new BakingRecyclerAdapter(this, recipesList);
        mrecyclerView.setAdapter(madapter);
        layoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);

        if (mListState != null) {
            mrecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
        }

        else {

            if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                mrecyclerView.setLayoutManager(layoutManager);
                mrecyclerView.getLayoutManager().onRestoreInstanceState(mListState);

            }       else {
                mrecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                mrecyclerView.getLayoutManager().onRestoreInstanceState(mListState);

            }
        }
        mrecyclerView.setItemAnimator(new DefaultItemAnimator());

        madapter.notifyDataSetChanged();
    }


    private void fetchJson() {
        Call<List<Recipe>> call = apiService.getRecipes();

        try {

            Log.e(LOG_TAG, "url is " + call.request().url());
            call.enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                    try {
                        if (response.body() != null) {
                            recipesList = response.body();
                        }

                        Log.e(LOG_TAG, "size is " + recipesList.size());

                    } catch (Exception e) {
                        Log.e(LOG_TAG, e.getMessage());
                    }

                    mrecyclerView.setAdapter(new BakingRecyclerAdapter(getApplicationContext(), recipesList));

                    if (mListState != null)
                        mrecyclerView.getLayoutManager().onRestoreInstanceState(mListState);


                    pb.setVisibility(View.GONE);
                    if (mswipeRefreshLayout.isRefreshing()) {
                        mswipeRefreshLayout.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {
                    Log.e(LOG_TAG, t.getMessage());

                    onFailureConnect();

                }
            });

        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());

            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG);
            mToast.show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
