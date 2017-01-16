package org.xm.xmnlp.math;

/**
 * 求最长公共字串的长度<br>
 */
public class LongestCommonSubstring {
    public static int compute(char[] str1, char[] str2) {
        int size1 = str1.length;
        int size2 = str2.length;
        if (size1 == 0 || size2 == 0) return 0;

        // the longest length
        int longest = 0;

        // record how many comparisons the solution did;
        // it can be used to know which algorithm is better

        for (int i = 0; i < size1; ++i) {
            int m = i;
            int n = 0;
            int length = 0;
            while (m < size1 && n < size2) {
                if (str1[m] != str2[n]) {
                    length = 0;
                } else {
                    ++length;
                    if (longest < length) {
                        longest = length;
                    }
                }

                ++m;
                ++n;
            }
        }

        // shift string2 to find the longest com.hankcs.common substring
        for (int j = 1; j < size2; ++j) {
            int m = 0;
            int n = j;
            int length = 0;
            while (m < size1 && n < size2) {
                if (str1[m] != str2[n]) {
                    length = 0;
                } else {
                    ++length;
                    if (longest < length) {
                        longest = length;
                    }
                }

                ++m;
                ++n;
            }
        }
        //System.out.printf("from %d of %s and %d of %s, compared for %d times\n", start1, new String(str1), start2, new String(str2), comparisons);
        return longest;
    }

    public static int compute(String str1, String str2) {
        return compute(str1.toCharArray(), str2.toCharArray());
    }
}
