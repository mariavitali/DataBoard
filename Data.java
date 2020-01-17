public interface Data {
    /*
    * OVERVIEW: Tipo di Dato che implementa un metodo display
    *
    * Typical element: <data>
    *
    * */

    //mostra il contenuto del dato
    void display();
    // EFFECTS: mostra il contenuto del dato

    //clona this
    Object clone() throws CloneNotSupportedException;
    // REQUIRES: l'oggetto deve poter essere clonato
    // THROWS: se this non può essere clonato, lancia CloneNotSupportedException
    // EFFECTS: crea una copia (deep) dell'oggetto
}
