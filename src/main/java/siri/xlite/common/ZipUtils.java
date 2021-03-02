package siri.xlite.common;

import lombok.extern.log4j.Log4j;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Log4j
public abstract class ZipUtils {

    public static void unzipArchive(File archive, File outputDir) throws IOException {
        log.info("[DSU] unzip archive : " + archive + " to " + outputDir);

        ZipFile zipfile = new ZipFile(archive);
        for (Enumeration<? extends ZipEntry> e = zipfile.entries(); e.hasMoreElements(); ) {
            ZipEntry entry = e.nextElement();
            unzipEntry(zipfile, entry, outputDir);
        }
    }

    private static void unzipEntry(ZipFile zipfile, ZipEntry entry, File outputDir) throws IOException {

        log.info("[DSU] unzip entry : " + entry);

        if (entry.isDirectory()) {
            createDir(new File(outputDir, entry.getName()));
            return;
        }

        File outputFile = new File(outputDir, entry.getName());
        if (!outputFile.getParentFile().exists()) {
            createDir(outputFile.getParentFile());
        }

        try (BufferedInputStream in = new BufferedInputStream(zipfile.getInputStream(entry));
             BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile))) {
            IOUtils.copy(in, out);
        }
    }

    private static void createDir(File dir) throws IOException {
        if (!dir.mkdirs())
            throw new IOException("Can not create dir " + dir);
    }

}
