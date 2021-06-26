package com.swlc.swlcexportmarketingservice.util;

import com.swlc.swlcexportmarketingservice.exception.SwlcExportMarketException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;

@Slf4j
@Component
public class FileHandler {

    @Value("${server.upload.url}")
    private String destPath;

    @Value("${server.upload.folder}")
    private String folder;

    @Value("${server.url}")
    private String baseURL;

    @Value("${server.upload.assets.url}")
    private String assetsPath;


    private final CustomGenerator customGenerator;

    public FileHandler(CustomGenerator customGenerator) {
        this.customGenerator = customGenerator;
    }


    /**
     * <h6>This function will save a Base64 string converted as an Image file with
     * a unique random name assigned to the image file.
     * the folder name will be prepended to every photo's name</h6>
     *
     * @param byteString the original byteString comes from the Base64 encoded file
     * @return the file name after saving
     * @throws SwlcExportMarketException if an error occurred when saving file to the disk
     */

    public String saveImageFile(String byteString) {

        String FOLDER_PATH = destPath + folder + "/" + assetsPath;

        try {

            // converts Base64 image into a byte value
            byte[] imageInBytes = Base64.decodeBase64(byteString);

            // gets mime type of the file
            String fileMimeType = getMimeType(imageInBytes);

            // gets the extension of the mime type
            String fileExtension = getExtension(fileMimeType);

            // generates a random number for assigning it to the image name
            String randomNumber = customGenerator.generateNumber();

            // setting the image name with random unique numbers
            String randomFileName = randomNumber + System.currentTimeMillis() + fileExtension;

            // full (absolute) directory name with file name
            String directoryWithFile = FOLDER_PATH + "/" + randomFileName;
            log.info("Directory with file name: " + directoryWithFile);

            // writes the byte image to the absolute path
            try (FileOutputStream fileOutputStream = new FileOutputStream(directoryWithFile)) {
                fileOutputStream.write(imageInBytes);
            }

            // returns the file name and last folder name together so this path can be saved in the database
            String urlPath = null;

            urlPath = baseURL + "/" + folder + "/" + assetsPath + "/" + randomFileName;

            return urlPath;

        } catch (Exception e) {
            throw new SwlcExportMarketException(405, "Error saving file ", e);
        }
    }

    /**
     * This function can use to get file mime type from a file byte array.
     *
     * @param base64ImageString the base64 decoded file byte array
     * @return the mime type of the file
     */
    private String getMimeType(byte[] base64ImageString) {
        return new Tika().detect(base64ImageString);
    }

    /**
     * This function can use to get the file extension from the given mime type
     *
     * @param contentType the mime type which needs to parse to get the extension
     * @return extension
     */
    public String getExtension(String contentType) {
        try {
            MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
            MimeType mime = allTypes.forName(contentType);

            if (mime.getExtension().equals("jpg") || mime.getExtension().equals("png") || mime.getExtension().equals("jpeg")) {
                return mime.getExtension();
            }

            throw new SwlcExportMarketException(405,"Unable to extract extension. only supports png, jpg, jpeg. please try again.");

        } catch (Exception e) {
            log.info("Unable to extract extension default jpg will be used." + e.getMessage());
            return ".jpg";
        }
    }


}
