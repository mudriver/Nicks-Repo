package ie.turfclub.trainers.controller;




import ie.turfclub.trainers.model.TeFile;
import ie.turfclub.trainers.model.TeFile.UploadType;
import ie.turfclub.trainers.service.FileService;
import ie.turfclub.trainers.service.TrainersService;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 *
 * @author jdmr
 */
@Controller
@RequestMapping(value = "/trainersEmployeesOnline")
public class FileController {
    
    private static final Logger log = LoggerFactory.getLogger(FileController.class);
    
    @Autowired
    private FileService fileService;
    @Autowired
    private TrainersService trainerService;
    //private String fileUploadDirectory = "/home/sky/turfclub";
    private String fileUploadDirectory = "/home/FTP-shared/stableread/ftp";
    //private String fileUploadDirectory = "C:/files";
 
    
    @RequestMapping(value = "/upload/{id}", method = RequestMethod.GET)
    public @ResponseBody Map list( @PathVariable Integer id) {
        System.out.println("uploadGet called " + id);
        List<TeFile> list = fileService.list(id);
        List<TeFile> filesUpload = new ArrayList<TeFile>();
        for(TeFile file : list) {
            if(file.getUploadType() == UploadType.UPLOADED){
            	file.setUrl("/turfclubPrograms/trainersEmployeesOnline/picture/"+file.getId());
                file.setThumbnailUrl("/turfclubPrograms/trainersEmployeesOnline/thumbnail/"+file.getId());
                file.setDeleteUrl("/turfclubPrograms/trainersEmployeesOnline/delete/"+file.getId());
                file.setDeleteType("GET");
                filesUpload.add(file);
            }
        	
        }
        Map<String, Object> files = new HashMap<>();
        files.put("files", filesUpload);
        log.debug("Returning: {}", files);
        return files;
    }
    
    @RequestMapping(value = "/upload/{id}", method = RequestMethod.POST)
    public @ResponseBody Map upload(MultipartHttpServletRequest request, HttpServletResponse response, Authentication authentication, 
			
			@PathVariable(value = "id") Integer trainerId) throws Exception {
        System.out.println("uploadPost called");
        
        Iterator<String> itr = request.getFileNames();
        MultipartFile mpf;
        List<TeFile> list = new LinkedList<>();
        
        while (itr.hasNext()) {
            mpf = request.getFile(itr.next());
            log.debug("Uploading {}", mpf.getOriginalFilename());
            
            String newFilenameBase = UUID.randomUUID().toString();
            String originalFileExtension = mpf.getOriginalFilename().substring(mpf.getOriginalFilename().lastIndexOf("."));
            String newFilename = newFilenameBase + originalFileExtension;
            String storageDirectory = fileUploadDirectory;
            String contentType = mpf.getContentType();
            
            File newFile = new File(storageDirectory + "/" + newFilename);
            try {
                mpf.transferTo(newFile);
                //if the file is not a pdf set the thumbnail preview
                String thumbnailFilename = "";
                File thumbnailFile = null;
                System.out.println(mpf.getOriginalFilename() + " " + originalFileExtension);
                if(!originalFileExtension.equals(".pdf")){
                	BufferedImage thumbnail = Scalr.resize(ImageIO.read(newFile), 290);
                    thumbnailFilename = newFilenameBase + "-thumbnail.png";
                    thumbnailFile = new File(storageDirectory + "/" + thumbnailFilename);
                    ImageIO.write(thumbnail, "png", thumbnailFile);
                }
                //otherwise use pdf icon
                else{
                	thumbnailFilename = "pdfpreview" + "-thumbnail.png";
                	thumbnailFile = new File(storageDirectory + "/" + thumbnailFilename);
                	
                }
                
                
                TeFile file = new TeFile();
                file.setName(mpf.getOriginalFilename());
                file.setThumbnailFilename(thumbnailFilename);
                file.setNewFilename(newFilename);
                file.setContentType(contentType);
                file.setSize(mpf.getSize());
                file.setThumbnailSize(thumbnailFile.length());
                file.setFileUserId(trainerService.getTrainer(trainerId));
                file.setMergeCount(1);
                file.setUploadType(UploadType.UPLOADED);
                fileService.create(file);
                String rootUrl = "/turfclubPrograms";// /TurfClubOnline
                file.setUrl(rootUrl+"/trainersEmployeesOnline/picture/"+file.getId());
                file.setThumbnailUrl(rootUrl+"/trainersEmployeesOnline/thumbnail/"+file.getId());
                file.setDeleteUrl(rootUrl+"/trainersEmployeesOnline/delete/"+file.getId());
                file.setDeleteType("GET");
                
                list.add(file);
                
            } catch(IOException e) {
                log.error("Could not upload file "+mpf.getOriginalFilename(), e);
            }
            
        }
        
        Map<String, Object> files = new HashMap<>();
        files.put("files", list);
        return files;
    }
    
    @RequestMapping(value = "/picture/{id}", method = RequestMethod.GET)
    public void picture(HttpServletResponse response, @PathVariable Long id) {
        TeFile file = fileService.get(id);
        File imageFile = new File(fileUploadDirectory+"/"+file.getNewFilename());
        response.setContentType(file.getContentType());
        response.setContentLength(file.getSize().intValue());
        try {
            InputStream is = new FileInputStream(imageFile);
            IOUtils.copy(is, response.getOutputStream());
        } catch(IOException e) {
            log.error("Could not show picture "+id, e);
        }
    }
    
    @RequestMapping(value = "/thumbnail/{id}", method=RequestMethod.GET)
    public void thumbnail(HttpServletResponse response, @PathVariable Long id) {
    	TeFile file = fileService.get(id);
        File imageFile = new File(fileUploadDirectory+"/"+file.getThumbnailFilename());
        response.setContentType(file.getContentType());
        response.setContentLength(file.getThumbnailSize().intValue());
        try {
            InputStream is = new FileInputStream(imageFile);
            IOUtils.copy(is, response.getOutputStream());
        } catch(IOException e) {
            log.error("Could not show thumbnail "+id, e);
        }
    }
    
    @RequestMapping(value = "/delete/{id}", method=RequestMethod.GET)
    public @ResponseBody List<Map<String, Object>> delete(@PathVariable Long id) {
    	System.out.println("DELETE" + id);
    	TeFile file = fileService.get(id);
        File imageFile = new File(fileUploadDirectory+"/"+file.getNewFilename());
        imageFile.delete();
        String originalFileExtension = imageFile.getName().substring(imageFile.getName().lastIndexOf("."));
        if(originalFileExtension.equals(".pdf")){
        	 File thumbnailFile = new File(fileUploadDirectory+"/"+file.getThumbnailFilename());
             thumbnailFile.delete();
        }
       
        fileService.delete(file);
        List<Map<String, Object>> results = new ArrayList<>();
        Map<String, Object> success = new HashMap<>();
        success.put("success", true);
        results.add(success);
        return results;
    }
}
