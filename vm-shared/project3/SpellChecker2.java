import java.io.*;
import java.util.*;

import java.text.NumberFormat;
import java.text.DecimalFormat;

public class SpellChecker2 {
    Map<String, Integer> dict; // dict contains all dictionary words and their frequency in corpus
    static final String alphabet = "abcdefghijklmnopqrstuvwxyz";

    // constructors
    public SpellChecker2() {
    }

    public SpellChecker2(String dictionaryFile) throws IOException {
        dict = readDictionary(dictionaryFile);
    }

    /*
     * calculateLM: returns the language model P(word) Input: word Output: P(word)
     */
    public double calculateLM(String word) {
        double corpusCount = 0;
        for (Integer value : dict.values()) {
            corpusCount += value;
        }

        return dict.get(word) / corpusCount;
    }

    /*
     * calculateLM: returns the error model P(correction|word) Input: word,
     * correction, and the editDistance between the two Output: P(correction|word)
     */
    public double calculateEM(String word, String correction, double editDistance) {
        // For Task 1, we simply return 1 as the error model P(correction|word)


        //TODO: need to implement this function. Need to figure out how to find probability of correction given word.
        //TODO: remove this.
    	
//        System.out.println("===========CalculateEM function=======");
//
//        System.out.println("word: " + word);
//        System.out.println("correction: " + correction);
//        System.out.println("editDistance:" + editDistance);
        
        List<Character> wordListArray = new ArrayList<Character>();
        List<Character> correctionListArray = new ArrayList<Character>();
        Set<Character> vowels = new HashSet<Character>() {{
            add('a');
            add('e');
            add('i');
            add('o');
            add('u');
        }};
        
        double EMResult = 0.0;


        // build Lists without vowels, but in ordered.
        // Some words without vowels are more efficient, as long the other letters are in ordered.
        // Note: vowels in first char is kept because it is a word.
        for(int l = 0; l < word.toCharArray().length; l++) {
        		if(l == 0) {
        			//first vowel should be kept because it can be made up a word.
        			wordListArray.add(word.charAt(l));
        		}
        		else if(l > 0 && !vowels.contains(word.charAt(l))) {
        			//else remove vowels
        			wordListArray.add(word.charAt(l));
        		}
        }
        
        for(int l = 0; l < correction.toCharArray().length; l++) {
    		if(l == 0) {
    			//first vowel should be kept because it can be made up a word.
    			correctionListArray.add(correction.charAt(l));
    		}
    		else if(l > 0 && !vowels.contains(correction.charAt(l))) {
    			//else remove vowels
    			correctionListArray.add(correction.charAt(l));
    		}
    }
        

        //TODO: remove this.
//        System.out.println("wordListArray:" + wordListArray);
//        System.out.println("correctionListArray:" + correctionListArray);
//        


        //This is the first error correction where it checks the length of vowels removed arrays and position.
        //1-check if both arrays (with vowels removed) are the exactly the same.
        //2-check if both arrays (without vowels removed) are exactly the same.
        if(Arrays.equals(wordListArray.toArray(), correctionListArray.toArray())) {
        		//penalize if the size (without vowels removed) are not the same..
        
        	
        		double exponentFactor = Math.abs(word.toCharArray().length - correction.toCharArray().length);
        		double sizePenalizeFactor = 1.0 - Math.pow(0.15, 1/exponentFactor);
        		
//        		System.out.println("sizePenalizeFactor: " + sizePenalizeFactor);
        	
        	
            //if both arrays are exactly the same in order.. then return a large EM (infinitely big)
       
        		EMResult = Double.MAX_VALUE * sizePenalizeFactor;
//            return EMResult ;
        }
        //if length (with vowels removed) are not the same
        else {
        		int wordListArraySize = wordListArray.size();
        		int correctionListArraySize = correctionListArray.size();
        		int correctCount = 0;
        		
        		//TODO: remove this.
//        		System.out.println("wordListArraySize:" + wordListArraySize);
//        		System.out.println("correctionListArraySize:" + correctionListArraySize);
//
//        		

        		
        		if( wordListArraySize <= correctionListArraySize) {	
            		for(int i = 0; i < wordListArray.size(); i++) {
            			
            			if (wordListArray.get(i) == correctionListArray.get(i)) {
            				correctCount++;
            			}
            			else {
            				//assume that word with double letters are correct..
            				if(i != 0) {
            					if (wordListArray.get(i) == 'y' && wordListArray.get(i-1) == 'l'){
                					correctCount *= 10;
            					}
            					else if(wordListArray.get(i) == 'd' && wordListArray.get(i-1) == 'e') {
            						correctCount *= 10;
            					}
            					else if(wordListArray.get(i) == 'g' && wordListArray.get(i-1) == 'n') {
            						correctCount *=10;
            					}
            				}
            			}
            		}
            		
//            		System.out.println("correctCount:" + correctCount);
            		
            		EMResult = 100*correctCount;
//            		return EMResult;
            		
            		
        		}
        		else if(wordListArraySize > correctionListArraySize) {
        			for(int i = 0; i < correctionListArray.size(); i++) {
            			if (wordListArray.get(i) == correctionListArray.get(i)) {
            				correctCount++;
            			}
            			else {
            				if( i != 0) {
            					if (wordListArray.get(i-1) == 'y' && wordListArray.get(i) == 'l'){
                					correctCount *= 10;
            					}
            					else if(wordListArray.get(i-1) == 'd' && wordListArray.get(i) == 'e') {
            						correctCount *= 10;
            					}
            					else if(wordListArray.get(i-1) == 'g' && wordListArray.get(i) == 'n') {
            						correctCount *=10;
            					}
            				}
        				}

            		}
//            		System.out.println("correctCount:" + correctCount);
            		
            		EMResult = 100*correctCount;
//            		return EMResult;
        		}
        	
        	
        }


        // return 1.0 has accuracy of 74.34%
        return EMResult;
    }

