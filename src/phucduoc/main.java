package phucduoc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class main {
    public static void main(String[] args) {
        /*for (int i = 401; i <= 500; i++) {
            try {
                WebCrawler crawler = new WebCrawler();
                String rootURL = "https://www.mathsdesign.com/math-puzzle-" + i;
                crawler.crawl(rootURL, 100, i);
                crawler.printLink();
            } catch (Exception e) {
                System.out.println("wrong i = " + i);
            }
        }*/
        List<String> data = Data.listImage400();
        for (String item : data) {
            try {
                String link = item.trim();
                int len = link.length();
                String nameFile = "i" + link.substring(len - 20, len);
                System.out.println(nameFile);
                saveImage(link, nameFile);
            } catch (Exception e) {

            }
        }

    }

    static void saveImage(String link, String nameFile) {
        BufferedImage image = null;
        try {

            URL url = new URL(link);
            // read the url
            image = ImageIO.read(url);

//                for png
//                ImageIO.write(image, "png",new File("/images/"+nameFile));

            // for jpg
            ImageIO.write(image, "jpg", new File("D:\\_DataApp\\2022 Hack Não IQ\\Source\\Math design\\" + nameFile));
            System.out.println("Success :" + link);
        } catch (IOException e) {
//                e.printStackTrace();
        }
    }

    public static class WebCrawler {

        private Queue<String> urlQueue;
        private List<String> visitedURLs;

        public WebCrawler() {
            urlQueue = new LinkedList<>();
            visitedURLs = new ArrayList<>();
        }

        public void printLink() {
            int max = 0;
            for (int i = 1; i < visitedURLs.size(); i++) {
                if (visitedURLs.get(i).length() > visitedURLs.get(max).length()) {
                    max = i;
                }
            }
            System.out.println("\""+visitedURLs.get(max)+"\",");
        }

        public void crawl(String rootURL, int breakpoint, int i) {
            urlQueue.add(rootURL);
            visitedURLs.add(rootURL);

            while (!urlQueue.isEmpty()) {

                // remove the next url string from the queue to begin traverse.
                String s = urlQueue.remove();
                String rawHTML = "";
                try {
                    // create url with the string.
                    URL url = new URL(s);
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                    String inputLine = in.readLine();

                    // read every line of the HTML content in the URL
                    // and concat each line to the rawHTML string until every line is read.
                    while (inputLine != null) {
                        rawHTML += inputLine;

                        inputLine = in.readLine();
                    }
                    in.close();
                } catch (Exception e) {
                    //e.printStackTrace();
                }

                // create a regex pattern matching a URL
                // that will validate the content of HTML in search of a URL.
//                String urlPattern = "(www|http:|https:)+[^\\s]+[\\w]";
                String urlPattern = String.format("(http(s?):)([/|.|\\w|\\s|-])*%d\\.(?:jpg|gif|png)", i);
                Pattern pattern = Pattern.compile(urlPattern);
                Matcher matcher = pattern.matcher(rawHTML);

                // Each time the regex matches a URL in the HTML,
                // add it to the queue for the next traverse and the list of visited URLs.
                breakpoint = getBreakpoint(breakpoint, matcher);

                // exit the outermost loop if it reaches the breakpoint.
                if (breakpoint == 0) {
                    break;
                }
            }
        }

        private int getBreakpoint(int breakpoint, Matcher matcher) {
            while (matcher.find()) {
                String actualURL = matcher.group();

                if (!visitedURLs.contains(actualURL)) {
                    visitedURLs.add(actualURL);
                    //System.out.println(actualURL);
                    urlQueue.add(actualURL);
                }

                // exit the loop if it reaches the breakpoint.
                if (breakpoint == 0) {
                    break;
                }
                breakpoint--;
            }
            return breakpoint;
        }
    }

}


