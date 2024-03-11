package hcmute.kltn.vtv.service.vtv.impl;

import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import hcmute.kltn.vtv.service.vtv.IImageService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements IImageService {

    private final Bucket storageClient;



    @Override
    @Transactional
    public String uploadImageToFirebase(MultipartFile multipartFile) {
        try {
            String fileName = createFileName(multipartFile);
            File file = convertToFile(multipartFile, fileName);
            uploadFile(file, fileName);
            String publicUrl = getPublicUrl(fileName);
            deleteTempFile(file);

            return publicUrl;
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi tải ảnh lên!");
        }
    }




    @Override
    @Transactional
    public void deleteImageFromFirebase(String imageUrl) {
        try {
            String fileName = getFileNameFromUrl(imageUrl);
            deleteFile(fileName);
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi xóa ảnh!");
        }
    }

    private void deleteFile(String fileName) {
        try {
            Storage storage = storageClient.getStorage();
            storage.delete(storageClient.getName(), fileName);
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi xóa file!");
        }
    }

    private String getFileNameFromUrl(String imageUrl) {
        try {
            // Decode the URL to get the original file name
            String decodedUrl = URLDecoder.decode(imageUrl, StandardCharsets.UTF_8);

            // Extract the file name from the decoded URL
            // Example: "https://firebasestorage.googleapis.com/v0/b/image-vtv.appspot.com/o/6bd77496-4b74-4a91-9e9d-42ce430e5dfb.jpg?alt=media"
            // The file name would be "6bd77496-4b74-4a91-9e9d-42ce430e5dfb.jpg"
            return decodedUrl.substring(decodedUrl.lastIndexOf("/") + 1, decodedUrl.indexOf("?alt=media"));
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi trích xuất tên file từ URL!");
        }
    }





    private void uploadFile(File file, String fileName) {
        try {
            storageClient.create(fileName, Files.readAllBytes(file.toPath()), "jpg");
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi upload hình ảnh!");
        }
    }

    private String createFileName(MultipartFile multipartFile) {
        return UUID.randomUUID().toString().concat(getExtension(multipartFile.getOriginalFilename()));
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            return tempFile;
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi chuyển đổi file!");
        }
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private String getPublicUrl(String fileName) {
        String baseUrl = "https://firebasestorage.googleapis.com/v0/b/image-vtv.appspot.com/o/%s?alt=media";

        return String.format(baseUrl, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    private void deleteTempFile(File file) {
        if (file.exists()) {
            file.delete();
        }
    }
}
