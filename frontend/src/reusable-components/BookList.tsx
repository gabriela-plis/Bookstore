import { url } from "inspector";
import { useEffect, useState } from "react";
import OperationTypes from "../OperationTypes";
import Book from "../DTO/BookDTO";
import useFetch from "../functions/useFetch";
import Popup from "./Popup";

export type Operation = {
    type: OperationTypes,
    handle: () => void
}

type Props = {
    url: string,
    operation?: Operation,
    bookId: number,
    setBookId: React.Dispatch<React.SetStateAction<number>>,
  }

const BookList = (props: Props) => {
    const {url, operation, bookId, setBookId} = {...props} 
    const [isOperationActive, setIsOperationActive] = useState(false);

    const books: Book[] = useFetch(url);

    const handleClick = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>, bookId: number) => {
        e.preventDefault();

        setBookId(bookId);
        setIsOperationActive(true);
    }

    return (
        <section className="book-list__container">
            <ul className="book-list__list">
                {books.map( book => (
                <li className="book" key={book.id} data-key={book.id}>
                    <h2>{book.title}</h2>
                    <div className="book__details">
                        <p>Author: {book.author}</p>
                        <p>Publish year: {book.publishYear}</p>  
                        <p>Type: {book.type}</p>
                    </div>
                    {operation && 
                    <>
                    <button className="btn" onClick={(e) => handleClick(e, book.id!)}>{capitalize(operation.type)}</button>
                    {isOperationActive && bookId === book.id && <Popup title={operation.type} book={book} handleOperation={operation.handle} setIsOperationActive={setIsOperationActive}/>}
                    </>
                    }
                    
                </li>
                ))}
            </ul>
        </section> 
     );
}

const capitalize = (word: string) => {
    return word.charAt(0).toUpperCase() + word.slice(1);
}
 
export default BookList;