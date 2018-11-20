package littiere.jeremie.tp_runtimepermission;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_RUNTIME_PERMISSION = "TAG_RUNTIME_PERMISSION";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int MULTIPLE_PERMISSION_REQUEST_CODE = 5;
    static final int PICK_CONTACT_REQUEST = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("TP Runtime Permission");

        // Ouverture d'une pop up afin d'accèder aux paramètres de l'application depuis le package manager.
        Button settingButton = (Button) findViewById(R.id.btn_settings);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettingsDialog();
            }
        });

        //Liste les permissions autorisées sur l'application
        Button listGrantedPermissionButton = (Button) findViewById(R.id.bt_perm_granted);
        listGrantedPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Récupération du nom du Package
                String packageName = getPackageName();
                // récupération de l'ensemble des permissions sur le package
                List<String> permissionList = getAllPermissions(packageName);
                StringBuffer grantedPermissionBuf = new StringBuffer();
                grantedPermissionBuf.append("Permissions accordées pour notre application: \r\n\n");

                // Pour chaque permission, vérification si accordée au Runtime
                int size = permissionList.size();
                for (int i = 0; i < size; i++) {
                    String permission = permissionList.get(i);
                    // Affichage de la permission si accordée
                    if (ContextCompat.checkSelfPermission(MainActivity.this, permission)
                            == PackageManager.PERMISSION_GRANTED) {
                        grantedPermissionBuf.append(permissionList.get(i));
                        if (i < size - 1) {
                            grantedPermissionBuf.append(",\r\n");
                        }
                    }
                }
                // Affichage de l'ensemble des permissions autorisés dans une alertDialog.
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setMessage(grantedPermissionBuf.toString());
                alertDialog.show();
            }
        });

        // **** STEP 1 - BROWSER WEB *****************************

        // **** STEP 2 – CAMERA **********************************

        // **** STEP 3 – CONTACTS - Permission Group *************

        // **** STEP 4 – MULTIPLE PERMISSIONS*********************


    }

    // Affichage de l'ensemble des permissions accordées pour cette application TP_runtimepermission
    // affichage des permissions "normales" (manifest) & "dangereuses" qui sont autorisées
    private List<String> getAllPermissions(String packageName) {
        List<String> permissionsList = new ArrayList<String>();
        try {

            // Récupération du nom du Package.
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);

            // Ajout de chaque permissions
            int length = packageInfo.requestedPermissions.length;
            for (int i = 0; i < length; i++) {
                String requestPermission = packageInfo.requestedPermissions[i];
                permissionsList.add(requestPermission);
            }
        } catch (PackageManager.NameNotFoundException ex) {
            Log.e(TAG_RUNTIME_PERMISSION, "getAllGrantedPermissions: " + ex.getMessage(), ex);
        } finally {
            return permissionsList;
        }
    }

    private void openSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Permissions requises");
        builder.setMessage("Pour pouvoir bénéficier de toutes les fonctionnalités de cette application, des permissions sont requises");
        builder.setPositiveButton("Paramètres de l'App", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // **** STEP 2 – AJOUT METHODE PERMISSION CALLBACK *******************


}