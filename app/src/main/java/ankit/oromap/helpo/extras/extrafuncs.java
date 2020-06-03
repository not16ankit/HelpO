package ankit.oromap.helpo.extras;

public class extrafuncs {
    public String twist(String an)
    {
        String res = "";
        int i = 0;
        while(i<(an.length()-1))
        {
            res = res + (char)an.charAt(i);
            i++;
        }
        return res;
    }
}
