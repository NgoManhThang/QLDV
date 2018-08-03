package com.viettel.api.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CreateBarcodePdf {

    public static void main(String... args) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("barcode.pdf"));

        document.open();
        PdfContentByte cb = writer.getDirectContent();

        Barcode39 barcode39 = new Barcode39();
        barcode39.setCode("123456789");
        Image code39Image = barcode39.createImageWithBarcode(cb, null, null);
        document.add(code39Image);
        document.newPage();

        Barcode128 barcode128 = new Barcode128();
        barcode128.setCode("itsol.vn");
        barcode128.setCodeType(Barcode.CODE128);
        Image code128Image = barcode128.createImageWithBarcode(cb, null, null);
        document.add(code128Image);
        document.newPage();

        BarcodeEAN barcodeEAN = new BarcodeEAN();
        barcodeEAN.setCode("3210123456789");
        barcodeEAN.setCodeType(Barcode.EAN13);
        Image codeEANImage = barcodeEAN.createImageWithBarcode(cb, null, null);
        document.add(codeEANImage);
        document.newPage();

        BarcodeQRCode barcodeQRCode = new BarcodeQRCode("http://itsol.vn", 1000, 1000, null);
        Image codeQrImage = barcodeQRCode.getImage();
        codeQrImage.scaleAbsolute(100, 100);
        document.add(codeQrImage);
        
        // Save to image
        java.awt.Image image = barcodeQRCode.createAwtImage(Color.BLACK, Color.WHITE);

        BufferedImage buffImg = new BufferedImage(image.getWidth(null), image.getWidth(null), BufferedImage.TYPE_4BYTE_ABGR);
        buffImg.getGraphics().drawImage(image, 0, 0, null);
        buffImg.getGraphics().dispose();

        File file = new File("tmp.png");
        ImageIO.write(buffImg, "png", file);

        document.close();
    }
}
