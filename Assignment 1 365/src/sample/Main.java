package sample;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

import javafx.geometry.Pos;
import javafx.application.Application;
import javafx.geometry.Insets;

import java.io.BufferedReader;
import java.io.FileReader;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javafx.scene.control.Button;
import javafx.stage.Window;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

public class Main extends Application {
    private static Map2<String, Map2<String, Word>> finalhash1;
    private static Map2<String, Word> finalhash2;
    private Button button;
    private Button button2;
    private Button button3;
    private Window window;

    private static double similarity(Map2<String, Map2<String, Word>> cats, Map2<String, Word> dogs) {//supposed similarity function
        double dotProduct = 0.0;//total
        double normA = 0.0;//for cats
        double normB = 0.0;//for dogs
        System.out.println("\nThese are your closest options to your URL\n");

        for (String c : cats.keys()) {
            //System.out.println(cats.keys());
            for (String d : cats.get(c).keys()) {
                if (dogs.get(d) != null) {//correct math, do not change
                    dotProduct += (cats.get(c).get(d).count * dogs.get(d).count);
                    normA += Math.pow(cats.get(c).get(d).count, 2);
                    normB += Math.pow(dogs.get(d).count, 2);
                }
            }
            //System.out.println("\n" + dotProduct / (Math.sqrt(normA) * Math.sqrt(normB)));
            double num1 = dotProduct / Math.sqrt(normA * normB);
            if (num1 >= .5) {//check for best similarity
                System.out.println(c);
            }
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    private static boolean isLink(String message) {
        try {
            new URL(message).toURI();//checks input with fun
            System.out.println(message + " is a URL, congrats man!");//prints
            txtInsert(message);//print frequency
            return true;//1
        } catch (MalformedURLException e) {//if the url sucks
            System.out.println("Sad! " + message + " is not a URL. Make sure you add 'http://'.");
            return false;//0
        } catch (URISyntaxException e) {
            e.printStackTrace();//so it doesn't error
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void txtInsert(String url) throws IOException {
        Map2<String, Word> countMap2 = new Map2<String, Word>();//!!!

        Map2<String, Map2<String, Word>> hashline2 = new Map2<>();//hashtable with the url and hash with word

        Document doc2 = Jsoup.connect(url).get();// jsoup connecting the url
        String text = doc2.body().text(); //body of text
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8))));
        String lines;//the line
        while ((lines = reader.readLine()) != null) {//while the line has stuff
            String[] words = lines.split("[^A-ZÃ…Ã„Ã–a-zÃ¥Ã¤Ã¶]+");//ahhhh
            for (String word : words) {//for the words
                if (word.length() < 3 || "".equals(word) || "the".equals(word) || "The".equals(word) || "as".equals(word) || "As".equals(word) || "and".equals(word) || "And".equals(word) || "for".equals(word) || "For".equals(word) || "to".equals(word) || "To".equals(word)
                        || "in".equals(word) || "In".equals(word) || "a".equals(word) || "A".equals(word) || "it".equals(word) || "It".equals(word) || "is".equals(word) || "Is".equals(word) || "from".equals(word) || "From".equals(word) || "some".equals(word) || "Some".equals(word)
                        || "s".equals(word) || "'s".equals(word) || "of".equals(word) || "Of".equals(word) || "on".equals(word) || "On".equals(word) || "its".equals(word) || "Its".equals(word) || "are".equals(word) || "Are".equals(word) ||
                        "has".equals(word) || "Has".equals(word) || "was".equals(word) || "Was".equals(word) || "an".equals(word) || "An".equals(word) || "that".equals(word) || "That".equals(word) || "S".equals(word) || "have".equals(word) || "Have".equals(word) || "by".equals(word) || "By".equals(word) || "or".equals(word) || "Or".equals(word) || "with".equals(word) || "With".equals(word) || "such".equals(word) || "Such".equals(word)
                        || "their".equals(word) || "Their".equals(word) || "they're".equals(word) || "They're".equals(word) || "most".equals(word) || "Most".equals(word) || "which".equals(word) || "Which".equals(word) || "This".equals(word) || "Into".equals(word) || "used".equals(word) || "Used".equals(word)
                        || "th".equals(word) || "this".equals(word) || "be".equals(word) || "Be".equals(word) || "will".equals(word) || "Will".equals(word) || "sure".equals(word) || "Sure".equals(word) || "there".equals(word) || "There".equals(word) || "They".equals(word) || "they".equals(word) || "may".equals(word) || "into".equals(word)) {//getting rid of those words
                    continue;//skip
                }
                Word wordObj = countMap2.get(word);//using the hash and getting the count
                if (wordObj == null) {//check
                    wordObj = new Word();
                    wordObj.word = word;
                    wordObj.count = 0;
                    countMap2.add(word, wordObj);
                }
                wordObj.count++;//make it go up
            }
        }
        reader.close();
        SortedSet<Word> sortWords2 = new TreeSet<Word>(countMap2.values());//sorting the set
        int i = 0;
        for (Word word : sortWords2) {
            if (i > 12) {//how many words appear
                break;
            }
            System.out.println(word.count + "\t" + word.word);//should be in this format
            i++;//go up
        }
        hashline2.add(url, countMap2);
        finalhash2 = countMap2;
    }

