import OperationTypes from "../../OperationTypes";
import Book from "../../DTO/Book";
import useFetch from "../../functions/useFetch";
import BookList from "../../reusable-components/BookList";

const RemoveBookPanel = () => {
    const handleRemove = () => {

    }

    return ( 
        <section className="remove-panel">
            <BookList url="http://localhost:8000/books" operationType={OperationTypes.Remove} handleOperation={handleRemove}/>
        </section>
     );
}
 
export default RemoveBookPanel;