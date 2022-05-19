package com.adda.controller;

import com.adda.service.QRcodeService;
import com.google.zxing.WriterException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("api/adv/qr")
public class QRcodeController {
    private static final String QR_CODE_IMAGE_PATH = "./src/main/resources/qr_codes/QRCode.png";

    @GetMapping("/")
    public ResponseEntity<String> getQRCode(@RequestBody String url) {
        String medium = "https://rahul26021999.medium.com/";
        String github = "https://www.google.com/";

        byte[] image = new byte[0];
        try {

            // Generate and Return Qr Code in Byte Array
            image = QRcodeService.getQRCodeImage(medium, 250, 250);

            // Generate and Save Qr Code Image in static/image folder
            QRcodeService.generateQRCodeImage(github, 250, 250, QR_CODE_IMAGE_PATH);

        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }

        // Convert Byte Array into Base64 Encode String
        String qrcode = Base64.getEncoder().encodeToString(image);

        return ResponseEntity.ok(qrcode);
    }

    @GetMapping("/heroku")
    public String heroku() {
        return "Heroku app is running";
    }
}
