package dev.faceless.pack.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import java.nio.file.*;

/**
 * Utility class for compressing directories into ZIP archives.
 *
 * <p>How it works:
 * <ol>
 *   <li>Opens a {@link ZipOutputStream} writing to the target file.</li>
 *   <li>Walks the source directory tree with {@link Files#walk}, filtering to
 *       regular files only (symlinks and directories are skipped).</li>
 *   <li>Each file is added as a {@link ZipEntry} whose name is the path
 *       <em>relative</em> to the source directory, preserving folder structure
 *       inside the archive.</li>
 *   <li>If {@code deleteAfterZip} is {@code true}, the entire source directory
 *       is removed after a successful zip — but only if no errors occurred
 *       during compression.</li>
 * </ol>
 */
public class ZipUtil {

    /**
     * Compresses an entire directory into a ZIP file.
     *
     * @param dirPath       path to the directory to compress
     * @param zipFilePath   path (including filename) for the output ZIP file
     * @param deleteAfterZip if {@code true}, deletes the source directory after
     *                       a successful zip operation
     * @throws IOException if the source directory does not exist, the ZIP file
     *                     cannot be created, or any file cannot be read
     */
    public static void zipDirectory(String dirPath, String zipFilePath, boolean deleteAfterZip) throws IOException {
        Path sourceDir = Paths.get(dirPath);

        if (!Files.isDirectory(sourceDir)) {
            throw new IOException("Source path is not a directory: " + dirPath);
        }

        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            try (var stream = Files.walk(sourceDir)) {
                for (Path path : (Iterable<Path>) stream.filter(Files::isRegularFile)::iterator) {
                    String entryName = sourceDir.relativize(path).toString();
                    zos.putNextEntry(new ZipEntry(entryName));
                    Files.copy(path, zos);
                    zos.closeEntry();
                }
            }
        }

        if (deleteAfterZip) {
            deleteDirectory(sourceDir);
        }

        System.out.printf("Zipped '%s' → '%s'%s%n", dirPath, zipFilePath, deleteAfterZip ? " (source deleted)" : "");
    }

    /**
     * Convenience overload that never deletes the source directory.
     *
     * @param dirPath     path to the directory to compress
     * @param zipFilePath path (including filename) for the output ZIP file
     * @throws IOException see {@link #zipDirectory(String, String, boolean)}
     */
    public static void zipDirectory(String dirPath, String zipFilePath) throws IOException {
        zipDirectory(dirPath, zipFilePath, false);
    }

    /**
     * Recursively deletes a directory and all of its contents.
     *
     * @param dir the directory to delete
     * @throws IOException if any file or directory cannot be deleted
     */
    private static void deleteDirectory(Path dir) throws IOException {
        try (var stream = Files.walk(dir)) {
            // Delete deepest files first, then their parent directories
            for (Path path : (Iterable<Path>) stream.sorted(Comparator.reverseOrder())::iterator) {
                Files.delete(path);
            }
        }
    }
}