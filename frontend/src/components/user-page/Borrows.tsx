import { useState } from "react";
import OperationTypes from "../../OperationTypes";
import BookList from "../../reusable-components/BookList";

const Borrows = () => {

    const [bookId, setBookId] = useState(0);

    const handleReturn = () => {

    }

    return ( 
        <section className="borrows">
            <h2 className="borrows__title">My Borrows</h2>
            <BookList url='http://localhost:8000/books' operationType={OperationTypes.Return} setBookId={setBookId} handleOperation={handleReturn}/>
        </section>
     );
}
 
export default Borrows;