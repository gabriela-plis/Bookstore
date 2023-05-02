import OperationTypes from "../../OperationTypes";
import BookList, { Operation } from "../../reusable-components/BookList";
import { useReducer, useState } from "react";

const RemoveBookPanel = () => {

    const [bookId, setBookId] = useState(0);
    const [trigger, forceUpdate] = useReducer(x => x + 1, 0);

    const handleRemove = () => {
        fetch('http://localhost:8080/books/' + bookId, {
            method: "DELETE",
            credentials: "include"
        }).then( () => {
            forceUpdate();
        })
    }

    const operation: Operation = {
        type: OperationTypes.Remove,
        handle: handleRemove
    }

    return ( 
        <section className="remove-panel">
            <h2 className="remove-panel__title">Remove Book</h2>
            <BookList url="http://localhost:8080/books/to-remove" operation={operation} bookId={bookId} setBookId={setBookId} forceUpdate={trigger}/>
        </section>
     );
}
 
export default RemoveBookPanel;