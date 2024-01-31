import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        GameProgress gameProgress1 = new GameProgress(100, 5, 2, 11);
        GameProgress gameProgress2 = new GameProgress(75, 15, 5, 23);
        GameProgress gameProgress3 = new GameProgress(77, 25, 10, 56);

        String pathFile1 = "D:\\Games\\savegames\\save1.dat";
        String pathFile2 = "D:\\Games\\savegames\\save2.dat";
        String pathFile3 = "D:\\Games\\savegames\\save3.dat";

        saveGame(pathFile1, gameProgress1);
        saveGame(pathFile2, gameProgress2);
        saveGame(pathFile3, gameProgress3);

        String pathZip = "D:\\Games\\savegames\\zip1.zip";
        List<String> listFiles = new ArrayList<>();
        listFiles.add(pathFile1);
        listFiles.add(pathFile2);
        listFiles.add(pathFile3);

        zipFiles(pathZip, listFiles);

        deleteFiles(pathFile1);
        deleteFiles(pathFile2);
        deleteFiles(pathFile3);

    }

    public static void saveGame(String pathFile, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(pathFile);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(gameProgress);

        } catch (IOException e) {
            System.out.println("Что-то пошло не так, сохранение прогресса не удалось.");
        }
    }

    public static void zipFiles(String pathZip, List<String> listFiles) {
        try (ZipOutputStream zout = new ZipOutputStream(
                new FileOutputStream(pathZip))) {
            int count = 1;
            for (String pathFile : listFiles) {
                try (FileInputStream fis = new FileInputStream(pathFile)) {

                    ZipEntry entry = new ZipEntry("save" + count + ".dat");
                    count++;
                    zout.putNextEntry(entry);

                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);

                    zout.write(buffer);

                    zout.closeEntry();

                } catch (IOException e) {
                    System.out.println("Что-то пошло не так.");
                }

            }

        } catch (IOException e) {
            System.out.println("Что-то пошло не так.");
        }
    }

    public static void deleteFiles(String pathFile) {
        File myFile = new File(pathFile);
        if (myFile.exists()) {
            myFile.delete();
        }
    }
}
