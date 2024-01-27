/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Admin
 */
public class Controller {

    public static void main(String[] args) {
        StringBuilder normalizedText = new StringBuilder();

        writeFile("output.txt", readFileAndNormalizeText("input.txt", normalizedText).toString());

    }

    

    public static StringBuilder readFileAndNormalizeText(String inputFile, StringBuilder formatedText) {
        try {
            File file = new File(inputFile);
            FileReader fileReader = null;
            BufferedReader bufferedReader = null;
            try {
                fileReader = new FileReader(inputFile);
                bufferedReader = new BufferedReader(fileReader);
                boolean needToUpperCaseFirstChar = true;    //đặt true luôn vì chữ đầu trong đoạn text thì phải viết hoa
                String line = bufferedReader.readLine();
                while (line != null) {
                    // nếu dòng trống thì đọc dòng mới(không trống thì xử lí, thêm dô formatedText r đọc dòng mới
                    if (!line.trim().isEmpty()) {
                        //Dòng nào bắt đầu một câu mới (đầu đoạn text hoặc dòng trước nó kết thúc vs dấu chấm) thì viết hoa chữ đầu
                        if (needToUpperCaseFirstChar) {
                            line = uppercaseFirstChar(line);
                        }
                        //xử lí xong rồi thì cho nó check dòng hiện tại để cập nhật trạng thái cho dòng kế tiếp
                        needToUpperCaseFirstChar = line.trim().endsWith(".");

                        //xử lí dòng hiện tại
                        line = removeRedundantSpace(line);
                        line = formatSpaceWithPunctuation(line);
                        line = formatSpaceWithDoubleQuotation(line);
                        line = upperCaseWordAfterDot(line);
                        //rồi thêm dòng đã xử lí vào chuỗi để chặp ghi file
                        if (line.trim().endsWith(".")) {
                            //nếu dòng hiện tại kết thúc câu với dấu chấm, thì thêm kí tự xuống dòng vào text
                            formatedText.append(line).append("\n");
                        } else {
                            //không thì thêm từ vào text với dấu cách ở giữa như bình thường
                            formatedText.append(line).append(" ");
                        }
                    }

                    line = bufferedReader.readLine();

                }
                
            } catch (FileNotFoundException ex) {
                System.err.println("File not found.");
            } finally {

                bufferedReader.close();
                fileReader.close();
            }
        } catch (IOException e) {
            System.err.println("Failed when reading file.");
        }
        return formatedText;
    }

    public static String uppercaseFirstChar(String line) {
        //xóa khoảng trắng thừa ở đầu và cuối chuỗi trc khi xử lí chứ hong nó lỗi
        line = line.trim();
        String firstChar = line.substring(0, 1).toUpperCase();
        String restOfString = line.substring(1).toLowerCase();
        line = firstChar + restOfString;
        return line;
    }

    public static String upperCaseWordAfterDot(String line) {
        StringBuilder formatLine = new StringBuilder();
        // tách dòng thành các câu trong dòng bởi dấu chấm. chạy vòng for cứ mỗi câu là viết hoa chữ cái đầu của câu.
        String sentences[] = line.split("\\.");
        for (String sentence : sentences) {
            sentence = uppercaseFirstChar(sentence);
            //rồi thêm vào chuỗi text với dấu chấm mỗi cuối câu. Như này thì cuối đoạn text auto đc thêm dấu chấm
            formatLine.append(sentence).append(". ");
        }

        return formatLine.toString();
    }
    public static String removeRedundantSpace(String line){
        //bỏ khoảng trắng thừa ở đầu và cuối dòng, thay toàn bộ khoảng trắng thừa thành một khoảng trắng
        line = line.replaceAll("\\s+", " ").trim();
        return line;
    }
    public static String formatSpaceWithPunctuation(String line){
        //bỏ khoảng trắng trước ,.:; 
        line = line.replaceAll("\\s+(?=[,.:;])", "");
        //có dấu ,.:; nào mà sau nó không có khoảng trắng thì thêm khoảng trắng (thay thế cái dấu đó = nó + 1 khoảng trắng phía sau)
        line = line.replaceAll("([,.:;])(?!\\s|$)", "$1 ");
       return line;
    }
    public static String formatSpaceWithDoubleQuotation(String line){
        //bỏ khoảng trắng thừa giữa dấu " và chữ trong ""
        //cái biểu thức regex này đọc k hiểu thì inbox
        line = line.replaceAll("(\")(\\s+)(\\D+)(\\s+)(\\\")", "$1$3$5");
        return line;
    }
    
    public static void writeFile(String outputFile, String text) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outputFile)))) {
                writer.write(text);
        } catch (IOException e) {
            System.out.println("Error when writing file!");
        }
        System.out.println("Saved successfully!");
    }

}
