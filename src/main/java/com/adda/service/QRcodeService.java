package com.adda.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

public class QRcodeService {

    public static byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageConfig con = new MatrixToImageConfig(0xFF000002, 0xFFFFFF);

        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream, con);
        return pngOutputStream.toByteArray();
    }

    public static String getUrlOfAdvertisement(UUID advertisementID) {
        String url = "http://localhost:3000/advertisement/" + advertisementID;
        byte[] image = new byte[0];
        try {
            // Generate and Return Qr Code in Byte Array
            image = QRcodeService.getQRCodeImage(url, 250, 250);

        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }

        String qrcode = Base64.getEncoder().encodeToString(image);

        return PhotoService.uploadPhotoOfQRcodeToAdvertisement(qrcode, "qr-code-" + advertisementID);
    }
}
