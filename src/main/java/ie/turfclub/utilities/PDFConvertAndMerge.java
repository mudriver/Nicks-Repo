package ie.turfclub.utilities;

import ie.turfclub.trainers.model.TeFile;
import ie.turfclub.trainers.model.TeFile.UploadType;
import ie.turfclub.trainers.model.TeTrainers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BadPdfFormatException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFConvertAndMerge {

	public static TeFile mergePdfFiles(List<TeFile> fileNames, String fileDirectory ){
		System.out.println("Merge " + fileNames.size() + " Files");
		String mergedFileName = UUID.randomUUID().toString() + ".pdf";
		TeTrainers trainer = null;
		Document document = new Document();
        // step 2
        PdfCopy copy = null;
		try {
			copy = new PdfCopy(document, new FileOutputStream(fileDirectory + mergedFileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // step 3
        document.open();
        // step 4
        PdfReader reader = null;
        int n;
        // loop over the documents you want to concatenate
        for (TeFile file : fileNames) {
            System.out.println("Merge File "+ fileDirectory + file.getNewFilename());
        	trainer = file.getFileUserId();
        	try {
				reader = new PdfReader(fileDirectory + file.getNewFilename());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            // loop over the pages in that document
            n = reader.getNumberOfPages();
            for (int page = 0; page < n; ) {
                try {
					copy.addPage(copy.getImportedPage(reader, ++page));
					copy.freeReader(reader);
				} catch (BadPdfFormatException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            
            reader.close();
        }
        
        // step 5
        document.close();
        File pdfFile = new File(fileDirectory + mergedFileName);
        TeFile mergedFile = new TeFile();
        mergedFile.setName(mergedFileName);
        mergedFile.setNewFilename(mergedFileName);
		mergedFile.setThumbnailFilename("pdfpreview" + "-thumbnail.png");
		mergedFile.setContentType("application/pdf");
		mergedFile.setSize(pdfFile.length());
		mergedFile.setThumbnailSize((long) 31796);
		mergedFile.setFileUserId(trainer);
		mergedFile.setMergeCount(fileNames.size());
		mergedFile.setUploadType(UploadType.MERGED);
        
		return mergedFile;
		
		
	}
	
	public static void convertFile(TeFile fileToConvert, String fileDirectory){
		try{
		    
			
			  
			
			
		    
		    //Get the input image to Convert to PDF
		    Image convertImage=Image.getInstance(fileDirectory + fileToConvert.getNewFilename());
		    //Add image to Document
		  //Create Document Object
		    Document convertImageToPdf=new Document();
		    //Create PdfWriter for Document to hold physical file
		   
		    
		    
		    convertImageToPdf.setPageSize(new Rectangle(convertImage.getWidth(), convertImage.getHeight()));
		    String[] splitFileName = (fileToConvert.getNewFilename()).split("\\.");
			
		    PdfWriter.getInstance(convertImageToPdf, new FileOutputStream(fileDirectory + splitFileName[0] + ".pdf"));
		    convertImageToPdf.open();
		    convertImageToPdf.add(convertImage);
		    //Close Document
		    convertImageToPdf.close();
		    System.out.println("Successfully Converted Image to PDF in iText");
		    
		}
		catch (Exception i1){
		    i1.printStackTrace();
		}
	}
	
}
