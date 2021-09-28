package com.android.hilltrackdoctorfinder.activity.notifier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.activity.BaseActivity;
import com.android.hilltrackdoctorfinder.adapter.NotificationListAdapter;
import com.android.hilltrackdoctorfinder.api.ApiClient;
import com.android.hilltrackdoctorfinder.api.ApiInterface;
import com.android.hilltrackdoctorfinder.model.Notification;
import com.android.hilltrackdoctorfinder.model.Wishlist;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.id_no_data)
    LinearLayout id_no_data;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    ImageButton back;
    private List<Notification> notificationList;
    private RecyclerView.LayoutManager layoutManager;
    private NotificationListAdapter adapter;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        title.setText(R.string.notification);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        getNotificationsList();
        back.setOnClickListener(this);
    }
    private void getNotificationsList() {
        loading.start();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Notification>> call = apiInterface.getNotifications();
        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                loading.end();
                if (response.code()==200) {
                    notificationList = response.body();
                    if (!notificationList.isEmpty())
                    {
                        adapter = new NotificationListAdapter(NotificationActivity.this, notificationList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
                        recyclerView.setAdapter(adapter);
                    }else {
                        id_no_data.setVisibility(View.VISIBLE);
                    }

                }
            }
            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                loading.end();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view==back)
        {
            finish();
        }
    }
}