package com.rnandroid;

import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;
import android.util.Base64;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;


public class FileSystem extends ReactContextBaseJavaModule {

  ReactApplicationContext context;

  public FileSystem(ReactApplicationContext reactContext) {
    super(reactContext);
    this.context = reactContext;
  }

  @Override
  public String getName() {
    return "FileSystem";
  }

  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();

    try { // Supported only after API level 19
      constants.put("DocumentsDir", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath());
    } catch (NoSuchFieldError ex) {
      constants.put("DocumentsDir", null);
    }
    constants.put("DownloadDir", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
    constants.put("DCIMDir", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath());
    constants.put("PictureDir", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
    constants.put("MusicDir", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath());
    constants.put("MovieDir", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath());
    constants.put("RingtoneDir", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES).getAbsolutePath());
    constants.put("DataAppDir", this.context.getApplicationInfo().dataDir);
    constants.put("RootDir", Environment.getRootDirectory().getAbsolutePath());
    constants.put("ExternalFilesDir", this.getExternalFilesDir());
    constants.put("separator", File.separator);

    return constants;
  }

  private String getExternalFilesDir() {
    File externalDirectory = this.context.getExternalFilesDir(null);
    if(externalDirectory != null && externalDirectory.exists()) {
      return externalDirectory.getAbsolutePath();
    }
    return null;
  }

  @ReactMethod
  public void ls(String path, Promise p) {
    try {
      File src = new File(path);
      if (!src.exists()) {
          p.reject("ERROR", "Path not found '" + path + "'");
          return;
      }
      if (!src.isDirectory()) {
          p.reject("ERROR", "'" + path + "'" + " is not a directory");
          return;
      }

      String[] files = new File(path).list();
      if(files == null) {
        p.reject("ERROR", "Can not read FileSystem");
        return;
      }

      WritableArray response = new WritableNativeArray();
      for(String i : files) {
          WritableMap wm = new WritableNativeMap();
          File tmpFile = new File(path+File.separator+i);
          wm.putString("name", i);
          if(tmpFile.isDirectory()) {
            wm.putBoolean("isDir", true);
            wm.putInt("size", 0);
          } else {
            wm.putBoolean("isDir", false);
            long length = tmpFile.length();
            if(length > Integer.MAX_VALUE) {// Overflow
              length = -1;
            }
            wm.putInt("size", (int)length);
          }
          response.pushMap(wm);
      }

      p.resolve(response);
    } catch (Exception e) {
        p.reject(e);
    }
  }

  @ReactMethod
  public void read(String path, Promise p) {
     try {
        File f = new File(path);
        if(!f.exists()) {
          p.reject("ERROR", "File not Found.");
          return;
        }

        long length = f.length();
        if(length > Integer.MAX_VALUE) { // Overflow
           p.reject("ERROR",
                    "A Overflow occurred, file size is so much big. "+
                    "File Size: " + String.valueOf(length) + " Bytes. "+
                    "Limit Size: " + String.valueOf(Integer.MAX_VALUE) + " Bytes. "
                    );
           return;
        }
        if(f.isDirectory()) {
           p.reject("ERROR",
                    "Expecting a file but '" + path + "' is a directory."
                    );
           return;
        }

        byte[] bytes = new byte[(int)length];
        FileInputStream in = new FileInputStream(f);
        in.read(bytes);
        in.close();

        p.resolve(Base64.encodeToString(bytes, Base64.NO_WRAP));
     } catch (Exception e) {
         p.reject(e);
     }
  }

  @ReactMethod
  public void write(String path, String data, Boolean append, Promise p) {
    try {
      File f = new File(path);
      if(!f.exists()) {
        p.reject("ERROR", "File Not Found");
        return;
      }

      FileOutputStream out = new FileOutputStream(f, append);
      out.write(Base64.decode(data, Base64.DEFAULT));
      out.close();

      p.resolve(true);
    } catch(Exception e) {
      p.reject(e);
    }
  }

  @ReactMethod
  public void rename(String path, String newName, Promise p) {
    File originalFile = new File(path);
    if(!originalFile.exists()) {
       p.reject("ERROR", "The file does not exists");
       return;
    }

    String parentPath = originalFile.getAbsolutePath();
    String[] partsOfParentPath = parentPath.split(File.separator);
    String fullPathName = "";
    for(int i = 0; i < partsOfParentPath.length-1; i++)
      fullPathName += File.separator+partsOfParentPath[i];
    File renamedFile = new File( fullPathName+File.separator+newName );

    if(renamedFile.exists()) {
       p.reject("ERROR", "The file "+newName+" already exists");
       return;
    }

    boolean success = originalFile.renameTo(renamedFile);
    if (!success) {
      p.reject("ERROR", "Unknown error when renaming the file");
      return;
    }

    p.resolve(renamedFile.getAbsolutePath());
  }

  @ReactMethod
  public void base64Decode(String data, Promise p) {
    try {
      p.resolve( new String(Base64.decode(data, Base64.DEFAULT)) );
    } catch(IllegalArgumentException e) {
      p.reject(e);
    }
  }

  @ReactMethod
  public void base64Encode(String data, Promise p) {
    try {
      p.resolve( Base64.encodeToString(data.getBytes(), Base64.NO_WRAP) );
    } catch(Exception e) {
      p.reject(e);
    }
  }

  @ReactMethod
  public void mkdirs(String path, Promise p) {
      File dest = new File(path);
      if(dest.exists()) {
          p.resolve(true);
          return;
      }
      try {
          if(!dest.mkdirs()) {
              p.reject("ERROR", "Unable to create: '" + path + "'");
              return;
          }
          p.resolve(true);
      } catch (Exception e) {
          p.reject(e);
      }
  }

  @ReactMethod
  public void mkfile(String path, Promise p) {
    try {
        if(!(new File(path)).createNewFile()) {
            p.reject("ERROR", "Can not create: '" + path + "'.");
        }
        p.resolve(true);
    } catch(Exception e) {
        p.reject(e);
    }
  }

  @ReactMethod
  public void exists(String path, Promise p) {
    try {
      p.resolve( (new File(path)).exists() );
    } catch(Exception e) {
      p.reject(e);
    }
  }

  @ReactMethod
  public void rm(String path, Promise p) {
    try {
      Boolean res = this.removeRecursively(path);
      if(res) {
        p.resolve(true);
      } else {
        p.reject("ERROR", "Unknown error when deleting "+path);
      }

    } catch(Exception e) {
      p.reject(e);
    }
  }

  private Boolean removeRecursively(String path) throws IOException  {
    File f = new File(path);
    if(!f.exists()) {
      return true;
    }

    if(f.isDirectory()) {
      File[] files = f.listFiles();
      if(files == null) {
        throw new IOException("Can not list files inside "+path);
      }

      for(File insideFile : files) {
        this.removeRecursively(insideFile.getAbsolutePath());
      }
    }

    return f.delete();
  }

}
