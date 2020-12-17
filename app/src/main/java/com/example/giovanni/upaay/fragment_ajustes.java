package com.example.giovanni.upaay;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;
import static com.example.giovanni.upaay.MainActivity.URL_SERVER;
import static com.example.giovanni.upaay.RealPathUtil.getRealPathFromURI_API11to18;
import static com.example.giovanni.upaay.RealPathUtil.getRealPathFromURI_API19;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.UploadStatusDelegate;

import iammert.com.expandablelib.ExpandableLayout;
import iammert.com.expandablelib.Section;


public class fragment_ajustes extends Fragment implements View.OnClickListener{


    ImageView imageView, iconArrow, iconChild;
    TextView parent, child;
    private int counter;
    //private UnploadTaskAsync tareaSegundoPlano;
    //private ProgressDialog pDialog;
    //private Uri imageFileUri;
    private static String APP_DIRECTORY = "UPay/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "photoProfile";

    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;
    private String mPath;
    private String imageName;
    private File newFile;
    private FrameLayout mVista;
    //DIALOGO DE ESPERA
    private Dialog loading = null;
    private Usuario usuario;
    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.ventana_ajustes, container, false);
        //INICIALIZAMOS LA BIBLIOTECA PARA SUBIDA DE IMAGENES AL SERVIDOR
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        imageView = (ImageView)view.findViewById(R.id.img_perfil);
        mVista = (FrameLayout) view.findViewById(R.id.contenedor);
        ExpandableLayout layout = (ExpandableLayout) view.findViewById(R.id.expandable_layout);

        counter = 0;
        //RECUPERACIÓN DE LOS DATOS DEL ALUMNO
        bundle = getArguments();
        usuario = (Usuario) bundle.getSerializable("datosUsuario");

        layout.setRenderer(new ExpandableLayout.Renderer() {
            @Override
            public void renderParent(View view, Object fieldName, boolean isExpanded, int parentPosition) {

                parent = view.findViewById(R.id.txt_parent);
                parent.setText(((String)fieldName));
                iconArrow = view.findViewById(R.id.arrow);

                switch (counter){

                    case 1:
                        iconArrow.setBackgroundResource(R.drawable.ic_tools_student);
                        break;

                    case 2:
                        iconArrow.setBackgroundResource(R.drawable.ic_tools_data);
                        break;

                    case 3:
                        iconArrow.setBackgroundResource(R.drawable.ic_tools_security);
                        break;

                    default:
                        break;

                }

            }

            @Override
            public void renderChild(View view, Object value, int parentPosition, int childPosition) {

                String cadena = ((String)value);
                child = view.findViewById(R.id.txt_child);
                iconChild = view.findViewById(R.id.arrowChild);
                child.setText(cadena);

                if(cadena.contains("contraseña") || cadena.contains("token"))
                    iconChild.setBackgroundResource(R.drawable.ic_tools_go);


            }
        });

        //PINTAMOS LOS CAMPOS DEL ACTIVITY
        layout.addSection(getSection());
        layout.addSection(getSection());
        layout.addSection(getSection());


        //ESTABLECEMOS LA FOTO DE PERFIL DEL USUARIO SI ES QUE YA LA DETERMINO
        recuperarFoto();
        imageView.setOnClickListener(this);
        return view;
    }


    //SECCIONAMOS LAS PESTAÑAS DEL LAYOUT CON SU CORRESPONDIENTE CONTENIDO
    private Section <String, String> getSection() {

        Section <String, String> section = new Section<>();

        List<String> list = new ArrayList<>();

            switch (counter){
                
                case 0:
                    section.parent = "Datos del alumno";
                    list.add("Matrícula del alumno: "+ usuario.getMatricula());
                    list.add("Nombre del alumno: " + usuario.getNombre() + " " + usuario.getApellidoP() + " " + usuario.getGetApellidoM());
                    list.add("Carrera: " + usuario.getCarrera());
                    list.add("Cuatrimestre y Grupo: " + usuario.getCuatrimestre() + usuario.getGrupo());
                    list.add("Estado del alumno: " + usuario.getStatusAlumno());

                    break;

                case 1:
                    section.parent = "Datos personales";
                    list.add("Telefono: " + usuario.getTelefono());
                    list.add("Correo: " + usuario.getCorreo());

                    break;

                case 2:
                    section.parent = "Seguridad y privacidad";
                    //list.add("Nombre de usuario: " + usuario.getUsername());
                    list.add("Cambiar contraseña" + usuario.getPassword());
                    list.add("Generar token de usuario");

                    break;

                    default:
                        break;

            }

        section.children.addAll(list);
        counter++;
        return section;
    }

    @Override
    public void onClick(View v) {

        imageView.setEnabled(true);
        //REVISAMOS LOS PERMISOS
        if(mayRequestStoragePermission()) {
            //HABILITAMOS LA IMAGEN Y MOSTRAMOS EL CUADRO DE DIÁLOGO CON LAS OPCIONES CORRESPONDIENTES
            //imageView.setEnabled(true);
            showOptions();
        }

    }

    private void showOptions() {


        final CharSequence[] option = {"Tomar foto", "Elegir de galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Seleccione una opción");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface loading, int which) {
                if(option[which] == "Tomar foto"){
                    //SOLICITAR PERMISOS PARA TOMAR FOTOGRAFÍAS Y ALMACENARAS EN EL DISPOSITIVO
                    openCamera();
                }else if(option[which] == "Elegir de galeria"){
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Selecciona una imagen de perfil"), SELECT_PICTURE);
                }else {
                    loading.dismiss();
                }
            }
        });

        builder.show();
    }

    private void openCamera() {

        //CREAMOS UN NUEVO ARCHIVO PARA VERIFICAR LA UBICACIÓN DE LA CARPETA EN LA QUE VAMOS A ALMACENAR LA FOTOS EN LA MEMORIA EXTERNA
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();
        //VERIFICAMOS SI EL DIRECTORIO EN DONDE VAMOS A GUARDAR YA EXISTE, DE LO CONTRARIO LO CREAMOS
        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated){
            //Long timestamp = System.currentTimeMillis() / 1000;
            //timestamp.toString() + ".jpg";
            //ASIGNAMOS PATH Y NOMBRE AL ARCHIVO
            imageName = setNombreFoto();
            mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY
                    + File.separator + imageName;

            //CREAMOS EL ARCHIVO FINAL CON TODAS LAS CARACTERÍSTICAS YA ESTABLECIDAS COMO EL PATH Y EL NOMBRE
            newFile = new File(mPath);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", newFile));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, PHOTO_CODE);
        }
    }


    public String setNombreFoto(){

        String nameFile="";
        // Producir nuevo int aleatorio entre 0 y 99
        Random aleatorio = new Random(System.currentTimeMillis());
        int num = aleatorio.nextInt(1000);
        //DAMOS FORMATO AL NOMBRE CON EL VALOR ALEATORIO AGREGADO
        nameFile = "UPayP" + String.valueOf(num) + ".jpg";
        // Refrescar datos aleatorios
        aleatorio.setSeed(System.currentTimeMillis());

        return nameFile;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageView.setRotation(0.0f);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case PHOTO_CODE:
                    MediaScannerConnection.scanFile(getActivity(),
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });


                    Picasso.get().load(newFile)
                            //fit() NOS PERMITE HACER UN RESIZE DE LA IMÁGEN A LO QUE ESPERA EL CONTENEDOR EN DONDE VA A ESTAR
                            .fit()
                            //CenterInside() NOS PERMITE MOSTRAR TODA LA IMAGEN DENTRO DEL CONTENEOR AUNQUE AVECES NO ABARCA TODO ESTE
                            //CenterCrop POR SU PARTE ABARCA TODO EL CONTENEDOR PERO PUEDE QUE NO SE MUESTRE TODA LA IMAGEN
                            .centerInside()
                            .into(imageView);

                    break;
                case SELECT_PICTURE:

                    Uri path = data.getData();
                    mPath = getRealPathFromURI_API19(getActivity().getApplicationContext(),path);
                    newFile = new File(mPath);
                    imageName =  newFile.getName();

                    Picasso.get().load(path)
                            //fit() NOS PERMITE HACER UN RESIZE DE LA IMÁGEN A LO QUE ESPERA EL CONTENEDOR EN DONDE VA A ESTAR
                            .fit()
                            //CenterInside() NOS PERMITE MOSTRAR TODA LA IMAGEN DENTRO DEL CONTENEOR AUNQUE AVECES NO ABARCA TODO ESTE
                            //CenterCrop POR SU PARTE ABARCA TODO EL CONTENEDOR PERO PUEDE QUE NO SE MUESTRE TODA LA IMAGEN
                            .centerInside()
                            .into(imageView);

                    //CUIDAMOS LA POSICION DE LA FOTO
                    String nameFoto = imageName.substring(0, 5);
                    float rotacion = imageView.getRotation();
                    if (nameFoto.equalsIgnoreCase("UPayP")) {
                        if(rotacion == 0.0)
                            imageView.setRotation(270.0f);
                    }else{
                        if(rotacion == 270.0)
                            imageView.setRotation(90.0f);
                    }
                    //imageView.setRotation(270.0f);
                    break;

                    default:

                        break;

            }

            //SE REGISTRA LA OPERACION EN LA BD
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        //PARA Obtener la datos usamos un onjeto Json
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean isPhotoRepeated = jsonResponse.getBoolean("repeated");

                        if(isPhotoRepeated){

                            Snackbar.make(mVista, "Foto de perfil actualizada", Snackbar.LENGTH_SHORT).show();

                        }else{
                            subirFoto();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            //enviamos los datos a la clase LoginRequest en la cuál se ejecutará del lado del servidor la solicitud
            searchPhotoRequest search = new searchPhotoRequest(imageName, usuario.getUsername(), responseListener);
            //RequestQueue queue = Volley.newRequestQueue(getContext());
            VolleySingleton.getInstance(getContext()).addToRequestQueue(search);
            //Se envía el objeto LoginRequest cargado con los datos a la cola de peticiones del servidor
            //queue.add(search);

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(outState != null)
            outState.putString("file_path", mPath);
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {

        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null)
            mPath = savedInstanceState.getString("file_path");

    }


    public void subirFoto(){

        String URL_SUBIRPICTURE = URL_SERVER + "unploadPicture.php";

        try {

            String uploadId = UUID.randomUUID().toString();
            new MultipartUploadRequest(getActivity(), uploadId, URL_SUBIRPICTURE)
                     .setUtf8Charset()
                    .addFileToUpload(mPath, "picture")
                    .addParameter("filename", imageName)
                    .addParameter("usuario", usuario.getUsername())
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {

                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                            Toast.makeText(context, "Ocurrió un error al subir la imagen", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

                            //Toast.makeText(context, "Imagen subida exitosamente", Toast.LENGTH_SHORT).show();

                            Snackbar.make(mVista, "Imagen subida exitosamente", Snackbar.LENGTH_SHORT).show();


                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {

                            Toast.makeText(context, "Cancelado", Toast.LENGTH_SHORT).show();

                        }

                    })
                    .setNotificationConfig(new UploadNotificationConfig())
                    .startUpload();

        } catch (Exception exc) {
            exc.printStackTrace();
            Log.e("AndroidUploadService", exc.getMessage(), exc);
            System.out.println(exc.getMessage()+" "+exc.getLocalizedMessage());
        }

    }


    public void recuperarFoto(){

        final String[] rutaImagen = {""};

        loading = new Dialog(getContext());
        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading.setContentView(R.layout.dialog_loading);
        loading.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        loading.show();

        //SE REGISTRA LA OPERACION EN LA BD
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    //PARA Obtener la datos usamos un onjeto Json
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    rutaImagen[0] = jsonResponse.getString("ruta");

                    if(success){

                        loading.dismiss();
                        Picasso.get().load(rutaImagen[0])
                                //fit() NOS PERMITE HACER UN RESIZE DE LA IMÁGEN A LO QUE ESPERA EL CONTENEDOR EN DONDE VA A ESTAR
                                .error(R.drawable.camara)
                                .fit()
                                //CenterInside() NOS PERMITE MOSTRAR TODA LA IMAGEN DENTRO DEL CONTENEOR AUNQUE AVECES NO ABARCA TODO ESTE
                                //CenterCrop POR SU PARTE ABARCA TODO EL CONTENEDOR PERO PUEDE QUE NO SE MUESTRE TODA LA IMAGEN
                                .centerInside()
                                .into(imageView);
                        //REPARAR EL DETALLE DE FOTOS CHUECAS CUANDO SON TOMADAS POR LA CAMARA, SOLO NOS BASAMOS EN LA NOMENCLATURA QUE
                        // NOSOTROS LE DAMOS
                        String nameFoto = rutaImagen[0].substring(55, 60);
                        if (nameFoto.equalsIgnoreCase("UPayP"))
                            imageView.setRotation(270.0f);

                    }else{

                        loading.dismiss();

                    }

                } catch (JSONException e) {
                    loading.dismiss();
                    e.printStackTrace();
                }
            }
        };

        //enviamos los datos a la clase LoginRequest en la cuál se ejecutará del lado del servidor la solicitud
        getImageRequest imageRequest = new getImageRequest(usuario.getUsername(), responseListener);
        //RequestQueue queue = Volley.newRequestQueue(getContext());
        VolleySingleton.getInstance(getContext()).addToRequestQueue(imageRequest);
        //Se envía el objeto LoginRequest cargado con los datos a la cola de peticiones del servidor
        //queue.add(ImageRequest);
    }


    //FUNCION PARA LOS PERMISOS CON BASE A LAS VERSIONES POSTERIORES A LA 6 DE ANDROID
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_PERMISSIONS){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                //Toast.makeText(getActivity(), "Permisos aceptados", Toast.LENGTH_SHORT).show();
                showOptions();
            }
        }else{
            //SI RECHAZO LOS PERMISOS, LA PRÓXIMA VEZ QUE INTENTE USAR LA APP SE MOSTRARÁ UNA EXPLICACIÓN DEL POR QUE SE PIDEN ESOS PERMISOS
            showExplanation();
        }
    }


    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Permisos denegados");
        builder.setMessage("Para usar las funciones de la app necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface loading, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface loading, int which) {
                loading.dismiss();
                getActivity().finish();
            }
        });

        builder.show();
    }

    //SOLICITAMOS LOS PERMISOS PARA TOMAR FOTOS Y USAR MULTIMEDIA DEL DISPOSITIVO
    private boolean mayRequestStoragePermission() {

        //VERIFICAMOS LA VERSIÓN DEL SDK DE ANDROID SI ES MAYOR O NO A LA VERSIÓN 6
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        /*if((checkSelfPermission(getActivity(),WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(getActivity(),CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;*/
        //EVALUAR SI ES NECESARIO DAR UNA EXPLICACIÓN DE LA SOLICITUD DE PERMISOS, ESTO SE DA CUANDO ANTES SE RECHAZARÓN
        if((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))){
            Snackbar.make(mVista, "Los permisos son necesarios para poder usar la aplicación",
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
                }
            }).show();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        }

        return false;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
