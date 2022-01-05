package org.example;

public class Main {

    record AutreRecord(){};

    public AutreRecord rec()
    {
        AutreRecord r=new AutreRecord();
        return r;
    }

    public static void main()
    {
        Main m=new Main();
        System.out.println(m.rec());
    }
}
