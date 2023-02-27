import { url } from "inspector";
import { useEffect, useState } from "react";
import Book from "../DTO/Book";
import useFetch from "../functions/useFetch";

type Props = {
    url: string
    isBorrowBtn: boolean
    isReturnBtn: boolean
}

const BookList = (props: Props) => {
    const books: Book[] = useFetch(props.url) as Book[];

    return (
        <section className="books">
            <ul className="book-list">
                {books.map( book => (
                <li className="book" key={book.id}>
                    <h2>{book.title}</h2>
                    <p>Author: {book.author}  Publish year: {book.publishYear}  Type: {book.type}</p>
                    {props.isBorrowBtn && <button className="btn btn--borrow">borrow</button>}
                    {props.isReturnBtn && <button className="btn btn--borrow">return</button>}
                </li>
                ))}
            </ul>
        </section> 
     );
}
 
export default BookList;