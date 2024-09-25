package aairline.music;


import org.springframework.stereotype.Service;

@Service
public class MusicService {

    private static final String BUCKET_NAME = "aairline";
    private static final String REGION = "ap-northeast-2";  // 버킷이 위치한 AWS 리전

    // 파일명을 받아 S3 URL을 생성
    public String getMusicUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", BUCKET_NAME, REGION, fileName);
    }
}