package com.karmanchik.chtotib_bot_rest_service.rest.file;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Log4j2
public class MultipartFileToFileConverter {
    public static File convert(MultipartFile mFile) throws IOException {
        File file = new File("src\\main\\resources\\files\\"+mFile.getName());
        try (OutputStream os = new FileOutputStream(file)) {
            os.write(mFile.getBytes());
        }
        return file;
    }
}