    /*
     * EditDist1: computes the candidate set C within edit distance 1 of input word
     * Input: word - the word typed by the user Output: map of all strings within
     * edit distance 1 of "word" the value of the output map is the edit distnace,
     * which is 1.0
     */
    private Map<String, Double> EditDist1(String word) {
        Map<String, Double> cands = new HashMap<>();
        for (int i = 0; i <= word.length(); ++i) {

            // split the word at position i
            String L = word.substring(0, i);
            String R = word.substring(i);
            String temp = "";

            // "deletion at [i]": drop the character at the current position i
            // goal: to hopefully remove an extra character so there will be a word that is
            // matched.
            if (R.length() > 0) {
                temp = L + R.substring(1);
                cands.put(temp, 1.0); // 1.0 indicates the edit distance 1
            }

            // "insertion at [i]" insert an alphabet (a-z) at the current position i
            for (int j = 0; j < alphabet.length(); ++j) {
                temp = L + alphabet.charAt(j) + R;
                cands.put(temp, 1.0); // 1.0 indicates the edit distance 1
            }


            // "swapping char at [i]" with all other places.
            if (R.length() > 0) {
                for (int j = 0; j < R.length(); ++j) {
                    char[] charArray = R.toCharArray();

                    for (int k = 1; k < charArray.length; ++k) {
                        char tempChar = charArray[j];
                        charArray[j] = charArray[k];
                        charArray[k] = tempChar;
                        temp = L + String.valueOf(charArray);
                        cands.put(temp, 1.0); // 1.0 indicates the edit distance 1
                    }
                }
            }

            //"transposition at [i]" by swapping two adjacents char
            char[] charArray = word.toCharArray();
            if(R.length() > 1) {
                temp = L + String.valueOf(charArray[i+1]) + String.valueOf(charArray[i]) + word.substring(i+2);
                cands.put(temp, 1.0);
            }

            // "substitution at [i]". substitute an alphabet(a-z) at current position i
            for (int j = 0; j < alphabet.length(); ++j) {
                if (R.length() > 0) {
                    temp = L + alphabet.charAt(j) + R.substring(1);
                    cands.put(temp, 1.0); // 1.0 indicates the edit distance 1
                }
            }
        }

        //TODO: Remove this.
//        System.out.println("===========EditDist1 cands: " + cands);


        return cands;
    }

