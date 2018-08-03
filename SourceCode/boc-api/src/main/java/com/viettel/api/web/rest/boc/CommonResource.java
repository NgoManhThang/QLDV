package com.viettel.api.web.rest.boc;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.viettel.api.config.Constants;
import com.viettel.api.dto.boc.BocConstantDto;
import com.viettel.api.dto.boc.BocFilesDto;
import com.viettel.api.dto.boc.BocRoleDto;
import com.viettel.api.dto.boc.BocRoleTargetDto;
import com.viettel.api.dto.boc.BocUnitDto;
import com.viettel.api.service.boc.CommonService;
import com.viettel.api.utils.DataUtil;
import com.viettel.api.web.rest.BaseController;

/**
 * Created by VTN-PTPM-NV04 on 2/7/2018.
 */
@RestController
@RequestMapping(Constants.API_PATH_PREFIX + "common")
public class CommonResource extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(CommonResource.class);

    @Autowired
    CommonService commonService;

    @GetMapping("/getImageByBarcode")
    @Timed
	public ResponseEntity<byte[]> getImageBarcode(HttpServletResponse response, @RequestParam("barCode") String barCode) {
		try {
	        Barcode128 barcode128 = new Barcode128();
	        barcode128.setCode(barCode.trim());
	        barcode128.setCodeType(Barcode.CODE128);
	        Image code128Image = barcode128.createAwtImage(Color.BLACK, Color.WHITE);
	        
	        BufferedImage bufferedImage = new BufferedImage(code128Image.getWidth(null), code128Image.getHeight(null), BufferedImage.TYPE_INT_RGB);
	        Graphics2D g2 = bufferedImage.createGraphics();
	        g2.drawImage(code128Image, null, null);
	
	        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	        ImageIO.write(bufferedImage, "jpg", outStream); 
	        return new ResponseEntity<byte[]>(outStream.toByteArray(), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
	}
    
    @GetMapping("/getPdfPrinter")
    @Timed
    public ResponseEntity<byte[]> getPdfPrinter(HttpServletRequest request, @RequestParam("barCode") String barCode) {
    	try {
    		String path = this.getClass().getClassLoader().getResource("").getPath();
            String fullPath = URLDecoder.decode(path, "UTF-8");
            String pathArr[] = fullPath.split("/target/classes");
            String rootPath = pathArr[0].substring(0, pathArr[0].lastIndexOf("/"));
            rootPath += File.separator + "file_barcode_out";
            String currentTime = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());

        	File barcodefile = new File(rootPath);
        	if (!barcodefile.exists()) {
        		barcodefile.mkdirs();
            }
        	
    		Rectangle rectangle = new Rectangle(166, 100);
            Document document = new Document(rectangle);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(rootPath + File.separator + "barcode_"+ currentTime +".pdf"));
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            
            com.itextpdf.text.Image imageIcon = com.itextpdf.text.Image.getInstance(fullPath + "/viettel.png");
            cb.addImage(imageIcon, 80f, 0f, 0f, 30f, 43f, 50f);
            
            Font font1 = FontFactory.getFont(FontFactory.TIMES_BOLD, 12);
            Phrase phrase1 = new Phrase("VIETTEL NETWORK", font1);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
            		phrase1,
                    (document.right() - document.left())/2 + document.leftMargin(),
                    document.top() + 20, 0);
            
            Font font2 = FontFactory.getFont(FontFactory.TIMES, 10);
            Phrase phrase2 = new Phrase("Security Ticket", font2);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
            		phrase2,
                    (document.right() - document.left())/2 + document.leftMargin(),
                    document.top() + 10, 0);
            
            Font font3 = FontFactory.getFont(FontFactory.TIMES, 10);
            Phrase phrase3 = new Phrase(DataUtil.date2ddMMyyyyHHMMss(new Date()), font3);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
            		phrase3,
                    (document.right() - document.left())/2 + document.leftMargin(),
                    document.top(), 0);
            
            Font font4 = FontFactory.getFont(FontFactory.TIMES, 10);
            Phrase phrase4 = new Phrase("Security Code: " + barCode.trim(), font4);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
            		phrase4,
                    (document.right() - document.left())/2 + document.leftMargin(),
                    document.top() - 10, 0);
            
            Font font5 = FontFactory.getFont(FontFactory.TIMES, 8);
            Phrase phrase5 = new Phrase("(Please keep this ticket carefully!)", font5);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
            		phrase5,
                    (document.right() - document.left())/2 + document.leftMargin(),
                    document.top() - 20, 0);
            
            Barcode128 barcode128 = new Barcode128();
            barcode128.setCode(barCode.trim());
            barcode128.setCodeType(Barcode.CODE128);
            java.awt.Image code128Image = barcode128.createAwtImage(Color.BLACK, Color.WHITE);
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(code128Image, null);
            cb.addImage(image, 80f, 0f, 0f, 30f, 43f, 10f);
            document.close();
            
            InputStream is = new FileInputStream(rootPath + File.separator + "barcode_"+ currentTime +".pdf");
            byte[] contents = IOUtils.toByteArray(is);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
            String filename = "barcode.pdf";
            headers.setContentDispositionFormData(filename, filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
            return response;
    	} catch (Exception e) {
    		logger.error(e.getMessage(), e);
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    }
    
    @GetMapping("/getFileById")
    @Timed
    public ResponseEntity<byte[]> getFileById(@RequestParam("fileId") Long fileId) {
        try {
        	BocFilesDto bocFilesDto = commonService.getFileById(fileId);
        	
            ResourceBundle resource = ResourceBundle.getBundle("config/globalConfig");
            String folderUpload = resource.getString("FOLDER_UPLOAD");
            InputStream is = new FileInputStream(folderUpload + File.separator + bocFilesDto.getFilePath());

            byte[] contents = IOUtils.toByteArray(is);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
            String filename = bocFilesDto.getFileName();
            headers.setContentDispositionFormData(filename, filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
            return response;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/getFile")
    @Timed
    public ResponseEntity<byte[]> getFile(@RequestBody BocFilesDto bocFilesDto) {
        try {
            ResourceBundle resource = ResourceBundle.getBundle("config/globalConfig");
            String folderUpload = resource.getString("FOLDER_UPLOAD");
            InputStream is = new FileInputStream(folderUpload + File.separator + bocFilesDto.getFilePath());

            byte[] contents = IOUtils.toByteArray(is);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
            String filename = bocFilesDto.getFileName();
            headers.setContentDispositionFormData(filename, filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
            return response;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/getListBocRole")
	@Timed
	public ResponseEntity<List<BocRoleDto>> getListBocRole() {
    	List<BocRoleDto> result = commonService.getListBocRole();
    	return new ResponseEntity<>(result, HttpStatus.OK);
	}
    
    @GetMapping("/getListBocUnit")
	@Timed
	public ResponseEntity<List<BocUnitDto>> getListBocUnit() {
    	List<BocUnitDto> result = commonService.getListBocUnit();
    	return new ResponseEntity<>(result, HttpStatus.OK);
	}
    
    @GetMapping("/getListBocRoleTarget")
	@Timed
	public ResponseEntity<List<BocRoleTargetDto>> getListBocRoleTarget() {
    	List<BocRoleTargetDto> result = commonService.getListBocRoleTarget();
    	return new ResponseEntity<>(result, HttpStatus.OK);
	}
    
    @PostMapping("/getListBocConstant")
	@Timed
	public ResponseEntity<List<BocConstantDto>> getListBocConstant(String constantType) {
    	List<BocConstantDto> result = commonService.getListBocConstant(constantType);
    	return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
