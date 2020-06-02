package com.feedhub.app.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.feedhub.app.R;
import com.feedhub.app.fragment.FragmentHeadlines;

public class SourcesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sources);

        Bundle args = new Bundle();
        args.putString("sourceTitle", getIntent().getStringExtra("sourceTitle"));
        args.putString("sourceId", getIntent().getStringExtra("sourceId"));

        getSupportFragmentManager().beginTransaction()
                .replace(
                        R.id.fragmentContainer,
                        FragmentHeadlines.newInstance(args),
                        FragmentHeadlines.class.getSimpleName()
                )
                .commitNow();
    }
}
