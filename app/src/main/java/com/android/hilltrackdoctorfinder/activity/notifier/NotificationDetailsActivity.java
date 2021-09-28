package com.android.hilltrackdoctorfinder.activity.notifier;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.activity.BaseActivity;
import com.android.hilltrackdoctorfinder.api.ApiClient;
import com.android.hilltrackdoctorfinder.api.ApiInterface;
import com.android.hilltrackdoctorfinder.model.BloodBank;
import com.android.hilltrackdoctorfinder.model.Notification;

import java.util.List;

public class NotificationDetailsActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.id_notification_title)
    TextView id_notification_title;
    @BindView(R.id.tv_description)
    TextView tv_description;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    ImageButton back;
    String id;
    private ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);
        ButterKnife.bind(this);
        title.setText(R.string.notification_details);
        id = getIntent().getStringExtra("id");
        getNotificationInfo(id);
        back.setOnClickListener(this);
    }

    private void getNotificationInfo(String id) {
        loading.start();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Notification>> call = apiInterface.getNotificationDetails(id);
        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if (response.code()==200) {
                    loading.end();
                    List<Notification> body=response.body();
                    id_notification_title.setText(body.get(0).getTitle());
                    tv_description.setText(body.get(0).getDetails());
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
        if (view==back){
            finish();
        }
    }
}