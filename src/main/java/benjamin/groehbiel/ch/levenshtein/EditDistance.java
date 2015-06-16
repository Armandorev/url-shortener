package benjamin.groehbiel.ch.levenshtein;

public class EditDistance {
    int[][] memo;
    String s1;
    String s2;
    int insertPenalty = 1;
    int deletePenalty = 1;
    int substitutionPenalty = 1;

    public EditDistance(String s1, String s2) {
        this.s1 = s1;
        this.s2 = s2;
        this.memo = new int[s1.length() + 1][s2.length() + 1];
        this.compute();
    }

    public void setPenalties(int insert, int delete, int substitute) {
        this.insertPenalty = insert;
        this.deletePenalty = delete;
        this.substitutionPenalty = substitute;
    }

    public int getEditDistance() {
        return this.memo[s1.length()][s2.length()];
    }

    private void compute() {

        for (int i = 0; i <= s1.length(); ++i) {
            this.memo[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); ++j) {
            this.memo[0][j] = j;
        }

        for (int i = 1; i <= s1.length(); ++i) {
            for (int j = 1; j <= s2.length(); ++j) {

                int substitution = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? this.memo[i - 1][j - 1] : this.memo[i - 1][j - 1] + 1;
                int insertion = this.memo[i - 1][j] + 1;
                int deletion = this.memo[i][j - 1] + 1;

                this.memo[i][j] = Math.min(Math.min(substitution, insertion), deletion);
            }
        }
    }

    public void backtrack() {
        int i = s1.length();
        int j = s2.length();

        while (i != 0 || j != 0) {
            int current = this.memo[i][j];

            int insertion = (i - 1 >= 0) ? this.memo[i - 1][j] : Integer.MAX_VALUE;
            int deletion = (j - 1 >= 0) ? this.memo[i][j - 1] : Integer.MAX_VALUE;
            int substitution = (i - 1 >= 0 && j - 1 >= 0) ? this.memo[i - 1][j - 1] : Integer.MAX_VALUE;

            int min = Math.min(substitution, Math.min(insertion, deletion));

            if (i == 0) System.out.println("{} " + s2.charAt(j - 1));
            else if (j == 0) System.out.println(s1.charAt(i - 1) + " {}");
            else System.out.println(s1.charAt(i - 1) + " " + s2.charAt(j - 1));

            if (min == substitution) {
                if (current == substitution) {
                    System.out.println("equal");
                    i -= 1;
                    j -= 1;
                } else {
                    System.out.println("substitution");
                    i -= 1;
                    j -= 1;
                }
            } else if (min == insertion) {
                System.out.println("insert");
                i -= 1;
            } else if (min == deletion) {
                System.out.println("delete");
                j -= 1;
            }
        }
    }
}
