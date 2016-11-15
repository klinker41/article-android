/*
 * Copyright (C) 2016 Jacob Klinker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.klinker.android.article.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Locale;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleApi {

    private static final String API_DEBUG_URL = "http://192.168.86.146:3000/";
    private static final String API_RELEASE_URL = "https://klinkerapps-article.herokuapp.com/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static CallAdapter.Factory callAdapterFactory = new CallAdapter.Factory() {
        @Override
        public CallAdapter<Object> get(final Type returnType, Annotation[] annotations,
                                       Retrofit retrofit) {
            // if returnType is retrofit2.Call, do nothing
            if (returnType.getClass().getPackage().getName().contains("retrofit2.Call")) {
                return null;
            }

            return new CallAdapter<Object>() {
                @Override
                public Type responseType() {
                    return returnType;
                }

                @Override
                public <R> Object adapt(Call<R> call) {
                    try {
                        Response response = call.execute();
                        return response.body();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            };
        }
    };

    private static Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setFieldNamingStrategy(new FieldNamingStrategy() {
                @Override
                public String translateName(Field f) {
                    return separateCamelCase(f.getName(), "_").toLowerCase(Locale.ROOT);
                }

                private String separateCamelCase(String name, String separator) {
                    StringBuilder translation = new StringBuilder();
                    for (int i = 0; i < name.length(); i++) {
                        char character = name.charAt(i);
                        if (Character.isUpperCase(character) && translation.length() != 0) {
                            translation.append(separator);
                        }
                        translation.append(character);
                    }
                    return translation.toString();
                }
            })
            .create();

    private Retrofit retrofit;
    private String baseUrl;

    public enum Environment {
        DEBUG, RELEASE
    }

    /**
     * Creates a new API access object with the release environment.
     */
    public ArticleApi(String apiToken) {
        this(Environment.RELEASE, apiToken);
    }

    /**
     * Creates a new API access object that will connect to the correct environment.
     *
     * @param environment the Environment to use to connect to the APIs.
     */
    private ArticleApi(Environment environment, String apiToken) {
        this(environment == Environment.DEBUG ? API_DEBUG_URL : API_RELEASE_URL, apiToken);
    }

    /**
     * Creates a new API access object that will automatically attach your API key to all
     * requests.
     */
    private ArticleApi(String baseUrl, final String apiToken) {
        if (httpClient.interceptors().size() == 0) {
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    HttpUrl url = request.url().newBuilder()
                            .addQueryParameter("api_token", apiToken).build();
                    request = request.newBuilder().url(url).build();
                    return chain.proceed(request);
                }
            });
        }

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .addCallAdapterFactory(callAdapterFactory);

        this.retrofit = builder.client(httpClient.build()).build();
        this.baseUrl = baseUrl;
    }

    /**
     * Gets a service that can be used for account requests such as signup and login.
     */
    public ArticleService article() {
        return retrofit.create(ArticleService.class);
    }

    public String baseUrl() {
        return baseUrl;
    }

}
