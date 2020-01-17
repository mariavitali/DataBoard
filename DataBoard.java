import java.util.Iterator;
import java.util.List;

public interface DataBoard<E extends Data> {
    /*
    * OVERVIEW: contenitore di oggetti generici che estendono il tipo di dato Data.
    *           E' uno spazio di memorizzazione e visualizzazione di dati.
    *           Ogni dato presente nella DataBoard ha associata la categoria del dato.
    *           Il proprietario della bacheca può definire le proprie categorie
    *           e stilare una lista di contatti (amici) a cui saranno visibili i dati per ogni tipologia di categoria.
    *           Gli amici possono associare un “like” al contenuto condiviso
    *
    * Typical element: <password, {categories}, {data}, {friends}, categorizeData, friendAccess, likes>
    *                   dove:
    *                       - password (String) è una stringa che identifica il creatore della DataBoard
    *                       - {categories} è un insieme di stringhe che rappresentano le categorie della DataBoard
    *                       - {data} è un insieme di oggetti di tipo E che rappresentano i dati presenti all'interno della DataBoard
    *                       - {friends} è un insieme di amici (String)
    *                       - categorizeData: {data} -> {categories}
    *                         è una funzione tale che ∀d ∈ {data}, ∃c ∈ {categories} . categorizeData(d) = c
    *                       - friendAccess: {categories} -> {friends_i | friends_i ∈ {friends}}
    *                         è una funzione tale che ∀c ∈ {categories}, friendAccess(c) = {friends_c | friends_c ∈ {friends}}
    *                         e corrisponde all'insieme degli amici che hanno accesso alla categoria c
    *                       - likes: {friends} X {data} -> [0, 1]
    *                         è una funzione tale che ∀(d,f) . d ∈ {data} e f ∈ {friends_c | friends_c ∈ friendAccess(categorizeData(d))},
    *                               likes(d, f) = 1        se l'amico f ha messo un "like" al dato d
    *                               likes(d, f) = 0        altrimenti
    * */

    //crea una nuova categoria di dati
    void createCategory (String category, String passw) throws WrongPasswordException;
    /*
    * REQUIRES: category != null
    *           passw != null
    *           category non è nell'insieme {categories}
    *           passw = password
    * THROWS:  se category = null OR passw = null, lancia NullPointerException (unchecked)
    *          se category è già presente nell'insieme delle categorie, lancia IllegalArgumentException
    *          se passw != password, lancia WrongPasswordException (checked)
    * MODIFIES: this
    * EFFECTS: crea una nuova categoria identificata dalla stringa category e la aggiunge all'insieme delle categorie
    *
    * */

    //rimuove una categoria di dati
    void removeCategory (String category, String passw) throws WrongPasswordException;
    /*
    * REQUIRES: category != null
    *           passw != null
    *           passw = password
    * THROWS: se category = null OR passw = null, lancia NullPointerException
    *         se passw != password, lancia WrongPasswordException (checked)
    * MODIFIES: this
    * EFFECTS: se category è presente nell'insieme delle categorie, lo rimuove
    *          rimuove anche tutti i dati associati a quella categoria e i rispettivi likes
    */

    //aggiunge un amico alla categoria di dati category
    void addFriend (String category, String passw, String friend) throws WrongPasswordException;
    /*
    * REQUIRES: category != null
    *           passw != null
    *           friend != null
    *           passw = password
    *           category è presente nell'insieme delle categorie
    * THROWS: se category = null OR passw = null OR friend = null, lancia NullPointerException
    *         se passw != password, lancia WrongPasswordException (checked)
    *         se category non è nell'insieme delle categorie, lancia IllegalArgumentException
    * MODIFIES: this
    * EFFECTS: l'amico friend ha accesso alla categoria category nella DataBoard
    * */

    //rimuove l'amico friend dalla categoria di dati category
    void removeFriend(String category, String passw, String friend) throws WrongPasswordException;
    /*
    * REQUIRES: category != null
    *           passw != null
    *           friend != null
    *           passw = password
    *           category è nell'insieme delle categorie
    * THROWS: se category = null OR passw = null OR friend = null, lancia NullPointerException
    *         se passw != password, lancia WrongPasswordException (checked)
    *         se category non è nell'insieme delle categorie, lancia IllegalArgumentException
    * MODIFIES: this
    * EFFECTS: toglie all'amico friend, se lo aveva, l'accesso ai dati della categoria category
    *
    * */

