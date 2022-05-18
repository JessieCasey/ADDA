package com.adda.service;

import com.adda.service.photoService.UploadClient;
import com.adda.service.photoService.parameters.ExpirationTime;
import com.adda.service.photoService.parameters.UploadParameters;
import com.adda.service.photoService.responses.OptionalResponse;
import org.springframework.stereotype.Service;

@Service
public class PhotoService {
    private static final String API_KEY = "3bcf090f1603553d4218e2cea8b30549";

    public static OptionalResponse[] uploadPhotoToServer(String[] imagesInBase64, String[] fileNames)  {
        OptionalResponse[] uploadedImages = new OptionalResponse[imagesInBase64.length];
        for (int i = 0; i < imagesInBase64.length; i++) {
            if (imagesInBase64[i] != null) {
                UploadParameters uploadParameters = new UploadParameters(API_KEY, imagesInBase64[i], fileNames[i], ExpirationTime.fromLong(100000));
                uploadedImages[i] = UploadClient.upload(uploadParameters);
            }
        }

        return uploadedImages;
    }
}