    public static void main(String[] args) throws IOException {
        {
            ArrayList<String> arrayurl = new ArrayList<>();//arraylist for urls
            BufferedReader read;
            read = new BufferedReader(new FileReader("C:\\Users\\Owner\\Desktop\\text.txt"));//reader for lines in txt file

            try (BufferedReader read2 = new BufferedReader(new FileReader("C:\\Users\\Owner\\Desktop\\text.txt"))) {
                String currentline;

                while ((currentline = read2.readLine()) != null) {//put in arraylist
                    arrayurl.add(currentline);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Map2<String, Map2<String, Word>> hashline = new Map2<>();//hashtable with the url and hash with word

            String line = "";//reset
            while (line != null) {//for getting frequnecy of each line in file
                line = read.readLine();//read next line
                if (line == null) break;
                Document doc = Jsoup.connect(line).get();// jsoup connecting the url
                Map2<String, Word> countMap = new Map2<>();//hashtable for the words
                System.out.println(line);//print link
                hashline.add(line, countMap);//put url and hash in 2nd hash

                String text = doc.body().text(); //body of text
                BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8))));//omg I wish I knew what this
                String lines;//each of the lines on the link
                while ((lines = reader.readLine()) != null) {//while the line has stuff
                    String[] words = lines.split("[^A-ZÃ…Ã„Ã–a-zÃ¥Ã¤Ã¶]+");//ahhhh
                    for (String word : words) {//for the words
                        if (word.length() < 3 || "".equals(word) || "the".equals(word) || "The".equals(word) || "as".equals(word) || "As".equals(word) || "and".equals(word) || "And".equals(word) || "for".equals(word) || "For".equals(word) || "to".equals(word) || "To".equals(word)
                                || "in".equals(word) || "In".equals(word) || "a".equals(word) || "A".equals(word) || "it".equals(word) || "It".equals(word) || "is".equals(word) || "Is".equals(word) || "from".equals(word) || "From".equals(word) || "some".equals(word) || "Some".equals(word)
                                || "s".equals(word) || "'s".equals(word) || "of".equals(word) || "Of".equals(word) || "on".equals(word) || "On".equals(word) || "its".equals(word) || "Its".equals(word) || "are".equals(word) || "Are".equals(word) ||
                                "has".equals(word) || "Has".equals(word) || "was".equals(word) || "Was".equals(word) || "an".equals(word) || "An".equals(word) || "that".equals(word) || "That".equals(word) || "S".equals(word) || "have".equals(word) || "Have".equals(word) || "by".equals(word) || "By".equals(word) || "or".equals(word) || "Or".equals(word) || "with".equals(word) || "With".equals(word) || "such".equals(word) || "Such".equals(word)
                                || "their".equals(word) || "Their".equals(word) || "they're".equals(word) || "They're".equals(word) || "most".equals(word) || "Most".equals(word) || "which".equals(word) || "Which".equals(word) || "This".equals(word) || "Into".equals(word) || "used".equals(word) || "Used".equals(word)
                                || "th".equals(word) || "this".equals(word) || "be".equals(word) || "Be".equals(word) || "will".equals(word) || "Will".equals(word) || "sure".equals(word) || "Sure".equals(word) || "there".equals(word) || "There".equals(word) || "They".equals(word) || "they".equals(word) || "may".equals(word) || "into".equals(word)) {//getting rid of those words
                            continue;//skip
                        }
                        Word wordObj = countMap.get(word);//using the hash and getting the count
                        if (wordObj == null) {//check
                            wordObj = new Word();
                            wordObj.word = word;
                            wordObj.count = 0;
                            countMap.add(word, wordObj);
                        }
                        wordObj.count++;//make it go up
                    }
                }
                reader.close();

                SortedSet<Word> sortWords = new TreeSet<Word>(countMap.values());//this is my sort set so they are in num order
                int i = 0;//It's an i, praise be to the letter i
                for (Word word : sortWords) {
                    if (i > 12) {//how many number of words
                        break;
                    }
                    System.out.println(word.count + "\t" + word.word);//should be in this format
                    i++;//go up'
                }

                Map2<String, Word> count2 = new Map2<>();
                for (String k : countMap.keys()) {
                    if (k == null) continue;
                    count2.add(k, countMap.get(k));
                }
                hashline.add(line, count2);
                finalhash1 = hashline;
            }
            read.close();
        }
        launch(args);//makes it go
    }

    @Override
    public void start(Stage s) {
        window = s;
        ((Stage) window).setTitle("Cameron's Super Duper Web Checker");
        //form
        final TextField nameInput = new TextField();//link as textfield
        nameInput.setPromptText("Enter URL here");//hint text that does not work
        button = new Button("Check Me Out!");//my buttons baby
        button.setOnAction(e -> isLink(nameInput.getText()));//checks

        WebView myweb = new WebView();//the webview
        WebEngine engine = myweb.getEngine();//vroom vroom
        button2 = new Button("Go To URL!");
        button2.setOnAction(e -> engine.load(nameInput.getText()));//loading url

        button3 = new Button("Check Similarity");
        button3.setOnAction(e -> similarity(finalhash1, finalhash2));

        //layout
        VBox layout = new VBox(15);//spacing between bars/buttons
        layout.setStyle("-fx-background-color: #EEE8AA;");//color
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(nameInput, button, button2, button3, myweb);//added myweb
        layout.setAlignment(Pos.CENTER);

        //scene
        Scene scene = new Scene(layout, 900, 900);
        ((Stage) window).setScene(scene);
        s.show();
    }

    public static class Word implements Comparable<Word> {//used
        String word;//the word
        int count;//number

        @Override
        public int compareTo(Word baby) {//needs this check cameron
            return baby.count - count;
        }//compare to the words baby! Need for some reason
    }
}

