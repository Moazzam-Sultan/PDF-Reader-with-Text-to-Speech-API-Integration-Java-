import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class PDFread {

    public static String read(String name, int startPage, int endPage) {
        try (PDDocument document = PDDocument.load(new File(name))) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setStartPage(startPage);
            stripper.setEndPage(endPage);
            return stripper.getText(document);
        } catch (IOException e) {
            System.out.println("Error reading PDF: " + e.getMessage());
            return "";
        }
    }

    public static String summarize(String text) {
        StringBuilder summary = new StringBuilder();
        String[] paragraphs = text.split("\\n+");

        for (String para : paragraphs) {
            para = para.trim();
            if (!para.isEmpty() && para.contains(".")) {
                summary.append(para, 0, para.indexOf(".") + 1).append("\n");
            }
        }
        return summary.toString();
    }
}
