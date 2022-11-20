package com.adda.url;

import com.adda.advert.Advert;
import com.adda.url.dto.UrlDto;
import com.adda.url.dto.UrlErrorResponseDto;
import com.adda.url.dto.UrlResponseDto;
import com.adda.url.service.UrlService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@RestController
public class UrlShorteningController {

    private final UrlService urlService;

    /**
     * Constructor for {@link UrlShorteningController}.
     *
     * @param urlService {@link UrlService}
     */
    @Autowired
    public UrlShorteningController(UrlService urlService) {
        this.urlService = urlService;
    }

    /**
     * Method that generating short links.  {@link Advert}
     *
     * @param urlDto DTO to create URL entity. {@link UrlDto}
     * @return ResponseEntity<AdvertResponseDTO> object in case of success. {@link ResponseEntity}
     */
    @PostMapping("/url")
    public ResponseEntity<?> generateShortLink(@RequestBody UrlDto urlDto) {
        Url urlToRet = urlService.generateShortLink(urlDto);

        if (urlToRet != null) {
            UrlResponseDto urlResponseDto = new UrlResponseDto();
            urlResponseDto.setOriginalUrl(urlToRet.getOriginalUrl());
            urlResponseDto.setExpirationDate(urlToRet.getExpirationDate());
            urlResponseDto.setShortLink(urlToRet.getShortLink());
            return new ResponseEntity<>(urlResponseDto, HttpStatus.OK);
        }

        UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
        urlErrorResponseDto.setStatus("404");
        urlErrorResponseDto.setError("There was an error processing your request. please try again.");
        return new ResponseEntity<>(urlErrorResponseDto, HttpStatus.OK);

    }

    /**
     * Method that redirect from the short links.  {@link Advert}
     *
     * @param shortLink short link that was generated earlier.
     */
    @GetMapping("/{shortLink}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortLink,
                                                   HttpServletResponse response) throws IOException {

        if (StringUtils.isEmpty(shortLink)) {
            UrlErrorResponseDto responseDto = new UrlErrorResponseDto();
            responseDto.setError("Invalid Url");
            responseDto.setStatus("400");
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        Url urlToRet = urlService.getEncodedUrl(shortLink);

        if (urlToRet == null) {
            UrlErrorResponseDto responseDto = new UrlErrorResponseDto();
            responseDto.setError("Url does not exist or it might have expired!");
            responseDto.setStatus("400");
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        if (urlToRet.getExpirationDate().isBefore(LocalDateTime.now())) {
            urlService.deleteShortLink(urlToRet);
            UrlErrorResponseDto responseDto = new UrlErrorResponseDto();
            responseDto.setError("Url Expired. Please try generating a fresh one.");
            responseDto.setStatus("200");
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        response.sendRedirect(urlToRet.getOriginalUrl());
        return null;
    }
}