package hello.productservice.main.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Repository
public class ProductImageRepositoryImpl implements ProductImageRepository {
    @Value("${file.dir}")
    private String uploadDirectory;


    //file.getBytes() -> MultipartFile 객체에서 바이트 배열로 데이터를 읽어와서 byte 형태로 저장
    //byte[]

    @Override
    public void saveImage(byte[] fileData, String filePath) throws IOException {
        //String 형태의 path를 Java의 Path 형식으로 변환
        Path file = Paths.get(filePath);
        //file 저장
        Files.write(file, fileData);
        }

        /*?
        저장된 파일 byte 배열로 읽어오기 기능
         */
    @Override
    public void findImage(String filePath) throws IOException {
        Path savedFilePath = Paths.get(filePath);
        byte[] fileData  = Files.readAllBytes(savedFilePath);
    }
}
