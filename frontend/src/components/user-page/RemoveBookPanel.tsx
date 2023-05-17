import OperationTypes from "../../variable/OperationTypes";
import { BOOKS_URL } from "../../variable/constants";
import BookList, { Operation } from "../../reusable-components/BookList";
import { useReducer, useState } from "react";

const RemoveBookPanel = () => {

    const [bookId, setBookId] = useState(0);
    const [trigger, forceUpdate] = useReducer(x => x + 1, 0);

    const handleRemove = () => {
        fetch(BOOKS_URL + `/${bookId}`, {
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

    const url = BOOKS_URL + '/to-remove';

    return ( 
        <section className="remove-panel">
            <h2 className="remove-panel__title">Remove Book</h2>
            <BookList url={url} operation={operation} bookId={bookId} setBookId={setBookId} forceUpdate={trigger}/>
        </section>
     );
}
 
export default RemoveBookPanel;