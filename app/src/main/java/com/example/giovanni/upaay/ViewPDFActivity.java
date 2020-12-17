package com.example.giovanni.upaay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class ViewPDFActivity extends AppCompatActivity {

    PDFView pdfView;
    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);

        pdfView = (PDFView) findViewById(R.id.pdfView);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){

            //RECIBIMOS LA DIRECCION DEL ARCHIVO PDF PARA ABRIRLO
            file = new File(bundle.getString("path",""));
        }

        pdfView.fromFile(file)

                .enableSwipe(true)
                .swipeHorizontal(true)
                .enableDoubletap(true)
                .enableAntialiasing(true)
                .load();

    }


}
