import { useState } from "react";
import OperationTypes from "../OperationTypes";
import Book from "../DTO/BookDTO";
import useFetch from "../functions/useFetch";
import Popup from "./Popup";

export type Operation = {
    type: OperationTypes,
    handle: () => void
}

export type FeedbackPopup = {
    render: boolean,
    text: string
}

type Props = {
    url: string,
    operation: Operation,
    bookId: number,
    setBookId: React.Dispatch<React.SetStateAction<number>>,
    feedback?: FeedbackPopup
    forceUpdate?: any
  }

const BookList = (props: Props) => {
    const {url, operation, bookId, setBookId, feedback, forceUpdate} = {...props} 
    const [isOperationActive, setIsOperationActive] = useState(false);
    const books: Book[] = useFetch(url, forceUpdate);

    const handleClick = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>, bookId: number) => {
        e.preventDefault();

        setBookId(bookId);
        setIsOperationActive(true);
    }

    if (books.length === 0) {
        return (
            <section className="book-list__container">
                <p>No books for display</p>
            </section>
        )
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
                        <p>Type: {book.type.name}</p>
                    </div>
                    <button className="btn" onClick={(e) => handleClick(e, book.id!)}>{capitalize(operation.type)}</button>
                    {isOperationActive && bookId === book.id && <Popup title={operation.type} book={book} handleOperation={operation.handle} setIsOperationActive={setIsOperationActive}/>}
                    {feedback?.render &&  
                    <div className="popup__background">
                        <section className="popup__container">
                            <p>{feedback.text}</p>
                        </section>
                    </div>
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