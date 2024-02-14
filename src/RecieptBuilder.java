import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import javax.swing.*;

public class RecieptBuilder {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private final List<String> monthsList = Arrays.asList("январь", "февраль", "март", "апрель", "май", "июнь", "июль", "август", "сентябрь", "октябрь", "ноябрь", "декабрь");
    private File output;
    private File checksFile;

    RecieptBuilder(File input, File output) {
        this.output = output;
        checksFile = input;
    }

    public void writeOutputFile() {
        String cur;
        String curt;
        List<String> buf;
        List<String> checksList = getChecks(checksFile);
        List<String> serviceList = getServices(checksFile);
        boolean em = true;
        try (FileWriter writer = new FileWriter(output, false)) {
            for (String month : monthsList) {
                for (String service : checksList) {
                    cur = service.substring(service.indexOf("_") + 1);
                    if (month.equals(cur.substring(0, cur.indexOf(".")))) {
                        writer.write("/" + month + "/" + service + "\n");
                        em = false;
                    }
                }
                if (!em) {
                    writer.write("\n");
                }
                em = true;
            }
            writer.write("******************************************\n\nне оплачены:\n");
            for (String month : monthsList) {
                buf = new ArrayList<>(serviceList);
                for (String service : checksList) {
                    cur = service.substring(0, service.indexOf("_"));
                    curt = service.substring(service.indexOf("_") + 1);
                    if (month.equals(curt.substring(0, curt.indexOf(".")))) {
                        buf.remove(cur);
                    }

                }
                if (buf.size() > 0) {
                    writer.write(month + ":\n");
                    for (String item : buf) {
                        writer.write(item + "\n");
                    }
                    writer.write("\n");
                }
            }
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void writeInputChecksFile(List<String> data) {
        try (FileWriter writer = new FileWriter(checksFile, false)) {
            for (String item : data) {
                writer.write(item + "\n");
            }
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeInputChecksFileByFolderScan(File folder) {
        List<String> data = processFilesFromFolder(folder);
        try (FileWriter writer = new FileWriter(checksFile, false)) {
            for (String item : data) {
                writer.write(item + "\n");
            }
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> processFilesFromFolder(File folder) {
        File[] folderEntries = folder.listFiles();
        List<String> buf = new ArrayList<>();
        for (File entry : folderEntries) {
            if (entry.isDirectory()) {
                processFilesFromFolder(entry);
                continue;
            } else {
                buf.add(entry.getName());
            }
        }
        return buf;
    }

    public List<String> getMonthsList() {
        return monthsList;
    }

    public File getOutput() {
        return output;
    }

    public File getChecksFile() {
        return checksFile;
    }

    public void setChecksFile(File checksFile) {
        this.checksFile = checksFile;
    }

    public void setOutput(File output) {
        this.output = output;
    }

    private List<String> getServices(File checks) {
        List<String> serviceList = new ArrayList<String>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(checks));
            String line = reader.readLine();
            while (line != null) {
                serviceList.add(line.substring(0, line.indexOf("_")));
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        serviceList = serviceList.stream().distinct().collect(Collectors.toList());
        try {
            if (serviceList.get(0) == serviceList.get(1)) {
                serviceList.remove(0);
            }
        } catch (Exception e) {
        }
        return serviceList;
    }

    private List<String> getChecks(File checks) {
        List<String> checksList = new ArrayList<String>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(checks));
            String line = reader.readLine();
            while (line != null) {
                checksList.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return checksList;
    }

    public void openFile(File file) {
        try {
            if (!Desktop.isDesktopSupported()) {
                System.out.println("not supported");
                return;
            }
            Desktop desktop = Desktop.getDesktop();
            if (file.exists())
                desktop.open(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private File changeFilePath(String file_name,File checks,Scanner input){
        System.out.printf("Поменять путь нахождения файла \"%s%s%s\"? \n\rТекущий путь: %s%s%s  \n\r(%sда - 1%s, %sнет - остальные символы%s)\n",
                ANSI_CYAN,file_name, ANSI_RESET, ANSI_CYAN, checks.getPath(),ANSI_RESET,ANSI_GREEN,ANSI_RESET,ANSI_RED,ANSI_RESET);
        String answer = input.nextLine();
        if(answer.equals("1")){
            System.out.printf("Введите новый путь до папки где лежит файл \"%s\": \n",file_name);
            String dir = input.nextLine();
            checks = new File(dir,file_name);
            System.out.printf("Новый путь: %s%s%s\n",ANSI_CYAN,checks.getPath() ,ANSI_RESET);
        }
        return checks;
    }*/
}
