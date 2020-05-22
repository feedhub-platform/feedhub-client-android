package com.feedhub.app.mvp.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.feedhub.app.item.Source;
import com.feedhub.app.net.RequestBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import ru.melod1n.library.mvp.base.MvpFields;
import ru.melod1n.library.mvp.base.MvpOnLoadListener;
import ru.melod1n.library.mvp.base.MvpRepository;

public class SourceRepository extends MvpRepository<Source> {

    @Override
    public void loadValues(@NonNull MvpFields fields, @Nullable MvpOnLoadListener<Source> listener) {
        RequestBuilder.create()
                .method("platform/sources")
                .execute(new RequestBuilder.OnResponseListener<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject root) {
                        JSONArray response = Objects.requireNonNull(root.optJSONArray("response"));

                        ArrayList<Source> values = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jSource = response.optJSONObject(i);

                            values.add(new Source(jSource));
                        }

                        sendValuesToPresenter(fields, values, listener);
                    }

                    @Override
                    public void onError(Exception e) {
                        sendError(listener, e);
                    }
                });
    }
}
