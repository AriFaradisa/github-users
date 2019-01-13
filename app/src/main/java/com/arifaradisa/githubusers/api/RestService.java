package com.arifaradisa.githubusers.api;

import com.arifaradisa.githubusers.api.response.SearchResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestService {

    @GET("search/users")
    Single<SearchResponse> searchUsers(@Query("q") String query, @Query("page") int page, @Query("per_page") int per_page);

}
