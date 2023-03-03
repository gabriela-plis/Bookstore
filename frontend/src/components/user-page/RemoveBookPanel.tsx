import OperationTypes from "../../OperationTypes";
import Book from "../../DTO/Book";
import BookList from "../../reusable-components/BookList";
import { useState } from "react";

const RemoveBookPanel = () => {

    const [bookId, setBookId] = useState(0);

    const handleRemove = () => {
        fetch('http://localhost:8000/books/' + bookId, {
            method: "DELETE"
        })

        //feedback
    }

    return ( 
        <section className="remove-panel">
            <h2 className="remove-panel__title">Remove Book</h2>
            <BookList url="http://localhost:8000/books" operationType={OperationTypes.Remove} handleOperation={handleRemove} setBookId={setBookId}/>
        </section>
     );
}
 
export default RemoveBookPanel;