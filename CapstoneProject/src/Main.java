public class Main
{
    public static void main(String[] args)throws AgenotvalidException
    {
        System.out.println("Hello world!");
        int age=12;
        if(age<18)throw new AgenotvalidException("NOT VALID");
        else {
            System.out.println("valid");
        }
    }
}