    /*
     * EditDist2: computes the candidate set C within edit distance 2 of input word
     * Input: ed1 - output from EditDist1() function. i.e., map of all strings
     * within edit distance 1 of the input word Output: map of all strings within
     * edit distance 2 of the original input word the value of the output map is the
     * edit distnace, which is 2.0
     */
    private Map<String, Double> EditDist2(Map<String, Double> ed1) {
        Map<String, Double> cands = new HashMap<>();

        // iterate over every string in edit-distance-1 candidate set
        for (Map.Entry<String, Double> entry : ed1.entrySet()) {

            String str = entry.getKey();
            double dist = entry.getValue();

            // get every string within edit-distance-1 of str
            Map<String, Double> ed2 = EditDist1(str);

            // iterate over every string in the edit-distance-1 of str
            for (Map.Entry<String, Double> entry2 : ed2.entrySet()) {
                String str2 = entry2.getKey();
                double dist2 = entry2.getValue();

                // put the newly obtained string into edit-distance-2 candidate set
                cands.put(str2, dist + dist2);
            }
        }

        //TODO: remove this.
//        System.out.println("===========EditDist2 cands: " + cands);


        return cands;
    }

    /*
     * generateCandidates: generate all candidate corrections given the input "word"
     * Input: A word with spelling error Output: All candidates that may be the
     * correct word. String is the candidate, and Double is the edit distance of the
     * candidate.
     */
    public Map<String, Double> generateCandidates(String word) {
        Map<String, Double> cand = new HashMap<>();

        // if the input word appears in the dictionary, we simply return the word as the
        // only candidate with edit distance 0
        if (dict.containsKey(word)) {
            cand.put(word, 0.0);
            
            return cand;
        }

        // obtain all strings within edit distance 1 of the input word
        Map<String, Double> ed1 = EditDist1(word);

        // System.out.println("generateCandiates EditDist1: " + ed1);

        // add an edit-distance-1 string to the candidate set only if it appears in the
        // dictionary
        for (String str : ed1.keySet()) {
            if (dict.containsKey(str))
                cand.put(str, ed1.get(str));
        }

        // if there is any dictionary entry within edit-distance-1, we return them as
        // the candidate set
        if (!cand.isEmpty()) {
            return cand;
        }
  

        // there is no dictionary word within edit distance 1, so we now
        // obtain all strings within edit distance 2 of input word
        Map<String, Double> ed2 = EditDist2(ed1);

        // add an edit-distance-2 string to candidate set only if it appears in the
        // dictionary
        for (String str : ed2.keySet()) {
            if (dict.containsKey(str))
                cand.put(str, ed2.get(str));
        }


        //TODO: remove this.
//        System.out.println("generateCandidate: " + cand);




        return cand;
    }

    /*
     * findCorrection: returns the corrected spelling of the input word Input: word:
     * the input word with spelling errors; candidates: the list of candidates
     * obtained from the function "generateCandidates" Output: The spell-corrected
     * word
     */
    public String findCorrection(String word, Map<String, Double> candidates) {
        // if the word appears in the dictionary or there is no candidates, return the
        // word as the "correction"
        if (dict.containsKey(word) || candidates.isEmpty())
            return word;

        // iterate over every word in the candidate set and return the one with the
        // highest probability
        double maxp = 0.0;
        String c = word;

//        System.out.println("===============findCorrection======================");
        //TODO: remove this.
//        System.out.println("candidates: " + candidates);



        for (String str : candidates.keySet()) {

            //TODO: REmove this.
//            System.out.println("word in candidates: " + str);



            // compute P(str|word) = P(word|str) * P(str)
            double prob = calculateEM(word, str, candidates.get(str)) * calculateLM(str);

            //TODO: remove this.
//            System.out.println("prob: " + prob);

            if (prob > maxp) {
                maxp = prob;
                c = str;
            }
        }

        //TODO: remove this.
//        System.out.println("Find Prediction: " + c);


        return c;
    }

