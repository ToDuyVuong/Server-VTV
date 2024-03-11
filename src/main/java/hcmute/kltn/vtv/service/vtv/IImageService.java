package hcmute.kltn.vtv.service.vtv;

import org.springframework.web.multipart.MultipartFile;

public interface IImageService {


    String uploadImageToFirebase(MultipartFile multipartFile);

    boolean deleteImageFromFirebase(String imageUrl);
}
