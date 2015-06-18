package utilities;

import com.google.gson.Gson;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@EBean
public class Persistence {
    @App PomodoroApp app;

    public boolean JSONToDisk(Object object, String fileName) {
        String wrapperJSONSerialized = new Gson().toJson(object);
        try {
            File file = new File(app.getFilesDir(), fileName);

            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.write(wrapperJSONSerialized);
            fileWriter.flush();
            fileWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Object JSONFromDisk(Class aClass, String fileName) {
        try {
            File file = new File(app.getFilesDir(), fileName);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            return new Gson().fromJson(bufferedReader, aClass);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}


