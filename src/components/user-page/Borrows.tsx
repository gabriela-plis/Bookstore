import OperationTypes from "../../OperationTypes";
import BookList from "../../reusable-components/BookList";

const Borrows = () => {

    const handleReturn = () => {

    }

    return ( 
        <section className="borrows">
            <BookList url='http://localhost:8000/books' operationType={OperationTypes.Return} handleOperation={handleReturn}/>
        </section>
     );
}
 
export default Borrows;