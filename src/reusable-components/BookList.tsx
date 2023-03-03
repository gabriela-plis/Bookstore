import { url } from "inspector";
import { useEffect, useState } from "react";
import OperationTypes from "../OperationTypes";
import Book from "../DTO/Book";
import useFetch from "../functions/useFetch";
import Popup from "./Popup";


type Props = {
    url: string,
    operationType: OperationTypes,
    handleOperation: () => void
  }

const BookList = (props: Props) => {
    const {url, operationType, handleOperation} = {...props} 

    const [isOperationActive, setIsOperationActive] = useState(false);

    const books: Book[] = useFetch(url);

    const handleClick = () => {
        setIsOperationActive(true);
    }

    return (
        <section className="books">
            <ul className="book-list">
                {books.map( book => (
                <li className="book" key={book.id}>
                    <h2>{book.title}</h2>
                    <p>Author: {book.author}  Publish year: {book.publishYear}  Type: {book.type}</p>
                    <button className="btn" onClick={handleClick}>{capitalize(operationType)}</button>
                    {isOperationActive && <Popup title={operationType} book={book} handleOperation={handleOperation} setIsOperationActive={setIsOperationActive}/>}
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