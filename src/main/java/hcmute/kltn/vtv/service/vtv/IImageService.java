package hcmute.kltn.vtv.service.vtv;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface IImageService {



    @Transactional
    String uploadImageToFirebase(MultipartFile multipartFile);

    @Transactional
    void deleteImageFromFirebase(String imageUrl);
}
