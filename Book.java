import java.time.Year;

public class Book implements Data, Cloneable  {
    /*
    * AF -> <this.code, this.title, this.author, this.year>
    * RI -> code >= 0
    *       && title != null
    *       && author != null
    *       && year <= currentYear
    *
    * */

    private int code;
    private String title;
    private String author;
    private int year;



    public Book(int code, String title, String author, int year){
        if(code < 0) throw new IllegalArgumentException("Invalid code");
        if(title == null || author == null) throw new NullPointerException();
        if(year > Year.now().getValue()) throw new IllegalArgumentException("Are you from the future?");
        this.code = code;
        this.title = title;
        this.author = author;
        this.year = year;
    }


    public int getCode() {
        return code;
    }

    public String getTitle(){
        return title;
    }

    public String getAuthor(){
        return author;
    }

    public int getYear(){
        return year;
    }

    @Override
    public void display() {
        System.out.println("Book_Code: " +  code);
        System.out.println("\t  Title: " +  title);
        System.out.println("\t  Author: " +  author);
        System.out.println("\t  Year: " +  year);
        System.out.println();
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        Book cloned = (Book) super.clone();
        return cloned;
    }
}
