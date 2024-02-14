import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.graphics.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DrawCheck {

    private String data;
    private String outputDir;

    /**
     * Constructor
     *
     * @param checkData checkData example: "SERVICE/DATE/PRICE/QUANTITY/UNIT/ADDRESS/ROOM_NUMBER/CLIENT/"
     */
    DrawCheck(String checkData, String outputDir) {
        data = checkData;
        this.outputDir = outputDir;
    }

    public String getData() {
        return data;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String dir) {
        outputDir = dir;
    }

    /**
     * change Data param
     *
     * @param data data example: "SERVICE/DATE/PRICE/QUANTITY/UNIT/ADDRESS/ROOM_NUMBER/CLIENT/"
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Draw check by Data
     * example: "SERVICE/DATE/PRICE/QUANTITY/UNIT/ADDRESS/ROOM_NUMBER/CLIENT/"
     */
    public void DrawMonthServiceCheck() {
        PdfDocument pdf = new PdfDocument();
        PdfPageBase page = pdf.appendPage();
        int y = 50;
        String month = "";
        String service = getItemFromDataByNum(data,0);
        String date = getItemFromDataByNum(data,1);
        try {
            String datebuf;
            datebuf = date.substring(date.indexOf("-"));
            datebuf = datebuf.substring(1);
            datebuf = datebuf.substring(0, datebuf.indexOf("-"));
            month = getMonthNameByNum(Integer.parseInt(datebuf));
        } catch (Exception e) {
            month = "parseError";
        }
        String total;
        String buf = getItemFromDataByNum(data,2).replace("?", "").replace(',', '.').replace(" ", "").replace(" ", "");
        try {
            double tot = (Double.parseDouble(getItemFromDataByNum(data,3)) * Double.parseDouble(buf));
            total = Double.toString(tot);
        } catch (Exception ex) {
            total = ex.getMessage();
        }
        String price = getItemFromDataByNum(data,2);
        String secstr = "Service: " + service + "";
        DrawText(page, "Check for payment", 0, 50, new PdfRGBColor(Color.red), 18f);
        DrawText(page, secstr, 0, 100, new PdfRGBColor(Color.black), 12f);
        DrawText(page, "Price per " + getItemFromDataByNum(data,4) + ": " + getItemFromDataByNum(data,2), 0, 150, new PdfRGBColor(Color.black), 12f);
        DrawText(page, "Quantity: " + getItemFromDataByNum(data,3), 0, 200, new PdfRGBColor(Color.black), 12f);
        DrawText(page, "Total payable: " + total, 0, 250, new PdfRGBColor(Color.black), 12f);
        DrawText(page, "Address: " + getItemFromDataByNum(data,5) + " Room: " + getItemFromDataByNum(data,6), 0, 300, new PdfRGBColor(Color.black), 12f);
        DrawText(page, "Client: " + getItemFromDataByNum(data,7), 0, 350, new PdfRGBColor(Color.black), 12f);
        DrawText(page, "Date: " + date, 0, 400, new PdfRGBColor(Color.black), 12f);
        pdf.saveToFile(outputDir + service + "_" + month + ".pdf", FileFormat.PDF);
    }

    public void drawCheckByMonth(BufferedImage baseImage, List<String> checks) {
        String ex = "SERVICE/DATE/PRICE/QUANTITY/UNIT/ADDRESS/ROOM_NUMBER/CLIENT/";
        Graphics2D g2D = (Graphics2D) baseImage.getGraphics();
        g2D.setColor(Color.white);
        g2D.fillRect(0,0,baseImage.getWidth(),baseImage.getHeight());
        g2D.setColor(Color.black);
        String fontName = "Microsoft YaHei";
        Font f = new Font(fontName, Font.PLAIN, baseImage.getWidth()/72);
        g2D.setFont(f);
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontMetrics fm = g2D.getFontMetrics(f);
        //int textWidth = fm.stringWidth(textToWrite);
        //int widthX = (baseImage.getWidth() - textWidth) / 2;
        //g2D.drawString(textToWrite, widthX, y);
        int x1=0,y1=0,x2=0,y2=baseImage.getHeight();
        int textWidth;
        int widthX;
        String textToWrite;
        for(int i =0;i<8;i++) {
            textToWrite=getItemFromDataByNum(ex,i);
            g2D.drawLine(x1,y1,x2,y2);
            textWidth = fm.stringWidth(textToWrite);
            widthX = baseImage.getWidth()/10;
            g2D.drawString(textToWrite, x1+12,y1+baseImage.getHeight()/16);
            x1+=baseImage.getWidth()/8;
            x2+=baseImage.getWidth()/8;
        }
        x1=0; x2=baseImage.getWidth();
        y1=0; y2=0;
        for(int i =0;i<checks.size();i++){
            ex=checks.get(i);
            y1+=baseImage.getHeight()/8;
            y2+=baseImage.getHeight()/8;
            g2D.drawLine(0,y1,x2,y2);
            x1=0; x2=baseImage.getWidth();
            for (int j=0;j<8;j++){
                textToWrite=getItemFromDataByNum(ex,j);
                g2D.drawString(textToWrite,x1+12,y1+baseImage.getHeight()/16);
                x1+=baseImage.getWidth()/8;
                x2+=baseImage.getWidth()/8;
            }
        }

        try {
            if (ImageIO.write(baseImage, "png", new File(outputDir,"check.png")))
            {
                System.out.println("-- saved");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        g2D.dispose();
        String month="123";
        /*String date = getItemFromDataByNum(1);
        try {
            String datebuf;
            datebuf = date.substring(date.indexOf("-"));
            datebuf = datebuf.substring(1);
            datebuf = datebuf.substring(0, datebuf.indexOf("-"));
            month = getMonthNameByNum(Integer.parseInt(datebuf));
        } catch (Exception e) {
            month = "parseError";
        }*/

        //PDDocument doc = null;
        try {
            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();
            doc.addPage(page);
            PDImageXObject pdImage = LosslessFactory.createFromImage(doc, baseImage);

            //creating the PDPageContentStream object
            PDPageContentStream contents = new PDPageContentStream(doc, page);
            contents.drawImage(pdImage, 0, 0);
            contents.close();
            //PDPageContentStream contentStream = new PDPageContentStream(doc, page);
            doc.save(new File(outputDir,month + ".pdf"));
            doc.close();
        }catch (IOException exception){
            System.out.println(exception.getMessage());
        }
    }

    private String getItemFromDataByNum(String data,int n) {
        String b = data;
        for (int i = 0; i < n; i++) {
            b = b.substring(b.indexOf("/"));
            b = b.substring(1);
        }
        return b.substring(0, b.indexOf("/"));
    }

    private static void DrawText(PdfPageBase page, String text, int x, int y, PdfRGBColor Color, float fontsize) {
        //Save graphics state
        PdfGraphicsState state = page.getCanvas().save();

        //Draw text - brush
        //PdfPen pen = PdfPens.getDeepSkyBlue();
        PdfSolidBrush brush = new PdfSolidBrush(Color);
        PdfStringFormat format = new PdfStringFormat();
        PdfFont font = new PdfFont(PdfFontFamily.Helvetica, fontsize);
        Dimension2D size = font.measureString(text, format);
        Rectangle rctg = new Rectangle(x, y,
                (int) size.getWidth() / 2 * 3, (int) size.getHeight() * 2);
        page.getCanvas().drawString(text, font, brush, rctg, format);

        //Restore graphics
        page.getCanvas().restore(state);
    }

    private static String getMonthNameByNum(int m) {
        String out = "";
        switch (m) {
            case 1:
                out = "январь";
                break;
            case 2:
                out = "февраль";
                break;
            case 3:
                out = "март";
                break;
            case 4:
                out = "апрель";
                break;
            case 5:
                out = "май";
                break;
            case 6:
                out = "июнь";
                break;
            case 7:
                out = "июль";
                break;
            case 8:
                out = "август";
                break;
            case 9:
                out = "сентябрь";
                break;
            case 10:
                out = "октябрь";
                break;
            case 11:
                out = "ноябрь";
                break;
            case 12:
                out = "декабрь";
                break;
            default:
                out = "unknown";
                break;
        }
        return out;
    }
}
