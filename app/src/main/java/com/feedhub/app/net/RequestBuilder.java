package com.feedhub.app.net;

import android.util.ArrayMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.feedhub.app.common.AppGlobal;
import com.feedhub.app.common.TaskManager;
import com.feedhub.app.fragment.FragmentSettings;
import com.feedhub.app.util.ArrayUtils;

import org.json.JSONObject;

import java.util.List;

public class RequestBuilder {

    public static String BASE_URL = AppGlobal.preferences.getString(FragmentSettings.KEY_SERVER_URL, "");

    private String baseUrl;
    private String methodName;

    private ArrayMap<String, String> parameters = new ArrayMap<>();

    public RequestBuilder() {
    }

    public RequestBuilder(String baseUrl, String methodName) {
        this.baseUrl = baseUrl;
        this.methodName = methodName;
    }

    public RequestBuilder(String baseUrl, String methodName, ArrayMap<String, String> parameters) {
        this.baseUrl = baseUrl;
        this.methodName = methodName;
        this.parameters = parameters;
    }

    public static RequestBuilder create() {
        RequestBuilder builder = new RequestBuilder();
        builder.baseUrl = BASE_URL;

        return builder;
    }

    public static void updateBaseUrl(String baseUrl) {
        RequestBuilder.BASE_URL = baseUrl;
    }

    public RequestBuilder baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public RequestBuilder method(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public RequestBuilder parameters(ArrayMap<String, String> parameters) {
        this.parameters = parameters;
        return this;
    }

    public RequestBuilder put(String key, @NonNull Object value) {
        parameters.put(key, value.toString());
        return this;
    }

    public RequestBuilder put(String key, @NonNull Object... values) {
        parameters.put(key, ArrayUtils.toString(values));
        return this;
    }

    public RequestBuilder put(String key, @NonNull List values) {
        parameters.put(key, ArrayUtils.toString(values));
        return this;
    }

    public String getSignedUrl() {
        StringBuilder params = new StringBuilder();

        try {
            for (int i = 0; i < parameters.size(); i++) {
                String key = parameters.keyAt(i);
                String value = parameters.valueAt(i);

                if (params.length() != 0) {
                    params.append("&");
                }

                params.append(key);
                params.append("=");
                params.append(value);
//                params.append(URLEncoder.encode(value, "UTF-8"));
            }
        } catch (Exception ignored) {
        }

        StringBuilder builder = new StringBuilder();
        builder.append(NetUtils.transformUrl(baseUrl));
        builder.append(methodName);

        if (params.length() > 0) {
            builder.append("?");
            builder.append(params.toString());
        }

        return builder.toString();
    }

    public <T> void execute(@Nullable OnResponseListener<T> listener) {
        execute(getSignedUrl(), listener);
    }

    private <T> void execute(@NonNull String url, @Nullable OnResponseListener<T> listener) {
        TaskManager.execute(() -> {
            try {
                JSONObject root = new JSONObject(HttpRequest.get(url).asString());

                if (listener != null) {
                    AppGlobal.handler.post(() -> listener.onSuccess((T) root));
                }
            } catch (Exception e) {
                e.printStackTrace();

                if (listener != null) {
                    AppGlobal.handler.post(() -> listener.onError(e));
                }
            }
        });
    }

    public interface OnResponseListener<T> {
        void onSuccess(T response);

        void onError(Exception e);
    }
}
