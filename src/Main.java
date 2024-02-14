
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        JFrame f=new JFrame();
        JButton b=new JButton("Напечатать чеки и состоваить по ним файл");
        JButton p=new JButton("Составить файл по псевдо чекам");
        JButton m=new JButton("Нарисовать чеки по месяцам");
        JTextArea year = new JTextArea("год");
        JTextArea room = new JTextArea("номер комнаты");
        f.setSize(500,400);

        b.addActionListener(e -> {
            try {
                Integer an = JOptionPane.showConfirmDialog(f, "Очистить папку от старых чеков?", "Вопрос",JOptionPane.YES_NO_CANCEL_OPTION);
                if(an==JOptionPane.OK_OPTION){
                    clearChecksFolder(new File("D:/mi/java/storage/output/checks/"));
                }else{
                    if(an==JOptionPane.CANCEL_OPTION){
                        return;
                    }
                }
                writeOutputByYearAndRoom(Integer.parseInt(year.getText()), Integer.parseInt(room.getText()));
            }catch (Exception exception){
                JOptionPane.showMessageDialog(f, "Год и номер комнаты должны быть указаны в целочисленнои формате!");
            }
        });
        m.addActionListener(e -> {
            try {
                Integer an = JOptionPane.showConfirmDialog(f, "Очистить папку от старых чеков?", "Вопрос",JOptionPane.YES_NO_CANCEL_OPTION);
                if(an==JOptionPane.OK_OPTION){
                    clearChecksFolder(new File("D:/mi/java/storage/output/checks/"));
                }else{
                    if(an==JOptionPane.CANCEL_OPTION){
                        return;
                    }
                }
                writeChekcsByYearAndRoom(Integer.parseInt(year.getText()), Integer.parseInt(room.getText()));
            }catch (Exception exception){
                JOptionPane.showMessageDialog(f, "Год и номер комнаты должны быть указаны в целочисленнои формате!");
            }
        });
        p.addActionListener(e -> writeOutputPsevdoTxt());
        f.add(b);
        f.add(year);
        f.add(room);
        f.add(p);
        f.add(m);
        f.setLayout(null);
        f.setVisible(true);
        f.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                if(f.getWidth()<500){f.setSize(500,f.getHeight());}
                if(f.getHeight()<400){f.setSize(f.getWidth(),400);}
                b.setBounds(f.getWidth()/2-(f.getWidth()/3)/2,50,f.getWidth()/3, 40);
                year.setBounds(f.getWidth()/2-(f.getWidth()/3)/2,100,f.getWidth()/3, 40);
                room.setBounds(f.getWidth()/2-(f.getWidth()/3)/2,150,f.getWidth()/3, 40);
                p.setBounds(f.getWidth()/2-(f.getWidth()/3)/2,200,f.getWidth()/3, 40);
                m.setBounds(f.getWidth()/2-(f.getWidth()/3)/2,250,f.getWidth()/3, 40);
            }
        });
    }


    public static void clearChecksFolder(File folder) {
        File[] folderEntries = folder.listFiles();
        List<String> buf = new ArrayList<>();
        for (File entry : folderEntries) {
            if (entry.isDirectory()) {
                clearChecksFolder(entry);
                continue;
            } else {
                entry.delete();
            }
        }
    }

    private static void writeChekcsByYearAndRoom(int year,int room_num) {
        RecieptBuilder recieptBuilder = new RecieptBuilder(new File("F://mi/java/storage", "чеки_database.txt"), new File("F://mi/java/storage/output", "чеки_по_папкам.txt"));
        PostgreSqlDataBase dataBase = new PostgreSqlDataBase("localhost", "5433", "services", "postgres", "masterkey");
        try {
            ResultSet table = dataBase.execQuery("SELECT services.service, reciepts.date, services.price, reciepts.quantity, units.unit_name, houses.address, rooms.number, rooms.owner FROM reciepts INNER JOIN services ON(services.id=reciepts.service_id) INNER JOIN rooms ON(rooms.id=reciepts.room_id) INNER JOIN units ON(units.id=reciepts.unit_id) INNER JOIN houses ON(houses.id=rooms.house_id) WHERE year="+year+" and rooms.number="+room_num);
            String checkBuf;
            table.beforeFirst();
            List<String> l = new ArrayList<>();
            while (table.next()) {
                checkBuf = "";
                for (int j = 1; j <= table.getMetaData().getColumnCount(); j++) {
                    checkBuf += table.getString(j) + "/";
                }
                l.add(checkBuf);
            }
            DrawCheck d = new DrawCheck("", "F:/mi/java/storage/output/checks/");
            d.drawCheckByMonth(new BufferedImage(1920,1080,BufferedImage.TYPE_INT_RGB),l);
            table.getStatement().close();
            table.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //recieptBuilder.writeInputChecksFileByFolderScan(new File("D:/mi/java/storage/output/checks/"));
        //recieptBuilder.writeOutputFile();
        //recieptBuilder.openFile(recieptBuilder.getOutput());
        recieptBuilder.openFile(new File("D:/mi/java/storage/output/checks/check.png"));
    }

    private static void writeOutputByYearAndRoom(int year,int room_num) {
        RecieptBuilder recieptBuilder = new RecieptBuilder(new File("D://mi/java/storage", "чеки_database.txt"), new File("D://mi/java/storage/output", "чеки_по_папкам.txt"));
        PostgreSqlDataBase dataBase = new PostgreSqlDataBase("localhost", "5433", "services", "postgres", "masterkey");
        try {
            ResultSet table = dataBase.execQuery("SELECT services.service, reciepts.date, services.price, reciepts.quantity, units.unit_name, houses.address, rooms.number, rooms.owner FROM reciepts INNER JOIN services ON(services.id=reciepts.service_id) INNER JOIN rooms ON(rooms.id=reciepts.room_id) INNER JOIN units ON(units.id=reciepts.unit_id) INNER JOIN houses ON(houses.id=rooms.house_id) WHERE year="+year+" and rooms.number="+room_num);
            String checkBuf;
            table.beforeFirst();
            while (table.next()) {
                checkBuf = "";
                for (int j = 1; j <= table.getMetaData().getColumnCount(); j++) {
                    checkBuf += table.getString(j) + "/";
                }
                DrawCheck d = new DrawCheck(checkBuf, "D:/mi/java/storage/output/checks/");
                d.DrawMonthServiceCheck();
                System.out.println();
            }
            table.getStatement().close();
            table.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        recieptBuilder.writeInputChecksFileByFolderScan(new File("F:/mi/java/storage/output/checks/"));
        recieptBuilder.writeOutputFile();
        recieptBuilder.openFile(recieptBuilder.getOutput());
        recieptBuilder.openFile(new File("F:/mi/java/storage/output/checks/"));
    }

    private static void writeOutputPsevdoTxt() {
        RecieptBuilder recieptBuilder = new RecieptBuilder(new File("F://mi/java/storage", "чеки.txt"), new File("F://mi/java/storage/output", "чеки_по_папкам.txt"));
        recieptBuilder.writeOutputFile();
        recieptBuilder.openFile(recieptBuilder.getOutput());
    }

}