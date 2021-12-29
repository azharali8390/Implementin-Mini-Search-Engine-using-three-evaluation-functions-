/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package assignment2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

/**
 *
 * @author Dell
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    class MyHashMap<K, V> extends HashMap<K, V> {

        Map<V, K> reverseMap = new HashMap<>();

        @Override
        public V put(K key, V value) {
            reverseMap.put(value, key);
            return super.put(key, value);
        }

        public K getKey(V value) {
            return reverseMap.get(value);
        }

    }

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        Test main_obj = new Test();
        main_obj.test();
    }

    public void test() throws IOException, ParserConfigurationException, SAXException {
        int current_terms = 171149;
        int total_vacab = 171149;
        int rows = 500;
        String arr = "--score";
        String pp = "TF-IDF";
        BufferedWriter tf_idf = new BufferedWriter(new FileWriter("tfidf.txt", true));
        BufferedWriter bm25 = new BufferedWriter(new FileWriter("bm25.txt", true));
        BufferedWriter  jm = new BufferedWriter(new FileWriter("jm.txt", true));
        String adres_of_docindex = "C:\\Users\\Dell\\Desktop\\part2 ir\\doc_index.txt";
        String term_index = "C:\\Users\\Dell\\Desktop\\part2 ir\\term_index.txt";
        int[] queries_id = new int[10];
        TF_IDF obj1 = new TF_IDF();
        obj1.Main_fun(adres_of_docindex, term_index, current_terms, rows);
//        System.out.println(args[0]);
//        System.out.println(args[1]);
        ////////this whole process is used to load the term id file in hashtable
        String termids = "C:\\Users\\Dell\\Desktop\\part2 ir\\termids.txt";
        String term_ids = new String(Files.readAllBytes(Paths.get(termids)));
        Query_Parsing obj = new Query_Parsing();
        String[] parsed_query = obj.main(queries_id);

        String[] sigle_id_term = term_ids.split("\n");
        MyHashMap<Integer, String> hashMap = new MyHashMap<Integer, String>();

        for (int i = 0; i < total_vacab; i++) {
            String[] my_term = sigle_id_term[i].split("    ");
            int key = Integer.parseInt(my_term[0]);

            if (my_term.length == 1) {
                hashMap.put(key, " ");
            } else {
                hashMap.put(key, my_term[1]);
            }

        }
        //////////////making hashmap for docname and its ids
        String doc_ids_addres = "C:\\Users\\Dell\\Desktop\\part2 ir\\docids.txt";
        String doc_ids_doc = new String(Files.readAllBytes(Paths.get(doc_ids_addres)));
        String[] spillited_doc_ids = doc_ids_doc.split("\n");
        MyHashMap<Integer, String> docs_hashmap = new MyHashMap<Integer, String>();
        for (int i = 0; i < spillited_doc_ids.length; i++) {
            String[] doc = spillited_doc_ids[i].split("    ");
            int key = Integer.parseInt(doc[1]);
            docs_hashmap.put((key-1), doc[0]);

        }
        //////////this function is used to find out whether the word of query is present in 
        /////////in our corpse or vacublary 
        double[][] tf_of_queries = new double[parsed_query.length][current_terms];
        double[][] tf_idf_of_query1 = new double[parsed_query.length][current_terms];
        double IDF_mat[] = new double[current_terms];
        IDF_mat = obj1.IDF(term_index, current_terms, rows);
        for (int row = 0; row < parsed_query.length; row++) {
            for (int col = 0; col < current_terms; col++) {
                tf_idf_of_query1[row][col] = 0; //Whatever value you want to set them to
                tf_of_queries[row][col] = 0;
            }
        }
        int count = 0;
        for (int q_count = 0; q_count < parsed_query.length; q_count++) {
            String[] one_query = parsed_query[q_count].split(" ");
            for (int i = 0; i < one_query.length; i++) {
                if (hashMap.containsValue(one_query[i])) {
                    int key = hashMap.getKey(one_query[i]);
                    //idf_of_queries[q_count][i]=key;

                    count = StringUtils.countMatches(parsed_query[q_count], one_query[i]);

                    tf_of_queries[q_count][key-1] = count;

                }

            }

        }
        for (int i = 0; i < current_terms; i++) {
            for (int j = 0; j < parsed_query.length; j++) {
                tf_idf_of_query1[j][i] = tf_of_queries[j][i] * IDF_mat[i];
            }
        }
        double total_length = 0;
        //TF_IDF tf_idf = new TF_IDF();
        int c = 0;
        c = 1;
        double[][] TF_Mat = new double[rows][current_terms];

        TF_Mat = obj1.Normalize_TF(adres_of_docindex, current_terms, rows);
        System.out.println("Azhar Ali");
        if (arr.equals("--score")) {
            if (pp.equals("TF-IDF")) {

//         double IDF_mat1[]=new double[631];
//        IDF_mat=tf_idf.IDF(term_index,current_terms,rows);
                double[][] TF_IDF = new double[rows][current_terms];
                TF_IDF = obj1.TF_IDF(TF_Mat, IDF_mat, current_terms, rows);
                double[][] score_tf_idf = new double[10][rows];
                double temp_sum = 0;
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < rows; j++) {
                        for (int k = 0; k < current_terms; k++) 
                        {
                          
                            temp_sum = (tf_idf_of_query1[i][k] * TF_IDF[j][k]) + temp_sum;
                        }
                        score_tf_idf[i][j] = temp_sum;
                        temp_sum=0;
                    }
                }
                double[] sum_of_squares = new double[10];
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < current_terms; j++) {
                        sum_of_squares[i] = sum_of_squares[i] + Math.pow(tf_idf_of_query1[i][j], 2);
                    }
                    sum_of_squares[i] = Math.sqrt(sum_of_squares[i]);
                }
                double[] sum_of_squares_docs = new double[rows];
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < current_terms; j++) {
                        sum_of_squares_docs[i] = sum_of_squares_docs[i] + Math.pow(TF_IDF[i][j], 2);
                    }
                    sum_of_squares_docs[i] = Math.sqrt(sum_of_squares_docs[i]);
                }
                double[][] multipied_vectors = new double[10][rows];
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < rows; j++) {
                        multipied_vectors[i][j] = sum_of_squares[i] * sum_of_squares_docs[j];
                    }
                }
                double[][] final_score = new double[10][rows];
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < rows; j++) {
                        Double x = score_tf_idf[i][j] / multipied_vectors[i][j];
                        if(x.isNaN())
                        {
                           x=0.0; 
                        }
                        else
                        {
                            final_score[i][j]=x;
                        }
                        tf_idf.append(queries_id[i]+" ");
                       String keyy= docs_hashmap.get(j);
                       tf_idf.append(keyy+" "+final_score[i][j]);
                       tf_idf.append("\n");
                    }
                }
                 for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < rows; j++) {
                        System.out.print(" " + final_score[i][j]);

                    }
                    System.out.print("\n");
                }
                //int ccc = 0;
               // ccc = 1;

            } else if (pp.equals("BM25")) {
                System.out.println("BM25");
                double k1 = 1.2;
                double k2 = 2;
                double b = 0.75;
                int D = 3465;         //total number of documents
                ArrayList<String> docs = new ArrayList<String>();
                final String path_variable = "D:\\Semester 7\\Information Retirievel\\corpus\\corpus";
                final String file_having_length_of_docs = "C:\\Users\\Dell\\Desktop\\part2 ir\\doc_length.txt";
                String str_having_length_of_docs = new String(Files.readAllBytes(Paths.get(file_having_length_of_docs)));
                //to_find_length_of_doc obj11 = new to_find_length_of_doc();
                //docs = obj11.read(path_variable);////function to find length of all documents
                System.out.println("size returned" + docs.size());
                String[] one_doc_info = str_having_length_of_docs.split("\n");
                //double[][] TF_Mat = new double[rows][current_terms];
                //TF_Mat = obj1.Normalize_TF(adres_of_docindex, current_terms, rows);
                int[] df = new int[current_terms];
                df = obj1.df_of_ith_term(term_index, current_terms);
                double avg_len = 0;
                int[] len_array_of_docs = new int[3465];
                for (int i = 0; i < 3465; i++) {
                    String[] single_doc = one_doc_info[i].split("    ");

                    len_array_of_docs[i] = Integer.parseInt(single_doc[1]);
                    System.out.println(" length of current docs " + len_array_of_docs[i]);
                    avg_len = avg_len + len_array_of_docs[i];

                }
                double K = 0;
                double tmp_sum_of_BM25 = 1;
                //double sum=0;
                double[][] final_score_BM25 = new double[10][rows];
                total_length = avg_len;////storing sum of length of all docs
                double average_len = (int) avg_len / rows;
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < rows; j++) {

                        //calculate the value of K
                        K = k1 * ((1 - b) + ((len_array_of_docs[j]) * b) / average_len);
                        //df = obj1.df_of_ith_term(term_index, current_terms);
                        for (int k = 0; k < current_terms; k++) {

                            Double sum = (Math.log10((D + 0.5) / df[k] + 0.5)) * (((1 + k1) * TF_Mat[j][k]) / (K + TF_Mat[j][k])) * (((1 + k2) * tf_of_queries[i][k]) / (k2 + tf_of_queries[i][k]));
                            if (sum.isNaN()) {
                                sum = 0.0;
                            } else {
                                tmp_sum_of_BM25 = tmp_sum_of_BM25 + sum;
                            }
                        }
//                        if(tmp_sum_of_BM25!=0)
//                        {
//                            final_score_BM25[i][j] = tmp_sum_of_BM25;
//                        tmp_sum_of_BM25 = 0;
//                        }
                        final_score_BM25[i][j] = Math.round(tmp_sum_of_BM25);
                        tmp_sum_of_BM25 = 0;
                        bm25.append(queries_id[i]+" ");
                       String keyy= docs_hashmap.get(j);
                       bm25.append(keyy+" "+final_score_BM25[i][j]);
                       bm25.append("\n");
                    }
                }
//                for (int i = 0; i < 10; i++) {
//                    for (int j = 0; j < rows; j++) {
//                        System.out.print(" " + final_score_BM25[i][j]);
//
//                    }
//                    System.out.print("\n");
//                }
//                int stop = 0;
//                stop = 3 * 4;
            } else {
                System.out.println("JM");
                double λ = 0.6;
                double sum_of_one_col = 0;
                double[] sum_of_one_col_inJM = new double[current_terms];
                for (int i = 0; i < current_terms; i++) {
                    for (int j = 0; j < rows; j++) {
                        sum_of_one_col = TF_Mat[j][i] + sum_of_one_col;
                    }
                    sum_of_one_col_inJM[i] = sum_of_one_col;
                    sum_of_one_col=0;
                }
                double value = 1 - λ;
                final String file_having_length_of_docs = "C:\\Users\\Dell\\Desktop\\part2 ir\\doc_length.txt";
                String str_having_length_of_docs = new String(Files.readAllBytes(Paths.get(file_having_length_of_docs)));
                String[] one_doc_info = str_having_length_of_docs.split("\n");
                //double[][] TF_Mat = new double[rows][current_terms];
                //TF_Mat = obj1.Normalize_TF(adres_of_docindex, current_terms, rows);
                double avg_len = 0;
                int[] len_array_of_docs = new int[3465];
                for (int i = 0; i < 3465; i++) {
                    String[] single_doc = one_doc_info[i].split("    ");

                    len_array_of_docs[i] = Integer.parseInt(single_doc[1]);
                    System.out.println(" length of current docs " + len_array_of_docs[i]);
                    avg_len = avg_len + len_array_of_docs[i];

                }
                ////TF_Mat contain tf of each with respect to each doument
                /////total_length sum of length of all docs
                //////len_array_of_docs array havin length of all docs
                double[][] prob_of_each_trm_in_each_doc = new double[rows][current_terms];
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < current_terms; j++) {
                        prob_of_each_trm_in_each_doc[i][j] = (λ * (TF_Mat[i][j] / len_array_of_docs[i])) + (value * (sum_of_one_col_inJM[j] / avg_len));
                    }
                }
                double[][] prob_of_query_in_doc = new double[10][rows];
                Double product_of_prob=0.0;
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < rows; j++) {
                        for (int k = 0; k < current_terms; k++) 
                        {
                            product_of_prob = product_of_prob * prob_of_each_trm_in_each_doc[j][k];
                        }
                        if(product_of_prob.isNaN())
                            product_of_prob=0.0;
                            prob_of_query_in_doc[i][j] = product_of_prob;
                       jm.append(queries_id[i]+" ");
                       String keyy= docs_hashmap.get(j);
                       jm.append(keyy+" "+prob_of_query_in_doc[i][j]);
                       jm.append("\n");
                    }
                }
//                 for (int i = 0; i < 10; i++) {
//                    for (int j = 0; j < rows; j++) {
//                        System.out.print(" " + prob_of_query_in_doc[i][j]);
//
//                    }
//                    System.out.print("\n");
//                }
//                  int stop = 0;
//                stop = 3 * 4;

            }

        } else {
            System.out.println("please enter values correctly");
        }
    }

}
