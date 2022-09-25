package com.adda.service.photoService.parameters;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A wrapper for upload POST-request parameters.
 * <p>
 * You must use the {@link UploadParameters.Builder} to build a new parameters instance.
 */
@Getter
@NoArgsConstructor
public class UploadParameters {

    // required parameters
    private String apiKey;
    private String imageBase64;

    // optional parameters
    private String imageName;
    private ExpirationTime expirationTime;

    public UploadParameters(String apiKey, String imageBase64, String imageName, ExpirationTime expirationTime) {
        this.apiKey = apiKey;
        this.imageBase64 = imageBase64;
        this.imageName = imageName;
        this.expirationTime = expirationTime;
    }

    /**
     * Serializes current parameters instance to {@link LinkedHashMap} to use it in the POST-request.
     * <p>
     * This method don't add a image parameter value, because it must be in a POST-request body.
     * @return Serialized parameters as {@link LinkedHashMap}.
     *
     * @throws RuntimeException when the required API key parameter is invalid.
     * @throws RuntimeException when the required image parameter is invalid.
     */


    public Map<String, String> toMap() {
        // api key validation
        if(apiKey == null || apiKey.isEmpty())
            throw new RuntimeException("The required API key parameter is invalid!");

        // api key validation
        if(imageBase64 == null || imageBase64.isEmpty())
            throw new RuntimeException("The required image parameter is invalid!");

        Map<String, String> parameters = new LinkedHashMap<>();

        // required API key & image parameter
        parameters.put("key", apiKey);
        parameters.put("image", imageBase64);

        // optional image name parameter
        if(imageName != null && !imageName.isEmpty())
            parameters.put("name", imageName);

        // optional expiration time parameter
        if(expirationTime != null)
            parameters.put("expiration", expirationTime.getAsString());

        return parameters;
    }

    /**
     * A builder for the {@link UploadParameters}, provides an easy way to create it.
     */
    public static class Builder {

        private final UploadParameters parameters;

        /**
         * Building of parameters starts from this step.
         * <p>
         * You created a builder and now you can use next methods to set parameters.
         */
        public Builder() {
            this.parameters = new UploadParameters();
        }

        /**
         * <b>[REQUIRED]</b>
         * <p>
         * Sets the required API key parameter.
         * @param value the API key to set.
         * @return Current builder instance.
         */
        public Builder apiKey(String value) {
            parameters.apiKey = value;
            return this;
        }

        /**
         * <b>[REQUIRED]</b>
         * <p>
         * Sets the required image parameter as a Base64 encoded string.
         * @param value the image base64 encoded string to set.
         * @return Current builder instance.
         */
        public Builder imageBase64(String value) {
            parameters.imageBase64 = value;
            return this;
        }

        /**
         * <b>(OPTIONAL)</b>
         * <p>
         * Sets the optional name parameter.
         * @param value the name of an image on website.
         * @return Current builder instance.
         */
        public Builder imageName(String value) {
            parameters.imageName = value;
            return this;
        }

        /**
         * <b>(OPTIONAL)</b>
         * <p>
         * Sets the optional expiration time parameter.
         * @param value the expiration time instance (see info below).
         * @return Current builder instance.
         *
         * @see ExpirationTime#fromLong(long)
         * @see ExpirationTime#fromString(String)
         */
        public Builder expirationTime(ExpirationTime value) {
            parameters.expirationTime = value;
            return this;
        }

        /**
         * Finishes a parameters building and returns builded parameters.
         * @return Builded upload parameters instance.
         */
        public UploadParameters build() {
            return parameters;
        }

    }

}