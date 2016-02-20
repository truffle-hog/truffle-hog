package edu.kit.trufflehog;

import edu.kit.trufflehog.model.FileSystem;
import edu.kit.trufflehog.model.configdata.ConfigDataModel;

import java.io.FileNotFoundException;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * <p>
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class Debug {
    public static void main(String[] args) {
        ConfigDataModel configDataModel = null;
        try {
            configDataModel = new ConfigDataModel(new FileSystem(), new ScheduledThreadPoolExecutor(1));
            configDataModel.load();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
