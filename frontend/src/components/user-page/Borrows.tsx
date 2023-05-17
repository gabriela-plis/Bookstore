import { useReducer, useState } from "react";
import OperationTypes from "../../variable/OperationTypes";
import BookList, { Operation } from "../../reusable-components/BookList";
import { BOOKS_URL } from "../../variable/constants";

const Borrows = () => {

    const [bookId, setBookId] = useState(0);
   
    const [trigger, forceUpdate] = useReducer(x => x + 1, 0);

    const handleReturn = () => {
        fetch(BOOKS_URL + `/${bookId}/return`, {
            method: "POST",
            credentials: "include"
        }).then( () => {
            forceUpdate();
        })

    }

    const url = BOOKS_URL + `/user/${sessionStorage.getItem('id')}`

    const operation: Operation = {
        type: OperationTypes.Return,
        handle: handleReturn
    }

    return (
        <section className="borrows">
            <h2 className="borrows__title">My Borrows</h2>
            <BookList url={url} operation={operation} bookId={bookId} setBookId={setBookId} forceUpdate={trigger}/>
        </section>
     );
}
 
export default Borrows;