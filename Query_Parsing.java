/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment2;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;
import org.xml.sax.SAXException;

/**
 *
 * @author Dell
 */
public class Query_Parsing 
{
      public static String removeManually(String stopwords, String[] tokenized_file) {
        StringBuilder builder = new StringBuilder();
        for (String word : tokenized_file) {
            if (!stopwords.contains(word)) {
                builder.append(word);
                builder.append(' ');
            }
        }
        return builder.toString().trim();
    }
       public static String stem(String s) {
        SnowballStemmer snowballStemmer = new englishStemmer();
        StringBuilder builder = new StringBuilder();
        String[] spliting = s.split(" ");
        for (String word : spliting) {
            snowballStemmer.setCurrent(word);
            snowballStemmer.stem();
            String result = snowballStemmer.getCurrent();
            builder.append(result);
            builder.append(' ');
        }
        return builder.toString().trim();
    }
    public  String[] main(int []queries_id) throws IOException, ParserConfigurationException, SAXException
    {
        
    String directry_name="D:\\Semester 7\\Information Retirievel\\Assignment2\\topics.xml";
    BufferedWriter parse_queries_file = new BufferedWriter(new FileWriter("parsed_topics", true));
        String query_file = new String(Files.readAllBytes(Paths.get(directry_name)));
        int count = 0;
        String query_file_after_removing_headr = null;
        
        System.out.print("hello"+query_file_after_removing_headr);
       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
       DocumentBuilder builder = factory.newDocumentBuilder();
       Document document = builder.parse(new File( directry_name ));
       document.getDocumentElement().normalize();
       Element root = document.getDocumentElement();
       System.out.println(root.getNodeName());
       NodeList nList = document.getElementsByTagName("topic");
       System.out.println(""+nList.getLength());
       //queries_id=new int[nList.getLength()];
        int[] topic_list=new int[nList.getLength()];
        String []queries=new String[nList.getLength()];
       for (int temp = 0; temp < nList.getLength(); temp++)
        {
            Node node = nList.item(temp);
            System.out.println("");    //Just a separator
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                //Print each employee's detail
                Element eElement = (Element) node;
                //System.out.println("Topic : " + eElement.getAttribute("number"));
                topic_list[temp]=Integer.parseInt(eElement.getAttribute("number"));
                //System.out.println("query : " + eElement.getElementsByTagName("query").item(0).getTextContent());
                queries[temp]=eElement.getElementsByTagName("query").item(0).getTextContent();
                String a=null;
                String[] tokenized_file;
                tokenized_file = queries[temp].split("[\\p{Punct}\\s]+");
                String dir_path_of_stopwords = "D:\\Semester 7\\Information Retirievel\\stoplist.txt";
                String stop_words = new String(Files.readAllBytes(Paths.get(dir_path_of_stopwords)));
                queries[temp]=removeManually(stop_words, tokenized_file);
               queries[temp] = queries[temp].toLowerCase();
                System.out.println("Topic : " + topic_list[temp]);
                queries_id[temp]=topic_list[temp];
                queries[temp] = stem(queries[temp]);
                parse_queries_file.append(queries[temp]);
                parse_queries_file.append("\n");
                System.out.println("Query : " + queries[temp]);
            }
        }
       //queries_id=topic_list;
       return queries;
        
    }
}
