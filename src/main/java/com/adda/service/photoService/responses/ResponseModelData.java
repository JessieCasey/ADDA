package com.adda.service.photoService.responses;

import com.adda.service.photoService.parameters.ExpirationTime;
import lombok.NoArgsConstructor;


import java.util.Date;

/**
 * Represents a upload response data.
 * <p>
 * Uses as deserialization model for Gson.
 */
@NoArgsConstructor
public class ResponseModelData {

    private String id;
    private String title;
    private String url_viewer;
    private String url;
    private String display_url;
    private String delete_url;

    private int size;
    private long time;
    private long expiration;

    private ResponseModelImage image;
    private ResponseModelImage thumb;
    private ResponseModelImage medium;

    /**
     * Returns an image ID on remote host.
     * @return Image ID.
     */
    public String getID() {
        return id;
    }

    /**
     * Returns an image name as title on remote host.
     * @return Image name.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns an URL to 'viewer' page <b>(not a direct link)</b>.
     * @return Image viewer URL.
     */
    public String getViewerUrl() {
        return url_viewer;
    }

    /**
     * Returns an URL to image page <b>(direct link)</b>.
     * @return Direct image URL.
     */
    public String getImageUrl() {
        return url;
    }

    /**
     * Returns an URL to image display page <b>(direct link)</b>.
     * <p>
     * The same as {@link ResponseModelData#getImageUrl()}
     * @return Direct image URL.
     */
    public String getDisplayUrl() {
        return display_url;
    }

    /**
     * Returns an URL to image deletion.
     * <p>
     * But I am not know how it works :/
     * @return Delete image URL.
     */
    public String getDeleteUrl() {
        return delete_url;
    }

    /**
     * Returns an image file size <b>in bytes</b>.
     * @return Image size in bytes.
     */
    public int getImageSize() {
        return size;
    }

    /**
     * Returns an image uploaded timestamp relatively a remote host.
     * <p>
     * It is a time in seconds since January 1, 1970, 00:00:00 GMT.
     * @return Image uploaded timestamp.
     * @see ResponseModelData#getUploadedDate()
     */
    public long getTimestamp() {
        return time;
    }

    /**
     * Returns an image uploaded timestamp relatively a remote host.
     * <p>
     * It is a {@link Date} since January 1, 1970, 00:00:00 GMT.
     * @return Image uploaded timestamp.
     * @see ResponseModelData#getTimestamp()
     */
    public Date getUploadedDate() {
        return new Date(time * 1000);
    }

    /**
     * Returns an image URLs expiration time <b>in seconds</b>.
     * @return Image URLs expiration time.
     *
     * @see ExpirationTime#MIN_VALUE
     * @see ExpirationTime#MAX_VALUE
     */
    public long getExpiration() {
        return expiration;
    }

    /**
     * Returns an original image information from remote host.
     * @return Original image information.
     */
    public ResponseModelImage getOriginalImage() {
        return image;
    }

    /**
     * Returns an thumbnail image information from remote host.
     * <p>
     * Thumbnail is a resized & centered & squared image with size 180 x 180 from the original image.
     * @return Thumbnail image information.
     */
    public ResponseModelImage getThumbnail() {
        return thumb;
    }

    /**
     * Returns an medium image information from remote host.
     * @return Medium image information.
     */
    public ResponseModelImage getMediumImage() {
        return medium;
    }

    @Override
    public String toString() {
        return "Data{"
                + "id=" + id + ", "
                + "title=" + title + ", "
                + "url_viewer=" + url_viewer + ", "
                + "url=" + url + ", "
                + "display_url=" + display_url + ", "
                + "delete_url=" + delete_url + ", "
                + "size=" + size + ", "
                + "time=" + time + ", "
                + "expiration=" + expiration + ", "
                + "image=" + image + ", "
                + "thumb=" + thumb + ", "
                + "medium=" + medium + "}";
    }

}
