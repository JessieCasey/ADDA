package com.adda.service.photoService.responses;

import lombok.NoArgsConstructor;

/**
 * Represents a upload response data image.
 * <p>
 * Uses as deserialization model for Gson.
 */
@NoArgsConstructor
public class ResponseModelImage {

    private String filename;
    private String name;
    private String mime;
    private String extension;
    private String url;

    /**
     * Returns an uploaded file name received by remote host.
     * @return File name on remote host.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Returns an uploaded image name on remote host.
     * @return Image name on remote host.
     */
    public String getImageName() {
        return name;
    }

    /**
     * Returns an uploaded file mime type.
     * @return Image MIME type.
     */
    public String getMimeType() {
        return mime;
    }

    /**
     * Returns an uploaded file extension (png, jpeg etc.)
     * @return Image file extension.
     */
    public String getFileExtension() {
        return extension;
    }

    /**
     * Returns an direct image file URL.
     * @return Direct file URL.
     */
    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Image{"
                + "filename=" + filename + ", "
                + "name=" + name + ", "
                + "mime=" + mime + ", "
                + "extension=" + extension + ", "
                + "url=" + url + "}";
    }

}