
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class Main {

    public static void speakText(String text, int rate) {
        System.setProperty("freetts.voices",
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

        VoiceManager vm = VoiceManager.getInstance();
        Voice voice = vm.getVoice("kevin16");

        if (voice != null) {
            voice.allocate();
            voice.setRate(rate);

            String[] lines = text.split("\\r?\\n");
            int total = lines.length;

            for (int i = 0; i < total; i++) {
                voice.speak(lines[i]);
                int percent = (i + 1) * 100 / total;
                System.out.print("\rReading... " + percent + "%");
            }

            voice.deallocate();
            System.out.println("\nDone.");
        } else {
            System.out.println("Voice not found.");
        }
    }

    public static void main(String[] args) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select a PDF to read aloud");
        chooser.setFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));

        int result = chooser.showOpenDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(null, "No file selected. Exiting.");
            return;
        }

        File pdfFile = chooser.getSelectedFile();


        int startPage = 1, endPage = 1;
        try {
            String startInput = JOptionPane.showInputDialog("Enter start page:");
            String endInput = JOptionPane.showInputDialog("Enter end page:");
            startPage = Integer.parseInt(startInput);
            endPage = Integer.parseInt(endInput);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid page range. Using full document.");
        }

        String text = PDFread.read(pdfFile.getAbsolutePath(), startPage, endPage);
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Failed to read PDF.");
            return;
        }


        int summaryOption = JOptionPane.showConfirmDialog(null,
                "Do you want to summarize the PDF before reading?", "Summarize?",
                JOptionPane.YES_NO_OPTION);
        if (summaryOption == JOptionPane.YES_OPTION) {
            text = PDFread.summarize(text);
        }

        int speed = 150;
        try {
            String speedInput = JOptionPane.showInputDialog("Enter speech speed (default 150):");
            if (speedInput != null && !speedInput.isEmpty()) {
                speed = Integer.parseInt(speedInput);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid speed. Using default.");
        }

        speakText(text, speed);
    }
}
