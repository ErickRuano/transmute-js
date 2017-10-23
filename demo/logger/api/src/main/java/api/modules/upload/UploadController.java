package mcce.sapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Paths;
import java.io.BufferedOutputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;



/**
 * A class to test interactions with the MySQL database using the songDao class.
 *
 * @author pentcloud
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class UploadController {

    /**
   * POST /uploadFile -> receive and locally save a file.
   * 
   * @param uploadfile The uploaded file as Multipart file parameter in the 
   * HTTP request. The RequestParam name must be the same of the attribute 
   * "name" in the input tag with type file.
   * 
   * @return An http OK status in case of success, an http 4xx status in case 
   * of errors.
   */
  @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity<?> uploadFile(
      @RequestParam("uploadfile") MultipartFile uploadfile) {
    
    try {
      // Get the filename and build the local file path (be sure that the 
      // application have write permissions on such directory)
      String filename = uploadfile.getOriginalFilename();
      String directory = "/var/www/html/training/transmute-js/demo/pcra/uploads";
      String filepath = Paths.get(directory, filename).toString();
      
      // Save the file locally
      BufferedOutputStream stream =
          new BufferedOutputStream(new FileOutputStream(new File(filepath)));
      stream.write(uploadfile.getBytes());
      stream.close();
    }
    catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    
    return new ResponseEntity<>(HttpStatus.OK);
  } // method uploadFile

  
} // class SongController