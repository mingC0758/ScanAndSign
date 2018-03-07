package com.scanandsign.network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author mingC
 * @date 2018/1/31
 */
public class RetrofitUtil {


	public static Retrofit getRetrofit(String url) {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(url)
				.addConverterFactory(GsonConverterFactory.create()) //注意要有这行
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.build();
		return retrofit;
	}

}
