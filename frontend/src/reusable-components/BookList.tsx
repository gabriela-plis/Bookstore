import { url } from "inspector";
import { useEffect, useState } from "react";
import OperationTypes from "../OperationTypes";
import Book from "../DTO/Book";
import useFetch from "../functions/useFetch";
import Popup from "./Popup";


type Props = {
    url: string,
    operationType: OperationTypes,
    bookId: number,
    setBookId: React.Dispatch<React.SetStateAction<number>>,
    handleOperation: () => void
  }

const BookList = (props: Props) => {
    const {url, operationType, bookId, setBookId, handleOperation} = {...props} 
    const [isOperationActive, setIsOperationActive] = useState(false);

    const books: Book[] = useFetch(url);

    const handleClick = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>, bookId: number) => {
        e.preventDefault();

        setBookId(bookId);
        setIsOperationActive(true);
    }

    return (
        <section className="books">
            <ul className="book-list">
                {books.map( book => (
                <li className="book" key={book.id} data-key={book.id}>
                    <h2>{book.title}</h2>
                    <div className="book__details">
                        <p>Author: {book.author}</p>
                        <p>Publish year: {book.publishYear}</p>  
                        <p>Type: {book.type}</p>
                    </div>
                    <button className="btn" onClick={(e) => handleClick(e, book.id!)}>{capitalize(operationType)}</button>
                    {isOperationActive && bookId === book.id && <Popup title={operationType} book={book} handleOperation={handleOperation} setIsOperationActive={setIsOperationActive}/>}
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