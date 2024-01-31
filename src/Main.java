import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
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

        openZip(pathZip, "D:\\Games\\savegames\\");

        System.out.println(openProgress(pathFile2));

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

    public static void openZip(String pathZip, String pathFolder) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(pathZip))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = pathFolder + entry.getName();
                FileOutputStream fout = new FileOutputStream(name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (IOException e) {
            System.out.println("Что-то пошло не так.");
        }
    }

    public static String openProgress(String pathFile) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(pathFile);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return gameProgress.toString();
    }
}
