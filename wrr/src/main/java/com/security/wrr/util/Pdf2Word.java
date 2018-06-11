package com.security.wrr.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;

/**
 * pdf转word
 */
public class Pdf2Word {

    public static File pdf2word(File pdfFile,String docFilePath,String md5Code){
        String docFile =docFilePath+md5Code+".doc";
        PDDocument doc= null;
        FileOutputStream fos=null;
        Writer writer=null;
        try {
            doc = PDDocument.load(pdfFile);
            int pagenumber=doc.getNumberOfPages();
            System.out.print("pages"+pagenumber);
            fos=new FileOutputStream(docFile);
            writer=new OutputStreamWriter(fos,"GBK");
            PDFTextStripper stripper=new PDFTextStripper();
            stripper.setSortByPosition(true);//排序
            stripper.setStartPage(1);//设置转换的开始页
            stripper.setEndPage(pagenumber);//设置转换的结束页
            stripper.writeText(doc,writer);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
                doc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return new File(docFile);
    }
}
