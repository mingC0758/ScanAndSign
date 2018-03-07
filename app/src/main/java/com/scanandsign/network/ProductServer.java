package com.scanandsign.network;

import com.scanandsign.bean.ProductResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * @author mingC
 * @date 2018/1/31
 */
public interface ProductServer {

	//抓取商品列表接口
	@GET("/")
	Observable<List<ProductResult>> getProductList();
}
