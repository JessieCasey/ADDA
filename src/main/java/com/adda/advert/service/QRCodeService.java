package com.adda.advert.service;

import com.adda.advert.dto.AdvertResponseDTO;
import com.adda.advert.photo.service.PhotoServiceImpl;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

/**
 * The QRCodeServiceImpl class is responsible for creating QR codes to the adverts.
 */

@Lazy
@Service
public class QRCodeService {

    /**
     * Method that getting QR code image.
     *
     * @param url    link of the advert.
     * @param width  width of the QR code.
     * @param height height of the QR code.
     * @return ResponseEntity<AdvertResponseDTO> object in case of success. {@link ResponseEntity<AdvertResponseDTO>}
     */

    public static byte[] getQRCodeImage(String url, int width, int height) throws WriterException, IOException {
        BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageConfig con = new MatrixToImageConfig(0xFF000002, 0xFFFFFF);

        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream, con);
        return pngOutputStream.toByteArray();
    }

    /**
     * Method that getting url of the advertisement
     *
     * @param advertId    the id of the advert.
     * @return Link to the QR code. {@link ResponseEntity<AdvertResponseDTO>}
     */

    public static String getUrlOfAdvertisement(UUID advertId) {
        byte[] image = new byte[0];
        try {
            image = QRCodeService.getQRCodeImage("http://localhost:8080/api/advert/" + advertId, 250, 250);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }

        String qrcode = Base64.getEncoder().encodeToString(image);

        return PhotoServiceImpl.uploadPhotoOfQRcodeToAdvertisement(qrcode, "qr-code-" + advertId);
    }
}
