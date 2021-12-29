/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.lang.*;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

/**
 *
 * @author Dell
 */

public class TF_IDF {
    public double[][] Normalize_TF(String doc_term_file,int current_terms,int rows ) throws IOException
    {
        String splited_doc_index = new String(Files.readAllBytes(Paths.get(doc_term_file)));
        String[] info_of_onedoc = splited_doc_index.split("\n");
        double[][] TF_Mat = new double[rows][current_terms];
        for (int row = 0; row < TF_Mat.length; row++) {
            for (int col = 0; col < TF_Mat[row].length; col++) {
                TF_Mat[row][col] = 0; //Whatever value you want to set them to
            }
        }
        //System.out.println("length" + info_of_onedoc.length);
        for (int i = 0; i < rows; i++) {
            String[] one_docinfo = info_of_onedoc[i].split("    ");
            int aa = (int) i / 3;
            for (int term_num = 1; term_num < one_docinfo.length; term_num++) {
                String[] count_of_one_term = one_docinfo[term_num].split(":");
                double Normalize_TF_OF_oneterm = (1 + Math.log10(Integer.parseInt(count_of_one_term[1])));
                //System.out.print(aa);
                int ccc = (Integer.parseInt(count_of_one_term[0])-1);
//                if (ccc < current_terms)//////////since coloums are only 70000
//                {
                    TF_Mat[aa][ccc] = Normalize_TF_OF_oneterm;
               // }

            }
            i = i + 2;////to ignore two extra "\n"
        }
        return TF_Mat;
        
    }
    
    ///////this function will return tf(q,i) for queries
    
    ////function to calculate df(i) for idf method and BM25
    public int[] df_of_ith_term(String term_index_file,int current_terms ) throws IOException
    {
        String splited_term_index = new String(Files.readAllBytes(Paths.get(term_index_file)));
        int [] df=new int[current_terms];
        for(int i=0;i<current_terms;i++)
        {
            df[i]=0;
        }
        String[] info_of_oneterm = splited_term_index.split("\n");
        for (int term_num = 0; term_num < info_of_oneterm.length; term_num++) 
        {
            String[] one_terminfo = info_of_oneterm[term_num].split("    ");
            int touched_documents_by_this_term = (one_terminfo.length - 1);
            df[(int)term_num/2]=touched_documents_by_this_term;
            term_num=term_num+1;/////////because I have placed a space in file for each line
        }
        return df;
    }

    public double[] IDF(String term_index_file,int current_terms,int rows) throws IOException
    {
      //current_terms=171149;
        int row = 3465;
        String splited_term_index = new String(Files.readAllBytes(Paths.get(term_index_file)));
        double[] IDF_mat = new double[current_terms];
        for(int i=0;i<current_terms;i++)
        {
            IDF_mat[i]=0;
        }
        String[] info_of_oneterm = splited_term_index.split("\n");
        //System.out.print("hello"+info_of_oneterm.length);
        int [] df=new int[current_terms];
        df=df_of_ith_term(term_index_file,current_terms );////to find df for all terms
        for (int term_num = 0; term_num < info_of_oneterm.length; term_num++) 
        {
//            if(term_num==12030)
//                row=(int)term_num/2;
            //System.out.print(df[(int)term_num/2]);
            IDF_mat[(int)term_num/2] = Math.log10(3465 / df[(int)term_num/2]);
            term_num=term_num+1;/////////because I have placed a space in file for each line
        }
        return IDF_mat;
    }
    public double[][] TF_IDF(double TF_Mat[][],double IDF_mat[],int current_terms,int rows)
    {
        double[][] TF_IDF = new double[rows][current_terms];
        
        for (int col = 0; col < current_terms; col++) {
            for (int row = 0; row < rows; row++) {
                TF_IDF[row][col] = IDF_mat[col] * TF_Mat[row][col];
            }
        }
        
        return TF_IDF;
    }
    public void Main_fun(String doc_term_file, String term_index_file,int current_terms,int rows) throws IOException, ParserConfigurationException, SAXException 
    {
        
        //current_terms=631;
        //rows=3465;
//        double IDF_mat[]=new double[current_terms];
//        IDF_mat=IDF(term_index_file,current_terms,rows);
//        double[][] TF_Mat = new double[rows][current_terms];
//        TF_Mat=Normalize_TF(doc_term_file,current_terms,rows);
//        double[][] TF_IDF = new double[rows][current_terms];
//        TF_IDF=TF_IDF(TF_Mat,IDF_mat,current_terms,rows);
//        int count=0;
//         for (int row = 0; row < rows; row++) {
//            for (int col = 0; col < current_terms; col++) {
//                System.out.print(TF_IDF[row][col]+" "); //Whatever value you want to set them to
//            }
//            System.out.print("value of count "+count);
//            //count++;
//            System.out.print("\n");
//        }
//         
         
        ////this function returns the parsed and stemmed queries
//        Query_Parsing obj=new Query_Parsing();
//        String[] parsed_query=obj.main();
//        for(int i=0;i<parsed_query.length;i++)
//        {
//            System.out.println(" Parsed query "+parsed_query[i]);
//        }
        //////////this whole process is used to load the term id file in hashtable
//        String termids="C:\\Users\\Dell\\Desktop\\part2 ir\\termids.txt";
//        String term_ids = new String(Files.readAllBytes(Paths.get(termids)));
//        String[] sigle_id_term = term_ids.split("\n");
//        Hashtable<Integer, String>hash_map = new Hashtable<Integer, String>();
//        for(String str:sigle_id_term)
//        {
//            String[] my_term=str.split("    ");
//            int key=Integer.parseInt(my_term[0]);
//            hash_map.put(key, my_term[1]);
//        }
//        
//        //////////this function is used to find out whether the word of query is present in 
//        /////////in our corpse or vacublary 
//        for(int q_count=0;q_count<parsed_query.length;q_count++)
//        {
//            String[] one_query=parsed_query[q_count].split(" ");
//            for(int i=0;i<one_query.length;i++)
//            {
//                if(hash_map.containsValue(one_query[i]))
//                {
//                    String aquired_key=hash_map.get(one_query[i]);
//                }
//                
//            }
//            
//            
//        }
        
         
        

    }
}
