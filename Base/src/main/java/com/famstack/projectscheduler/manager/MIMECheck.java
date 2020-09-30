package com.famstack.projectscheduler.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import oracle.net.aso.i;

public class MIMECheck {
    public static final int[] exeMagicHeader = new int[]{ 0x4d, 0x5a, 0x90, 0x00, 0x3, 0x00, 0x00, 0x00 };
    public static final int[] msiMagicHeader = new int[]{ 0xd0, 0xcf, 0x11, 0xe0, 0xa1, 0xb1, 0x1a, 0xe1 };
    
    public static final int[] jpgMagicHeader = new int[] { 0xff,0xd8,0xff,0xe0,0x0,0x10,0x4a,0x46 };
    public static final int[] jpegMagicHeader = new int[] { 0xff,0xd8,0xff,0xe0,0x0,0x10,0x4a,0x46 };
    public static final int[] pngMagicHeader = new int[] { 0x89,0x50,0x4e,0x47,0xd,0xa,0x1a,0xa };
    public static final int[] gifMagicHeader = new int[] { 0x47,0x49,0x46,0x38,0x39,0x61,0xfd,0x3 };
    public static final int[] docMagicHeader = new int[] { 0xd0,0xcf,0x11,0xe0,0xa1,0xb1,0x1a,0xe1 };
    public static final int[] docxMagicHeader = new int[] { 0x50,0x4b,0x3,0x4,0x14,0x0,0x6,0x0 };
    public static final int[] xlsMagicHeader = new int[] { 0x3c,0x68,0x74,0x6d,0x6c,0x20,0x78,0x6d };
    public static final int[] xlsxMagicHeader = new int[] { 0x50,0x4b,0x3,0x4,0x14,0x0,0x8,0x8 };
    public static final int[] pdfMagicHeader = new int[] { 0x25,0x50,0x44,0x46,0x2d,0x31,0x2e,0x37 };

    public static void main(String[] args) {
       File OriginalTempFile = new File("c:/famstack/test/exe.jpg");
       generateMagicArray(new File("c:/famstack/test/jpg.jpg"));
       generateMagicArray(new File("c:/famstack/test/jpeg.jpeg"));
       generateMagicArray(new File("c:/famstack/test/png.png"));
       generateMagicArray(new File("c:/famstack/test/gif.gif"));
       generateMagicArray(new File("c:/famstack/test/doc.doc"));
       generateMagicArray(new File("c:/famstack/test/docx.docx"));
       generateMagicArray(new File("c:/famstack/test/xls.xls"));
       generateMagicArray(new File("c:/famstack/test/xlsx.xlsx"));
       generateMagicArray(new File("c:/famstack/test/pdf.pdf"));
    }
    public static boolean isHarmFul(int[] intArray) {
    	return isFileMatch(intArray, exeMagicHeader);
    }
    
    public static boolean isAllowedFiles(int[] intArray) {
    	return (isFileMatch(intArray, pdfMagicHeader) 
    			|| isFileMatch(intArray, jpegMagicHeader)
    			|| isFileMatch(intArray, xlsxMagicHeader)
    			|| isFileMatch(intArray, xlsMagicHeader)
    			|| isFileMatch(intArray, docxMagicHeader)
    			|| isFileMatch(intArray, docMagicHeader)
    			|| isFileMatch(intArray, jpgMagicHeader)
    			|| isFileMatch(intArray, pngMagicHeader)
    			|| isFileMatch(intArray, gifMagicHeader)) ? true : false;
    }

    public static boolean isFileMatch(int[] file, int[] magicHeader) {
        try {
            for (int i = 0; i < 8; i++) {
                if (file[i] != magicHeader[i]) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    
    public static boolean isFileMatch(InputStream inputStream, int[] magicHeader) {
        try {
            for (int i = 0; i < 8; i++) {
            	int b = inputStream.read();
                if (b != magicHeader[i]) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    
    public static void generateMagicArray(File file) {
        String createMagicArray = "public final int[] "+file.getName().substring(0,file.getName().lastIndexOf('.'))+"MagicHeader = new int[] { ";
        try {
            FileInputStream ins = new FileInputStream(file);
            for (int i = 0; i < 8; i++) {
                createMagicArray += "0x" + Integer.toHexString(ins.read()) + ",";
            }
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        createMagicArray = createMagicArray.substring(0, createMagicArray.length() - 1);
        createMagicArray += " };";
        System.out.println(createMagicArray);
    }
}