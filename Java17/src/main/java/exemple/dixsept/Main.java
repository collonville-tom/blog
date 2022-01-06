package exemple.dixsept;

public class Main{
    public static void main(String... args)
    {
        Object o=Integer.valueOf(0);
        String result=switch (o) {
            case null       -> "Oops";
            case Integer i -> String.format("int %d", i);
            case Long l    -> String.format("long %d", l);
            case Double d  -> String.format("double %f", d);
            case String s  -> String.format("String %s", s);
            default        -> o.toString();
        };
    }
}