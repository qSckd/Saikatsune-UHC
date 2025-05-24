package net.saikatsune.uhc.util;

import net.minecraft.util.org.apache.commons.io.IOUtils;
import net.saikatsune.uhc.Game;

import java.io.*;
import java.util.List;
import java.util.Objects;

public class FileUtil {

    public static void copyFolder(File src, File dest) throws IOException {
        if(src.isDirectory()){
            if(!dest.exists()){
                dest.mkdir();
                //.logConsole(CC.SECONDARY + "Directory copied from " + CC.PRIMARY + src + CC.SECONDARY + "  to " + CC.PRIMARY + dest);
            }

            for(String file : Objects.requireNonNull(src.list())) {
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);

                copyFolder(srcFile, destFile);
            }
        } else {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];

            int length;
            while((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            in.close();
            out.close();

            //Msg.logConsole(CC.SECONDARY + "File copied from " + CC.PRIMARY + src + CC.SECONDARY + " to " + CC.PRIMARY + dest);
        }
    }

    public static List<String> readLines(String file) {
        try {
            return IOUtils.readLines(Objects.requireNonNull(Game.class.getClassLoader().getResourceAsStream(file)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean deleteDirectory(File path) {
        if(path.exists()) {
            File[] files = path.listFiles();

            if(files != null) {
                for(File file : files) {
                    if(file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }

        return (path.delete());
    }
}
