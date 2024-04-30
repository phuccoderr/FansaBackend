package com.fansa.admin.fileServer;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String upload(MultipartFile file)  {
        try{
            String data = this.cloudinary.uploader().upload(file.getBytes(),
                    Map.of("public_id", UUID.randomUUID().toString())).get("url").toString();
            return data;
        }catch (IOException io){
            throw new RuntimeException("Image upload fail");
        }
    }

    public void deleteImage(String imageUrl) {
        try {
            String publicIdFromUrl = getPublicIdFromUrl(imageUrl);
            Map result = cloudinary.uploader().destroy(publicIdFromUrl, null);
        } catch (IOException e) {
            throw new RuntimeException("Image delete fail");
        }
    }

    private String getPublicIdFromUrl(String imageUrl) {
        // https://res.cloudinary.com/dp4tp9gwa/image/upload/v1713253619/b8db51d6-d20b-420d-860e-3228352026f4.jpg
        String[] parts = imageUrl.split("/");
        String publicIdWithExtension = parts[parts.length - 1]; // b8db51d6-d20b-420d-860e-3228352026f4.jpg
        String[] publicIdParts = publicIdWithExtension.split("\\."); // b8db51d6-d20b-420d-860e-3228352026f4
        return publicIdParts[0]; // Public_id là phần trước dấu chấm trong publicIdWithExtension
    }

}
