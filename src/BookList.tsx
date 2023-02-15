import { useEffect, useState } from "react";
import Book from "./Book";

const BookList = () => {
    const [books, setBooks] = useState<Book[]>([]);

    useEffect ( () => {
        fetch('http://localhost:8000/books')
        .then ( resp => resp.json() )
        .then ( data => {
            console.log(data)
            setBooks(data);
        })
    },[]);

    return ( 
        <ul className="book-list">
            {books.map( book => (
            <li className="book" key={book.ID}>
                <h2>{book.title}</h2>
                <p>Author: {book.author}  Publish year: {book.publishYear}  Type: {book.type}</p>
                <button className="borrow-btn">borrow</button>
            </li>
            ))}
        </ul>
     );
}
 
export default BookList;