    /*
    readDictionary: read the dictionary file and return the <dictionary word, frequency> pair in a map
    Input: The filename of dictionary
    Output: A map with contents <dictionary word, frequency>
    You do not need to modify this function
 */
    private Map < String, Integer > readDictionary(String dictionaryFile) throws IOException {
        dict = new HashMap < > ();
        InputStreamReader fis = new InputStreamReader(new FileInputStream(dictionaryFile));
        BufferedReader br = new BufferedReader(fis);
        String line;
        while ((line = br.readLine()) != null) {
            String[] tmp = line.split("\t");
            if (tmp.length > 1) {
                dict.put(tmp[0], Integer.parseInt(tmp[1]));
            } else {
                dict.put(tmp[0], 1);
            }
        }
        System.out.println("Finished loading dictionary. Total size: " + dict.size());
        br.close();
        fis.close();
        return dict;
    }

    /*
        readTestset: read testset file and return the <wrong spelling, correct word> pair in a map
        Input: The filename of testset
        Output: A map with contents <wrong spelling, correct word>
        You do not need to modify this function
     */
    private Map < String, String > readTestset(String testsetFile) throws IOException {
        Map < String, String > cases = new HashMap < > ();
        InputStreamReader fis = new InputStreamReader(new FileInputStream(testsetFile));
        BufferedReader br = new BufferedReader(fis);
        String line;
        while ((line = br.readLine()) != null) {
            String[] temp1 = line.split(":");
            String right = temp1[0].trim();
            String[] wrongs = temp1[1].split(" ");
            for (String wrong: wrongs)
                if (!wrong.equals(" ") && !wrong.isEmpty())
                    cases.put(wrong.trim(), right);
        }
        br.close();
        fis.close();
        return cases;
    }

    /*
        runTestcases: run spell correction algorithm on all test_caes and prints out the results
        Input: test_cases - a map of <wrong spelling, correct word> pairs
               print_all - if true, the function print out the correction result for each test case
        IMPORTANT: DO NOT MODIFY THIS FUNCTION!!! 
                   IT WILL MAKE OUR GRADING SCRIPT FAIL!
     */
    public void runTestcases(Map < String, String > test_cases, boolean print_all) {
        int n = test_cases.size();
        int nCorrect = 0, nUnknown = 0;
        boolean accurate;
        for (String wrong: test_cases.keySet()) {
            Map < String, Double > candidates = generateCandidates(wrong);
            String predict = findCorrection(wrong, candidates);
            accurate = predict.equals(test_cases.get(wrong));
            
            //TODO: remove this
            System.out.println("Correct word:" + test_cases.get(wrong));

            if (accurate) nCorrect++;
            else {
                if (!dict.containsKey(test_cases.get(wrong)))
                    nUnknown++;
            }
            if (print_all)
                System.out.println(wrong + " -> " + predict + ": " + (accurate ? '1' : '0'));
        }
        
        
        NumberFormat formatter = new DecimalFormat("#0.00");
        System.out.println("FINAL RESULT: " +
                nCorrect + " of " + n + " correct. ( " +
                formatter.format(nCorrect*100.0/n) + "% accuracy " +
                formatter.format(nUnknown*100.0/n) + "% unknown )");
    }

    /*
        The main function: 
        args[0] is the path of dictionary.
        args[1] is the path of test set.
        args[2] is optional, decide whether to output the full lists of predictions.
        IMPORTANT: DO NOT CHANGE THE COMMAND LINE PARAMETERS!!! 
                   IT WILL BREAK OUR GRADING SCRIPT
     */
    public static void main(String args[]) throws IOException {
        SpellChecker2 sc = new SpellChecker2(args[0]);
        Map < String, String > testset = sc.readTestset(args[1]);
        boolean printAll = (args.length < 3) ? true : Boolean.parseBoolean(args[2]);
        sc.runTestcases(testset, printAll);
    }

}

