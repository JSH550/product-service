package hello.productservice.main.repository;


import java.io.IOException;

public interface ProductImageRepository    {
    void saveImage(byte[] fileData, String filePath) throws IOException;

    void findImage(String filePath) throws IOException;
}
