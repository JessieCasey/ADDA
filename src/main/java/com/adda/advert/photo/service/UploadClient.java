package com.adda.advert.photo.service;

import com.adda.advert.photo.service.parameters.UploadParameters;
import com.adda.advert.photo.service.responses.OptionalResponse;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import java.io.IOException;

public class UploadClient {

    private static final String API_URL = "https://api.imgbb.com/1/upload";
    private static final String USER_AGENT = "Imgbb Java SDK";
    private static final int TIMEOUT = 5000;

    public static OptionalResponse upload(UploadParameters parameters) {
        try {
            Response response = Jsoup.connect(API_URL)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .method(Method.POST)
                    .data(parameters.toMap())
                    .timeout(TIMEOUT)
                    .userAgent(USER_AGENT)
                    .execute();

            return OptionalResponse.of(response);
        } catch (IOException ex) {
            throw new RuntimeException("I/O exception was caught while try to upload image!", ex);
        }
    }

}