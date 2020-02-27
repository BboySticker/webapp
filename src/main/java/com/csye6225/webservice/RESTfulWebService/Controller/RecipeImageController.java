package com.csye6225.webservice.RESTfulWebService.Controller;

import com.csye6225.webservice.RESTfulWebService.Entity.Recipe.Image;
import com.csye6225.webservice.RESTfulWebService.Entity.Recipe.Recipe;
import com.csye6225.webservice.RESTfulWebService.Entity.User.User;
import com.csye6225.webservice.RESTfulWebService.Exception.AttachImageException;
import com.csye6225.webservice.RESTfulWebService.Exception.UserNotFoundException;
import com.csye6225.webservice.RESTfulWebService.Service.ImageService;
import com.csye6225.webservice.RESTfulWebService.Service.RecipeService;
import com.csye6225.webservice.RESTfulWebService.Service.S3Services;
import com.csye6225.webservice.RESTfulWebService.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;


@RestController
public class RecipeImageController {

    @Autowired
    ImageService imageService;
    @Autowired
    S3Services s3Services;
    @Autowired
    RecipeService recipeService;
    @Autowired
    UserService userService;
    @Autowired
    private Environment environment;

    private Logger logger = Logger.getLogger(getClass().getName());

    //Save the uploaded file to this folder
    @Value("${app.imagefolder.path}")
    private String UPLOADED_FOLDER;

    @Value("${app.profile.name}")
    private String PROFILE_NAME;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @PostMapping("/v1/recipe/{id}/image")
    private @ResponseBody Image attach(@PathVariable String id, @RequestParam("recipeImage") MultipartFile image) {

        Recipe recipe = recipeService.findById(id);

        // make validations on uploaded image
        if (image.isEmpty()) {
            throw new AttachImageException("Attach Image Failed!");
        }

        String originalFileName = image.getOriginalFilename();
        int i = originalFileName.lastIndexOf('.');

        if (i > 0) {
            String ext = originalFileName.substring(i + 1);
            if (! (ext.equalsIgnoreCase("jpeg")
                    || ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("jpg"))) {
                throw new AttachImageException("Attach Image Failed!");
            }
        }
        else {
            throw new AttachImageException("Attach Image Failed!");
        }

        // try to upload image onto aws s3 bucket
        try {
            File f = new File(UPLOADED_FOLDER + recipe.getId());
            File imageFile = new File(f.getPath(), originalFileName);

            // Upload to s3
            if (PROFILE_NAME.equals("aws")) {
                // use recipe id as a unique identifier
                String keyName = "csye6225/upload-images/" + recipe.getId() + "/" + originalFileName;
                s3Services.uploadFile(keyName, image);
            }
            else {
                if (f.exists()) {
                    for (String s : f.list()) {
                        File fi = new File(f.getPath(), s);
                        if (fi.exists() && fi.isFile()) {
                            fi.delete();
                        }
                    }
                    if (! f.delete()) {
                        // TODO: throw exception
                    }
                }
                f.mkdir();
                f.setReadable(true, false);
                f.setWritable(true, false);
                image.transferTo(imageFile);
                imageFile.setReadable(true, false);
                imageFile.setWritable(true, false);
            }

            // create the Image object
            Image theImage = new Image();
            theImage.setId(UUID.randomUUID().toString());
            theImage.setUrl(imageFile.getPath());

            // attach the file to recipe and update the image table
            return imageService.attach(recipe, theImage);
        }
        catch (IOException e) {
//            if (PROFILE_NAME.equals("aws")) {
//                String keyName ="https://s3.amazonaws.com/" + bucketName + "/" + "csye6225/profiles/default/defaultpic.jpeg";
//                user.setPath("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQY-cYNxqLIgGM2GtDUUWlw0BFz9v_M8pl-YUXsfvVHFPmUAhMH");
//            }
//            else {
//                user.setPath("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQY-cYNxqLIgGM2GtDUUWlw0BFz9v_M8pl-YUXsfvVHFPmUAhMH");
//            }
//            userService.updateUser(user);
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
