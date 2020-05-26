/*This is the spring boot image controller class
* tis wil be called when the end user hots the URL*/
package server;

import model.ImageChunksMetaData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;


@Controller
public class ImageController {

    private ImageChunksMetaData[] arrImagesChunkData = null;
    private byte[] arrImage = null;
    private File imageFile = null;


    /*This method will be called when the end user hits teh uRL
    * It maps the Base64 string of image data with the image name and sends it to welcome.jsp web page*/
    @GetMapping(value = "/")
    private String getImage(ModelMap model) throws IOException {
        arrImagesChunkData = ServerImpl.getArrImagesChunkData();
//        String [] arimage = new String[]{"screen_1.jpeg","screen_2.jpeg","screen_3.jpeg","screen_4.jpeg","screen_5.jpeg","screen_6.jpeg","screen_7.jpeg","screen_8.jpeg","screen_9.jpeg"};
        if (arrImagesChunkData == null) return "No images available";
        model.put("count", arrImagesChunkData.length);
        model.put("gridValue", (int) Math.sqrt(arrImagesChunkData.length));
//        if(arrImagesChunkData == null || arrImagesChunkData.length == 0) return "No images found";
        for (int i = 0, arrSize = arrImagesChunkData.length; i < arrSize; i++) {
            imageFile = new File(arrImagesChunkData[i].getImageName());
            if (imageFile != null && imageFile.exists()) {
                try (FileInputStream imageInFile = new FileInputStream(imageFile)) {
                    byte imageData[] = new byte[(int) imageFile.length()];
                    imageInFile.read(imageData);
                    model.put(arrImagesChunkData[i].getImageName().replace(".jpeg", ""), Base64.getEncoder().encodeToString(imageData));
                }
            }
        }
        return "welcome";
    }


}
