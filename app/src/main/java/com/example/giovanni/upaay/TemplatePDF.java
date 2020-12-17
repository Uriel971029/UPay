package com.example.giovanni.upaay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Uriel on 06/04/2018.
 */

public class TemplatePDF {

    Context context;
    File pdfFile;
    File file;
    Document document;
    PdfWriter pdfWriter;
    Paragraph paragraph;

    int cont = 0;
    Context TheThis;

    String NameOfFolder = "/RecursosQR";
    String NameOfFile = "codigo"+cont;

    Font fTitle = new Font(Font.FontFamily.TIMES_ROMAN,20,Font.BOLD);
    Font fSubTitle = new Font(Font.FontFamily.TIMES_ROMAN,18,Font.BOLD);
    Font fText = new Font(Font.FontFamily.TIMES_ROMAN,12,Font.BOLD);
    Font fHighText = new Font(Font.FontFamily.TIMES_ROMAN,15,Font.BOLD, BaseColor.BLUE);

    public TemplatePDF(Context context) {

        this.context = context;
    }

    public  void openDocument(){

        createFile();

        try{

            document = new Document(PageSize.A4);
            pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(pdfFile));

            document.open();

        }catch (Exception e){

            Log.e("openDocument",e.toString());
        }

    }


    private void createFile(){

        int cont = 0;

        File folder = new File(Environment.getExternalStorageDirectory().toString(),"UPAY_PDF");

        if(!folder.exists())
            folder.mkdirs();

        cont++;
        pdfFile = new File(folder, "FICHA_PAGO"+cont+".pdf");


    }

    public void closeDocument(){


    document.close();

    }

    public void addMetaData(String title, String subject, String author){

        document.addTitle(title);
        document.addSubject(subject);
        document.addAuthor(author);

    }


    public void addTitles(String title, String subtitle, String date){

        try{
        paragraph = new Paragraph();
        addChildP(new Paragraph(title,fTitle));
        addChildP(new Paragraph(subtitle,fSubTitle));
        addChildP(new Paragraph("Fecha:"+ date,fSubTitle));
        paragraph.setSpacingAfter(30);
        document.add(paragraph);
        }catch (Exception e){

            Log.e("addTitles",e.toString());
        }
    }

    private void addChildP(Paragraph childParagraph){


        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);
    }



    public void addParagraph(String text){


        try{
        paragraph = new Paragraph(text,fHighText);
        paragraph.setSpacingAfter(5);
        paragraph.setSpacingBefore(5);
        document.add(paragraph);
        }catch (Exception e){

            Log.e("addParagraph",e.toString());
        }
    }



    //CREAR TABLAS EN PDF
    public void createTable(String[]header, ArrayList<String[]>clients){

        paragraph = new Paragraph();
        paragraph.setFont(fText);
        PdfPTable pdfPTable = new PdfPTable(header.length);
        pdfPTable.setWidthPercentage(100);
        pdfPTable.setSpacingBefore(20);
        PdfPCell pdfPCell;



        int indexC = 0;

        while(indexC < header.length){

            pdfPCell = new PdfPCell(new Phrase(header[indexC++],fSubTitle));
            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfPCell.setBackgroundColor(BaseColor.ORANGE);

            //AGREGAMOS LA CELDA DISEÑADA A NUESTRA TABALA
            pdfPTable.addCell(pdfPCell);
        }


        for(int indexR=0;indexR<clients.size();indexR++){

            String[]row = clients.get(indexR);

            for(indexC=0;indexC<header.length;indexC++){

                pdfPCell = new PdfPCell(new Phrase(row[indexC]));

                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);

                pdfPCell.setFixedHeight(40);

                pdfPTable.addCell(pdfPCell);

            }

        }

        paragraph.add(pdfPTable);

        try{
        document.add(paragraph);

        }catch (Exception e){

            Log.e("createTable",e.toString());
        }
    }


    //FUNCION PARA CREAR LA IMAGEN DEL CÓDIGO QR QUE SE VA INSERTAR EN EL DOCUMENTO PDF
    public void createImage(Context contexto, Bitmap ImageToSave){

        cont++;

        NameOfFile = "recurso"+cont;

        TheThis = contexto;
        //CREAMOS NUESTRA CARPETA
        String file_path = Environment.getExternalStorageDirectory().toString() + NameOfFolder;
        //String CurrentDateAndTime = getCurrentDateAndTime();

        File dir = new File(file_path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //CREAMOS Y GUARDAMOS EL ARCHIVO QUE CONTENDRA LA IMAGEN DENTRO DE LA CARPETA RECIEN CREADA
        file = new File(dir, NameOfFile + ".png");

        //CREACIÓN DE LA IMAGEN
        try {

            FileOutputStream fOut = new FileOutputStream(file);
            //CREAMOS LA IMAGEN DEL CODIGO QR
            ImageToSave.compress(Bitmap.CompressFormat.PNG, 85, fOut);

            fOut.flush();
            fOut.close();
            AbleToSave();
        }
        catch(FileNotFoundException e) {
            UnableToSave();
        }
        catch(IOException e) {
            UnableToSave();
        }
    }


    private void UnableToSave() {
        Toast.makeText(TheThis, "¡No se ha podido generar el recurso!", Toast.LENGTH_SHORT).show();
    }
    private void AbleToSave() {
        Toast.makeText(TheThis, "Recurso generado", Toast.LENGTH_SHORT).show();
    }

    //FUNCION PARA INSERTAR LA FECHA CON FORMATO EN TIEMPO REAL
        /*private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-­ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }*/

    public void insertImage(){

        try{
        Image imagen = Image.getInstance(file.getAbsolutePath());
        document.add(imagen);

        }catch (Exception e){

            Log.e("createImage",e.toString());
        }

    }


    public void insertImage(String path){

        try{
            Image imagen = Image.getInstance(path);
            document.add(imagen);

        }catch (Exception e){

            Log.e("createImage",e.toString());
        }

    }



    public void viewPDF(){

        Intent intent = new Intent(context,ViewPDFActivity.class);
        intent.putExtra("path",pdfFile.getAbsolutePath());
        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);



    }



}
