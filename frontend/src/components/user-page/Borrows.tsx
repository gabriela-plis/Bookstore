import { useState } from "react";
import OperationTypes from "../../OperationTypes";
import BookList, { Operation } from "../../reusable-components/BookList";

const Borrows = () => {

    const [bookId, setBookId] = useState(0);

    const handleReturn = () => {

    }

    const operation: Operation = {
        type: OperationTypes.Return,
        handle: handleReturn
    }

    return (
        <section className="borrows">
            <h2 className="borrows__title">My Borrows</h2>
            <BookList url={'http://localhost:8000/books/user/' + sessionStorage.getItem("id")} operation={operation} bookId={bookId} setBookId={setBookId} />
        </section>
     );
}
 
export default Borrows;