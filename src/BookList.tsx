import { useEffect, useState } from "react";
import Book from "./DTO/Book";
import useFetch from "./useFetch";

const BookList = () => {
    const books: Book[] = useFetch(('http://localhost:8000/books')) as Book[]; 


    return (
        <section className="books">
            <ul className="book-list">
                {books.map( book => (
                <li className="book" key={book.ID}>
                    <h2>{book.title}</h2>
                    <p>Author: {book.author}  Publish year: {book.publishYear}  Type: {book.type}</p>
                    <button className="btn btn--borrow">borrow</button>
                </li>
                ))}
            </ul>
        </section> 
     );
}
 
export default BookList;