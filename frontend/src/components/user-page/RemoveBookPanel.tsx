import OperationTypes from "../../OperationTypes";
import Book from "../../DTO/BookDTO";
import BookList, { Operation } from "../../reusable-components/BookList";
import { useState } from "react";

const RemoveBookPanel = () => {

    const [bookId, setBookId] = useState(0);

    const handleRemove = () => {
        fetch('http://localhost:8080/books/' + bookId, {
            method: "DELETE",
            credentials: "include"
        })

        //feedback
    }

    const operation: Operation = {
        type: OperationTypes.Remove,
        handle: handleRemove
    }

    return ( 
        <section className="remove-panel">
            <h2 className="remove-panel__title">Remove Book</h2>
            <BookList url="http://localhost:8080/books/to-remove" operation={operation} bookId={bookId} setBookId={setBookId}/>
        </section>
     );
}
 
export default RemoveBookPanel;