    //inserisce un dato in bacheca, se vengono rispettati i controlli di identità
    boolean put (String passw, E data, String category) throws WrongPasswordException;
    /*
    * REQUIRES: passw != null
    *           data != null
    *           category != null
    *           passw = password
    *           data non è presente nell'insieme dei dati della DataBoard
    *           category appartiene all'insieme delle categorie
    * THROWS: se pass = null OR data = null OR category = null, lancia NullPointerException
    *         se passw != password, lancia WrongPasswordException (checked)
    *         se data è già presente nella DataBoard OR
    *            category non appartiene all'insieme delle categorie, lancia IllegalArgumentException
    * MODIFIES: this
    * EFFECTS: inserisce il dato nella DataBoard, assegnandogli la categoria specificata e restituisce true se l'inserimento ha avuto successo
    *          restituisce false altrimenti
    * */

    //ottiene una copia del dato in bacheca, se vengono rispettati i controlli di identità
    E get (String passw, E data) throws WrongPasswordException, EmptyDataBoardException;
    /*
    * REQUIRES: passw != null
    *           passw = password
    *           data != null
    *           insieme dei dati non vuoto
    * THROWS: se passw = null OR data = null, lancia NullPointerException
    *         se passw != password, lancia WrongPasswordException (checked)
    *         se l'insieme dei dati è vuoto, lancia EmptyDataBoardException (checked)
    * EFFECTS: restituisce il dato data, se esiste
    *          altrimenti restituisce null
    *
    * */

    //rimuove il dato dalla DataBoard, se vengono rispettati i controlli di identità
    E remove (String passw, E data) throws WrongPasswordException, EmptyDataBoardException;
    /*
     * REQUIRES: passw != null
     *           passw = password
     *           data != null
     *           insieme dei dati non vuoto
     * THROWS: se passw = null OR data = null, lancia NullPointerException
     *         se passw != password, lancia WrongPasswordException (checked)
     *         se l'insieme dei dati è vuoto, lancia EmptyDataBoardException (checked)
     * EFFECTS: restituisce il dato data, se esiste, e lo elimina
     *          altrimenti restituisce null se il dato non è presente
     *
     * */

    //Crea la lista dei dati in bacheca su una determinata categoria, se vengono rispettati i controlli di identità
    List<E> getDataCategory (String passw, String category) throws WrongPasswordException;
    /*
    * REQUIRES: passw != null
    *           category != null
    *           passw = password (la password è corretta)
    * THROWS: se passw = null OR category = null, lancia NullPOinterException
    *         se passw != password, lancia WrongPasswordException (checked)
    * EFFECTS: restituisce una lista dei dati della categoria category,
    *          null se la categoria non esiste
    * */

    //restituisce un iteratore senza remove, che genera tutti i dati nella DataBoard,
    //ordinati rispetto al numero di like.
    Iterator<E> getIterator (String passw) throws WrongPasswordException;
    /*
    * REQUIRES: passw != null
    *           passw = password (la password è corretta)
    * THROWS: se passw = null, lancia NullPointerException
    *         se passw != password, lancia WrongPasswordException
    * EFFECTS: restituisce un iteratore che itera su tutti i dati della DataBoard ordinati rispetto al numero di like
    *          se la DataBoard è vuota e non c'è nessun dato su cui iterare, restituisce null
    * */


    //aggiunge un like a un dato
    void insertLike (String friend, E data);
    /*
    * REQUIRES: friend != null
    *           data != null
    *           data appartiene all'insieme dei dati
    *           friend ha accesso alla categoria del dato data
    * THROWS: se friend = null OR data = null, lancia NullPointerException
    *         se data non è nell'insieme dei dati, lancia NoSuchElementException
    *         se friend non ha accesso alla categoria di quel dato, lancia IllegalArgumentException
    * MODIFIES: this
    * EFFECTS: inserisce il like al dato data da parte dell'amico friend nella DataBoard, se non era già presente
    *
    * */


    //legge un dato condiviso
    //restituisce un iteratore senza remove, che genera tutti i dati condivisi con friend nella DataBoard
    Iterator<E> getFriendIterator (String friend);
    /*
    * REQUIRES: friend != null
    * THROWS: se friend = null, lancia NullPointerException
    * EFFECTS: restituisce un iteratore che genera tutti i dati la cui categoria è condivisa con friend nella DataBoard
    *          se friend non ha accesso a nessuna categoria, restituisce null
    * */

}
