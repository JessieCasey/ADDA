package com.adda.advert.photo.service;

import com.adda.advert.photo.Photo;
import com.adda.advert.photo.service.parameters.ExpirationTime;
import com.adda.advert.photo.service.parameters.UploadParameters;
import com.adda.advert.photo.service.responses.OptionalResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class PhotoServiceImpl {

    private static String API_KEY;

    public PhotoServiceImpl(@Value("${api.key}") String API_KEY) {
        PhotoServiceImpl.API_KEY = API_KEY;
    }

    /**
     * Method that uploading photos to the hosting server and tie up with advert
     *
     * @param fileList List of the multipart files {@link List<MultipartFile>}
     * @return Photo entity in case of success. {@link Photo}
     */

    public static Photo uploadPhotoToAdvertisement(List<MultipartFile> fileList) throws IOException {
        String[] arrayOfPath = new String[8];
        String[] fileNames = new String[8];

        String uniqueFileName = null;
        String[] imagesBase64 = new String[8];
        for (int i = 0; i < fileList.size(); i++) {
            uniqueFileName = UUID.randomUUID() + fileList.get(i).getOriginalFilename();
            fileNames[i] = uniqueFileName;

            imagesBase64[i] = Base64.getEncoder().encodeToString(fileList.get(i).getBytes());
        }

        Photo photo = new Photo(fileList.size());
        OptionalResponse[] optionalResponse = PhotoServiceImpl.uploadPhotoToServer(imagesBase64, fileNames);
        for (int i = 0; i < optionalResponse.length; i++) {
            if (optionalResponse[i] != null) {
                arrayOfPath[i] = optionalResponse[i].get().getResponseData().getImageUrl();
            }
        }

        photo.setPhotos(arrayOfPath);
        return photo;
    }

    /**
     * Method that uploading photos to the hosting server. Related to the 'uploadPhotoToAdvertisement'.
     *
     * @param imagesInBase64 encoded image in base64.
     * @return OptionalResponse[] in case of success. {@link OptionalResponse}
     */
    public static OptionalResponse[] uploadPhotoToServer(String[] imagesInBase64, String[] fileNames) {
        OptionalResponse[] uploadedImages = new OptionalResponse[imagesInBase64.length];
        for (int i = 0; i < imagesInBase64.length; i++) {
            if (imagesInBase64[i] != null) {
                UploadParameters uploadParameters = new UploadParameters(API_KEY, imagesInBase64[i], fileNames[i], ExpirationTime.fromLong(5530000));
                uploadedImages[i] = UploadClient.upload(uploadParameters);
            }
        }
        return uploadedImages;
    }

    /**
     * Methods that uploading photo of QR code to the hosting server.
     *
     * @param qrCodeInBase64 encoded image of QR code in base64.
     * @param url            link to the advert which is encoded in QR code.
     *
     * @return link of QR code in case of success
     */
    public static String uploadPhotoOfQRcodeToAdvertisement(String qrCodeInBase64, String url) {
        OptionalResponse optionalResponse = PhotoServiceImpl.uploadPhotoOfQRcodeToServer(qrCodeInBase64, url);
        return optionalResponse.get().getResponseData().getImageUrl();
    }

    public static OptionalResponse uploadPhotoOfQRcodeToServer(String qrCodeInBase64, String fileNames) {
        UploadParameters uploadParameters = new UploadParameters(API_KEY, qrCodeInBase64, fileNames, ExpirationTime.fromLong(170000));
        return UploadClient.upload(uploadParameters);
    }
}
