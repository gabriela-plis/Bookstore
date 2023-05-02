import { useReducer, useState } from "react";
import OperationTypes from "../../OperationTypes";
import BookList, { Operation } from "../../reusable-components/BookList";

const Borrows = () => {

    const [bookId, setBookId] = useState(0);
   
    const [trigger, forceUpdate] = useReducer(x => x + 1, 0);

    const handleReturn = () => {
        fetch(`http://localhost:8080/books/${bookId}/return`, {
            method: "POST",
            credentials: "include"
        }).then( () => {
            forceUpdate();
        })

    }

    const operation: Operation = {
        type: OperationTypes.Return,
        handle: handleReturn
    }

    return (
        <section className="borrows">
            <h2 className="borrows__title">My Borrows</h2>
            <BookList url={"http://localhost:8080/books/user/" + sessionStorage.getItem("id")} operation={operation} bookId={bookId} setBookId={setBookId} forceUpdate={trigger}/>
        </section>
     );
}
 
export default Borrows;